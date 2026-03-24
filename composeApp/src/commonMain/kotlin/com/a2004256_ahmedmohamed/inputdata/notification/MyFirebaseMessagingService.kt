package com.a2004256_ahmedmohamed.inputdata.notification

import androidx.compose.runtime.mutableStateListOf
import com.a2004256_ahmedmohamed.inputdata.getCurrentDateTime
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.datetime.*
import kotlin.time.Clock.System.now
import kotlin.time.ExperimentalTime

class FirebaseTriggerKMP(private val scope: CoroutineScope) {

    private val database = Firebase.database.reference("users")

    val notifications = mutableStateListOf<Pair<String, String>>()

    fun startListening() {
        scope.launch {
            database.valueEvents.collect { snapshot ->
                snapshot.children.forEach { userSnapshot ->
                    checkBirthday(userSnapshot)
                    checkSessions(userSnapshot)
                }
            }
        }
    }

    private fun monthsBetween(start: LocalDate, end: LocalDate): Int {
        val yearsDiff = end.year - start.year
        val monthsDiff = end.monthNumber - start.monthNumber
        return yearsDiff * 12 + monthsDiff
    }

    private fun getRequiredMonths(type: String): Int? = when (type.trim()) {
        "إبرة بوتكس" -> 6
        "إبرة نضارة" -> 3
        "ليزر" -> 1
        "إكسوزوم" -> 1
        "هيدرافيشل" -> 3
        else -> null
    }

    private fun parseDate(date: String): LocalDate? {
        return try {
            // dd/MM/yyyy
            val parts = date.split("/")
            if (parts.size != 3) return null
            val day = parts[0].toInt()
            val month = parts[1].toInt()
            val year = parts[2].toInt()
            LocalDate(year, month, day)
        } catch (e: Exception) {
            null
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun currentDateTime(): LocalDateTime {
        return try {
            val isoString = getCurrentDateTime()
            val instant = Instant.parse(isoString)
            instant.toLocalDateTime(TimeZone.currentSystemDefault())
        } catch (e: Throwable) {
            LocalDateTime(1970, 1, 1, 0, 0)
        }
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun checkBirthday(userSnapshot: DataSnapshot) {
        val name = userSnapshot.child("name").value as? String ?: return
        val birthDateStr = userSnapshot.child("birthDate").value as? String ?: return
        val birthDate = parseDate(birthDateStr) ?: return

        val currentMonth = currentDateTime().monthNumber
        if (birthDate.monthNumber != currentMonth) return

        val notifyRef = userSnapshot.ref.child("birthdayNotification")

        shouldNotifyOncePerDay(notifyRef) {
            showNotification("عيد ميلاد هذا الشهر", "عيد ميلاد $name خلال هذا الشهر ")
        }
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun checkSessions(userSnapshot: DataSnapshot) {
        val name = userSnapshot.child("name").value as? String ?: return
        val sessionsRef = userSnapshot.child("sessions")
        if (!sessionsRef.exists) return

        val today = currentDateTime().date

        sessionsRef.children.forEach { sessionSnapshot ->
            val type = sessionSnapshot.child("type").value as? String ?: return@forEach
            val dateStr = sessionSnapshot.child("date").value as? String ?: return@forEach
            val sessionDate = parseDate(dateStr) ?: return@forEach
            val requiredMonths = getRequiredMonths(type) ?: return@forEach

            val monthsPassed = monthsBetween(sessionDate, today)
            if (monthsPassed < requiredMonths) return@forEach

            val notifyRef = sessionSnapshot.ref.child("notification")

            shouldNotifyOncePerDay(notifyRef) {
                showNotification("جلسة مستحقة", "$name حان موعد جلسة $type")
            }
        }
    }

    private fun showNotification(title: String, message: String) {
        notifications.add(title to message)
        saveNotificationToFirebase(title, message)
        scope.launch {
            delay(5000)
            notifications.remove(title to message)
        }
    }
}

@OptIn(ExperimentalTime::class)
private fun saveNotificationToFirebase(title: String, message: String) {
    val ref = Firebase.database.reference("notifications").push()

    val data = mapOf(
        "title" to title,
        "message" to message,
        "timestamp" to now().toEpochMilliseconds()
    )

    CoroutineScope(Dispatchers.Default).launch {
        ref.setValue(data)
    }
}

@OptIn(ExperimentalTime::class)
private fun shouldNotifyOncePerDay(
    ref: DatabaseReference,
    onAllowed: () -> Unit
) {
    CoroutineScope(Dispatchers.Default).launch {
        val snapshot = ref.valueEvents.first()
        val lastDate = snapshot.child("lastNotificationDate").value as? String

        val todayStr = now().toLocalDateTime(TimeZone.currentSystemDefault())
            .date.toString() // "YYYY-MM-DD"

        if (lastDate != todayStr) {
            onAllowed()
            ref.child("lastNotificationDate").setValue(todayStr)
        }
    }
}