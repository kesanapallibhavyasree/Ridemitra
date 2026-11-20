package com.ridemitra.app.ui.screens.passenger

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ridemitra.app.domain.model.Airport
import com.ridemitra.app.domain.model.BookingStatus
import com.ridemitra.app.domain.model.Destination
import com.ridemitra.app.domain.model.DriverVehicleInfo
import com.ridemitra.app.domain.model.FareEstimate
import com.ridemitra.app.domain.model.FlightInfo
import com.ridemitra.app.domain.model.RideBooking
import com.ridemitra.app.domain.model.RideOptionType
import com.ridemitra.app.domain.model.RidePreferences
import com.ridemitra.app.domain.model.allAirportDestinations
import com.ridemitra.app.domain.model.indianAirportsList
import com.ridemitra.app.domain.model.sampleActiveBookings
import com.ridemitra.app.domain.model.sampleRideHistory
import com.ridemitra.app.domain.pricing.AirportPricingEngine
import com.ridemitra.app.navigation.BottomNavItem
import com.ridemitra.app.navigation.RideMitraBottomNavigation
import com.ridemitra.app.navigation.passengerBottomNavItems
import com.ridemitra.app.ui.theme.BackgroundLight
import kotlinx.coroutines.launch

/**
 * Passenger sub-screen destination enum inside the Passenger Experience.
 */
enum class PassengerSubScreen {
    HOME,
    AIRPORT_SELECTION,
    FLIGHT_DETAILS,
    DESTINATION_SEARCH,
    MAP_PREVIEW,
    PASSENGER_LUGGAGE,
    TRIP_TIMING,
    RIDE_CATEGORY,
    RIDE_MATCHING,
    MATCHING_RESULTS,
    DRIVER_VEHICLE_DETAILS,
    FARE_BREAKDOWN,
    BOOKING_CONFIRMATION,
    RIDE_HISTORY,
    NOTIFICATIONS,
    SAFETY_SOS
}

