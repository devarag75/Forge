
package com.devarag.forge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.devarag.forge.navigation.AppNavigation
import com.devarag.forge.ui.components.BottomNavigationBar
import com.devarag.forge.ui.design.ForgeColors

@Composable
fun ForgeApp() {

    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = ForgeColors.Background,
        contentWindowInsets = WindowInsets.safeDrawing,

        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ForgeColors.Background)
                    .navigationBarsPadding()
            ) {
                BottomNavigationBar(
                    navController = navController
                )
            }
        }

    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ForgeColors.Background)
                .padding(innerPadding)
        ) {
            AppNavigation(
                navController = navController
            )
        }
    }
}
