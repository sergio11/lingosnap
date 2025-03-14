package com.dreamsoftware.lingosnap.ui.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.dreamsoftware.lingosnap.ui.navigation.Screens

@Composable
fun MainNavigationGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Main.startDestination
    ) {
        HomeNavigationGraph(
            navController = navController
        )
    }
}