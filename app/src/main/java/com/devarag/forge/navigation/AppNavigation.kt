package com.devarag.forge.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.devarag.forge.ui.screens.FocusScreen
import com.devarag.forge.ui.screens.HomeScreen
import com.devarag.forge.ui.screens.InsightsScreen
import com.devarag.forge.ui.screens.ProfileScreen
import com.devarag.forge.ui.screens.CollegeScreen

@Composable
fun AppNavigation(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen()
        }

        composable(Screen.Focus.route) {
            FocusScreen()
        }

        composable(Screen.College.route) {
            CollegeScreen()
        }

        composable(Screen.Insights.route) {
            InsightsScreen()
        }

        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}