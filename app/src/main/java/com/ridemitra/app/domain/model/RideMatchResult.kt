package com.ridemitra.app.domain.model

data class CoPassenger(
    val name: String,
    val flightNumber: String,
    val dropLocation: String,
    val luggageSummary: String
)

data class DriverVehicleInfo(
    val driverName: String,
    val rating: Double,
    val vehicleModel: String,
    val vehicleNumber: String,
    val vehicleTotalCapacity: Int,
    val allowedPassengerCapacity: Int // Capacity - 1
)

data class RideMatchResult(
    val matchId: String,
    val rideType: RideOptionType,
    val destination: Destination,
    val fareEstimate: FareEstimate,
    val detourMinutes: Int = 0,
    val coPassengers: List<CoPassenger> = emptyList(),
    val driverVehicleInfo: DriverVehicleInfo? = null,
    val departureTimeFormatted: String = "19:15",
    val pickupPillar: String = "Terminal 3, Pillar 14B"
)
