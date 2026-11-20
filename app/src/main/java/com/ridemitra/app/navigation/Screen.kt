package com.ridemitra.app.navigation

/**
 * RideMitra Navigation Routes
 */
sealed class Screen(val route: String) {
    // Onboarding & Gateway
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object RoleSelection : Screen("role_selection")

    // Role-based main hosts
    object PassengerMain : Screen("passenger_main")
    object DriverMain : Screen("driver_main")
    object AdminMain : Screen("admin_main")

    // Passenger main tabs
    object PassengerHome : Screen("passenger_home")
    object PassengerMyRides : Screen("passenger_my_rides")
    object PassengerTrack : Screen("passenger_track")
    object PassengerOffers : Screen("passenger_offers")
    object PassengerProfile : Screen("passenger_profile")

    // Passenger Phase 2 Specialized Screens & Booking Journey
    object AirportSelection : Screen("passenger_airport_selection")
    object FlightDetails : Screen("passenger_flight_details")
    object DestinationSearch : Screen("passenger_destination_search")
    object MapSelection : Screen("passenger_map_selection")
    object PassengerLuggageDetails : Screen("passenger_luggage_details")
    object DepartureTimeSelection : Screen("passenger_departure_time")
    object MaxWaitingTime : Screen("passenger_max_waiting_time")
    object MaxDetourPreference : Screen("passenger_max_detour")
    object RideCategorySelection : Screen("passenger_ride_category")
    object RideMatching : Screen("passenger_ride_matching")
    object MatchingResults : Screen("passenger_matching_results")
    object DriverVehicleDetails : Screen("passenger_driver_vehicle")
    object FareBreakdown : Screen("passenger_fare_breakdown")
    object BookingConfirmation : Screen("passenger_booking_confirmation")
    object RideHistory : Screen("passenger_ride_history")
    object PassengerNotifications : Screen("passenger_notifications")
    object SafetySos : Screen("passenger_safety_sos")

    // Driver tabs
    object DriverHome : Screen("driver_home")
    object DriverRequests : Screen("driver_requests")
    object DriverActiveRides : Screen("driver_active_rides")
    object DriverEarnings : Screen("driver_earnings")
    object DriverProfile : Screen("driver_profile")
}
