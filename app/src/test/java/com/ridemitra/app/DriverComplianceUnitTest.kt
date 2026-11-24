package com.ridemitra.app

import com.ridemitra.app.domain.model.DriverApplication
import com.ridemitra.app.domain.model.DriverLicenseInfo
import com.ridemitra.app.domain.model.DriverPersonalInfo
import com.ridemitra.app.domain.model.DriverVerificationStatus
import com.ridemitra.app.domain.model.VehicleDetails
import com.ridemitra.app.domain.model.VehicleRegulatoryDocs
import com.ridemitra.app.domain.validation.DocumentValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests verifying RideMitra Phase 3 Driver Compliance rules:
 * - Proper document expiry validation (DL, Insurance, PUC, Permits, Fitness)
 * - Expired document blocks approval and booking eligibility
 * - Strict N-1 passenger capacity calculation
 * - Configurable PAN collection
 * - Separate verification statuses
 */
class DriverComplianceUnitTest {

    @Test
    fun testValidDocumentDatesPassValidation() {
        val dlCheck = DocumentValidator.validateExpiry("31/12/2030", "Driving Licence")
        assertTrue("Future DL expiry date must be valid", dlCheck.isValid)
        assertFalse("Future DL expiry date must not be expired", dlCheck.isExpired)

        val insCheck = DocumentValidator.validateExpiry("15/08/2028", "Motor Insurance")
        assertTrue("Future Insurance expiry date must be valid", insCheck.isValid)
        assertFalse("Future Insurance expiry date must not be expired", insCheck.isExpired)

        val pucCheck = DocumentValidator.validateExpiry("20/10/2027", "PUC Certificate")
        assertTrue("Future PUC expiry date must be valid", pucCheck.isValid)
        assertFalse("Future PUC expiry date must not be expired", pucCheck.isExpired)
    }

    @Test
    fun testExpiredDocumentDatesAreDetectedAndRejected() {
        val dlExpired = DocumentValidator.validateExpiry("01/01/2020", "Driving Licence")
        assertFalse("Past DL date must not be valid", dlExpired.isValid)
        assertTrue("Past DL date must be marked expired", dlExpired.isExpired)
        assertNotNull(dlExpired.errorMessage)
        assertTrue(dlExpired.errorMessage!!.contains("expired", ignoreCase = true))

        val insExpired = DocumentValidator.validateExpiry("15/03/2021", "Motor Insurance")
        assertFalse("Past insurance date must not be valid", insExpired.isValid)
        assertTrue("Past insurance date must be marked expired", insExpired.isExpired)

        val pucExpired = DocumentValidator.validateExpiry("10/10/2022", "PUC Certificate")
        assertFalse("Past PUC date must not be valid", pucExpired.isValid)
        assertTrue("Past PUC date must be marked expired", pucExpired.isExpired)
    }

    @Test
    fun testInvalidDateFormatsAreRejected() {
        val invalidFormat = DocumentValidator.validateExpiry("invalid-date", "Driving Licence")
        assertFalse("Gibberish date string must be rejected", invalidFormat.isValid)
        assertFalse("Gibberish date is an invalid format, not an expired date", invalidFormat.isExpired)
        assertNotNull(invalidFormat.errorMessage)
    }

    @Test
    fun testExpiredDocumentBlocksBookingEligibilityEvenIfStatusIsApproved() {
        val appWithExpiredDocs = DriverApplication(
            licenseInfo = DriverLicenseInfo(
                expiryDate = "01/01/2020" // Expired DL
            ),
            vehicleDocs = VehicleRegulatoryDocs(
                insuranceExpiryDate = "15/08/2028",
                pucExpiryDate = "20/10/2027"
            ),
            status = DriverVerificationStatus.APPROVED
        )

        assertTrue("Must detect expired DL", appWithExpiredDocs.hasExpiredDocuments)
        assertFalse(
            "Booking eligibility MUST be blocked when any document is expired even if status was APPROVED",
            appWithExpiredDocs.isEligibleForBookings
        )
        assertEquals(1, appWithExpiredDocs.getExpiredDocuments().size)
    }

