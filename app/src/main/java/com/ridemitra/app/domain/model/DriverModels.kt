package com.ridemitra.app.domain.model

/**
 * Driver Verification Status lifecycle.
 */
enum class DriverVerificationStatus(val label: String) {
    NOT_SUBMITTED("Registration Incomplete"),
    PENDING_VERIFICATION("Pending Verification"),
    UNDER_REVIEW("Under Admin Review"),
    APPROVED("Approved & Verified"),
    REJECTED("Action Required / Rejected"),
    SUSPENDED("Account Suspended"),
    EXPIRED("Documents Expired")
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
 */
data class DriverIdentityDocs(
    val documentType: String = "Aadhaar Card",
    val documentNumberMasked: String = "", // Never expose full sensitive ID
    val documentFrontUri: String? = null,
    val documentBackUri: String? = null,
    val panNumberMasked: String = "",
    val panCardUri: String? = null,
    val addressProofUri: String? = null,
    val selfiePhotoUri: String? = null
)

/**
 * Step 3: Driving Licence Information
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
 * Actual passenger capacity excluding driver - 1.
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
    // Strictly: vehicle capacity - 1
    val allowedRideMitraCapacity: Int get() = (passengerCapacityExclDriver - 1).coerceAtLeast(1)
}

/**
 * Step 5: Vehicle Regulatory Documents
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
    val commercialPermitUri: String? = null
)

/**
 * Full Driver Onboarding Application Record
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
    val reUploadRequiredStep: Int? = null
)

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
    val flightNumber: String? = null
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
    val remainingCashToCollect: Double = 486.0 // 90% balance from passenger
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
    val paymentMode: String = "Direct Bank Settlement"
)

/**
 * Driver Financial Summary
 */
data class DriverEarningsSummary(
    val todayNetEarnings: Double = 0.0,
    val weeklyNetEarnings: Double = 0.0,
    val monthlyNetEarnings: Double = 0.0,
    val todayCompletedTrips: Int = 0,
    val totalCompletedTrips: Int = 0,
    val driverRating: Double = 4.88,
    val pendingPayoutBalance: Double = 0.0,
    val platformCommissionDeductedToday: Double = 0.0,
    val recentTransactions: List<DriverEarningsTransaction> = emptyList()
)
