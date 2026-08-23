package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.os.BatteryManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SessionCompletionResult
import com.example.data.StudyQuestRepository
import com.example.data.local.AppDatabase
import com.example.data.local.entity.AchievementEntity
import com.example.data.local.entity.DailyQuestEntity
import com.example.data.local.entity.ShopItemEntity
import com.example.data.local.entity.StudySessionEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.model.AmbientSound
import com.example.model.FocusMode
import com.example.model.LevelInfo
import com.example.model.Subject
import com.example.model.TimeFilter
import com.example.util.LevelCalculator
import com.example.util.SoundHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class AppTab(val title: String, val icon: String) {
    HOME("Home", "🏠"),
    START_TASK("Start Task", "📝"),
    FLIP_CLOCK("Focus", "⏱️"),
    PROGRESS("Progress", "📊"),
    PROFILE("Profile", "👤"),
    ONBOARDING("Welcome", "✨")
}

data class StudyQuestUiState(
    val userProfile: UserProfileEntity? = null,
    val levelInfo: LevelInfo = LevelCalculator.calculateLevelInfo(2840),
    val todayQuests: List<DailyQuestEntity> = emptyList(),
    val allSessions: List<StudySessionEntity> = emptyList(),
    val achievements: List<AchievementEntity> = emptyList(),
    val shopItems: List<ShopItemEntity> = emptyList(),
    val currentTab: AppTab = AppTab.HOME,
    // Task Configuration
    val selectedSubject: Subject = Subject.MATH,
    val customSubjectName: String = "",
    val chapterOrObjective: String = "Chapter 03",
    val focusMode: FocusMode = FocusMode.DEEP_FOCUS,
    val ambientSound: AmbientSound = AmbientSound.NONE,
    val timerTotalSeconds: Int = 45 * 60,
    val timerRemainingSeconds: Int = 45 * 60,
    val isTimerRunning: Boolean = false,
    val isTimerPaused: Boolean = false,
    // Flip Clock Display & Orientation
    val isLandscapeOrientation: Boolean = false,
    val isIdleClockMode: Boolean = false,
    val idleCurrentHour: Int = 23,
    val idleCurrentMinute: Int = 21,
    val idleCurrentSecond: Int = 0,
    val idleDayDateString: String = "Friday, 30 Feb 2024",
    val batteryPercentage: Int = 63,
    // Progress Tab Filter
    val progressTimeFilter: TimeFilter = TimeFilter.WEEKLY,
    // Dialogs & celebratory triggers
    val completionResult: SessionCompletionResult? = null,
    val showCompletionDialog: Boolean = false,
    val showLevelUpDialog: Boolean = false,
    val showEditNameDialog: Boolean = false,
    val showCustomDurationDialog: Boolean = false,
    val toastMessage: String? = null
)

class StudyQuestViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: StudyQuestRepository
    val soundHelper: SoundHelper = SoundHelper(application)

    private val _currentTab = MutableStateFlow(AppTab.HOME)
    private val _selectedSubject = MutableStateFlow(Subject.MATH)
    private val _customSubjectName = MutableStateFlow("")
    private val _chapterOrObjective = MutableStateFlow("Chapter 03")
    private val _focusMode = MutableStateFlow(FocusMode.DEEP_FOCUS)
    private val _ambientSound = MutableStateFlow(AmbientSound.NONE)
    private val _timerTotalSeconds = MutableStateFlow(45 * 60)
    private val _timerRemainingSeconds = MutableStateFlow(45 * 60)
    private val _isTimerRunning = MutableStateFlow(false)
    private val _isTimerPaused = MutableStateFlow(false)
    private val _isLandscapeOrientation = MutableStateFlow(false)
    private val _isIdleClockMode = MutableStateFlow(false)
    private val _progressTimeFilter = MutableStateFlow(TimeFilter.WEEKLY)

    private val _idleHour = MutableStateFlow(23)
    private val _idleMinute = MutableStateFlow(21)
    private val _idleSecond = MutableStateFlow(0)
    private val _idleDayDate = MutableStateFlow("Friday, 30 Feb 2024")
    private val _batteryLevel = MutableStateFlow(63)

    private val _completionResult = MutableStateFlow<SessionCompletionResult?>(null)
    private val _showCompletionDialog = MutableStateFlow(false)
    private val _showLevelUpDialog = MutableStateFlow(false)
    private val _showEditNameDialog = MutableStateFlow(false)
    private val _showCustomDurationDialog = MutableStateFlow(false)
    private val _toastMessage = MutableStateFlow<String?>(null)

    private var timerJob: Job? = null
    private var clockTickerJob: Job? = null

    init {
        val db = AppDatabase.getDatabase(application)
        repository = StudyQuestRepository(db)
        viewModelScope.launch {
            repository.initializeDefaultsIfEmpty()
        }
        startClockTicker()
        updateBatteryLevel()
    }

    private fun updateBatteryLevel() {
        try {
            val bm = getApplication<Application>().getSystemService(Context.BATTERY_SERVICE) as? BatteryManager
            val level = bm?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) ?: 63
            _batteryLevel.value = if (level in 1..100) level else 63
        } catch (_: Exception) {
            _batteryLevel.value = 63
        }
    }

    private fun startClockTicker() {
        clockTickerJob?.cancel()
        clockTickerJob = viewModelScope.launch {
            val dateFormat = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.US)
            while (isActive) {
                val cal = Calendar.getInstance()
                _idleHour.value = cal.get(Calendar.HOUR_OF_DAY)
                _idleMinute.value = cal.get(Calendar.MINUTE)
                _idleSecond.value = cal.get(Calendar.SECOND)
                _idleDayDate.value = dateFormat.format(cal.time)
                delay(1000)
            }
        }
    }

    val uiState: StateFlow<StudyQuestUiState> = combine(
        repository.userProfileFlow.onStart { emit(null) },
        repository.getTodayQuestsFlow().onStart { emit(emptyList()) },
        repository.allSessionsFlow.onStart { emit(emptyList()) },
        repository.allAchievementsFlow.onStart { emit(emptyList()) },
        repository.allShopItemsFlow.onStart { emit(emptyList()) },
        _currentTab,
        _selectedSubject,
        _chapterOrObjective,
        _focusMode,
        _ambientSound,
        _timerTotalSeconds,
        _timerRemainingSeconds,
        _isTimerRunning,
        _isTimerPaused,
        _isLandscapeOrientation,
        _isIdleClockMode,
        _progressTimeFilter,
        _idleHour,
        _idleMinute,
        _idleSecond,
        _idleDayDate,
        _batteryLevel,
        _completionResult,
        _showCompletionDialog,
        _showLevelUpDialog,
        _showEditNameDialog,
        _showCustomDurationDialog,
        _toastMessage
    ) { args: Array<Any?> ->
        val profile = args[0] as? UserProfileEntity
        @Suppress("UNCHECKED_CAST")
        val quests = args[1] as? List<DailyQuestEntity> ?: emptyList()
        @Suppress("UNCHECKED_CAST")
        val sessions = args[2] as? List<StudySessionEntity> ?: emptyList()
        @Suppress("UNCHECKED_CAST")
        val achievements = args[3] as? List<AchievementEntity> ?: emptyList()
        @Suppress("UNCHECKED_CAST")
        val shopItems = args[4] as? List<ShopItemEntity> ?: emptyList()
        val tab = args[5] as? AppTab ?: AppTab.HOME
        val subject = args[6] as? Subject ?: Subject.MATH
        val chapter = args[7] as? String ?: "Chapter 03"
        val mode = args[8] as? FocusMode ?: FocusMode.DEEP_FOCUS
        val sound = args[9] as? AmbientSound ?: AmbientSound.NONE
        val totalSecs = args[10] as? Int ?: (45 * 60)
        val remSecs = args[11] as? Int ?: (45 * 60)
        val running = args[12] as? Boolean ?: false
        val paused = args[13] as? Boolean ?: false
        val isLandscape = args[14] as? Boolean ?: false
        val isIdle = args[15] as? Boolean ?: false
        val filter = args[16] as? TimeFilter ?: TimeFilter.WEEKLY
        val hour = args[17] as? Int ?: 23
        val minute = args[18] as? Int ?: 21
        val second = args[19] as? Int ?: 0
        val dayDate = args[20] as? String ?: "Friday, 30 Feb 2024"
        val battery = args[21] as? Int ?: 63
        val compRes = args[22] as? SessionCompletionResult
        val showComp = args[23] as? Boolean ?: false
        val showLvl = args[24] as? Boolean ?: false
        val showEdit = args[25] as? Boolean ?: false
        val showCustomDur = args[26] as? Boolean ?: false
        val toast = args[27] as? String

        val levelInfo = LevelCalculator.calculateLevelInfo(profile?.xp ?: 2840)

        StudyQuestUiState(
            userProfile = profile,
            levelInfo = levelInfo,
            todayQuests = quests,
            allSessions = sessions,
            achievements = achievements,
            shopItems = shopItems,
            currentTab = tab,
            selectedSubject = subject,
            customSubjectName = _customSubjectName.value,
            chapterOrObjective = chapter,
            focusMode = mode,
            ambientSound = sound,
            timerTotalSeconds = totalSecs,
            timerRemainingSeconds = remSecs,
            isTimerRunning = running,
            isTimerPaused = paused,
            isLandscapeOrientation = isLandscape,
            isIdleClockMode = isIdle,
            progressTimeFilter = filter,
            idleCurrentHour = hour,
            idleCurrentMinute = minute,
            idleCurrentSecond = second,
            idleDayDateString = dayDate,
            batteryPercentage = battery,
            completionResult = compRes,
            showCompletionDialog = showComp,
            showLevelUpDialog = showLvl,
            showEditNameDialog = showEdit,
            showCustomDurationDialog = showCustomDur,
            toastMessage = toast
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = StudyQuestUiState()
    )

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun openStartTaskScreen() {
        _currentTab.value = AppTab.START_TASK
    }

    fun toggleIdleClock() {
        _isIdleClockMode.value = !_isIdleClockMode.value
        _currentTab.value = AppTab.FLIP_CLOCK
    }

    fun setIdleClockMode(idle: Boolean) {
        _isIdleClockMode.value = idle
    }

    fun toggleOrientation() {
        _isLandscapeOrientation.value = !_isLandscapeOrientation.value
    }

    fun setSubject(subject: Subject) {
        _selectedSubject.value = subject
    }

    fun setCustomSubjectName(name: String) {
        _customSubjectName.value = name
    }

    fun setChapterOrObjective(chapter: String) {
        _chapterOrObjective.value = chapter
    }

    fun setFocusMode(mode: FocusMode) {
        _focusMode.value = mode
    }

    fun setAmbientSound(sound: AmbientSound) {
        _ambientSound.value = sound
        if (_isTimerRunning.value && !_isTimerPaused.value && sound != AmbientSound.NONE) {
            soundHelper.startAmbientSound(sound.frequency)
        } else {
            soundHelper.stopAmbientSound()
        }
    }

    fun setDurationPreset(minutes: Int) {
        if (!_isTimerRunning.value) {
            _timerTotalSeconds.value = minutes * 60
            _timerRemainingSeconds.value = minutes * 60
        }
    }

    fun setCustomDurationMinutes(minutes: Int) {
        if (minutes > 0 && !_isTimerRunning.value) {
            _timerTotalSeconds.value = minutes * 60
            _timerRemainingSeconds.value = minutes * 60
            _showCustomDurationDialog.value = false
        }
    }

    fun openCustomDurationDialog() {
        _showCustomDurationDialog.value = true
    }

    fun dismissCustomDurationDialog() {
        _showCustomDurationDialog.value = false
    }

    fun startTaskAndFlip() {
        _isIdleClockMode.value = false
        _currentTab.value = AppTab.FLIP_CLOCK
        startTimer()
    }

    fun startTimer() {
        if (_isTimerRunning.value && !_isTimerPaused.value) return
        if (_isTimerPaused.value) {
            resumeTimer()
            return
        }

        _isTimerRunning.value = true
        _isTimerPaused.value = false

        if (_ambientSound.value != AmbientSound.NONE) {
            soundHelper.startAmbientSound(_ambientSound.value.frequency)
        }

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive && _timerRemainingSeconds.value > 0) {
                delay(1000)
                if (!_isTimerPaused.value) {
                    val remaining = _timerRemainingSeconds.value - 1
                    _timerRemainingSeconds.value = remaining
                    if (remaining <= 5 && remaining > 0) {
                        soundHelper.playTick()
                    }
                }
            }
            if (_timerRemainingSeconds.value <= 0) {
                onTimerFinished()
            }
        }
    }

    fun pauseTimer() {
        if (_isTimerRunning.value && !_isTimerPaused.value) {
            _isTimerPaused.value = true
            soundHelper.stopAmbientSound()
            if (_focusMode.value == FocusMode.DEEP_FOCUS) {
                _toastMessage.value = "⚠️ Deep Focus paused"
            }
        }
    }

    fun resumeTimer() {
        if (_isTimerRunning.value && _isTimerPaused.value) {
            _isTimerPaused.value = false
            if (_ambientSound.value != AmbientSound.NONE) {
                soundHelper.startAmbientSound(_ambientSound.value.frequency)
            }
        }
    }

    fun stopAndCompleteSession() {
        val totalSecs = _timerTotalSeconds.value
        val remSecs = _timerRemainingSeconds.value
        val studiedSeconds = totalSecs - remSecs
        val studiedMinutes = (studiedSeconds / 60).coerceAtLeast(1)

        timerJob?.cancel()
        _isTimerRunning.value = false
        _isTimerPaused.value = false
        soundHelper.stopAmbientSound()

        viewModelScope.launch {
            val subjectName = if (_selectedSubject.value == Subject.OTHER && _customSubjectName.value.isNotBlank()) {
                _customSubjectName.value
            } else {
                _selectedSubject.value.displayName
            }

            val result = repository.recordCompletedSession(
                subject = subjectName,
                durationMinutes = studiedMinutes,
                focusMode = _focusMode.value.title,
                notes = _chapterOrObjective.value,
                xpMultiplier = _focusMode.value.xpMultiplier
            )

            _timerRemainingSeconds.value = _timerTotalSeconds.value
            _completionResult.value = result
            _showCompletionDialog.value = true
            soundHelper.playLevelUp()
        }
    }

    private fun onTimerFinished() {
        val studiedMinutes = (_timerTotalSeconds.value / 60).coerceAtLeast(1)
        timerJob?.cancel()
        _isTimerRunning.value = false
        _isTimerPaused.value = false
        soundHelper.stopAmbientSound()

        viewModelScope.launch {
            val subjectName = if (_selectedSubject.value == Subject.OTHER && _customSubjectName.value.isNotBlank()) {
                _customSubjectName.value
            } else {
                _selectedSubject.value.displayName
            }

            val result = repository.recordCompletedSession(
                subject = subjectName,
                durationMinutes = studiedMinutes,
                focusMode = _focusMode.value.title,
                notes = _chapterOrObjective.value,
                xpMultiplier = _focusMode.value.xpMultiplier
            )

            _timerRemainingSeconds.value = _timerTotalSeconds.value
            _completionResult.value = result
            _showCompletionDialog.value = true
            soundHelper.playLevelUp()
        }
    }

    fun setProgressTimeFilter(filter: TimeFilter) {
        _progressTimeFilter.value = filter
    }

    fun claimQuest(questId: String) {
        viewModelScope.launch {
            val didClaim = repository.claimQuestReward(questId)
            if (didClaim) {
                _toastMessage.value = "Reward Claimed! 🌟"
                soundHelper.playCoin()
            }
        }
    }

    fun claimAchievement(achievementId: String) {
        viewModelScope.launch {
            val didClaim = repository.claimAchievementReward(achievementId)
            if (didClaim) {
                _toastMessage.value = "Achievement Claimed! 🏆"
                soundHelper.playLevelUp()
            }
        }
    }

    fun openEditNameDialog() {
        _showEditNameDialog.value = true
    }

    fun dismissEditNameDialog() {
        _showEditNameDialog.value = false
    }

    fun updateUsername(newName: String) {
        viewModelScope.launch {
            repository.updateUsername(newName)
            _showEditNameDialog.value = false
            _toastMessage.value = "Callsign updated to $newName"
        }
    }

    fun dismissCompletionDialog() {
        val result = _completionResult.value
        _showCompletionDialog.value = false
        if (result?.didLevelUp == true) {
            _showLevelUpDialog.value = true
        }
    }

    fun dismissLevelUpDialog() {
        _showLevelUpDialog.value = false
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun deleteSession(sessionId: Long) {
        viewModelScope.launch {
            repository.deleteSession(sessionId)
            _toastMessage.value = "Session log removed"
        }
    }
}
