package com.dreamsoftware.lingosnap.ui.navigation

import android.os.Bundle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.dreamsoftware.lingosnap.ui.screens.chat.ChatScreenArgs
import com.dreamsoftware.lingosnap.ui.screens.detail.OutfitDetailScreenArgs

sealed class Screens(val route: String, arguments: List<NamedNavArgument> = emptyList()) {

    data object Splash: Screens("splash")
    data object Onboarding: Screens("onboarding")
    data object SignIn: Screens("sign_in")
    data object SignUp: Screens("sign_up")


    sealed class Main private constructor(route: String) : Screens(route) {
        companion object {
            const val route = "main"
            const val startDestination = Home.route
        }

        sealed class Home private constructor(route: String) : Screens(route) {
            companion object {
                const val route = "home"
                val startDestination = Info.route
            }
            data object Info : Screens("info")
            data object CreateOutfit : Screens("CreateOutfit")
            data object Settings: Screens("settings")
            data object Detail : Screens("detail/{outfit_id}", arguments = listOf(
                navArgument("outfit_id") {
                    type = NavType.StringType
                }
            )) {
                fun buildRoute(outfitId: String): String =
                    route.replace(
                        oldValue = "{outfit_id}",
                        newValue = outfitId
                    )

                fun parseArgs(args: Bundle): OutfitDetailScreenArgs? = with(args) {
                    getString("outfit_id")?.let {
                        OutfitDetailScreenArgs(id = it)
                    }
                }
            }
            data object Chat : Screens("chat/{outfit_id}", arguments = listOf(
                navArgument("outfit_id") {
                    type = NavType.StringType
                }
            )) {
                fun buildRoute(outfitId: String): String =
                    route.replace(
                        oldValue = "{outfit_id}",
                        newValue = outfitId
                    )

                fun parseArgs(args: Bundle): ChatScreenArgs? = with(args) {
                    getString("outfit_id")?.let {
                        ChatScreenArgs(id = it)
                    }
                }
            }
        }
    }
}