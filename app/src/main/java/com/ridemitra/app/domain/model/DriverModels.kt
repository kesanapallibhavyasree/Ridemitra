package com.ridemitra.app.domain.model

import com.ridemitra.app.domain.validation.DocumentValidator

/**
 * Driver Verification Status lifecycle.
 * Strictly separates each stage:
 * - NOT_SUBMITTED: Registration form not yet completed.
 * - UPLOADED: All required documents uploaded, ready for submission.
 * - PENDING_REVIEW: Submitted by driver, awaiting compliance admin review.
 * - APPROVED: Verified by compliance desk, eligible for dispatches if documents valid.
 * - REJECTED: One or more documents rejected by compliance team; re-upload needed.
 * - EXPIRED: Regulatory document(s) passed validity date; booking eligibility blocked.
 * - SUSPENDED: Temporarily suspended for safety or policy audit.
 */
enum class DriverVerificationStatus(val label: String) {
    NOT_SUBMITTED("Registration Incomplete"),
    UPLOADED("Uploaded"),
    PENDING_REVIEW("Pending Review"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    EXPIRED("Expired"),
    SUSPENDED("Suspended")
}

/**
 * Step 1: Personal Information
 */
data class DriverPersonalInfo(
    val fullName: String = "",
    val mobileNumber: String = "",
    val isMobileVerified: Boolean = false,
    val email: String = "",
    val isEmailVerified: Boolean = false,
    val dateOfBirth: String = "",
    val residentialAddress: String = "",
    val profilePhotoUri: String? = null,
    val emergencyContactName: String = "",
    val emergencyContactNumber: String = ""
)

/**
 * Step 2: Identity Documents
 * Supports configurable PAN requirements.
 */
data class DriverIdentityDocs(
    val documentType: String = "Aadhaar Card",
    val documentNumberMasked: String = "", // Never expose full sensitive ID
    val documentFrontUri: String? = null,
    val documentBackUri: String? = null,
    val panNumberMasked: String = "",
    val panCardUri: String? = null,
    val addressProofUri: String? = null,
    val selfiePhotoUri: String? = null,
    val isPanConfiguredMandatory: Boolean = false
)

/**
 * Step 3: Driving Licence Information
 * Enforces proper expiry validation.
 */
data class DriverLicenseInfo(
    val licenseNumber: String = "",
    val licenseFrontUri: String? = null,
    val licenseBackUri: String? = null,
    val issueDate: String = "",
    val expiryDate: String = "",
    val licenseCategory: String = "Commercial / LMV Transport",
    val isExpiryValid: Boolean = true
)

/**
 * Step 4: Vehicle Information
 * Strictly enforces RideMitra's N-1 seat capacity rule:
 * Maximum passengers = registered passenger seats excluding driver - 1.
 */
data class VehicleDetails(
    val registrationNumber: String = "",
    val manufacturer: String = "",
    val model: String = "",
    val manufacturingYear: String = "",
    val vehicleCategory: String = "Sedan", // Sedan, SUV, EV, Compact
    val passengerCapacityExclDriver: Int = 4, // Total registered passenger seats
    val luggageCapacityBags: Int = 3,
    val vehicleFrontUri: String? = null,
    val vehicleRearUri: String? = null
) {
    // Strictly: registered passenger capacity - 1
    val allowedRideMitraCapacity: Int
        get() = DocumentValidator.calculateRideMitraCapacity(passengerCapacityExclDriver)
}

/**
 * Step 5: Vehicle Regulatory Documents
 * Tracks expiry dates for Insurance, PUC, Fitness, and Commercial Permit.
 */
data class VehicleRegulatoryDocs(
    val rcNumber: String = "",
    val rcDocUri: String? = null,
    val insurancePolicyNumber: String = "",
    val insuranceExpiryDate: String = "",
    val insuranceDocUri: String? = null,
    val pucExpiryDate: String = "",
    val pucDocUri: String? = null,
    val fitnessExpiryDate: String = "",
    val fitnessDocUri: String? = null,
    val commercialPermitUri: String? = null,
    val commercialPermitExpiryDate: String = ""
)

/**
 * Full Driver Onboarding Application Record.
 * Includes expiry inspection and booking eligibility checks.
 */
data class DriverApplication(
    val applicationId: String = "RM-DRV-APP-1029",
    val personalInfo: DriverPersonalInfo = DriverPersonalInfo(),
    val identityDocs: DriverIdentityDocs = DriverIdentityDocs(),
    val licenseInfo: DriverLicenseInfo = DriverLicenseInfo(),
    val vehicleDetails: VehicleDetails = VehicleDetails(),
    val vehicleDocs: VehicleRegulatoryDocs = VehicleRegulatoryDocs(),
    val status: DriverVerificationStatus = DriverVerificationStatus.NOT_SUBMITTED,
    val submissionDate: String = "",
    val adminFeedback: String? = null,
    val reUploadRequiredStep: Int? = null,
    val isPanRequired: Boolean = false // Configurable PAN collection requirement
) {
    /**
     * Inspects all documents for expiration.
     * Returns a list of descriptive messages for any expired documents.
     */
    fun getExpiredDocuments(): List<String> {
        val expiredList = mutableListOf<String>()

        val dlCheck = DocumentValidator.validateExpiry(licenseInfo.expiryDate, "Driving Licence")
        if (dlCheck.isExpired) {
            expiredList.add(dlCheck.errorMessage ?: "Driving Licence is expired.")
        }

        val insCheck = DocumentValidator.validateExpiry(vehicleDocs.insuranceExpiryDate, "Motor Insurance")
        if (insCheck.isExpired) {
            expiredList.add(insCheck.errorMessage ?: "Motor Insurance is expired.")
        }

        val pucCheck = DocumentValidator.validateExpiry(vehicleDocs.pucExpiryDate, "PUC Certificate")
        if (pucCheck.isExpired) {
            expiredList.add(pucCheck.errorMessage ?: "PUC Certificate is expired.")
        }

        if (vehicleDocs.fitnessExpiryDate.isNotBlank()) {
            val fitCheck = DocumentValidator.validateExpiry(vehicleDocs.fitnessExpiryDate, "Fitness Certificate", isRequired = false)
            if (fitCheck.isExpired) {
                expiredList.add(fitCheck.errorMessage ?: "Fitness Certificate is expired.")
            }
        }

        if (vehicleDocs.commercialPermitExpiryDate.isNotBlank()) {
            val permitCheck = DocumentValidator.validateExpiry(vehicleDocs.commercialPermitExpiryDate, "Commercial Permit", isRequired = false)
            if (permitCheck.isExpired) {
                expiredList.add(permitCheck.errorMessage ?: "Commercial Permit is expired.")
            }
        }

        return expiredList
    }

    val hasExpiredDocuments: Boolean get() = getExpiredDocuments().isNotEmpty()

    /**
     * A driver is eligible to receive and accept bookings ONLY when:
     * 1. Status is APPROVED by compliance desk
     * 2. No required documents are expired
     */
    val isEligibleForBookings: Boolean
        get() = (status == DriverVerificationStatus.APPROVED && !hasExpiredDocuments)
}

/**
 * Ride Status for Driver Execution
 */
enum class DriverRideStatus(val label: String) {
    ACCEPTED("Trip Accepted"),
    DRIVER_ARRIVING("Arriving at Airport"),
    DRIVER_ARRIVED("Arrived at Pillar"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled")
}

/**
 * Booking Request for Drivers
 */
data class DriverBookingRequest(
    val requestId: String,
    val passengerPickup: String,
    val destination: String,
    val distanceKm: Double,
    val estimatedDurationMinutes: Int,
    val passengerCount: Int,
    val luggageSummary: String,
    val requestedDepartureTime: String,
    val estimatedDriverPayout: Double,
    val platformCommission: Double,
    val rideType: RideOptionType,
    val flightNumber: String? = null,
    val isDemoRequest: Boolean = true
)

/**
 * Active Ongoing Ride
 */
data class DriverActiveRide(
    val bookingId: String,
    val passengerName: String,
    val passengerPhone: String,
    val pickupPillar: String,
    val destination: String,
    val distanceKm: Double,
    val estimatedDurationMinutes: Int,
    val passengerCount: Int,
    val luggageSummary: String,
    val rideType: RideOptionType,
    val status: DriverRideStatus,
    val expectedOtp: String = "4829",
    val driverPayout: Double = 540.0,
    val remainingCashToCollect: Double = 486.0, // 90% balance from passenger
    val isDemoRide: Boolean = true
)

/**
 * Earnings Record Line Item
 */
data class DriverEarningsTransaction(
    val tripId: String,
    val dateFormatted: String,
    val routeSummary: String,
    val totalPassengerFare: Double,
    val platformCommission: Double,
    val netDriverPayout: Double,
    val paymentMode: String = "Direct Bank Settlement",
    val isDemoData: Boolean = true
)

/**
 * Driver Financial Summary
 * Explicitly distinguishes demo/simulated figures vs live zero-state.
 */
data class DriverEarningsSummary(
    val isDemoData: Boolean = true,
    val todayNetEarnings: Double = 0.0,
    val weeklyNetEarnings: Double = 0.0,
    val monthlyNetEarnings: Double = 0.0,
    val todayCompletedTrips: Int = 0,
    val totalCompletedTrips: Int = 0,
    val driverRating: Double = 4.88,
    val hasLiveRating: Boolean = false,
    val pendingPayoutBalance: Double = 0.0,
    val platformCommissionDeductedToday: Double = 0.0,
    val recentTransactions: List<DriverEarningsTransaction> = emptyList()
)
