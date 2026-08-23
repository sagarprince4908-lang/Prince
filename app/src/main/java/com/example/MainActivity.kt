package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.BottomNavBar
import com.example.ui.components.CustomDurationDialog
import com.example.ui.components.EditNameDialog
import com.example.ui.components.LevelUpDialog
import com.example.ui.components.QuestCompleteDialog
import com.example.ui.screens.FlipClockScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.screens.StartTaskScreen
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.StudyQuestTheme
import com.example.viewmodel.AppTab
import com.example.viewmodel.StudyQuestViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: StudyQuestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val themeId = uiState.userProfile?.selectedTheme ?: "theme_cyber_dark"

            val context = LocalContext.current
            LaunchedEffect(uiState.toastMessage) {
                uiState.toastMessage?.let { message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    viewModel.clearToast()
                }
            }

            StudyQuestTheme(themeId = themeId) {
                val showBottomNav = uiState.currentTab != AppTab.ONBOARDING &&
                        uiState.currentTab != AppTab.START_TASK &&
                        !(uiState.currentTab == AppTab.FLIP_CLOCK && uiState.isLandscapeOrientation)

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DarkCanvas),
                    containerColor = DarkCanvas,
                    bottomBar = {
                        if (showBottomNav) {
                            Box(modifier = Modifier.navigationBarsPadding()) {
                                BottomNavBar(
                                    selectedTab = uiState.currentTab,
                                    onTabSelected = { viewModel.selectTab(it) }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .padding(innerPadding)
                    ) {
                        when (uiState.currentTab) {
                            AppTab.ONBOARDING -> OnboardingScreen(viewModel = viewModel)
                            AppTab.HOME -> HomeScreen(uiState = uiState, viewModel = viewModel)
                            AppTab.START_TASK -> StartTaskScreen(uiState = uiState, viewModel = viewModel)
                            AppTab.FLIP_CLOCK -> FlipClockScreen(uiState = uiState, viewModel = viewModel)
                            AppTab.PROGRESS -> ProgressScreen(uiState = uiState, viewModel = viewModel)
                            AppTab.PROFILE -> ProfileScreen(uiState = uiState, viewModel = viewModel)
                        }
                    }
                }

                // Session Completion Modal
                if (uiState.showCompletionDialog && uiState.completionResult != null) {
                    QuestCompleteDialog(
                        result = uiState.completionResult!!,
                        onDismiss = { viewModel.dismissCompletionDialog() }
                    )
                }

                // Milestone / Level Up Modal
                if (uiState.showLevelUpDialog) {
                    LevelUpDialog(
                        levelInfo = uiState.levelInfo,
                        onDismiss = { viewModel.dismissLevelUpDialog() }
                    )
                }

                // Edit Callsign / Name Modal
                if (uiState.showEditNameDialog) {
                    EditNameDialog(
                        currentName = uiState.userProfile?.username ?: "Kazu",
                        onSave = { viewModel.updateUsername(it) },
                        onDismiss = { viewModel.dismissEditNameDialog() }
                    )
                }

                // Custom Duration Selector Modal
                if (uiState.showCustomDurationDialog) {
                    CustomDurationDialog(
                        initialMinutes = uiState.timerTotalSeconds / 60,
                        onConfirm = { viewModel.setCustomDurationMinutes(it) },
                        onDismiss = { viewModel.dismissCustomDurationDialog() }
                    )
                }
            }
        }
    }
}
