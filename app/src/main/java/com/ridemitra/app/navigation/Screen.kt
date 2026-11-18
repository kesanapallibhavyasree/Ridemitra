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

    // Passenger tabs
    object PassengerHome : Screen("passenger_home")
    object PassengerMyRides : Screen("passenger_my_rides")
    object PassengerTrack : Screen("passenger_track")
    object PassengerOffers : Screen("passenger_offers")
    object PassengerProfile : Screen("passenger_profile")

    // Driver tabs
    object DriverHome : Screen("driver_home")
    object DriverRequests : Screen("driver_requests")
    object DriverActiveRides : Screen("driver_active_rides")
    object DriverEarnings : Screen("driver_earnings")
    object DriverProfile : Screen("driver_profile")
}
