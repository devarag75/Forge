
package com.devarag.forge.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.devarag.forge.navigation.bottomItems
import com.devarag.forge.ui.design.ForgeColors

private val NavBackground = Color(0xFF171720)
private val NavSelectedBackground = Color(0xFF35234F)
private val NavSelectedColor = Color(0xFFB879FF)
private val NavUnselectedColor = Color(0xFF858594)

@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(
                horizontal = 20.dp,
                vertical = 10.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(NavBackground)
                .padding(
                    horizontal = 8.dp,
                    vertical = 9.dp
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomItems.forEach { item ->

                val selected = currentRoute == item.screen.route

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(18.dp))
                        .clickable {
                            if (!selected) {
                                navController.navigate(item.screen.route) {
                                    popUpTo(
                                        navController.graph.startDestinationId
                                    ) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                        .padding(
                            horizontal = 2.dp,
                            vertical = 2.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                if (selected) {
                                    NavSelectedBackground
                                } else {
                                    Color.Transparent
                                }
                            )
                            .padding(
                                horizontal = 15.dp,
                                vertical = 5.dp
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.screen.title,
                            tint = if (selected) {
                                NavSelectedColor
                            } else {
                                NavUnselectedColor
                            },
                            modifier = Modifier.size(21.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = item.screen.title,
                        color = if (selected) {
                            NavSelectedColor
                        } else {
                            NavUnselectedColor
                        },
                        fontSize = 10.sp,
                        fontWeight = if (selected) {
                            FontWeight.SemiBold
                        } else {
                            FontWeight.Normal
                        },
                        maxLines = 1
                    )
                }
            }
        }
    }
}
