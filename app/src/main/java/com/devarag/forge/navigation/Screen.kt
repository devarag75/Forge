package com.devarag.forge.navigation

sealed class Screen(
    val route: String,
    val title: String
) {
    object Home : Screen("home", "Home")

    object Focus : Screen("focus", "Focus")

    object College : Screen("college", "College")

    object Insights : Screen("insights", "Insights")

    object Profile : Screen("profile", "Profile")
}