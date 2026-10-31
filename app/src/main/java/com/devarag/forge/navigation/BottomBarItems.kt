package com.devarag.forge.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomBarItem(
    val screen: Screen,
    val icon: ImageVector
)

val bottomItems = listOf(
    BottomBarItem(Screen.Home, Icons.Default.Home),
    BottomBarItem(Screen.Focus, Icons.Default.PlayArrow),
    BottomBarItem(Screen.College, Icons.Default.DateRange),
    BottomBarItem(Screen.Insights, Icons.Default.Settings),
    BottomBarItem(Screen.Profile, Icons.Default.Person)
)