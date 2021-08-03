package com.maikkkko1.android_base_arq.common

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import com.maikkkko1.android_base_arq.common.entity.ScheduleItemAndroidCalendar

class CommonManager(private val activity: Activity) {
    fun scheduleItemOnAndroidCalendar(scheduleItemAndroidCalendar: ScheduleItemAndroidCalendar) {
        scheduleItemAndroidCalendar.let {
            val calendarIntent = Intent(Intent.ACTION_INSERT).apply {
                data = CalendarContract.Events.CONTENT_URI
                putExtra(CalendarContract.Events.TITLE, scheduleItemAndroidCalendar.eventTitle)
                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, it.startDate.time)
                putExtra(CalendarContract.EXTRA_EVENT_END_TIME, it.endDate.time)
            }

            activity.startActivity(calendarIntent)
        }
    }

    fun openWebView(webUrl: String) {
        val webIntent = Intent(activity, WebViewActivity::class.java).apply {
            putExtra("targetUrl", webUrl)
        }

        activity.startActivity(webIntent)
    }

    fun callPhoneNumber(number: String) {
        activity.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${number}")))
    }

    fun openGoogleMapsDirectionForCoordinates(lat: Double, lng: Double) {
        val gmmIntentUri = Uri.parse("google.navigation:q=${lat},${lng}&mode=d")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
            setPackage("com.google.android.apps.maps")
        }

        activity.startActivity(mapIntent)
    }
}