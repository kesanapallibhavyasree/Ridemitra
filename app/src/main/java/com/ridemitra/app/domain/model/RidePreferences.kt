package com.ridemitra.app.domain.model

enum class RideOptionType {
    SHARED_POOL,
    SOLO_RIDE
}

enum class LuggageOption(val label: String) {
    HANDBAG_ONLY("Handbag Only"),
    ONE_TROLLEY("1 Cabin / Medium Bag"),
    TWO_OR_MORE("2+ Large Bags")
}

data class RidePreferences(
    val rideType: RideOptionType = RideOptionType.SHARED_POOL,
    val passengerCount: Int = 1,
    val luggage: LuggageOption = LuggageOption.ONE_TROLLEY,
    val departureTiming: String = "Ready Now",
    val maxWaitingMinutes: Int = 10,
    val maxDetourMinutes: Int = 15
)
