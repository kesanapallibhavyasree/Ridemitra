package com.ridemitra.app.domain.model

enum class FlightSyncStatus(val label: String) {
    NOT_CONNECTED("Not Connected"),
    MANUALLY_ENTERED("Entered by Passenger"),
    AWAITING_RADAR("Awaiting Radar Verification"),
    VERIFIED_ON_TIME("On Time"),
    VERIFIED_DELAYED("Delayed")
}

/**
 * Passenger flight details for terminal synchronization and delay-tolerant pickup.
 */
data class FlightInfo(
    val airline: String = "",
    val flightNumber: String = "",
    val originCity: String = "",
    val arrivalTime: String = "",
    val arrivalDate: String = "",
    val terminal: String = "",
    val syncStatus: FlightSyncStatus = FlightSyncStatus.NOT_CONNECTED,
    val delayMinutes: Int = 0,
    val baggageBelt: String? = null
) {
    val isConfigured: Boolean get() = flightNumber.isNotBlank()
}
