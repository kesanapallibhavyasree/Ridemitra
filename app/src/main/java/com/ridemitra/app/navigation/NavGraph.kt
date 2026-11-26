package com.ridemitra.app.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ridemitra.app.ui.screens.admin.AdminMainScreen
import com.ridemitra.app.ui.screens.auth.RoleSelectionScreen
import com.ridemitra.app.ui.screens.driver.DriverMainScreen
import com.ridemitra.app.ui.screens.onboarding.OnboardingScreen
import com.ridemitra.app.ui.screens.passenger.PassengerMainScreen
import com.ridemitra.app.ui.screens.splash.SplashScreen

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

        // 6. Admin Portal - Phase 4 Complete Dashboard
        composable(Screen.AdminMain.route) {
            AdminMainScreen(
                onNavigateBackToRole = {
                    navController.navigate(Screen.RoleSelection.route) {
                        popUpTo(Screen.AdminMain.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
