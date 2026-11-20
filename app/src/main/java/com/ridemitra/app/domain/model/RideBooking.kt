package com.ridemitra.app.domain.model

enum class BookingStatus(val label: String) {
    PENDING_MATCH("Matching Riders"),
    MATCHED("Matched"),
    CONFIRMED("Confirmed"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled")
}

data class RideBooking(
    val bookingId: String,
    val airport: Airport,
    val terminal: String,
    val pickupPillar: String,
    val destination: Destination,
    val rideType: RideOptionType,
    val fareEstimate: FareEstimate,
    val status: BookingStatus,
    val scheduledDepartureTime: String,
    val departureTimestampMs: Long = System.currentTimeMillis() + 3600_000L * 2, // 2 hours in future by default
    val passengerCount: Int = 1,
    val luggage: LuggageOption = LuggageOption.ONE_TROLLEY,
    val startOtp: String = "4829",
    val driverVehicleInfo: DriverVehicleInfo? = null,
    val coPassengers: List<CoPassenger> = emptyList(),
    val flightNumber: String? = null,
    val bookedAtFormatted: String = "Today, 19:10",
    val isSoloPlatformDiscountApplied: Boolean = false
) {
    /**
     * Cancellation policy evaluation:
     * - More than 1 hour before scheduled departure: refund 5% of total ride fare.
     * - Within 1 hour: 0% refund.
     * - After scheduled departure: 0% refund.
     */
    fun calculateCancellationRefund(currentTimeMs: Long = System.currentTimeMillis()): Double {
        val diffHours = (departureTimestampMs - currentTimeMs) / (1000.0 * 60 * 60)
        return if (diffHours > 1.0) {
            fareEstimate.eligibleCancellationRefund // 5% of total ride fare
        } else {
            0.0
        }
    }
}

val sampleActiveBookings = listOf(
    RideBooking(
        bookingId = "RM-POOL-8821",
        airport = indianAirportsList[0], // DEL
        terminal = "Terminal 3 (Int'l & Domestic)",
        pickupPillar = "T3 Pillar 14B",
        destination = allAirportDestinations[0], // DLF Cyber City
        rideType = RideOptionType.SHARED_POOL,
        fareEstimate = FareEstimate(
            originalFare = 540.0,
            discountAmount = 0.0,
            finalEstimatedFare = 540.0,
            advanceDepositRequired = 54.0,
            remainingBalancePayable = 486.0,
            eligibleCancellationRefund = 27.0,
            routeDistanceKm = 14.5,
            routeDurationMinutes = 24,
            isSolo = false,
            baseFare = 100.0,
            distanceCharge = 239.25,
            timeCharge = 48.0,
            airportTollFee = 120.0,
            isConfirmed = true
        ),
        status = BookingStatus.CONFIRMED,
        scheduledDepartureTime = "20:30",
        startOtp = "7291",
        driverVehicleInfo = DriverVehicleInfo(
            driverName = "Suresh Verma",
            rating = 4.88,
            vehicleModel = "Maruti Suzuki Dzire (White)",
            vehicleNumber = "DL 01 AB 9482",
            vehicleTotalCapacity = 4,
            allowedPassengerCapacity = 3 // Strictly Capacity - 1
        ),
        coPassengers = listOf(
            CoPassenger(
                name = "Ananya Sharma",
                flightNumber = "6E-204",
                dropLocation = "Cyber Hub, DLF Phase 2",
                luggageSummary = "1 Trolley"
            )
        ),
        flightNumber = "AI-802"
    )
)

val sampleRideHistory = listOf(
    RideBooking(
        bookingId = "RM-POOL-7104",
        airport = indianAirportsList[0],
        terminal = "Terminal 3",
        pickupPillar = "Pillar 8B",
        destination = allAirportDestinations[1], // Connaught Place
        rideType = RideOptionType.SHARED_POOL,
        fareEstimate = FareEstimate(
            originalFare = 620.0,
            discountAmount = 0.0,
            finalEstimatedFare = 620.0,
            advanceDepositRequired = 62.0,
            remainingBalancePayable = 558.0,
            eligibleCancellationRefund = 31.0,
            routeDistanceKm = 18.2,
            routeDurationMinutes = 35,
            isSolo = false,
            baseFare = 100.0,
            distanceCharge = 300.0,
            timeCharge = 70.0,
            airportTollFee = 120.0,
            isConfirmed = true
        ),
        status = BookingStatus.COMPLETED,
        scheduledDepartureTime = "14:15",
        bookedAtFormatted = "18 Nov, 14:00",
        driverVehicleInfo = DriverVehicleInfo(
            driverName = "Manoj Tiwari",
            rating = 4.90,
            vehicleModel = "Toyota Etios (Silver)",
            vehicleNumber = "DL 02 CK 1029",
            vehicleTotalCapacity = 4,
            allowedPassengerCapacity = 3
        ),
        coPassengers = listOf(
            CoPassenger(
                name = "Rahul Gupta",
                flightNumber = "UK-955",
                dropLocation = "Barakhamba Road",
                luggageSummary = "1 Backpack"
            )
        )
    ),
    RideBooking(
        bookingId = "RM-SOLO-6512",
        airport = indianAirportsList[1], // BOM
        terminal = "Terminal 2 (Sahar)",
        pickupPillar = "Level P4 Pillar P2",
        destination = allAirportDestinations[5], // BKC Mumbai
        rideType = RideOptionType.SOLO_RIDE,
        fareEstimate = FareEstimate(
            originalFare = 780.0,
            discountAmount = 78.0, // 10% platform solo promo
            finalEstimatedFare = 702.0,
            advanceDepositRequired = 70.0,
            remainingBalancePayable = 632.0,
            eligibleCancellationRefund = 35.0,
            routeDistanceKm = 11.2,
            routeDurationMinutes = 28,
            isSolo = true,
            baseFare = 100.0,
            distanceCharge = 184.8,
            timeCharge = 56.0,
            airportTollFee = 140.0,
            isConfirmed = true
        ),
        status = BookingStatus.COMPLETED,
        scheduledDepartureTime = "10:45",
        bookedAtFormatted = "12 Nov, 10:30",
        driverVehicleInfo = DriverVehicleInfo(
            driverName = "Sunil Patil",
            rating = 4.85,
            vehicleModel = "Hyundai Aura (White)",
            vehicleNumber = "MH 02 EE 7731",
            vehicleTotalCapacity = 4,
            allowedPassengerCapacity = 3
        ),
        isSoloPlatformDiscountApplied = true
    )
)
