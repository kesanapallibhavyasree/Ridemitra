package com.ridemitra.app.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ridemitra.app.ui.components.RideMitraButton
import com.ridemitra.app.ui.components.RideMitraHeader
import com.ridemitra.app.ui.screens.auth.RoleSelectionScreen
import com.ridemitra.app.ui.screens.driver.DriverMainScreen
import com.ridemitra.app.ui.screens.onboarding.OnboardingScreen
import com.ridemitra.app.ui.screens.passenger.PassengerMainScreen
import com.ridemitra.app.ui.screens.splash.SplashScreen
import com.ridemitra.app.ui.theme.BackgroundLight
import com.ridemitra.app.ui.theme.PrimaryNavy
import com.ridemitra.app.ui.theme.TextSecondary

@Composable
fun RideMitraNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier,
        enterTransition = {
            fadeIn(animationSpec = tween(350)) + slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(350)
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(350)) + slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(350)
            )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(350)) + slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(350)
            )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(350)) + slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(350)
            )
        }
    ) {
        // 1. Splash Screen
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateNext = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // 2. Onboarding Screen
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinishOnboarding = {
                    navController.navigate(Screen.RoleSelection.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        // 3. Role Selection Gateway
        composable(Screen.RoleSelection.route) {
            RoleSelectionScreen(
                onSelectPassenger = {
                    navController.navigate(Screen.PassengerMain.route)
                },
                onSelectDriver = {
                    navController.navigate(Screen.DriverMain.route)
                },
                onSelectAdmin = {
                    navController.navigate(Screen.AdminMain.route)
                }
            )
        }

        // 4. Passenger Experience
        composable(Screen.PassengerMain.route) {
            PassengerMainScreen(
                onNavigateBackToRole = {
                    navController.navigate(Screen.RoleSelection.route) {
                        popUpTo(Screen.PassengerMain.route) { inclusive = true }
                    }
                }
            )
        }

        // 5. Driver Experience
        composable(Screen.DriverMain.route) {
            DriverMainScreen(
                onNavigateBackToRole = {
                    navController.navigate(Screen.RoleSelection.route) {
                        popUpTo(Screen.DriverMain.route) { inclusive = true }
                    }
                }
            )
        }

        // 6. Admin Portal Preview (Phase 4)
        composable(Screen.AdminMain.route) {
            AdminPlaceholderScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
private fun AdminPlaceholderScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        RideMitraHeader(
            title = "Admin Portal",
            subtitle = "RideMitra Operations & Compliance",
            onBackClick = onBack
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Platform Administration",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = PrimaryNavy
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Admin management dashboard, vehicle approvals, passenger records, and analytics are scheduled for Phase 4.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
                RideMitraButton(
                    text = "Back to Roles",
                    onClick = onBack
                )
            }
        }
    }
}
