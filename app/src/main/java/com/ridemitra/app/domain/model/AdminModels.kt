package com.ridemitra.app.domain.model

/**
 * RideMitra Admin Domain Models - Phase 4
 * All statistics marked as DEMO DATA until real backend is integrated.
 */

// ─── Admin Authentication ───────────────────────────────────────────────────

data class AdminUser(
    val adminId: String = "ADMIN-001",
    val name: String = "Platform Administrator",
    val email: String = "admin@ridemitra.in",
    val role: AdminRole = AdminRole.SUPER_ADMIN,
    val lastLogin: String = "25 Nov 2026, 04:10 PM"
)

enum class AdminRole(val label: String) {
    SUPER_ADMIN("Super Admin"),
    OPERATIONS_ADMIN("Operations Admin"),
    COMPLIANCE_ADMIN("Compliance Admin"),
    SUPPORT_ADMIN("Support Admin")
}

// ─── Dashboard Stats ─────────────────────────────────────────────────────────

data class AdminDashboardStats(
    val isDemoData: Boolean = true,
    val totalPassengers: Int = 1248,
    val totalDrivers: Int = 384,
    val pendingApplications: Int = 27,
    val approvedDrivers: Int = 312,
    val activeRides: Int = 18,
    val completedRidesToday: Int = 143,
    val cancelledRidesToday: Int = 11,
    val platformRevenueToday: Double = 42850.0,
    val platformRevenueMonth: Double = 1284000.0,
    val newPassengersToday: Int = 34,
    val newDriverApplicationsToday: Int = 5
)

// ─── Driver Application (Admin View) ─────────────────────────────────────────

enum class AdminDriverStatus(val label: String, val colorKey: String) {
    PENDING_VERIFICATION("Pending Verification", "warning"),
    UNDER_REVIEW("Under Review", "info"),
    APPROVED("Approved", "success"),
    REJECTED("Rejected", "error"),
    SUSPENDED("Suspended", "error"),
    EXPIRED("Expired", "warning")
}

data class AdminDriverApplicationSummary(
    val applicationId: String,
    val driverName: String,
    val driverId: String,
    val mobileNumberMasked: String,
    val vehicleRegNumber: String,
    val registrationDate: String,
    val status: AdminDriverStatus,
    val city: String = "Bengaluru",
    val isDemoData: Boolean = true
)

data class AdminDriverApplicationDetail(
    val summary: AdminDriverApplicationSummary,
    val personalInfo: DriverPersonalInfo,
    val identityDocs: DriverIdentityDocs,
    val licenseInfo: DriverLicenseInfo,
    val vehicleDetails: VehicleDetails,
    val vehicleDocs: VehicleRegulatoryDocs,
    val reviewingAdmin: String? = null,
    val reviewDate: String? = null,
    val rejectionReason: String? = null,
    val additionalDocsRequested: List<String> = emptyList(),
    // Verification checklist
    val identityChecked: Boolean = false,
    val licenseChecked: Boolean = false,
    val rcChecked: Boolean = false,
    val insuranceChecked: Boolean = false,
    val pucChecked: Boolean = false,
    val permitsChecked: Boolean = false
)

// ─── Passenger Management ─────────────────────────────────────────────────────

enum class PassengerAccountStatus(val label: String) {
    ACTIVE("Active"),
    SUSPENDED("Suspended"),
    UNDER_REVIEW("Under Review")
}

data class AdminPassengerSummary(
    val userId: String,
    val name: String,
    val mobileNumberMasked: String,
    val email: String,
    val registrationDate: String,
    val totalBookings: Int,
    val completedRides: Int,
    val cancelledRides: Int,
    val accountStatus: PassengerAccountStatus,
    val isDemoData: Boolean = true
)

// ─── Driver Management ───────────────────────────────────────────────────────

data class AdminDriverSummary(
    val driverId: String,
    val name: String,
    val verificationStatus: AdminDriverStatus,
    val vehicleInfo: String,
    val completedRides: Int,
    val rating: Double,
    val monthlyEarnings: Double,
    val licenseExpiry: String,
    val insuranceExpiry: String,
    val pucExpiry: String,
    val isDemoData: Boolean = true
)

// ─── Booking / Ride Management ───────────────────────────────────────────────

