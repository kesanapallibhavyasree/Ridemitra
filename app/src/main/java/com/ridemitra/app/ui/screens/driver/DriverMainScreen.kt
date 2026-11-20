package com.ridemitra.app.ui.screens.driver

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.domain.model.DriverActiveRide
import com.ridemitra.app.domain.model.DriverApplication
import com.ridemitra.app.domain.model.DriverBookingRequest
import com.ridemitra.app.domain.model.DriverEarningsSummary
import com.ridemitra.app.domain.model.DriverEarningsTransaction
import com.ridemitra.app.domain.model.DriverIdentityDocs
import com.ridemitra.app.domain.model.DriverLicenseInfo
import com.ridemitra.app.domain.model.DriverPersonalInfo
import com.ridemitra.app.domain.model.DriverRideStatus
import com.ridemitra.app.domain.model.DriverVerificationStatus
import com.ridemitra.app.domain.model.RideOptionType
import com.ridemitra.app.domain.model.VehicleDetails
import com.ridemitra.app.domain.model.VehicleRegulatoryDocs
import com.ridemitra.app.navigation.BottomNavItem
import com.ridemitra.app.navigation.RideMitraBottomNavigation
import com.ridemitra.app.navigation.driverBottomNavItems
import com.ridemitra.app.ui.screens.driver.dashboard.DriverActiveRidesScreen
import com.ridemitra.app.ui.screens.driver.dashboard.DriverEarningsScreen
import com.ridemitra.app.ui.screens.driver.dashboard.DriverHomeScreen
import com.ridemitra.app.ui.screens.driver.dashboard.DriverRequestsScreen
import com.ridemitra.app.ui.screens.driver.profile.DriverProfileScreen
import com.ridemitra.app.ui.screens.driver.registration.DriverIdentityScreen
import com.ridemitra.app.ui.screens.driver.registration.DriverLicenseScreen
import com.ridemitra.app.ui.screens.driver.registration.DriverRegistrationScreen
import com.ridemitra.app.ui.screens.driver.registration.DriverReviewScreen
import com.ridemitra.app.ui.screens.driver.registration.VehicleDocumentsScreen
import com.ridemitra.app.ui.screens.driver.registration.VehicleRegistrationScreen
import com.ridemitra.app.ui.screens.driver.verification.DriverVerificationStatusScreen
import com.ridemitra.app.ui.theme.BackgroundLight
import com.ridemitra.app.ui.theme.PrimaryNavy
import com.ridemitra.app.ui.theme.RoyalBlue
import com.ridemitra.app.ui.theme.SuccessGreen
import com.ridemitra.app.ui.theme.WarningAmber
import kotlinx.coroutines.launch

/**
 * Driver Navigation Flow Modes
 */
enum class DriverFlowMode {
    DASHBOARD,
    VERIFICATION_STATUS,
    REG_STEP_1_PERSONAL,
    REG_STEP_2_IDENTITY,
    REG_STEP_3_LICENSE,
    REG_STEP_4_VEHICLE,
    REG_STEP_5_DOCUMENTS,
    REG_STEP_6_REVIEW
}

