package com.ridemitra.app.domain.model

enum class NotificationType {
    RIDE_MATCH,
    TRIP_STATUS,
    FLIGHT_UPDATE,
    SAFETY_ALERT,
    PROMO
}

data class PassengerNotification(
    val id: String,
    val title: String,
    val message: String,
    val timestampFormatted: String,
    val type: NotificationType,
    val isRead: Boolean = false,
    val actionDeepLink: String? = null
)

val samplePassengerNotifications = listOf(
    PassengerNotification(
        id = "notif-001",
        title = "Ride Confirmed with 10% Advance Paid",
        message = "Your airport ride RM-POOL-8821 is confirmed. Advance of 10% has been secured. Meet driver at Terminal Pillar.",
        timestampFormatted = "10 mins ago",
        type = NotificationType.RIDE_MATCH,
        isRead = false
    ),
    PassengerNotification(
        id = "notif-002",
        title = "Flight Arrival Sync Ready",
        message = "Connect your arriving flight number to receive automated pickup time adjustments if your flight is delayed.",
        timestampFormatted = "1 hour ago",
        type = NotificationType.FLIGHT_UPDATE,
        isRead = false
    ),
    PassengerNotification(
        id = "notif-003",
        title = "Platform Solo Discount Active",
        message = "No matching co-traveler? Book a solo ride and get a guaranteed 10% platform discount with full driver payout.",
        timestampFormatted = "Yesterday",
        type = NotificationType.PROMO,
        isRead = true
    ),
    PassengerNotification(
        id = "notif-004",
        title = "RideMitra Safety Shield Verified",
        message = "24/7 Police SOS & live route tracking are enabled for all your airport journeys.",
        timestampFormatted = "2 days ago",
        type = NotificationType.SAFETY_ALERT,
        isRead = true
    )
)
