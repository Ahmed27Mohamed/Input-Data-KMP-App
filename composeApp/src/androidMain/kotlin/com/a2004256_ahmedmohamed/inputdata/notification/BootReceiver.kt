package com.a2004256_ahmedmohamed.inputdata.notification

@SuppressLint("RestrictedApi")
class BootReceiver : ForceStopRunnable.BroadcastReceiver() {
    @SuppressLint("RestrictedApi")
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent!!.action == Intent.ACTION_BOOT_COMPLETED) {
            val serviceIntent = Intent(context, FirebaseTriggerService::class.java)
            ContextCompat.startForegroundService(context, serviceIntent)
        }
    }
}