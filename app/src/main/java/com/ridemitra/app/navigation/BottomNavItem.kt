package com.ridemitra.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val badgeCount: Int = 0
) {
    // Passenger Items
    object PassengerHome : BottomNavItem(Screen.PassengerHome.route, "Home", Icons.Default.Home)
    object PassengerMyRides : BottomNavItem(Screen.PassengerMyRides.route, "My Rides", Icons.Default.DirectionsCar)
    object PassengerTrack : BottomNavItem(Screen.PassengerTrack.route, "Track", Icons.Default.NearMe)
    object PassengerOffers : BottomNavItem(Screen.PassengerOffers.route, "Offers", Icons.Default.LocalOffer)
    object PassengerProfile : BottomNavItem(Screen.PassengerProfile.route, "Profile", Icons.Default.Person)

    // Driver Items
    object DriverHome : BottomNavItem(Screen.DriverHome.route, "Home", Icons.Default.Home)
    object DriverRequests : BottomNavItem(Screen.DriverRequests.route, "Requests", Icons.Default.Notifications, badgeCount = 2)
    object DriverActiveRides : BottomNavItem(Screen.DriverActiveRides.route, "Active", Icons.Default.DirectionsCar)
    object DriverEarnings : BottomNavItem(Screen.DriverEarnings.route, "Earnings", Icons.Default.AccountBalanceWallet)
    object DriverProfile : BottomNavItem(Screen.DriverProfile.route, "Profile", Icons.Default.Person)
}

val passengerBottomNavItems = listOf(
    BottomNavItem.PassengerHome,
    BottomNavItem.PassengerMyRides,
    BottomNavItem.PassengerTrack,
    BottomNavItem.PassengerOffers,
    BottomNavItem.PassengerProfile
)

val driverBottomNavItems = listOf(
    BottomNavItem.DriverHome,
    BottomNavItem.DriverRequests,
    BottomNavItem.DriverActiveRides,
    BottomNavItem.DriverEarnings,
    BottomNavItem.DriverProfile
)
