package com.example.data

import com.example.data.local.AppDatabase
import com.example.data.local.entity.AchievementEntity
import com.example.data.local.entity.DailyQuestEntity
import com.example.data.local.entity.ShopItemEntity
import com.example.data.local.entity.StudySessionEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.util.LevelCalculator
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class StudyQuestRepository(private val database: AppDatabase) {

    private val userDao = database.userProfileDao()
    private val sessionDao = database.studySessionDao()
    private val questDao = database.dailyQuestDao()
    private val achievementDao = database.achievementDao()
    private val shopDao = database.shopItemDao()

    val userProfileFlow: Flow<UserProfileEntity?> = userDao.getUserProfile()
    val allSessionsFlow: Flow<List<StudySessionEntity>> = sessionDao.getAllSessions()
    val allAchievementsFlow: Flow<List<AchievementEntity>> = achievementDao.getAllAchievements()
    val allShopItemsFlow: Flow<List<ShopItemEntity>> = shopDao.getAllShopItems()

    fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    fun getTodayQuestsFlow(): Flow<List<DailyQuestEntity>> {
        return questDao.getQuestsForDate(getTodayDateString())
    }

    suspend fun initializeDefaultsIfEmpty() {
        val existingProfile = userDao.getUserProfileDirect()
        val todayStr = getTodayDateString()

        if (existingProfile == null) {
            val defaultProfile = UserProfileEntity(
                id = 1,
                username = "Kazu",
                title = "Scholar 🎓",
                level = 7,
                xp = 2840,
                coins = 320,
                gems = 25,
                streakDays = 7,
                bestStreakDays = 7,
                lastStudyDateMillis = System.currentTimeMillis(),
                totalStudyMinutes = 402,
                totalSessionsCompleted = 18,
                completedTasksCount = 12,
                streakFreezeCount = 1,
                doubleXpPotionsCount = 1,
                isDoubleXpActive = false,
                selectedAvatar = "hero_scholar",
                selectedTheme = "theme_cyber_dark",
                equippedPet = "pet_owl",
                intelligenceStat = 14,
                focusStat = 18,
                staminaStat = 12,
                characterEvolutionStage = 2,
                isOnboardingCompleted = true
            )
            userDao.insertProfile(defaultProfile)

            // Seed initial study sessions across the week for beautiful bar chart data
            val now = System.currentTimeMillis()
            val dayMillis = 24L * 60 * 60 * 1000
            val seededSessions = listOf(
                StudySessionEntity(subject = "Mathematics", durationMinutes = 45, xpEarned = 120, coinsEarned = 40, focusMode = "Deep Focus", timestamp = now - dayMillis * 6, notes = "Calculus limits and derivatives", completed = true),
                StudySessionEntity(subject = "Physics", durationMinutes = 60, xpEarned = 150, coinsEarned = 50, focusMode = "Deep Focus", timestamp = now - dayMillis * 5, notes = "Electromagnetism wave theory", completed = true),
                StudySessionEntity(subject = "Chemistry", durationMinutes = 35, xpEarned = 90, coinsEarned = 30, focusMode = "Normal Mode", timestamp = now - dayMillis * 4, notes = "Organic synthesis mechanisms", completed = true),
                StudySessionEntity(subject = "Mathematics", durationMinutes = 90, xpEarned = 240, coinsEarned = 80, focusMode = "Deep Focus", timestamp = now - dayMillis * 3, notes = "Integration & series approximation", completed = true),
                StudySessionEntity(subject = "English", durationMinutes = 50, xpEarned = 130, coinsEarned = 45, focusMode = "Normal Mode", timestamp = now - dayMillis * 2, notes = "Literature critical analysis", completed = true),
                StudySessionEntity(subject = "Accountancy", durationMinutes = 40, xpEarned = 100, coinsEarned = 35, focusMode = "Deep Focus", timestamp = now - dayMillis * 1, notes = "Balance sheets & depreciation", completed = true),
                StudySessionEntity(subject = "Mathematics", durationMinutes = 45, xpEarned = 120, coinsEarned = 40, focusMode = "Deep Focus", timestamp = now - 1000 * 60 * 60 * 2, notes = "Chapter 03", completed = true)
            )
            seededSessions.forEach { sessionDao.insertSession(it) }
        }

        // Initialize today's quests if none exist
        val existingQuests = questDao.getQuestsForDateDirect(todayStr)
        if (existingQuests.isEmpty()) {
            val defaultQuests = listOf(
                DailyQuestEntity(
                    id = "quest_study_30_$todayStr",
                    title = "Daily Study Master",
                    description = "Study for 30 minutes today",
                    emoji = "📖",
                    targetType = "STUDY_MINUTES",
                    targetValue = 30,
                    currentValue = 25,
                    xpReward = 50,
                    coinReward = 20,
                    isCompleted = false,
                    isClaimed = false,
                    questDate = todayStr
                ),
                DailyQuestEntity(
                    id = "quest_sessions_2_$todayStr",
                    title = "Focus Champion",
                    description = "Complete 2 study sessions",
                    emoji = "⚡",
                    targetType = "SESSIONS_COUNT",
                    targetValue = 2,
                    currentValue = 1,
                    xpReward = 40,
                    coinReward = 15,
                    isCompleted = false,
                    isClaimed = false,
                    questDate = todayStr
                ),
                DailyQuestEntity(
                    id = "quest_deep_focus_$todayStr",
                    title = "Rune Deep Focus",
                    description = "Finish 1 session in Hardcore Mode",
                    emoji = "🔥",
                    targetType = "DEEP_FOCUS_COUNT",
                    targetValue = 1,
                    currentValue = 0,
                    xpReward = 60,
                    coinReward = 25,
                    isCompleted = false,
                    isClaimed = false,
                    questDate = todayStr
                ),
                DailyQuestEntity(
                    id = "quest_subjects_2_$todayStr",
                    title = "Polymath Explorer",
                    description = "Study at least 2 different subjects",
                    emoji = "🧪",
                    targetType = "SUBJECT_VARIETY",
                    targetValue = 2,
                    currentValue = 1,
                    xpReward = 45,
                    coinReward = 20,
                    isCompleted = false,
                    isClaimed = false,
                    questDate = todayStr
                )
            )
            questDao.insertQuests(defaultQuests)
        }

        // Initialize Achievements
        val defaultAchievements = listOf(
            AchievementEntity(
                id = "ach_first_quest",
                title = "First Quest 🏅",
                description = "Complete your first study session",
                emoji = "🏅",
                category = "SPECIAL",
                maxProgress = 1,
                currentProgress = 1,
                isUnlocked = true,
                isClaimed = true,
                rewardXp = 50,
                rewardCoins = 30,
                rewardGems = 2
            ),
            AchievementEntity(
                id = "ach_week_warrior",
                title = "Week Warrior 🔥",
                description = "Maintain a 7-day study streak",
                emoji = "🔥",
                category = "STREAK",
                maxProgress = 7,
                currentProgress = 1,
                isUnlocked = false,
                isClaimed = false,
                rewardXp = 200,
                rewardCoins = 100,
                rewardGems = 10
            ),
            AchievementEntity(
                id = "ach_time_master_10h",
                title = "Time Master ⏰",
                description = "Accumulate 10 hours (600 mins) of focus time",
                emoji = "⏰",
                category = "TIME",
                maxProgress = 600,
                currentProgress = 25,
                isUnlocked = false,
                isClaimed = false,
                rewardXp = 300,
                rewardCoins = 150,
                rewardGems = 15
            ),
            AchievementEntity(
                id = "ach_bookworm_50",
                title = "Bookworm 📚",
                description = "Complete 50 study quests",
                emoji = "📚",
                category = "SESSIONS",
                maxProgress = 50,
                currentProgress = 1,
                isUnlocked = false,
                isClaimed = false,
                rewardXp = 500,
                rewardCoins = 250,
                rewardGems = 25
            ),
            AchievementEntity(
                id = "ach_month_legend",
                title = "Month of Glory 🏆",
                description = "Reach a 30-day streak",
                emoji = "🏆",
                category = "STREAK",
                maxProgress = 30,
                currentProgress = 1,
                isUnlocked = false,
                isClaimed = false,
                rewardXp = 1000,
                rewardCoins = 500,
                rewardGems = 50
            ),
            AchievementEntity(
                id = "ach_coin_hoarder",
                title = "Treasure Dragon 🪙",
                description = "Collect 500 total coins",
                emoji = "🪙",
                category = "SPECIAL",
                maxProgress = 500,
                currentProgress = 120,
                isUnlocked = false,
                isClaimed = false,
                rewardXp = 150,
                rewardCoins = 100,
                rewardGems = 5
            )
        )
        achievementDao.insertAchievements(defaultAchievements)

        // Initialize Shop Items
        val defaultShopItems = listOf(
            ShopItemEntity(
                id = "avatar_hero_scholar",
                name = "Arcane Scholar",
                description = "Robes imbued with celestial focus runes",
                category = "AVATAR",
                iconEmoji = "🧑‍🎓",
                priceCoins = 0,
                isPurchased = true,
                isEquipped = true,
                statBoost = "+5% XP Bonus"
            ),
            ShopItemEntity(
                id = "avatar_cyber_knight",
                name = "Cyber Knight",
                description = "Neon armor for impenetrable concentration",
                category = "AVATAR",
                iconEmoji = "🤖",
                priceCoins = 150,
                isPurchased = false,
                isEquipped = false,
                requiredLevel = 3,
                statBoost = "+10% Coin Boost"
            ),
            ShopItemEntity(
                id = "avatar_astro_mage",
                name = "Astro Mage",
                description = "Harness the power of the study cosmos",
                category = "AVATAR",
                iconEmoji = "🧙‍♂️",
                priceCoins = 300,
                priceGems = 5,
                isPurchased = false,
                isEquipped = false,
                requiredLevel = 5,
                statBoost = "+15% XP Bonus"
            ),
            ShopItemEntity(
                id = "pet_owl",
                name = "Chrono Owl",
                description = "Wise familiar keeping you awake & sharp",
                category = "PET",
                iconEmoji = "🦉",
                priceCoins = 0,
                isPurchased = true,
                isEquipped = true,
                statBoost = "Focus +5"
            ),
            ShopItemEntity(
                id = "pet_dragon",
                name = "Focus Dragonling",
                description = "Breathes sparks of pure motivation",
                category = "PET",
                iconEmoji = "🐉",
                priceCoins = 250,
                priceGems = 10,
                isPurchased = false,
                isEquipped = false,
                requiredLevel = 4,
                statBoost = "+10% Streak Resilience"
            ),
            ShopItemEntity(
                id = "pet_cat",
                name = "Zen Neko",
                description = "Purrs at 432Hz to dissolve study anxiety",
                category = "PET",
                iconEmoji = "🐱",
                priceCoins = 180,
                isPurchased = false,
                isEquipped = false,
                requiredLevel = 2,
                statBoost = "Stamina +5"
            ),
            ShopItemEntity(
                id = "potion_streak_freeze",
                name = "Streak Freeze Rune",
                description = "Protects your streak if you miss a study day",
                category = "POTION",
                iconEmoji = "❄️",
                priceCoins = 100,
                priceGems = 2,
                isPurchased = false,
                isEquipped = false,
                statBoost = "Auto-consumes when inactive"
            ),
            ShopItemEntity(
                id = "potion_double_xp",
                name = "Elixir of 2x XP",
                description = "Doubles all XP earned in your next session",
                category = "POTION",
                iconEmoji = "🧪",
                priceCoins = 80,
                priceGems = 1,
                isPurchased = false,
                isEquipped = false,
                statBoost = "2x XP on next Quest"
            ),
            ShopItemEntity(
                id = "theme_cyber_dark",
                name = "Neon Cyber Dark",
                description = "Obsidian slate with vibrant violet & electric cyan",
                category = "THEME",
                iconEmoji = "🌌",
                priceCoins = 0,
                isPurchased = true,
                isEquipped = true
            ),
            ShopItemEntity(
                id = "theme_emerald_forest",
                name = "Emerald Sanctuary",
                description = "Calming deep botanical forest greens",
                category = "THEME",
                iconEmoji = "🌲",
                priceCoins = 120,
                isPurchased = false,
                isEquipped = false,
                requiredLevel = 2
            ),
            ShopItemEntity(
                id = "theme_synthwave_sunset",
                name = "Synthwave Sunset",
                description = "Retro magenta, sunset amber, and neon grid",
                category = "THEME",
                iconEmoji = "🌆",
                priceCoins = 220,
                isPurchased = false,
                isEquipped = false,
                requiredLevel = 4
            ),
            ShopItemEntity(
                id = "title_grand_scholar",
                name = "Honor: Grand Scholar",
                description = "Display title on your hero profile",
                category = "TITLES",
                iconEmoji = "👑",
                priceCoins = 200,
                isPurchased = false,
                isEquipped = false,
                requiredLevel = 5
            )
        )
        shopDao.insertShopItems(defaultShopItems)
    }

    suspend fun completeStudySession(
        subject: String,
        durationMinutes: Int,
        focusModeTitle: String,
        isDeepFocus: Boolean,
        notes: String
    ): SessionCompletionResult {
        val profile = userDao.getUserProfileDirect() ?: return SessionCompletionResult(0, 0, false, 1, "Novice 🌱")
        val oldLevel = profile.level

        // Calculate XP & Coins with multipliers
        var baseMultiplier = if (isDeepFocus) 1.25f else 1.0f
        if (profile.isDoubleXpActive) {
            baseMultiplier *= 2.0f
        }

        val earnedXp = (durationMinutes * 2 * baseMultiplier).toInt().coerceAtLeast(10)
        val earnedCoins = (durationMinutes * 0.8f + (if (isDeepFocus) 5 else 2)).toInt().coerceAtLeast(5)

        // Save session record
        sessionDao.insertSession(
            StudySessionEntity(
                subject = subject,
                durationMinutes = durationMinutes,
                xpEarned = earnedXp,
                coinsEarned = earnedCoins,
                focusMode = focusModeTitle,
                notes = notes,
                completed = true
            )
        )

        // Calculate updated Streak
        val now = System.currentTimeMillis()
        val lastDate = Calendar.getInstance().apply { timeInMillis = profile.lastStudyDateMillis }
        val currentDate = Calendar.getInstance().apply { timeInMillis = now }

        val isSameDay = lastDate.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) &&
                lastDate.get(Calendar.DAY_OF_YEAR) == currentDate.get(Calendar.DAY_OF_YEAR)

        val isYesterday = lastDate.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) &&
                lastDate.get(Calendar.DAY_OF_YEAR) == currentDate.get(Calendar.DAY_OF_YEAR) - 1

        var newStreak = profile.streakDays
        var streakFreezeUsed = false

        if (!isSameDay) {
            if (isYesterday) {
                newStreak += 1
            } else {
                // Missed more than 1 day
                if (profile.streakFreezeCount > 0) {
                    streakFreezeUsed = true
                    newStreak += 1 // streak freeze saved streak
                } else {
                    newStreak = 1
                }
            }
        }

        val bestStreak = maxOf(profile.bestStreakDays, newStreak)
        val newTotalXp = profile.xp + earnedXp
        val newTotalMinutes = profile.totalStudyMinutes + durationMinutes
        val newTotalSessions = profile.totalSessionsCompleted + 1
        val newCoins = profile.coins + earnedCoins

        // Calculate new Level & Title
        val levelInfo = LevelCalculator.calculateLevelInfo(newTotalXp)
        val newLevel = levelInfo.level
        val newTitle = levelInfo.title
        val didLevelUp = newLevel > oldLevel

        val updatedProfile = profile.copy(
            xp = newTotalXp,
            level = newLevel,
            title = newTitle,
            coins = newCoins,
            streakDays = newStreak,
            bestStreakDays = bestStreak,
            lastStudyDateMillis = now,
            totalStudyMinutes = newTotalMinutes,
            totalSessionsCompleted = newTotalSessions,
            isDoubleXpActive = false, // consume potion if active
            streakFreezeCount = if (streakFreezeUsed) (profile.streakFreezeCount - 1).coerceAtLeast(0) else profile.streakFreezeCount,
            intelligenceStat = profile.intelligenceStat + (durationMinutes / 15),
            focusStat = profile.focusStat + (if (isDeepFocus) 2 else 1),
            staminaStat = profile.staminaStat + 1,
            characterEvolutionStage = when {
                newLevel >= 25 -> 5
                newLevel >= 20 -> 4
                newLevel >= 15 -> 3
                newLevel >= 10 -> 2
                newLevel >= 5 -> 1
                else -> 0
            }
        )
        userDao.updateProfile(updatedProfile)

        // Update Daily Quests progress
        val todayStr = getTodayDateString()
        val todayQuests = questDao.getQuestsForDateDirect(todayStr)
        for (quest in todayQuests) {
            when (quest.targetType) {
                "STUDY_MINUTES" -> {
                    val updatedVal = quest.currentValue + durationMinutes
                    questDao.updateQuestProgress(quest.id, updatedVal, updatedVal >= quest.targetValue)
                }
                "SESSIONS_COUNT" -> {
                    val updatedVal = quest.currentValue + 1
                    questDao.updateQuestProgress(quest.id, updatedVal, updatedVal >= quest.targetValue)
                }
                "DEEP_FOCUS_COUNT" -> {
                    if (isDeepFocus) {
                        val updatedVal = quest.currentValue + 1
                        questDao.updateQuestProgress(quest.id, updatedVal, updatedVal >= quest.targetValue)
                    }
                }
                "SUBJECT_VARIETY" -> {
                    val updatedVal = (quest.currentValue + 1).coerceAtMost(quest.targetValue)
                    questDao.updateQuestProgress(quest.id, updatedVal, updatedVal >= quest.targetValue)
                }
            }
        }

        // Update Achievements progress
        val achievements = achievementDao.getAllAchievementsDirect()
        for (ach in achievements) {
            var updatedProgress = ach.currentProgress
            when (ach.id) {
                "ach_first_quest" -> updatedProgress = 1
                "ach_week_warrior" -> updatedProgress = newStreak
                "ach_time_master_10h" -> updatedProgress = newTotalMinutes
                "ach_bookworm_50" -> updatedProgress = newTotalSessions
                "ach_month_legend" -> updatedProgress = newStreak
                "ach_coin_hoarder" -> updatedProgress = newCoins
            }
            val isUnlocked = updatedProgress >= ach.maxProgress
            if (updatedProgress != ach.currentProgress || isUnlocked != ach.isUnlocked) {
                achievementDao.updateAchievement(
                    ach.copy(
                        currentProgress = updatedProgress,
                        isUnlocked = isUnlocked
                    )
                )
            }
        }

        return SessionCompletionResult(
            earnedXp = earnedXp,
            earnedCoins = earnedCoins,
            didLevelUp = didLevelUp,
            newLevel = newLevel,
            newTitle = newTitle
        )
    }

    suspend fun recordCompletedSession(
        subject: String,
        durationMinutes: Int,
        focusMode: String,
        notes: String,
        xpMultiplier: Float
    ): SessionCompletionResult {
        val isDeepFocus = xpMultiplier > 1.0f
        return completeStudySession(subject, durationMinutes, focusMode, isDeepFocus, notes)
    }

    suspend fun claimQuestReward(questId: String): Boolean {
        val todayQuests = questDao.getQuestsForDateDirect(getTodayDateString())
        val quest = todayQuests.find { it.id == questId } ?: return false
        if (!quest.isCompleted || quest.isClaimed) return false

        val profile = userDao.getUserProfileDirect() ?: return false
        val newXp = profile.xp + quest.xpReward
        val newCoins = profile.coins + quest.coinReward
        val levelInfo = LevelCalculator.calculateLevelInfo(newXp)

        userDao.updateProfile(
            profile.copy(
                xp = newXp,
                coins = newCoins,
                level = levelInfo.level,
                title = levelInfo.title
            )
        )
        questDao.markClaimed(questId)
        return true
    }

    suspend fun claimAllDailyBonus(): Boolean {
        val todayQuests = questDao.getQuestsForDateDirect(getTodayDateString())
        val allCompleted = todayQuests.isNotEmpty() && todayQuests.all { it.isCompleted }
        if (!allCompleted) return false

        val profile = userDao.getUserProfileDirect() ?: return false
        val newXp = profile.xp + 100
        val newCoins = profile.coins + 50
        val newGems = profile.gems + 3
        val levelInfo = LevelCalculator.calculateLevelInfo(newXp)

        userDao.updateProfile(
            profile.copy(
                xp = newXp,
                coins = newCoins,
                gems = newGems,
                level = levelInfo.level,
                title = levelInfo.title
            )
        )
        return true
    }

    suspend fun claimAchievementReward(achId: String): Boolean {
        val achievements = achievementDao.getAllAchievementsDirect()
        val ach = achievements.find { it.id == achId } ?: return false
        if (!ach.isUnlocked || ach.isClaimed) return false

        val profile = userDao.getUserProfileDirect() ?: return false
        val newXp = profile.xp + ach.rewardXp
        val newCoins = profile.coins + ach.rewardCoins
        val newGems = profile.gems + ach.rewardGems
        val levelInfo = LevelCalculator.calculateLevelInfo(newXp)

        userDao.updateProfile(
            profile.copy(
                xp = newXp,
                coins = newCoins,
                gems = newGems,
                level = levelInfo.level,
                title = levelInfo.title
            )
        )
        achievementDao.markClaimed(achId)
        return true
    }

    suspend fun buyShopItem(item: ShopItemEntity): Boolean {
        val profile = userDao.getUserProfileDirect() ?: return false
        if (profile.coins < item.priceCoins || profile.gems < item.priceGems) return false
        if (profile.level < item.requiredLevel) return false

        val newCoins = profile.coins - item.priceCoins
        val newGems = profile.gems - item.priceGems

        var updatedProfile = profile.copy(coins = newCoins, gems = newGems)

        if (item.category == "POTION") {
            when (item.id) {
                "potion_streak_freeze" -> updatedProfile = updatedProfile.copy(streakFreezeCount = profile.streakFreezeCount + 1)
                "potion_double_xp" -> updatedProfile = updatedProfile.copy(doubleXpPotionsCount = profile.doubleXpPotionsCount + 1)
            }
        } else {
            shopDao.markPurchased(item.id)
        }

        userDao.updateProfile(updatedProfile)
        return true
    }

    suspend fun equipShopItem(item: ShopItemEntity) {
        val profile = userDao.getUserProfileDirect() ?: return
        shopDao.unequipCategory(item.category)
        shopDao.setEquipped(item.id)

        var updated = profile
        when (item.category) {
            "AVATAR" -> updated = updated.copy(selectedAvatar = item.id)
            "THEME" -> updated = updated.copy(selectedTheme = item.id)
            "PET" -> updated = updated.copy(equippedPet = item.id)
            "TITLES" -> updated = updated.copy(title = item.name)
        }
        userDao.updateProfile(updated)
    }

    suspend fun activateDoubleXpPotion(): Boolean {
        val profile = userDao.getUserProfileDirect() ?: return false
        if (profile.doubleXpPotionsCount <= 0 || profile.isDoubleXpActive) return false

        userDao.updateProfile(
            profile.copy(
                doubleXpPotionsCount = profile.doubleXpPotionsCount - 1,
                isDoubleXpActive = true
            )
        )
        return true
    }

    suspend fun updateUsername(newName: String) {
        val profile = userDao.getUserProfileDirect() ?: return
        userDao.updateProfile(profile.copy(username = newName.trim()))
    }

    suspend fun deleteSession(sessionId: Long) {
        sessionDao.deleteSession(sessionId)
    }
}

data class SessionCompletionResult(
    val earnedXp: Int,
    val earnedCoins: Int,
    val didLevelUp: Boolean,
    val newLevel: Int,
    val newTitle: String
)
