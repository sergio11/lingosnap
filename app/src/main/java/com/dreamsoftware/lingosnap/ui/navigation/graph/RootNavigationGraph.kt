package com.dreamsoftware.lingosnap.ui.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dreamsoftware.lingosnap.ui.navigation.Screens
import com.dreamsoftware.lingosnap.ui.navigation.utils.navigateSingleTopTo
import com.dreamsoftware.lingosnap.ui.screens.account.onboarding.OnboardingScreen
import com.dreamsoftware.lingosnap.ui.screens.account.signin.SignInScreen
import com.dreamsoftware.lingosnap.ui.screens.account.signup.SignUpScreen
import com.dreamsoftware.lingosnap.ui.screens.account.splash.SplashScreen
import com.dreamsoftware.lingosnap.ui.screens.main.MainScreen

@Composable
fun RootNavigationGraph(
    navController: NavHostController
) {
    NavHost(
        startDestination = Screens.Splash.route,
        navController = navController
    ) {
        composable(
            route = Screens.Splash.route
        ) {
            with(navController) {
                SplashScreen(
                    onGoToOnboarding = {
                        navigate(Screens.Onboarding.route)
                    },
                    onGoToHome = {
                        navigateSingleTopTo(Screens.Main.route)
                    }
                )
            }
        }

        composable(
            route = Screens.Onboarding.route
        ) {
            with(navController) {
                OnboardingScreen(
                    onGoToSignIn = {
                        navigate(Screens.SignIn.route)
                    },
                    onGoToSignUp = {
                        navigate(Screens.SignUp.route)
                    }
                )
            }
        }

        composable(
            route = Screens.SignIn.route
        ) {
            with(navController) {
                SignInScreen(
                    onAuthenticated = {
                        navigateSingleTopTo(Screens.Main.route)
                    },
                    onBackPressed = {
                        popBackStack()
                    }
                )
            }
        }

        composable(
            route = Screens.SignUp.route
        ) {
            with(navController) {
                SignUpScreen(
                    onGoToSignIn = {
                        navigate(Screens.SignIn.route)
                    },
                    onGoToHome = {
                        navigateSingleTopTo(Screens.Main.route)
                    },
                    onBackPressed = {
                        popBackStack()
                    }
                )
            }
        }

        composable(
            route = Screens.Main.route
        ) {
            MainScreen()
        }
    }
}