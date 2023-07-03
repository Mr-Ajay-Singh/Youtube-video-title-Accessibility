package com.overall.youtubetestaccessibilit


import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import androidx.annotation.RequiresApi
import splitties.init.appCtx
import timber.log.Timber


class MyAccessibilityService : AccessibilityService() {

    companion object


    var instance: MyAccessibilityService? = null
        private set
    var previousUrl = ""
    var previousTitle = ""
    var isFound = false
    var prefs : SharedPreferences? = null


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onAccessibilityEvent(event: AccessibilityEvent) {

        //crashTest()

//        Timber.d("==>AccEvent ${event.source.className} - ${event.source.} - ${event.packageName} - ${event.action} - ${event.className} - ${event.contentDescription} - ${event.windowId} ")


        try {
            instance = this

            Timber.d("==>Exception $event")

//            if (event?.viewIdResourceName?.contains("com.google.android.youtube:id/title") == true && source.text != null){
//
//            }

            isFound = false
            getInfo(event.source)
            getInfo(rootInActiveWindow)


        } catch (e: Exception) {
            Timber.d("==>Exception $e")
        }
    }

    val list = arrayListOf("Cast. Disconnected","Search","Explore menu")

    private fun getInfo(source: AccessibilityNodeInfo?) {
//        val node =blockerFindAccessibilityNodeInfosByViewId(source,"com.google.android.youtube:id/title")
//        if(node != null){
//            Timber.d("==>info322 ${node}")
//            node.forEach{
//                if(it.text != null){
//                    Toast.makeText(appCtx, it.text, Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }

        Timber.d("==>Info00 ${source} ")
        if (isFound) return
        if (source?.viewIdResourceName?.contains("com.google.android.youtube:id/title") == true && source.text != null && !source.text.equals(previousTitle)) {

            Timber.d("==>Info1 ${source} ")

//            prefs?.edit()?.putString("testing1",source?.text.toString())?.apply()
            previousTitle = source.text.toString()
            Toast.makeText(appCtx, "${source.text}", Toast.LENGTH_SHORT).show()
            isFound = true
        }

//        if(source?.packageName?.contains("com.google.android.youtube")==true &&
//                    source?.contentDescription?.isNotEmpty() == true && source?.isFocusable == true && source?.isClickable == true
//            && source?.isEnabled == true && source?.isImportantForAccessibility == true && source?.isVisibleToUser == true &&
//                    !list.contains(source?.contentDescription) && !source.contentDescription.equals(previousTitle)
//        ){
//            Timber.d("==>Info2 ${source} ")
//            Timber.d("==>Info22 ${source.contentDescription} ")
//            previousTitle = source.contentDescription.toString()
////            prefs?.edit()?.putString("testing1",source?.text.toString())?.apply()
//            Toast.makeText(appCtx, "${source.contentDescription}", Toast.LENGTH_SHORT).show()
//        }

        for (i in 0 until (source?.childCount ?: 0)) {
            try {
                getInfo(source?.getChild(i))
            } catch (e: Exception) {
                Timber.d(e)
            }
        }
    }

    private fun blockerFindAccessibilityNodeInfosByViewId(
        nodeInfo: AccessibilityNodeInfo?,
        viewId: String
    ): List<AccessibilityNodeInfo>? {
        return try {
            nodeInfo?.findAccessibilityNodeInfosByViewId(viewId)
        } catch (e: Exception) {
            Timber.d(e)
            null
        } catch (e: SecurityException) {
            Timber.d(e)
            null
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()

        val config = serviceInfo

//        flagDefault|flagRequestEnhancedWebAccessibility|flagReportViewIds|flagIncludeNotImportantViews|flagRetrieveInteractiveWindows
//        typeWindowStateChanged|typeViewFocused|typeViewClicked|typeViewLongClicked|typeWindowContentChanged|typeContextClicked

        config.flags = (AccessibilityServiceInfo.DEFAULT
                or AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY
                or AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
                or AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
                or AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS)


        config.eventTypes = (AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or
                AccessibilityEvent.TYPE_VIEW_CLICKED or
                AccessibilityEvent.CONTENT_CHANGE_TYPE_UNDEFINED or
                AccessibilityEvent.CONTENT_CHANGE_TYPE_TEXT or
                AccessibilityEvent.TYPE_WINDOWS_CHANGED
                        or AccessibilityEvent.TYPE_VIEW_LONG_CLICKED
                        or AccessibilityEvent.TYPE_VIEW_CONTEXT_CLICKED
                or AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
                         or AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED
                        or AccessibilityEvent.TYPE_VIEW_FOCUSED)


        config.notificationTimeout = 100

        serviceInfo = config
        prefs= getSharedPreferences(
            "testing", MODE_PRIVATE
        )

        appCtx.startActivity(Intent(appCtx, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    override fun onInterrupt() {

    }

}