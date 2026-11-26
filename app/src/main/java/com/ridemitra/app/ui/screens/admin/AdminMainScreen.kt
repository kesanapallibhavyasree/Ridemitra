package com.ridemitra.app.ui.screens.admin

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.domain.model.*
import com.ridemitra.app.ui.theme.BackgroundLight
import com.ridemitra.app.ui.theme.PrimaryNavy
import com.ridemitra.app.ui.theme.RoyalBlue
import kotlinx.coroutines.launch

enum class AdminSection {
    DASHBOARD,
    DRIVER_APPLICATIONS,
    DRIVER_MANAGEMENT,
    PASSENGER_MANAGEMENT,
    BOOKINGS,
    PAYMENTS,
    COMPLAINTS,
    ANALYTICS,
    NOTIFICATIONS,
    SETTINGS,
    DRIVER_APP_DETAIL
}

/**
 * AdminMainScreen - Phase 4 Complete Admin Dashboard
 * Replaces AdminPlaceholderScreen. Requires admin login before showing dashboard.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMainScreen(
    onNavigateBackToRole: () -> Unit
) {
    var isLoggedIn by remember { mutableStateOf(false) }
    var currentSection by remember { mutableStateOf(AdminSection.DASHBOARD) }
    var selectedDriverAppId by remember { mutableStateOf<String?>(null) }
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Admin data state (Demo data - clearly labelled)
    val dashboardStats by remember { mutableStateOf(AdminDashboardStats()) }
    var driverApplications by remember { mutableStateOf(sampleAdminDriverApplications) }
    var passengers by remember { mutableStateOf(sampleAdminPassengers) }
    val bookings by remember { mutableStateOf(sampleAdminBookings) }
    val payments by remember { mutableStateOf(sampleAdminPayments) }
    var complaints by remember { mutableStateOf(sampleAdminComplaints) }
    var notifications by remember { mutableStateOf(sampleAdminNotifications) }
    val analytics by remember { mutableStateOf(AdminAnalyticsSummary()) }
    val adminUser by remember { mutableStateOf(AdminUser()) }

    val unreadNotifications = notifications.count { !it.isRead }

    BackHandler(enabled = isLoggedIn && currentSection != AdminSection.DASHBOARD) {
        currentSection = AdminSection.DASHBOARD
    }

    if (!isLoggedIn) {
        AdminLoginScreen(
            onLoginSuccess = { isLoggedIn = true },
            onBack = onNavigateBackToRole
        )
        return
    }

    // Admin navigation drawer
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AdminNavigationDrawer(
                currentSection = currentSection,
                adminUser = adminUser,
                unreadNotifications = unreadNotifications,
                onSectionSelected = { section ->
                    currentSection = section
                    scope.launch { drawerState.close() }
                },
                onLogout = {
                    scope.launch { drawerState.close() }
                    isLoggedIn = false
                }
            )
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                AdminTopBar(
                    section = currentSection,
                    unreadNotifications = unreadNotifications,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onNotificationsClick = { currentSection = AdminSection.NOTIFICATIONS }
                )
            },
            bottomBar = {
                AdminBottomNavigation(
                    currentSection = currentSection,
                    unreadNotifications = unreadNotifications,
                    onSectionSelected = { currentSection = it }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundLight)
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = currentSection,
                    transitionSpec = {
                        fadeIn(tween(250)) togetherWith fadeOut(tween(200))
                    },
                    label = "adminSectionAnim"
                ) { section ->
                    when (section) {
                        AdminSection.DASHBOARD -> AdminDashboardScreen(
                            stats = dashboardStats,
                            notifications = notifications,
                            recentBookings = bookings,
                            pendingApplications = driverApplications.filter {
                                it.status == AdminDriverStatus.PENDING_VERIFICATION ||
                                        it.status == AdminDriverStatus.UNDER_REVIEW
                            },
                            recentComplaints = complaints,
                            onNavigateToDriverApps = { currentSection = AdminSection.DRIVER_APPLICATIONS },
                            onNavigateToPassengers = { currentSection = AdminSection.PASSENGER_MANAGEMENT },
                            onNavigateToBookings = { currentSection = AdminSection.BOOKINGS },
                            onNavigateToComplaints = { currentSection = AdminSection.COMPLAINTS },
                            onNavigateToAnalytics = { currentSection = AdminSection.ANALYTICS }
                        )

                        AdminSection.DRIVER_APPLICATIONS -> AdminDriverApplicationsScreen(
                            applications = driverApplications,
                            onViewApplication = { appId ->
                                selectedDriverAppId = appId
                                currentSection = AdminSection.DRIVER_APP_DETAIL
                            },
                            onApprove = { appId ->
                                driverApplications = driverApplications.map {
                                    if (it.applicationId == appId) it.copy(status = AdminDriverStatus.APPROVED) else it
                                }
                                scope.launch { snackbarHostState.showSnackbar("Application $appId approved. Backend confirmation required.") }
                            },
                            onReject = { appId ->
                                driverApplications = driverApplications.map {
                                    if (it.applicationId == appId) it.copy(status = AdminDriverStatus.REJECTED) else it
                                }
                                scope.launch { snackbarHostState.showSnackbar("Application $appId rejected. Driver will be notified.") }
                            }
                        )

                        AdminSection.DRIVER_APP_DETAIL -> {
                            val appId = selectedDriverAppId
                            val app = driverApplications.firstOrNull { it.applicationId == appId }
                            if (app != null) {
                                AdminDriverAppDetailScreen(
                                    application = app,
                                    onApprove = {
                                        driverApplications = driverApplications.map {
                                            if (it.applicationId == appId) it.copy(status = AdminDriverStatus.APPROVED) else it
                                        }
                                        currentSection = AdminSection.DRIVER_APPLICATIONS
                                        scope.launch { snackbarHostState.showSnackbar("Application approved. Awaiting backend confirmation.") }
                                    },
                                    onReject = { reason ->
                                        driverApplications = driverApplications.map {
                                            if (it.applicationId == appId) it.copy(status = AdminDriverStatus.REJECTED) else it
                                        }
                                        currentSection = AdminSection.DRIVER_APPLICATIONS
                                        scope.launch { snackbarHostState.showSnackbar("Application rejected: $reason") }
                                    },
                                    onRequestDocuments = { docs ->
                                        scope.launch { snackbarHostState.showSnackbar("Document request sent: $docs") }
                                    },
                                    onSuspend = {
                                        driverApplications = driverApplications.map {
                                            if (it.applicationId == appId) it.copy(status = AdminDriverStatus.SUSPENDED) else it
                                        }
                                        currentSection = AdminSection.DRIVER_APPLICATIONS
                                        scope.launch { snackbarHostState.showSnackbar("Driver suspended pending review.") }
                                    },
                                    onBack = { currentSection = AdminSection.DRIVER_APPLICATIONS }
                                )
                            }
                        }

                        AdminSection.DRIVER_MANAGEMENT -> AdminDriverManagementScreen(
                            drivers = sampleAdminDriverApplications,
                            onSuspend = { id ->
                                scope.launch { snackbarHostState.showSnackbar("Driver $id suspended.") }
                            },
                            onReactivate = { id ->
                                scope.launch { snackbarHostState.showSnackbar("Driver $id reactivated.") }
                            }
                        )

                        AdminSection.PASSENGER_MANAGEMENT -> AdminPassengerManagementScreen(
                            passengers = passengers,
                            onSuspend = { id ->
                                passengers = passengers.map {
                                    if (it.userId == id) it.copy(accountStatus = PassengerAccountStatus.SUSPENDED) else it
                                }
                                scope.launch { snackbarHostState.showSnackbar("Passenger $id suspended.") }
                            },
                            onRestore = { id ->
                                passengers = passengers.map {
                                    if (it.userId == id) it.copy(accountStatus = PassengerAccountStatus.ACTIVE) else it
                                }
                                scope.launch { snackbarHostState.showSnackbar("Passenger $id restored.") }
                            }
                        )

                        AdminSection.BOOKINGS -> AdminBookingManagementScreen(
                            bookings = bookings
                        )

                        AdminSection.PAYMENTS -> AdminPaymentManagementScreen(
                            payments = payments
                        )

                        AdminSection.COMPLAINTS -> AdminComplaintManagementScreen(
                            complaints = complaints,
                            onUpdateStatus = { id, status ->
                                complaints = complaints.map {
                                    if (it.complaintId == id) it.copy(status = status) else it
                                }
                                scope.launch { snackbarHostState.showSnackbar("Complaint $id updated to ${status.label}.") }
                            }
                        )

                        AdminSection.ANALYTICS -> AdminAnalyticsScreen(analytics = analytics)

                        AdminSection.NOTIFICATIONS -> AdminNotificationsScreen(
                            notifications = notifications,
                            onMarkRead = { id ->
                                notifications = notifications.map {
                                    if (it.notificationId == id) it.copy(isRead = true) else it
                                }
                            },
                            onMarkAllRead = {
                                notifications = notifications.map { it.copy(isRead = true) }
                            }
                        )

                        AdminSection.SETTINGS -> AdminSettingsScreen(
                            adminUser = adminUser,
                            onLogout = {
                                isLoggedIn = false
                                onNavigateBackToRole()
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdminTopBar(
    section: AdminSection,
    unreadNotifications: Int,
    onMenuClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    val title = when (section) {
        AdminSection.DASHBOARD -> "Admin Dashboard"
        AdminSection.DRIVER_APPLICATIONS -> "Driver Applications"
        AdminSection.DRIVER_APP_DETAIL -> "Application Review"
        AdminSection.DRIVER_MANAGEMENT -> "Driver Management"
        AdminSection.PASSENGER_MANAGEMENT -> "Passenger Management"
        AdminSection.BOOKINGS -> "Booking Management"
        AdminSection.PAYMENTS -> "Payment Management"
        AdminSection.COMPLAINTS -> "Complaint Management"
        AdminSection.ANALYTICS -> "Analytics & Reports"
        AdminSection.NOTIFICATIONS -> "Notifications"
        AdminSection.SETTINGS -> "Settings"
    }

    TopAppBar(
        title = {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    text = "RideMitra Operations",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    )
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, "Menu", tint = Color.White)
            }
        },
        actions = {
            BadgedBox(
                badge = {
                    if (unreadNotifications > 0) {
                        Badge(containerColor = Color(0xFFEF4444)) {
                            Text(
                                unreadNotifications.toString(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White, fontSize = 9.sp
                                )
                            )
                        }
                    }
                }
            ) {
                IconButton(onClick = onNotificationsClick) {
                    Icon(Icons.Default.Notifications, "Notifications", tint = Color.White)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryNavy)
    )
}

@Composable
private fun AdminBottomNavigation(
    currentSection: AdminSection,
    unreadNotifications: Int,
    onSectionSelected: (AdminSection) -> Unit
) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = currentSection == AdminSection.DASHBOARD,
            onClick = { onSectionSelected(AdminSection.DASHBOARD) },
            icon = { Icon(Icons.Default.Dashboard, null) },
            label = { Text("Dashboard", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryNavy,
                selectedTextColor = PrimaryNavy,
                indicatorColor = Color(0xFFEBF2FE)
            )
        )
        NavigationBarItem(
            selected = currentSection == AdminSection.DRIVER_APPLICATIONS,
            onClick = { onSectionSelected(AdminSection.DRIVER_APPLICATIONS) },
            icon = { Icon(Icons.Default.Assignment, null) },
            label = { Text("Drivers", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryNavy,
                selectedTextColor = PrimaryNavy,
                indicatorColor = Color(0xFFEBF2FE)
            )
        )
        NavigationBarItem(
            selected = currentSection == AdminSection.PASSENGER_MANAGEMENT,
            onClick = { onSectionSelected(AdminSection.PASSENGER_MANAGEMENT) },
            icon = { Icon(Icons.Default.People, null) },
            label = { Text("Passengers", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryNavy,
                selectedTextColor = PrimaryNavy,
                indicatorColor = Color(0xFFEBF2FE)
            )
        )
        NavigationBarItem(
            selected = currentSection == AdminSection.BOOKINGS,
            onClick = { onSectionSelected(AdminSection.BOOKINGS) },
            icon = { Icon(Icons.Default.DirectionsCar, null) },
            label = { Text("Bookings", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryNavy,
                selectedTextColor = PrimaryNavy,
                indicatorColor = Color(0xFFEBF2FE)
            )
        )
        NavigationBarItem(
            selected = currentSection == AdminSection.NOTIFICATIONS,
            onClick = { onSectionSelected(AdminSection.NOTIFICATIONS) },
            icon = {
                BadgedBox(badge = {
                    if (unreadNotifications > 0) {
                        Badge { Text(unreadNotifications.toString(), fontSize = 8.sp) }
                    }
                }) {
                    Icon(Icons.Default.Notifications, null)
                }
            },
            label = { Text("Alerts", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryNavy,
                selectedTextColor = PrimaryNavy,
                indicatorColor = Color(0xFFEBF2FE)
            )
        )
    }
}

@Composable
private fun AdminNavigationDrawer(
    currentSection: AdminSection,
    adminUser: AdminUser,
    unreadNotifications: Int,
    onSectionSelected: (AdminSection) -> Unit,
    onLogout: () -> Unit
) {
    ModalDrawerSheet(drawerContainerColor = Color.White) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryNavy)
                .padding(20.dp)
                .statusBarsPadding()
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(Color.White.copy(alpha = 0.2f), shape = MaterialTheme.shapes.medium),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AdminPanelSettings, null, tint = Color.White, modifier = Modifier.size(28.dp))
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    adminUser.name,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold, color = Color.White
                    )
                )
                Text(
                    adminUser.role.label,
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.7f))
                )
                Text(
                    adminUser.email,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val navItems = listOf(
            Triple(AdminSection.DASHBOARD, Icons.Default.Dashboard, "Dashboard"),
            Triple(AdminSection.DRIVER_APPLICATIONS, Icons.Default.Assignment, "Driver Applications"),
            Triple(AdminSection.DRIVER_MANAGEMENT, Icons.Default.DirectionsCar, "Driver Management"),
            Triple(AdminSection.PASSENGER_MANAGEMENT, Icons.Default.People, "Passenger Management"),
            Triple(AdminSection.BOOKINGS, Icons.Default.BookOnline, "Bookings"),
            Triple(AdminSection.PAYMENTS, Icons.Default.AccountBalanceWallet, "Payments"),
            Triple(AdminSection.COMPLAINTS, Icons.Default.Report, "Complaints"),
            Triple(AdminSection.ANALYTICS, Icons.Default.BarChart, "Analytics"),
            Triple(AdminSection.NOTIFICATIONS, Icons.Default.Notifications, "Notifications"),
            Triple(AdminSection.SETTINGS, Icons.Default.Settings, "Settings")
        )

        navItems.forEach { (section, icon, label) ->
            NavigationDrawerItem(
                icon = {
                    BadgedBox(badge = {
                        if (section == AdminSection.NOTIFICATIONS && unreadNotifications > 0) {
                            Badge { Text(unreadNotifications.toString()) }
                        }
                    }) {
                        Icon(icon, null)
                    }
                },
                label = { Text(label) },
                selected = currentSection == section,
                onClick = { onSectionSelected(section) },
                modifier = Modifier.padding(horizontal = 8.dp),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = Color(0xFFEBF2FE),
                    selectedIconColor = PrimaryNavy,
                    selectedTextColor = PrimaryNavy
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(8.dp))

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Logout, null, tint = Color(0xFFEF4444)) },
            label = {
                Text(
                    "Logout",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFFEF4444))
                )
            },
            selected = false,
            onClick = onLogout,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

