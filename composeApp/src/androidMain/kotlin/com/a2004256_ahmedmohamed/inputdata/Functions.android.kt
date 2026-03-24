package com.a2004256_ahmedmohamed.inputdata

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.ImageBitmap
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


fun provideContext(context: Context) {
    appContext = context
}

actual suspend fun isOnline(): Boolean = withContext(Dispatchers.Default) {
    val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val capabilities = connectivityManager.getNetworkCapabilities(network)
    capabilities != null &&
            (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
}

actual fun addDaysToDate(date: String, days: Int): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val parsedDate = sdf.parse(date)
        val cal = Calendar.getInstance()
        if (parsedDate != null) {
            cal.time = parsedDate
            cal.add(Calendar.DAY_OF_MONTH, days)
            sdf.format(cal.time)
        } else date
    } catch (e: Exception) {
        date
    }
}

actual fun requestNotificationPermission() {
}

@OptIn(ExperimentalEncodingApi::class)
actual suspend fun base64ToImageBitmap(base64: String): ImageBitmap? {
    if (base64.isEmpty()) return null
    val bytes = Base64.decode(base64.substringAfter(","))
    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    return bitmap?.asImageBitmap()
}

actual fun playNotificationSound() {
}