package com.example

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.model.AmbientSound
import com.example.model.FocusMode
import com.example.model.Subject
import com.example.model.TimeFilter
import com.example.viewmodel.AppTab
import com.example.viewmodel.StudyQuestViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class StudyQuestViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var application: Application
    private lateinit var viewModel: StudyQuestViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        application = ApplicationProvider.getApplicationContext()
        viewModel = StudyQuestViewModel(application)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialStateValues() = runTest(testDispatcher) {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(AppTab.HOME, state.currentTab)
        assertEquals(Subject.MATH, state.selectedSubject)
        assertEquals(FocusMode.DEEP_FOCUS, state.focusMode)
        assertEquals(45 * 60, state.timerTotalSeconds)
        assertEquals(45 * 60, state.timerRemainingSeconds)
        assertFalse(state.isTimerRunning)
        assertFalse(state.isTimerPaused)
    }

    @Test
    fun testSubjectSelectionAndCustomSubject() = runTest(testDispatcher) {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        advanceUntilIdle()

        viewModel.setSubject(Subject.PHYSICS)
        advanceUntilIdle()
        assertEquals(Subject.PHYSICS, viewModel.uiState.value.selectedSubject)

        viewModel.setSubject(Subject.OTHER)
        viewModel.setCustomSubjectName("Quantum Computing")
        advanceUntilIdle()
        assertEquals(Subject.OTHER, viewModel.uiState.value.selectedSubject)
        assertEquals("Quantum Computing", viewModel.uiState.value.customSubjectName)
    }

    @Test
    fun testDurationPresetsSelection() = runTest(testDispatcher) {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        advanceUntilIdle()

        viewModel.setDurationPreset(25)
        advanceUntilIdle()
        assertEquals(25 * 60, viewModel.uiState.value.timerTotalSeconds)
        assertEquals(25 * 60, viewModel.uiState.value.timerRemainingSeconds)

        viewModel.setDurationPreset(60)
        advanceUntilIdle()
        assertEquals(60 * 60, viewModel.uiState.value.timerTotalSeconds)
        assertEquals(60 * 60, viewModel.uiState.value.timerRemainingSeconds)
    }

    @Test
    fun testFocusModeAndAmbientSoundSelection() = runTest(testDispatcher) {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        advanceUntilIdle()

        viewModel.setFocusMode(FocusMode.STANDARD)
        advanceUntilIdle()
        assertEquals(FocusMode.STANDARD, viewModel.uiState.value.focusMode)

        viewModel.setAmbientSound(AmbientSound.RAIN)
        advanceUntilIdle()
        assertEquals(AmbientSound.RAIN, viewModel.uiState.value.ambientSound)
    }

    @Test
    fun testTimerStartPauseResume() = runTest(testDispatcher) {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        testScheduler.runCurrent()

        viewModel.setDurationPreset(45)
        testScheduler.runCurrent()
        assertEquals(45 * 60, viewModel.uiState.value.timerRemainingSeconds)

        viewModel.startTimer()
        testScheduler.runCurrent()
        assertTrue(viewModel.uiState.value.isTimerRunning)
        assertFalse(viewModel.uiState.value.isTimerPaused)

        testScheduler.advanceTimeBy(3000)
        testScheduler.runCurrent()

        viewModel.pauseTimer()
        testScheduler.runCurrent()
        assertTrue(viewModel.uiState.value.isTimerPaused)
        assertTrue(viewModel.uiState.value.isTimerRunning)

        viewModel.resumeTimer()
        testScheduler.runCurrent()
        assertFalse(viewModel.uiState.value.isTimerPaused)
        assertTrue(viewModel.uiState.value.isTimerRunning)
    }

    @Test
    fun testTabNavigationAndFlipClock() = runTest(testDispatcher) {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        advanceUntilIdle()

        viewModel.selectTab(AppTab.START_TASK)
        advanceUntilIdle()
        assertEquals(AppTab.START_TASK, viewModel.uiState.value.currentTab)

        viewModel.selectTab(AppTab.FLIP_CLOCK)
        advanceUntilIdle()
        assertEquals(AppTab.FLIP_CLOCK, viewModel.uiState.value.currentTab)

        viewModel.selectTab(AppTab.PROGRESS)
        advanceUntilIdle()
        assertEquals(AppTab.PROGRESS, viewModel.uiState.value.currentTab)

        viewModel.selectTab(AppTab.PROFILE)
        advanceUntilIdle()
        assertEquals(AppTab.PROFILE, viewModel.uiState.value.currentTab)

        viewModel.toggleOrientation()
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.isLandscapeOrientation)

        viewModel.setProgressTimeFilter(TimeFilter.MONTHLY)
        advanceUntilIdle()
        assertEquals(TimeFilter.MONTHLY, viewModel.uiState.value.progressTimeFilter)
    }
}
