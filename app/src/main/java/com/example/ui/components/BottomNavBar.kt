package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentPurpleLight
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPure
import com.example.viewmodel.AppTab

private data class NavigationTabItem(
    val tab: AppTab,
    val title: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
    val tag: String
)

@Composable
fun BottomNavBar(
    selectedTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavigationTabItem(AppTab.HOME, "Home", Icons.Filled.Home, Icons.Outlined.Home, "tab_home"),
        NavigationTabItem(AppTab.FLIP_CLOCK, "Focus", Icons.Filled.Timer, Icons.Outlined.Timer, "tab_focus"),
        NavigationTabItem(AppTab.PROGRESS, "Progress", Icons.Filled.BarChart, Icons.Outlined.BarChart, "tab_progress"),
        NavigationTabItem(AppTab.PROFILE, "Profile", Icons.Filled.Person, Icons.Outlined.Person, "tab_profile")
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkCanvas.copy(alpha = 0.96f))
            .border(
                width = 1.dp,
                color = DarkCardBorder.copy(alpha = 0.6f),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = when (item.tab) {
                    AppTab.HOME -> selectedTab == AppTab.HOME
                    AppTab.FLIP_CLOCK -> selectedTab == AppTab.FLIP_CLOCK || selectedTab == AppTab.START_TASK
                    AppTab.PROGRESS -> selectedTab == AppTab.PROGRESS
                    AppTab.PROFILE -> selectedTab == AppTab.PROFILE
                    else -> false
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onTabSelected(item.tab) }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .testTag(item.tag)
                ) {
                    Icon(
                        imageVector = if (isSelected) item.filledIcon else item.outlinedIcon,
                        contentDescription = item.title,
                        tint = if (isSelected) AccentPurpleLight else TextMuted,
                        modifier = Modifier.size(22.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.title,
                        color = if (isSelected) TextPure else TextMuted,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
