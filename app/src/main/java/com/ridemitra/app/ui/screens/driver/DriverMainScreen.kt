package com.ridemitra.app.ui.screens.driver

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ridemitra.app.navigation.BottomNavItem
import com.ridemitra.app.navigation.RideMitraBottomNavigation
import com.ridemitra.app.navigation.driverBottomNavItems
import com.ridemitra.app.ui.theme.BackgroundLight
import com.ridemitra.app.ui.theme.PrimaryNavy
import com.ridemitra.app.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun DriverMainScreen(
    onNavigateBackToRole: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentTab by remember { mutableStateOf<BottomNavItem>(BottomNavItem.DriverHome) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            RideMitraBottomNavigation(
                items = driverBottomNavItems,
                currentRoute = currentTab.route,
                onItemClick = { item ->
                    currentTab = item
                    if (item != BottomNavItem.DriverHome) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Navigated to Driver ${item.title} (Phase 3 Full Screen)")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(innerPadding)
        ) {
            when (currentTab) {
                is BottomNavItem.DriverHome -> {
                    DriverDashboardScreen(
                        onSwitchRole = onNavigateBackToRole,
                        onActionClicked = { actionName ->
                            scope.launch {
                                snackbarHostState.showSnackbar("Driver Action: $actionName")
                            }
                        }
                    )
                }
                else -> {
                    DriverTabPlaceholderScreen(tabName = currentTab.title)
                }
            }
        }
    }
}

@Composable
private fun DriverTabPlaceholderScreen(tabName: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Driver $tabName Screen",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy
                )
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Text(
                text = "Full implementation scheduled for Phase 3",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary
                )
            )
        }
    }
}