enum class AdminRideStatus(val label: String) {
    REQUESTED("Requested"),
    MATCHED("Matched"),
    CONFIRMED("Confirmed"),
    DRIVER_ARRIVING("Driver Arriving"),
    DRIVER_ARRIVED("Driver Arrived"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled")
}

data class AdminBookingSummary(
    val bookingId: String,
    val passengerName: String,
    val driverName: String,
    val pickupAirport: String,
    val destination: String,
    val rideType: String,
    val passengerCount: Int,
    val bookingTime: String,
    val scheduledDeparture: String,
    val fare: Double,
    val status: AdminRideStatus,
    val isDemoData: Boolean = true
)

// ─── Payment Management ───────────────────────────────────────────────────────

enum class PaymentStatus(val label: String) {
    PAID("Paid"),
    PENDING("Pending"),
    REFUNDED("Refunded"),
    FAILED("Failed"),
    PARTIAL_REFUND("Partial Refund")
}

data class AdminPaymentRecord(
    val transactionId: String,
    val bookingId: String,
    val passengerName: String,
    val driverName: String,
    val totalFare: Double,
    val advancePayment: Double,
    val remainingPayment: Double,
    val platformCommission: Double,
    val driverPayout: Double,
    val paymentStatus: PaymentStatus,
    val refundStatus: String = "N/A",
    val refundAmount: Double = 0.0,
    val paymentDate: String,
    val isDemoData: Boolean = true
) {
    companion object {
        /** Business Rule: Advance = 10% of total fare */
        fun calculateAdvance(totalFare: Double): Double = totalFare * 0.10

        /** Business Rule: Refund based on cancellation time */
        fun calculateRefund(totalFare: Double, minutesBeforeDeparture: Int): Double = when {
            minutesBeforeDeparture > 60 -> totalFare * 0.05 // 5% refund
            else -> 0.0 // No refund within 1 hour or after departure
        }
    }
}

// ─── Complaint Management ────────────────────────────────────────────────────

enum class ComplaintCategory(val label: String) {
    DRIVER_BEHAVIOUR("Driver Behaviour"),
    PASSENGER_BEHAVIOUR("Passenger Behaviour"),
    PAYMENT_ISSUE("Payment Issue"),
    BOOKING_ISSUE("Booking Issue"),
    SAFETY_CONCERN("Safety Concern"),
    VEHICLE_ISSUE("Vehicle Issue"),
    OTHER("Other")
}

enum class ComplaintStatus(val label: String) {
    OPEN("Open"),
    UNDER_REVIEW("Under Review"),
    ESCALATED("Escalated"),
    RESOLVED("Resolved"),
    CLOSED("Closed")
}

enum class ComplaintPriority(val label: String) {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    CRITICAL("Critical")
}

data class AdminComplaint(
    val complaintId: String,
    val reportedBy: String,
    val reportedByType: String, // "Passenger" or "Driver"
    val bookingId: String,
    val category: ComplaintCategory,
    val description: String,
    val submittedDate: String,
    val priority: ComplaintPriority,
    val status: ComplaintStatus,
    val internalNotes: String = "",
    val isDemoData: Boolean = true
)

// ─── Analytics ───────────────────────────────────────────────────────────────

enum class AnalyticsPeriod { DAILY, WEEKLY, MONTHLY }

data class AdminAnalyticsSummary(
    val period: AnalyticsPeriod = AnalyticsPeriod.WEEKLY,
    val isDemoData: Boolean = true,
    val totalBookings: Int = 1041,
    val sharedRides: Int = 723,
    val soloRides: Int = 318,
    val newPassengers: Int = 218,
    val newDriverApplications: Int = 42,
    val driverApprovalRate: Double = 74.3,
    val completedRides: Int = 963,
    val cancellationRate: Double = 7.5,
    val platformRevenue: Double = 298450.0,
    val driverPayouts: Double = 268605.0,
    val chartPoints: List<Pair<String, Double>> = listOf(
        Pair("Mon", 38200.0), Pair("Tue", 41500.0), Pair("Wed", 39800.0),
        Pair("Thu", 44100.0), Pair("Fri", 51200.0), Pair("Sat", 46800.0),
        Pair("Sun", 36850.0)
    )
)

// ─── Notifications ────────────────────────────────────────────────────────────

enum class AdminNotificationType(val label: String) {
    NEW_DRIVER_APPLICATION("New Driver Application"),
    DOCUMENT_REVIEW_NEEDED("Document Review Needed"),
    DOCUMENT_EXPIRING("Document Expiring"),
    NEW_COMPLAINT("New Complaint"),
    BOOKING_DISPUTE("Booking Dispute"),
    PAYMENT_ISSUE("Payment Issue"),
    SAFETY_REPORT("Safety Report")
}

data class AdminNotification(
    val notificationId: String,
    val type: AdminNotificationType,
    val title: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean = false,
    val relatedId: String? = null
)

// ─── Sample Demo Data ─────────────────────────────────────────────────────────

val sampleAdminDriverApplications = listOf(
    AdminDriverApplicationSummary(
        applicationId = "APP-DRV-2026-0051",
        driverName = "Mahesh Sharma",
        driverId = "DRV-20260051",
        mobileNumberMasked = "+91 98XXX X3920",
        vehicleRegNumber = "DL 01 AB 4821",
        registrationDate = "24 Nov 2026",
        status = AdminDriverStatus.PENDING_VERIFICATION
    ),
    AdminDriverApplicationSummary(
        applicationId = "APP-DRV-2026-0048",
        driverName = "Priya Nair",
        driverId = "DRV-20260048",
        mobileNumberMasked = "+91 97XXX X8812",
        vehicleRegNumber = "KA 03 MN 9201",
        registrationDate = "23 Nov 2026",
        status = AdminDriverStatus.UNDER_REVIEW
    ),
    AdminDriverApplicationSummary(
        applicationId = "APP-DRV-2026-0044",
        driverName = "Rajesh Kumar",
        driverId = "DRV-20260044",
        mobileNumberMasked = "+91 98XXX X3210",
        vehicleRegNumber = "KA 01 MJ 4829",
        registrationDate = "18 Nov 2026",
        status = AdminDriverStatus.APPROVED
    ),
    AdminDriverApplicationSummary(
        applicationId = "APP-DRV-2026-0040",
        driverName = "Suresh Verma",
        driverId = "DRV-20260040",
        mobileNumberMasked = "+91 96XXX X5544",
        vehicleRegNumber = "MH 02 CD 1239",
        registrationDate = "15 Nov 2026",
        status = AdminDriverStatus.REJECTED
    ),
    AdminDriverApplicationSummary(
        applicationId = "APP-DRV-2026-0038",
        driverName = "Anjali Singh",
        driverId = "DRV-20260038",
        mobileNumberMasked = "+91 95XXX X7721",
        vehicleRegNumber = "TN 01 AB 8821",
        registrationDate = "12 Nov 2026",
        status = AdminDriverStatus.SUSPENDED
    ),
    AdminDriverApplicationSummary(
        applicationId = "APP-DRV-2026-0035",
        driverName = "Kishore Kumar",
        driverId = "DRV-20260035",
        mobileNumberMasked = "+91 94XXX X1109",
        vehicleRegNumber = "KA 05 AB 1002",
        registrationDate = "10 Nov 2026",
        status = AdminDriverStatus.EXPIRED
    )
)

val sampleAdminPassengers = listOf(
    AdminPassengerSummary(
        userId = "PSG-20261001",
        name = "Arjun Verma",
        mobileNumberMasked = "+91 98XXX X9881",
        email = "arjun.v@example.com",
        registrationDate = "10 Oct 2026",
        totalBookings = 14,
        completedRides = 12,
        cancelledRides = 2,
        accountStatus = PassengerAccountStatus.ACTIVE
    ),
    AdminPassengerSummary(
        userId = "PSG-20261002",
        name = "Sneha Patel",
        mobileNumberMasked = "+91 97XXX X2211",
        email = "sneha.p@example.com",
        registrationDate = "15 Sep 2026",
        totalBookings = 8,
        completedRides = 7,
        cancelledRides = 1,
        accountStatus = PassengerAccountStatus.ACTIVE
    ),
    AdminPassengerSummary(
        userId = "PSG-20261003",
        name = "Rohit Mehra",
        mobileNumberMasked = "+91 96XXX X3301",
        email = "rohit.m@example.com",
        registrationDate = "02 Nov 2026",
        totalBookings = 3,
        completedRides = 1,
        cancelledRides = 2,
        accountStatus = PassengerAccountStatus.UNDER_REVIEW
    )
)

val sampleAdminBookings = listOf(
    AdminBookingSummary(
        bookingId = "RM-POOL-9821",
        passengerName = "Arjun Verma",
        driverName = "Rajesh Kumar",
        pickupAirport = "BLR - Terminal 1",
        destination = "Indiranagar, Bengaluru",
        rideType = "Shared Pool",
        passengerCount = 2,
        bookingTime = "25 Nov 2026, 03:45 PM",
        scheduledDeparture = "25 Nov 2026, 05:00 PM",
        fare = 890.0,
        status = AdminRideStatus.CONFIRMED
    ),
    AdminBookingSummary(
        bookingId = "RM-SOLO-9810",
        passengerName = "Sneha Patel",
        driverName = "Priya Nair",
        pickupAirport = "DEL - Terminal 3",
        destination = "Noida Sector 62",
        rideType = "Solo",
        passengerCount = 1,
        bookingTime = "25 Nov 2026, 02:30 PM",
        scheduledDeparture = "25 Nov 2026, 04:30 PM",
        fare = 1240.0,
        status = AdminRideStatus.IN_PROGRESS
    ),
    AdminBookingSummary(
        bookingId = "RM-POOL-9798",
        passengerName = "Rohit Mehra",
        driverName = "Suresh Verma",
        pickupAirport = "BOM - Terminal 2",
        destination = "Bandra West, Mumbai",
        rideType = "Shared Pool",
        passengerCount = 1,
        bookingTime = "24 Nov 2026, 11:00 AM",
        scheduledDeparture = "24 Nov 2026, 01:00 PM",
        fare = 740.0,
        status = AdminRideStatus.COMPLETED
    )
)

val sampleAdminPayments = listOf(
    AdminPaymentRecord(
        transactionId = "TXN-20261125-9821",
        bookingId = "RM-POOL-9821",
        passengerName = "Arjun Verma",
        driverName = "Rajesh Kumar",
        totalFare = 890.0,
        advancePayment = 89.0,
        remainingPayment = 801.0,
        platformCommission = 89.0,
        driverPayout = 801.0,
        paymentStatus = PaymentStatus.PARTIAL_REFUND,
        paymentDate = "25 Nov 2026"
    ),
    AdminPaymentRecord(
        transactionId = "TXN-20261124-9798",
        bookingId = "RM-POOL-9798",
        passengerName = "Rohit Mehra",
        driverName = "Suresh Verma",
        totalFare = 740.0,
        advancePayment = 74.0,
        remainingPayment = 666.0,
        platformCommission = 74.0,
        driverPayout = 666.0,
        paymentStatus = PaymentStatus.PAID,
        paymentDate = "24 Nov 2026"
    )
)

val sampleAdminComplaints = listOf(
    AdminComplaint(
        complaintId = "CMP-2026-0128",
        reportedBy = "Arjun Verma",
        reportedByType = "Passenger",
        bookingId = "RM-POOL-9798",
        category = ComplaintCategory.DRIVER_BEHAVIOUR,
        description = "Driver was rude and took a longer route without informing.",
        submittedDate = "24 Nov 2026, 03:00 PM",
        priority = ComplaintPriority.HIGH,
        status = ComplaintStatus.UNDER_REVIEW
    ),
    AdminComplaint(
        complaintId = "CMP-2026-0119",
        reportedBy = "Rajesh Kumar",
        reportedByType = "Driver",
        bookingId = "RM-SOLO-9810",
        category = ComplaintCategory.PAYMENT_ISSUE,
        description = "Platform commission deducted incorrectly for cancelled ride.",
        submittedDate = "23 Nov 2026, 09:15 AM",
        priority = ComplaintPriority.MEDIUM,
        status = ComplaintStatus.OPEN
    ),
    AdminComplaint(
        complaintId = "CMP-2026-0110",
        reportedBy = "Sneha Patel",
        reportedByType = "Passenger",
        bookingId = "RM-POOL-9780",
        category = ComplaintCategory.SAFETY_CONCERN,
        description = "Driver asked for personal contact details outside the app.",
        submittedDate = "20 Nov 2026, 06:30 PM",
        priority = ComplaintPriority.CRITICAL,
        status = ComplaintStatus.ESCALATED
    )
)

val sampleAdminNotifications = listOf(
    AdminNotification(
        notificationId = "NOTIF-001",
        type = AdminNotificationType.NEW_DRIVER_APPLICATION,
        title = "New Driver Application",
        message = "Mahesh Sharma has submitted a new driver application (APP-DRV-2026-0051).",
        timestamp = "25 Nov 2026, 03:48 PM",
        isRead = false
    ),
    AdminNotification(
        notificationId = "NOTIF-002",
        type = AdminNotificationType.SAFETY_REPORT,
        title = "Safety Concern Escalated",
        message = "Complaint CMP-2026-0110 has been escalated – safety concern reported by passenger.",
        timestamp = "25 Nov 2026, 02:30 PM",
        isRead = false
    ),
    AdminNotification(
        notificationId = "NOTIF-003",
        type = AdminNotificationType.DOCUMENT_EXPIRING,
        title = "Driver Document Expiring Soon",
        message = "Driver DRV-20260044's PUC certificate expires in 30 days. Review required.",
        timestamp = "25 Nov 2026, 10:00 AM",
        isRead = true
    ),
    AdminNotification(
        notificationId = "NOTIF-004",
        type = AdminNotificationType.NEW_COMPLAINT,
        title = "New Complaint Filed",
        message = "Driver Rajesh Kumar filed a payment dispute for booking RM-SOLO-9810.",
        timestamp = "23 Nov 2026, 09:17 AM",
        isRead = true
    ),
    AdminNotification(
        notificationId = "NOTIF-005",
        type = AdminNotificationType.DOCUMENT_REVIEW_NEEDED,
        title = "Documents Awaiting Review",
        message = "Priya Nair's application is under review. DL and RC documents need manual check.",
        timestamp = "23 Nov 2026, 08:00 AM",
        isRead = true
    )
)