    @Test
    fun testValidDocumentsAllowBookingEligibilityWhenApproved() {
        val validApp = DriverApplication(
            licenseInfo = DriverLicenseInfo(
                expiryDate = "14/03/2035"
            ),
            vehicleDocs = VehicleRegulatoryDocs(
                insuranceExpiryDate = "28/11/2027",
                pucExpiryDate = "10/05/2027"
            ),
            status = DriverVerificationStatus.APPROVED
        )

        assertFalse("Must not have expired documents", validApp.hasExpiredDocuments)
        assertTrue("Must be eligible for bookings when approved and all documents valid", validApp.isEligibleForBookings)
    }

    @Test
    fun testUnapprovedStatusBlocksBookingEligibility() {
        val pendingApp = DriverApplication(
            licenseInfo = DriverLicenseInfo(expiryDate = "14/03/2035"),
            vehicleDocs = VehicleRegulatoryDocs(insuranceExpiryDate = "28/11/2027", pucExpiryDate = "10/05/2027"),
            status = DriverVerificationStatus.PENDING_REVIEW
        )
        assertFalse("Pending review driver must NOT be eligible for bookings", pendingApp.isEligibleForBookings)

        val uploadedApp = DriverApplication(
            status = DriverVerificationStatus.UPLOADED
        )
        assertFalse("Uploaded status driver must NOT be eligible for bookings", uploadedApp.isEligibleForBookings)

        val rejectedApp = DriverApplication(
            status = DriverVerificationStatus.REJECTED
        )
        assertFalse("Rejected status driver must NOT be eligible for bookings", rejectedApp.isEligibleForBookings)
    }

    @Test
    fun testStrictNMinusOnePassengerCapacityRule() {
        // 4-seater cab -> max 3 RideMitra passengers
        assertEquals(3, DocumentValidator.calculateRideMitraCapacity(4))
        val sedan = VehicleDetails(passengerCapacityExclDriver = 4)
        assertEquals(3, sedan.allowedRideMitraCapacity)

        // 6-seater cab -> max 5 RideMitra passengers
        assertEquals(5, DocumentValidator.calculateRideMitraCapacity(6))
        val suv = VehicleDetails(passengerCapacityExclDriver = 6)
        assertEquals(5, suv.allowedRideMitraCapacity)

        // 7-seater cab -> max 6 RideMitra passengers
        assertEquals(6, DocumentValidator.calculateRideMitraCapacity(7))
        val van = VehicleDetails(passengerCapacityExclDriver = 7)
        assertEquals(6, van.allowedRideMitraCapacity)

        // Minimum capacity is 1
        assertEquals(1, DocumentValidator.calculateRideMitraCapacity(2))
    }

    @Test
    fun testConfigurablePanValidation() {
        // When PAN is optional
        val optionalEmpty = DocumentValidator.validatePanNumber("", isMandatory = false)
        assertTrue("Empty PAN must pass when optional", optionalEmpty.isValid)

        // When PAN is mandatory
        val mandatoryEmpty = DocumentValidator.validatePanNumber("", isMandatory = true)
        assertFalse("Empty PAN must fail when mandatory", mandatoryEmpty.isValid)

        // Valid PAN format (5 letters, 4 digits, 1 letter)
        val validPan = DocumentValidator.validatePanNumber("ABCDE1234F", isMandatory = true)
        assertTrue("Valid PAN format must pass", validPan.isValid)

        // Invalid PAN format
        val invalidPan = DocumentValidator.validatePanNumber("12345ABCDE", isMandatory = true)
        assertFalse("Invalid PAN format must fail", invalidPan.isValid)
    }

    @Test
    fun testAge18PlusValidation() {
        val adult = DocumentValidator.validateDateOfBirth("01/01/1990")
        assertTrue("Adult must be valid", adult.isValid)

        val underAge = DocumentValidator.validateDateOfBirth("01/01/2020")
        assertFalse("Underage (child) must be rejected", underAge.isValid)
        assertTrue(underAge.errorMessage!!.contains("18 years", ignoreCase = true))
    }
}
