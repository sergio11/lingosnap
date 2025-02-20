package com.dreamsoftware.lingosnap.ui.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dreamsoftware.lingosnap.ui.navigation.Screens
import com.dreamsoftware.lingosnap.ui.screens.chat.ChatScreen
import com.dreamsoftware.lingosnap.ui.screens.create.CreateLingoSnapScreen
import com.dreamsoftware.lingosnap.ui.screens.detail.LingoSnapDetailScreen
import com.dreamsoftware.lingosnap.ui.screens.home.HomeScreen
import com.dreamsoftware.lingosnap.ui.screens.settings.SettingsScreen

fun NavGraphBuilder.HomeNavigationGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = Screens.Main.Home.startDestination,
        route = Screens.Main.Home.route
    ) {
        composable(
            route = Screens.Main.Home.Info.route
        ) {
            with(navController) {
                HomeScreen(
                    onGoToChat = {
                        navigate(Screens.Main.Home.Chat.buildRoute(it))
                    },
                    onGoToDetail = {
                        navigate(Screens.Main.Home.Detail.buildRoute(it))
                    }
                )
            }
        }

        composable(
            route = Screens.Main.Home.CreateLingoSnap.route
        ) {
            with(navController) {
                CreateLingoSnapScreen(
                    onGoToChat = {
                        popBackStack()
                        navigate(Screens.Main.Home.Chat.buildRoute(it))
                    },
                    onBackPressed = {
                        popBackStack()
                    }
                )
            }
        }

        composable(
            route = Screens.Main.Home.Detail.route
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                Screens.Main.Home.Detail.parseArgs(args)?.let {
                    with(navController) {
                        LingoSnapDetailScreen(
                            args = it,
                            onGoToChat = {
                                navigate(Screens.Main.Home.Chat.buildRoute(it))
                            },
                            onBackPressed = {
                                popBackStack()
                            }
                        )
                    }
                }
            }
        }

        composable(
            route = Screens.Main.Home.Chat.route
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                Screens.Main.Home.Chat.parseArgs(args)?.let {
                    with(navController) {
                        ChatScreen(
                            args = it,
                            onBackPressed = {
                                popBackStack()
                            }
                        )
                    }
                }
            }
        }

        composable(
            route = Screens.Main.Home.Settings.route
        ) {
            with(navController) {
                SettingsScreen(
                    onBackPressed = {
                        popBackStack()
                    }
                )
            }
        }
    }
}