@Composable
fun PassengerMainScreen(
    onNavigateBackToRole: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentTab by remember { mutableStateOf<BottomNavItem>(BottomNavItem.PassengerHome) }
    var currentSubScreen by remember { mutableStateOf(PassengerSubScreen.HOME) }

    // Passenger Journey State across the session
    var selectedAirport by remember { mutableStateOf(indianAirportsList[0]) } // Default to DEL
    var selectedTerminal by remember { mutableStateOf(indianAirportsList[0].defaultTerminal) }
    var flightInfo by remember { mutableStateOf(FlightInfo()) }
    var selectedDestination by remember { mutableStateOf<Destination?>(allAirportDestinations[0]) }
    var ridePreferences by remember { mutableStateOf(RidePreferences()) }
    var activeBookingsList by remember { mutableStateOf(sampleActiveBookings) }
    var confirmedBooking by remember { mutableStateOf<RideBooking?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Dynamic Fare Calculation when destination and preferences are set
    val dynamicFare: FareEstimate = remember(selectedDestination, ridePreferences, selectedAirport) {
        val dest = selectedDestination ?: allAirportDestinations[0]
        AirportPricingEngine.calculateFare(
            distanceKm = dest.distanceKm,
            durationMinutes = dest.estimatedDurationMinutes,
            rideType = ridePreferences.rideType,
            passengerCount = ridePreferences.passengerCount,
            airportTollFee = selectedAirport.baseTollFee
        )
    }

    // Hardware back press handler
    BackHandler(enabled = currentSubScreen != PassengerSubScreen.HOME || currentTab != BottomNavItem.PassengerHome) {
        if (currentSubScreen != PassengerSubScreen.HOME) {
            currentSubScreen = when (currentSubScreen) {
                PassengerSubScreen.AIRPORT_SELECTION -> PassengerSubScreen.HOME
                PassengerSubScreen.FLIGHT_DETAILS -> PassengerSubScreen.HOME
                PassengerSubScreen.DESTINATION_SEARCH -> PassengerSubScreen.HOME
                PassengerSubScreen.MAP_PREVIEW -> PassengerSubScreen.DESTINATION_SEARCH
                PassengerSubScreen.PASSENGER_LUGGAGE -> PassengerSubScreen.HOME
                PassengerSubScreen.TRIP_TIMING -> PassengerSubScreen.PASSENGER_LUGGAGE
                PassengerSubScreen.RIDE_CATEGORY -> PassengerSubScreen.TRIP_TIMING
                PassengerSubScreen.RIDE_MATCHING -> PassengerSubScreen.RIDE_CATEGORY
                PassengerSubScreen.MATCHING_RESULTS -> PassengerSubScreen.RIDE_CATEGORY
                PassengerSubScreen.DRIVER_VEHICLE_DETAILS -> PassengerSubScreen.MATCHING_RESULTS
                PassengerSubScreen.FARE_BREAKDOWN -> PassengerSubScreen.DRIVER_VEHICLE_DETAILS
                PassengerSubScreen.BOOKING_CONFIRMATION -> PassengerSubScreen.HOME
                PassengerSubScreen.RIDE_HISTORY -> PassengerSubScreen.HOME
                PassengerSubScreen.NOTIFICATIONS -> PassengerSubScreen.HOME
                PassengerSubScreen.SAFETY_SOS -> PassengerSubScreen.HOME
                PassengerSubScreen.HOME -> PassengerSubScreen.HOME
            }
        } else {
            currentTab = BottomNavItem.PassengerHome
        }
    }

    val isTopLevelScreen = currentSubScreen == PassengerSubScreen.HOME

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (isTopLevelScreen) {
                RideMitraBottomNavigation(
                    items = passengerBottomNavItems,
                    currentRoute = currentTab.route,
                    onItemClick = { item ->
                        currentTab = item
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(if (isTopLevelScreen) innerPadding else androidx.compose.foundation.layout.PaddingValues())
        ) {
            if (currentSubScreen != PassengerSubScreen.HOME) {
                // Sub-screen navigation within Passenger experience
                AnimatedContent(
                    targetState = currentSubScreen,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(250)) togetherWith fadeOut(animationSpec = tween(200))
                    },
                    label = "subScreenAnim"
                ) { target ->
                    when (target) {
                        PassengerSubScreen.AIRPORT_SELECTION -> {
                            AirportSelectionScreen(
                                currentAirport = selectedAirport,
                                selectedTerminal = selectedTerminal,
                                onAirportSelected = { airport, terminal ->
                                    selectedAirport = airport
                                    selectedTerminal = terminal
                                    // Reset destination to matching airport
                                    selectedDestination = allAirportDestinations.firstOrNull { it.airportCode == airport.code }
                                    currentSubScreen = PassengerSubScreen.HOME
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Airport changed to ${airport.city} (${airport.code})")
                                    }
                                },
                                onBackClick = { currentSubScreen = PassengerSubScreen.HOME }
                            )
                        }

                        PassengerSubScreen.FLIGHT_DETAILS -> {
                            FlightDetailsScreen(
                                currentAirport = selectedAirport,
                                existingFlightInfo = flightInfo,
                                onSaveFlightInfo = { info ->
                                    flightInfo = info
                                    currentSubScreen = PassengerSubScreen.HOME
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Flight ${info.flightNumber} connected for delayed-pickup protection")
                                    }
                                },
                                onSkip = { currentSubScreen = PassengerSubScreen.HOME },
                                onBackClick = { currentSubScreen = PassengerSubScreen.HOME }
                            )
                        }

                        PassengerSubScreen.DESTINATION_SEARCH -> {
                            DestinationSearchScreen(
                                airport = selectedAirport,
                                currentDestination = selectedDestination,
                                onDestinationSelected = { dest ->
                                    selectedDestination = dest
                                    currentSubScreen = PassengerSubScreen.PASSENGER_LUGGAGE
                                },
                                onOpenMapPicker = {
                                    currentSubScreen = PassengerSubScreen.MAP_PREVIEW
                                },
                                onBackClick = { currentSubScreen = PassengerSubScreen.HOME }
                            )
                        }

                        PassengerSubScreen.MAP_PREVIEW -> {
                            MapDestinationScreen(
                                airport = selectedAirport,
                                initialDestination = selectedDestination,
                                onConfirmDestination = { dest ->
                                    selectedDestination = dest
                                    currentSubScreen = PassengerSubScreen.PASSENGER_LUGGAGE
                                },
                                onBackClick = { currentSubScreen = PassengerSubScreen.DESTINATION_SEARCH }
                            )
                        }

                        PassengerSubScreen.PASSENGER_LUGGAGE -> {
                            PassengerLuggageScreen(
                                currentPreferences = ridePreferences,
                                onSavePreferences = { prefs ->
                                    ridePreferences = prefs
                                    currentSubScreen = PassengerSubScreen.TRIP_TIMING
                                },
                                onBackClick = { currentSubScreen = PassengerSubScreen.DESTINATION_SEARCH }
                            )
                        }

                        PassengerSubScreen.TRIP_TIMING -> {
                            TripTimingPreferencesScreen(
                                currentPreferences = ridePreferences,
                                onSavePreferences = { prefs ->
                                    ridePreferences = prefs
                                    currentSubScreen = PassengerSubScreen.RIDE_CATEGORY
                                },
                                onBackClick = { currentSubScreen = PassengerSubScreen.PASSENGER_LUGGAGE }
                            )
                        }

                        PassengerSubScreen.RIDE_CATEGORY -> {
                            RideCategorySelectionScreen(
                                airport = selectedAirport,
                                destination = selectedDestination ?: allAirportDestinations[0],
                                preferences = ridePreferences,
                                onCategorySelected = { category ->
                                    ridePreferences = ridePreferences.copy(rideType = category)
                                    currentSubScreen = PassengerSubScreen.RIDE_MATCHING
                                },
                                onBackClick = { currentSubScreen = PassengerSubScreen.TRIP_TIMING }
                            )
                        }

                        PassengerSubScreen.RIDE_MATCHING -> {
                            RideMatchingScreen(
                                airport = selectedAirport,
                                destination = selectedDestination ?: allAirportDestinations[0],
                                rideType = ridePreferences.rideType,
                                onMatchFound = {
                                    currentSubScreen = PassengerSubScreen.MATCHING_RESULTS
                                },
                                onCancelMatching = {
                                    currentSubScreen = PassengerSubScreen.RIDE_CATEGORY
                                }
                            )
                        }

                        PassengerSubScreen.MATCHING_RESULTS -> {
                            MatchingResultsScreen(
                                airport = selectedAirport,
                                destination = selectedDestination ?: allAirportDestinations[0],
                                rideType = ridePreferences.rideType,
                                fareEstimate = dynamicFare,
                                onProceedToVehicleDetails = {
                                    currentSubScreen = PassengerSubScreen.DRIVER_VEHICLE_DETAILS
                                },
                                onSwitchToSoloAlternative = {
                                    ridePreferences = ridePreferences.copy(rideType = RideOptionType.SOLO_RIDE)
                                },
                                onBackClick = { currentSubScreen = PassengerSubScreen.RIDE_CATEGORY }
                            )
                        }

                        PassengerSubScreen.DRIVER_VEHICLE_DETAILS -> {
                            val defaultDriver = DriverVehicleInfo(
                                driverName = if (ridePreferences.rideType == RideOptionType.SHARED_POOL) "Suresh Verma" else "Vikram Singh",
                                rating = 4.88,
                                vehicleModel = if (ridePreferences.rideType == RideOptionType.SHARED_POOL) "Maruti Suzuki Dzire (White)" else "Hyundai Aura (Silver)",
                                vehicleNumber = if (ridePreferences.rideType == RideOptionType.SHARED_POOL) "DL 01 AB 9482" else "DL 01 AZ 3301",
                                vehicleTotalCapacity = 4,
                                allowedPassengerCapacity = 3 // Strictly Capacity - 1
                            )

                            DriverVehicleDetailsScreen(
                                airport = selectedAirport,
                                pickupPillar = "${selectedTerminal.split(" ")[0]} Pillar 14B",
                                driverInfo = defaultDriver,
                                onProceedToFareBreakdown = {
                                    currentSubScreen = PassengerSubScreen.FARE_BREAKDOWN
                                },
                                onBackClick = { currentSubScreen = PassengerSubScreen.MATCHING_RESULTS }
                            )
                        }

                        PassengerSubScreen.FARE_BREAKDOWN -> {
                            FareBreakdownScreen(
                                airport = selectedAirport,
                                destination = selectedDestination ?: allAirportDestinations[0],
                                rideType = ridePreferences.rideType,
                                fare = dynamicFare,
                                onProceedToConfirmation = {
                                    // Construct new confirmed booking
                                    val newBooking = RideBooking(
                                        bookingId = "RM-${if (ridePreferences.rideType == RideOptionType.SHARED_POOL) "POOL" else "SOLO"}-${(1000..9999).random()}",
                                        airport = selectedAirport,
                                        terminal = selectedTerminal,
                                        pickupPillar = "${selectedTerminal.split(" ")[0]} Pillar 14B",
                                        destination = selectedDestination ?: allAirportDestinations[0],
                                        rideType = ridePreferences.rideType,
                                        fareEstimate = dynamicFare.copy(isConfirmed = true),
                                        status = BookingStatus.CONFIRMED,
                                        scheduledDepartureTime = "20:30",
                                        passengerCount = ridePreferences.passengerCount,
                                        luggage = ridePreferences.luggage,
                                        driverVehicleInfo = DriverVehicleInfo(
                                            driverName = "Suresh Verma",
                                            rating = 4.88,
                                            vehicleModel = "Maruti Suzuki Dzire (White)",
                                            vehicleNumber = "DL 01 AB 9482",
                                            vehicleTotalCapacity = 4,
                                            allowedPassengerCapacity = 3
                                        ),
                                        flightNumber = if (flightInfo.isConfigured) flightInfo.flightNumber else null
                                    )
                                    confirmedBooking = newBooking
                                    activeBookingsList = listOf(newBooking) + activeBookingsList
                                    currentSubScreen = PassengerSubScreen.BOOKING_CONFIRMATION
                                },
                                onBackClick = { currentSubScreen = PassengerSubScreen.DRIVER_VEHICLE_DETAILS }
                            )
                        }

                        PassengerSubScreen.BOOKING_CONFIRMATION -> {
                            val bookingToShow = confirmedBooking ?: activeBookingsList.firstOrNull() ?: sampleActiveBookings[0]
                            BookingConfirmationScreen(
                                booking = bookingToShow,
                                onNavigateToHome = { currentSubScreen = PassengerSubScreen.HOME },
                                onViewMyRides = {
                                    currentSubScreen = PassengerSubScreen.HOME
                                    currentTab = BottomNavItem.PassengerMyRides
                                }
                            )
                        }

                        PassengerSubScreen.RIDE_HISTORY -> {
                            RideHistoryScreen(
                                onBackClick = { currentSubScreen = PassengerSubScreen.HOME }
                            )
                        }

                        PassengerSubScreen.NOTIFICATIONS -> {
                            PassengerNotificationsScreen(
                                onBackClick = { currentSubScreen = PassengerSubScreen.HOME }
                            )
                        }

                        PassengerSubScreen.SAFETY_SOS -> {
                            SafetySosScreen(
                                onBackClick = { currentSubScreen = PassengerSubScreen.HOME }
                            )
                        }

                        PassengerSubScreen.HOME -> Unit
                    }
                }
            } else {
                // Top-Level Bottom Navigation Tabs
                when (currentTab) {
                    is BottomNavItem.PassengerHome -> {
                        PassengerHomeScreen(
                            selectedAirport = selectedAirport,
                            selectedTerminal = selectedTerminal,
                            flightInfo = flightInfo,
                            activeBooking = activeBookingsList.firstOrNull(),
                            onSelectAirport = { currentSubScreen = PassengerSubScreen.AIRPORT_SELECTION },
                            onEnterFlightDetails = { currentSubScreen = PassengerSubScreen.FLIGHT_DETAILS },
                            onSearchDestination = { currentSubScreen = PassengerSubScreen.DESTINATION_SEARCH },
                            onOpenMapPicker = { currentSubScreen = PassengerSubScreen.MAP_PREVIEW },
                            onStartBookingFlow = { currentSubScreen = PassengerSubScreen.DESTINATION_SEARCH },
                            onOpenSafetySos = { currentSubScreen = PassengerSubScreen.SAFETY_SOS },
                            onOpenNotifications = { currentSubScreen = PassengerSubScreen.NOTIFICATIONS },
                            onSwitchRole = onNavigateBackToRole
                        )
                    }

                    is BottomNavItem.PassengerMyRides -> {
                        MyRidesScreen(
                            activeBookings = activeBookingsList,
                            onViewRideHistory = { currentSubScreen = PassengerSubScreen.RIDE_HISTORY },
                            onBookNewRide = { currentSubScreen = PassengerSubScreen.DESTINATION_SEARCH },
                            onRideCancelled = { bookingId ->
                                activeBookingsList = activeBookingsList.filterNot { it.bookingId == bookingId }
                                scope.launch {
                                    snackbarHostState.showSnackbar("Booking #$bookingId cancelled.")
                                }
                            }
                        )
                    }

                    is BottomNavItem.PassengerTrack -> {
                        PassengerTrackScreen(
                            activeBooking = activeBookingsList.firstOrNull(),
                            onBookNewRide = { currentSubScreen = PassengerSubScreen.DESTINATION_SEARCH }
                        )
                    }

                    is BottomNavItem.PassengerOffers -> {
                        OffersCouponsScreen(
                            onCouponApplied = { coupon ->
                                scope.launch {
                                    snackbarHostState.showSnackbar("Coupon ${coupon.code} applied: ${coupon.discountPercent}% Off")
                                }
                            }
                        )
                    }

                    is BottomNavItem.PassengerProfile -> {
                        PassengerProfileScreen(
                            onSwitchRole = onNavigateBackToRole,
                            onManageEmergencyContacts = { currentSubScreen = PassengerSubScreen.SAFETY_SOS }
                        )
                    }

                    else -> Unit
                }
            }
        }
    }
}