@Composable
fun DriverMainScreen(
    onNavigateBackToRole: () -> Unit,
    modifier: Modifier = Modifier
) {
    var flowMode by remember { mutableStateOf(DriverFlowMode.DASHBOARD) }
    var currentTab by remember { mutableStateOf<BottomNavItem>(BottomNavItem.DriverHome) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 1. Realistic Driver Application Data
    var application by remember {
        mutableStateOf(
            DriverApplication(
                applicationId = "RM-DRV-2026-8921",
                personalInfo = DriverPersonalInfo(
                    fullName = "Rajesh Kumar",
                    mobileNumber = "+91 98765 43210",
                    isMobileVerified = true,
                    email = "rajesh.kumar@drivermitra.in",
                    isEmailVerified = true,
                    dateOfBirth = "14/08/1988",
                    residentialAddress = "No. 42, 3rd Cross, Airport Road, Kodihalli, Bengaluru 560008",
                    profilePhotoUri = "content://media/driver_photo.jpg",
                    emergencyContactName = "Sunita Kumar (Spouse)",
                    emergencyContactNumber = "+91 98450 11223"
                ),
                identityDocs = DriverIdentityDocs(
                    documentType = "Aadhaar Card",
                    documentNumberMasked = "XXXX-XXXX-4920",
                    documentFrontUri = "content://media/aadhaar_front.jpg",
                    documentBackUri = "content://media/aadhaar_back.jpg",
                    panNumberMasked = "ABCDE****F",
                    panCardUri = "content://media/pan_card.jpg",
                    addressProofUri = "content://media/address_proof.jpg",
                    selfiePhotoUri = "content://media/live_selfie.jpg"
                ),
                licenseInfo = DriverLicenseInfo(
                    licenseNumber = "KA01-20150048291",
                    licenseFrontUri = "content://media/dl_front.jpg",
                    licenseBackUri = "content://media/dl_back.jpg",
                    issueDate = "15/03/2015",
                    expiryDate = "14/03/2035",
                    licenseCategory = "Commercial / LMV Transport",
                    isExpiryValid = true
                ),
                vehicleDetails = VehicleDetails(
                    registrationNumber = "KA 01 MJ 4829",
                    manufacturer = "Toyota",
                    model = "Etios (Commercial Sedan)",
                    manufacturingYear = "2022",
                    vehicleCategory = "Sedan",
                    passengerCapacityExclDriver = 4, // 4 passenger seats -> max 3 RideMitra passengers
                    luggageCapacityBags = 3,
                    vehicleFrontUri = "content://media/car_front.jpg",
                    vehicleRearUri = "content://media/car_rear.jpg"
                ),
                vehicleDocs = VehicleRegulatoryDocs(
                    rcNumber = "KA01MJ4829",
                    rcDocUri = "content://media/rc_doc.jpg",
                    insurancePolicyNumber = "BAJAJ-ALLIANZ-COMM-4892",
                    insuranceExpiryDate = "28/11/2026",
                    insuranceDocUri = "content://media/insurance.jpg",
                    pucExpiryDate = "10/05/2026",
                    pucDocUri = "content://media/puc.jpg",
                    fitnessExpiryDate = "14/03/2028",
                    fitnessDocUri = "content://media/fitness.jpg",
                    commercialPermitUri = "content://media/permit.jpg"
                ),
                status = DriverVerificationStatus.APPROVED,
                submissionDate = "18 Nov 2026, 10:30 AM",
                adminFeedback = "All documents verified and authorized for Bengaluru International Airport (BLR) operations."
            )
        )
    }

    // 2. Online / Offline state
    var isOnline by remember { mutableStateOf(true) }

    // 3. Incoming Booking Requests (Sample airport dispatch)
    var incomingRequests by remember {
        mutableStateOf(
            listOf(
                DriverBookingRequest(
                    requestId = "REQ-BLR-9021",
                    passengerPickup = "Kempegowda Int'l Airport (BLR) - Terminal 1",
                    destination = "Indiranagar 100ft Road, Bengaluru",
                    distanceKm = 38.5,
                    estimatedDurationMinutes = 52,
                    passengerCount = 2,
                    luggageSummary = "2 Trolleys + 1 Backpack",
                    requestedDepartureTime = "Today, in 15 mins",
                    estimatedDriverPayout = 540.0,
                    platformCommission = 54.0,
                    rideType = RideOptionType.SHARED_POOL,
                    flightNumber = "6E-512 (Landed)"
                ),
                DriverBookingRequest(
                    requestId = "REQ-BLR-9022",
                    passengerPickup = "Kempegowda Int'l Airport (BLR) - Terminal 2",
                    destination = "Whitefield ITPL Main Road",
                    distanceKm = 44.2,
                    estimatedDurationMinutes = 65,
                    passengerCount = 1,
                    luggageSummary = "1 Medium Strolley",
                    requestedDepartureTime = "Today, in 25 mins",
                    estimatedDriverPayout = 680.0,
                    platformCommission = 68.0,
                    rideType = RideOptionType.SOLO_RIDE, // Eligible for 10% platform discount; 100% driver payout protected
                    flightNumber = "AI-804 (Arrived)"
                )
            )
        )
    }

    // 4. Active Ongoing Ride
    var activeRide by remember {
        mutableStateOf<DriverActiveRide?>(
            DriverActiveRide(
                bookingId = "RM-BK-BLR-4891",
                passengerName = "Arjun Verma",
                passengerPhone = "+91 98450 99881",
                pickupPillar = "Terminal 1 - Pillar 4B (Carpool Bay)",
                destination = "Koramangala 4th Block, Bengaluru",
                distanceKm = 41.0,
                estimatedDurationMinutes = 55,
                passengerCount = 2,
                luggageSummary = "2 Large Check-in + 1 Cabin Bag",
                rideType = RideOptionType.SHARED_POOL,
                status = DriverRideStatus.ACCEPTED,
                expectedOtp = "4829",
                driverPayout = 580.0,
                remainingCashToCollect = 522.0
            )
        )
    }

    // 5. Driver Earnings Ledger
    var earningsSummary by remember {
        mutableStateOf(
            DriverEarningsSummary(
                todayNetEarnings = 1377.0,
                weeklyNetEarnings = 8450.0,
                monthlyNetEarnings = 34200.0,
                todayCompletedTrips = 2,
                totalCompletedTrips = 312,
                driverRating = 4.88,
                pendingPayoutBalance = 1377.0,
                platformCommissionDeductedToday = 153.0,
                recentTransactions = listOf(
                    DriverEarningsTransaction(
                        tripId = "TRIP-BLR-8921",
                        dateFormatted = "Today, 03:45 PM",
                        routeSummary = "BLR Airport T1 -> Whitefield ITPL",
                        totalPassengerFare = 890.0,
                        platformCommission = 89.0,
                        netDriverPayout = 801.0,
                        paymentMode = "NEFT direct to HDFC A/C **4920"
                    ),
                    DriverEarningsTransaction(
                        tripId = "TRIP-BLR-8910",
                        dateFormatted = "Today, 11:15 AM",
                        routeSummary = "BLR Airport T2 -> Koramangala 4th Block",
                        totalPassengerFare = 640.0,
                        platformCommission = 64.0,
                        netDriverPayout = 576.0,
                        paymentMode = "Instant IMPS to HDFC A/C **4920"
                    )
                )
            )
        )
    }

    // Developer test toolbar visibility
    var showDevBar by remember { mutableStateOf(true) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (flowMode == DriverFlowMode.DASHBOARD) {
                RideMitraBottomNavigation(
                    items = driverBottomNavItems,
                    currentRoute = currentTab.route,
                    onItemClick = { item ->
                        currentTab = item
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(innerPadding)
        ) {
            // Quick Developer & Review Toolbar (Enables testing all 7 verification states & onboarding wizard)
            AnimatedVisibility(visible = showDevBar) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PrimaryNavy)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Build,
                                contentDescription = null,
                                tint = WarningAmber,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "PHASE 3 DEV TEST BAR",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 10.sp
                                )
                            )
                        }

                        IconButton(
                            onClick = { showDevBar = false },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Bar",
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterChip(
                            selected = flowMode == DriverFlowMode.DASHBOARD,
                            onClick = { flowMode = DriverFlowMode.DASHBOARD },
                            label = { Text("Dashboard (5 Tabs)", fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = RoyalBlue,
                                selectedLabelColor = Color.White
                            )
                        )

                        FilterChip(
                            selected = flowMode == DriverFlowMode.VERIFICATION_STATUS,
                            onClick = { flowMode = DriverFlowMode.VERIFICATION_STATUS },
                            label = { Text("Verification Screen", fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = RoyalBlue,
                                selectedLabelColor = Color.White
                            )
                        )

                        FilterChip(
                            selected = flowMode == DriverFlowMode.REG_STEP_1_PERSONAL,
                            onClick = { flowMode = DriverFlowMode.REG_STEP_1_PERSONAL },
                            label = { Text("Onboarding Wizard (1-6)", fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = RoyalBlue,
                                selectedLabelColor = Color.White
                            )
                        )

                        // Quick switcher for the 7 Verification Statuses when testing
                        if (flowMode == DriverFlowMode.VERIFICATION_STATUS || flowMode == DriverFlowMode.DASHBOARD) {
                            Text(
                                text = "| Status:",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 10.sp
                            )
                            DriverVerificationStatus.values().forEach { st ->
                                FilterChip(
                                    selected = application.status == st,
                                    onClick = {
                                        application = application.copy(status = st)
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Switched status to: ${st.label}")
                                        }
                                    },
                                    label = { Text(st.name, fontSize = 9.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = SuccessGreen,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Main Flow Content
            Box(modifier = Modifier.weight(1f)) {
                when (flowMode) {
                    DriverFlowMode.DASHBOARD -> {
                        when (currentTab) {
                            is BottomNavItem.DriverHome -> {
                                DriverHomeScreen(
                                    application = application,
                                    earningsSummary = earningsSummary,
                                    incomingRequests = incomingRequests,
                                    isOnline = isOnline,
                                    onToggleOnline = { isOnline = it },
                                    onViewRequests = { currentTab = BottomNavItem.DriverRequests },
                                    onViewActiveRides = { currentTab = BottomNavItem.DriverActiveRides },
                                    onViewEarnings = { currentTab = BottomNavItem.DriverEarnings },
                                    onCheckVerificationStatus = { flowMode = DriverFlowMode.VERIFICATION_STATUS },
                                    onSwitchRole = onNavigateBackToRole
                                )
                            }

                            is BottomNavItem.DriverRequests -> {
                                DriverRequestsScreen(
                                    requests = incomingRequests,
                                    onAcceptRequest = { req ->
                                        // Transition request to active ride
                                        activeRide = DriverActiveRide(
                                            bookingId = "RM-BK-${req.requestId.takeLast(4)}",
                                            passengerName = "Verified Passenger",
                                            passengerPhone = "+91 98450 11990",
                                            pickupPillar = req.passengerPickup,
                                            destination = req.destination,
                                            distanceKm = req.distanceKm,
                                            estimatedDurationMinutes = req.estimatedDurationMinutes,
                                            passengerCount = req.passengerCount,
                                            luggageSummary = req.luggageSummary,
                                            rideType = req.rideType,
                                            status = DriverRideStatus.ACCEPTED,
                                            expectedOtp = "4829",
                                            driverPayout = req.estimatedDriverPayout,
                                            remainingCashToCollect = req.estimatedDriverPayout * 0.90
                                        )
                                        incomingRequests = incomingRequests.filter { it.requestId != req.requestId }
                                        currentTab = BottomNavItem.DriverActiveRides
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Request ${req.requestId} accepted! Active trip started.")
                                        }
                                    },
                                    onRejectRequest = { req ->
                                        incomingRequests = incomingRequests.filter { it.requestId != req.requestId }
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Request ${req.requestId} declined.")
                                        }
                                    }
                                )
                            }

                            is BottomNavItem.DriverActiveRides -> {
                                DriverActiveRidesScreen(
                                    activeRide = activeRide,
                                    onUpdateStatus = { newStatus ->
                                        activeRide = activeRide?.copy(status = newStatus)
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Ride status updated to: ${newStatus.label}")
                                        }
                                    },
                                    onVerifyOtpAndStart = { otp ->
                                        if (otp == activeRide?.expectedOtp) {
                                            activeRide = activeRide?.copy(status = DriverRideStatus.IN_PROGRESS)
                                            true
                                        } else {
                                            false
                                        }
                                    },
                                    onCompleteTrip = {
                                        activeRide?.let { completed ->
                                            val newTransaction = DriverEarningsTransaction(
                                                tripId = "TRIP-${completed.bookingId.takeLast(4)}",
                                                dateFormatted = "Just now",
                                                routeSummary = "${completed.pickupPillar.take(15)} -> ${completed.destination}",
                                                totalPassengerFare = completed.driverPayout * 1.10,
                                                platformCommission = completed.driverPayout * 0.10,
                                                netDriverPayout = completed.driverPayout,
                                                paymentMode = "Direct Bank Settlement"
                                            )
                                            earningsSummary = earningsSummary.copy(
                                                todayNetEarnings = earningsSummary.todayNetEarnings + completed.driverPayout,
                                                todayCompletedTrips = earningsSummary.todayCompletedTrips + 1,
                                                totalCompletedTrips = earningsSummary.totalCompletedTrips + 1,
                                                recentTransactions = listOf(newTransaction) + earningsSummary.recentTransactions
                                            )
                                            activeRide = null
                                            scope.launch {
                                                snackbarHostState.showSnackbar("Trip completed! ₹${completed.driverPayout} added to Net Payouts.")
                                            }
                                        }
                                    }
                                )
                            }

                            is BottomNavItem.DriverEarnings -> {
                                DriverEarningsScreen(
                                    earningsSummary = earningsSummary,
                                    onDownloadStatement = {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Generating PDF statement for ${application.personalInfo.fullName}...")
                                        }
                                    },
                                    onRequestInstantPayout = {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Instant payout of ₹${earningsSummary.todayNetEarnings} queued to HDFC Bank A/C **4920.")
                                        }
                                    }
                                )
                            }

                            is BottomNavItem.DriverProfile -> {
                                DriverProfileScreen(
                                    application = application,
                                    onEditSection = { step ->
                                        flowMode = when (step) {
                                            1 -> DriverFlowMode.REG_STEP_1_PERSONAL
                                            2 -> DriverFlowMode.REG_STEP_2_IDENTITY
                                            3 -> DriverFlowMode.REG_STEP_3_LICENSE
                                            4 -> DriverFlowMode.REG_STEP_4_VEHICLE
                                            5 -> DriverFlowMode.REG_STEP_5_DOCUMENTS
                                            else -> DriverFlowMode.REG_STEP_6_REVIEW
                                        }
                                    },
                                    onViewVerificationStatus = {
                                        flowMode = DriverFlowMode.VERIFICATION_STATUS
                                    },
                                    onSwitchRole = onNavigateBackToRole
                                )
                            }

                            else -> {
                                DriverHomeScreen(
                                    application = application,
                                    earningsSummary = earningsSummary,
                                    incomingRequests = incomingRequests,
                                    isOnline = isOnline,
                                    onToggleOnline = { isOnline = it },
                                    onViewRequests = { currentTab = BottomNavItem.DriverRequests },
                                    onViewActiveRides = { currentTab = BottomNavItem.DriverActiveRides },
                                    onViewEarnings = { currentTab = BottomNavItem.DriverEarnings },
                                    onCheckVerificationStatus = { flowMode = DriverFlowMode.VERIFICATION_STATUS },
                                    onSwitchRole = onNavigateBackToRole
                                )
                            }
                        }
                    }

                    DriverFlowMode.VERIFICATION_STATUS -> {
                        DriverVerificationStatusScreen(
                            application = application,
                            onStartOrResumeRegistration = {
                                flowMode = DriverFlowMode.REG_STEP_1_PERSONAL
                            },
                            onEnterDashboardIfApproved = {
                                flowMode = DriverFlowMode.DASHBOARD
                                currentTab = BottomNavItem.DriverHome
                            },
                            onReUploadDocuments = { step ->
                                flowMode = when (step) {
                                    1 -> DriverFlowMode.REG_STEP_1_PERSONAL
                                    2 -> DriverFlowMode.REG_STEP_2_IDENTITY
                                    3 -> DriverFlowMode.REG_STEP_3_LICENSE
                                    4 -> DriverFlowMode.REG_STEP_4_VEHICLE
                                    5 -> DriverFlowMode.REG_STEP_5_DOCUMENTS
                                    else -> DriverFlowMode.REG_STEP_6_REVIEW
                                }
                            },
                            onBackClick = {
                                flowMode = DriverFlowMode.DASHBOARD
                            }
                        )
                    }

                    DriverFlowMode.REG_STEP_1_PERSONAL -> {
                        DriverRegistrationScreen(
                            initialData = application.personalInfo,
                            onSaveAndNext = { updatedPersonal ->
                                application = application.copy(personalInfo = updatedPersonal)
                                flowMode = DriverFlowMode.REG_STEP_2_IDENTITY
                            },
                            onBackClick = { flowMode = DriverFlowMode.DASHBOARD }
                        )
                    }

                    DriverFlowMode.REG_STEP_2_IDENTITY -> {
                        DriverIdentityScreen(
                            initialData = application.identityDocs,
                            onSaveAndNext = { updatedIdentity ->
                                application = application.copy(identityDocs = updatedIdentity)
                                flowMode = DriverFlowMode.REG_STEP_3_LICENSE
                            },
                            onBackClick = { flowMode = DriverFlowMode.REG_STEP_1_PERSONAL }
                        )
                    }

                    DriverFlowMode.REG_STEP_3_LICENSE -> {
                        DriverLicenseScreen(
                            initialData = application.licenseInfo,
                            onSaveAndNext = { updatedLicense ->
                                application = application.copy(licenseInfo = updatedLicense)
                                flowMode = DriverFlowMode.REG_STEP_4_VEHICLE
                            },
                            onBackClick = { flowMode = DriverFlowMode.REG_STEP_2_IDENTITY }
                        )
                    }

                    DriverFlowMode.REG_STEP_4_VEHICLE -> {
                        VehicleRegistrationScreen(
                            initialData = application.vehicleDetails,
                            onSaveAndNext = { updatedVehicle ->
                                application = application.copy(vehicleDetails = updatedVehicle)
                                flowMode = DriverFlowMode.REG_STEP_5_DOCUMENTS
                            },
                            onBackClick = { flowMode = DriverFlowMode.REG_STEP_3_LICENSE }
                        )
                    }

                    DriverFlowMode.REG_STEP_5_DOCUMENTS -> {
                        VehicleDocumentsScreen(
                            initialData = application.vehicleDocs,
                            onSaveAndNext = { updatedDocs ->
                                application = application.copy(vehicleDocs = updatedDocs)
                                flowMode = DriverFlowMode.REG_STEP_6_REVIEW
                            },
                            onBackClick = { flowMode = DriverFlowMode.REG_STEP_4_VEHICLE }
                        )
                    }

                    DriverFlowMode.REG_STEP_6_REVIEW -> {
                        DriverReviewScreen(
                            application = application,
                            onEditStep = { step ->
                                flowMode = when (step) {
                                    1 -> DriverFlowMode.REG_STEP_1_PERSONAL
                                    2 -> DriverFlowMode.REG_STEP_2_IDENTITY
                                    3 -> DriverFlowMode.REG_STEP_3_LICENSE
                                    4 -> DriverFlowMode.REG_STEP_4_VEHICLE
                                    5 -> DriverFlowMode.REG_STEP_5_DOCUMENTS
                                    else -> DriverFlowMode.REG_STEP_1_PERSONAL
                                }
                            },
                            onSubmitApplication = {
                                application = application.copy(
                                    status = DriverVerificationStatus.UNDER_REVIEW,
                                    submissionDate = "Just now"
                                )
                                flowMode = DriverFlowMode.VERIFICATION_STATUS
                                scope.launch {
                                    snackbarHostState.showSnackbar("Application submitted successfully! Queued for Admin Review.")
                                }
                            },
                            onBackClick = { flowMode = DriverFlowMode.REG_STEP_5_DOCUMENTS }
                        )
                    }
                }
            }
        }
    }
}
