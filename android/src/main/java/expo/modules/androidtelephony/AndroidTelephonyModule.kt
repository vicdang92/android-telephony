package expo.modules.androidtelephony

import android.annotation.SuppressLint
import expo.modules.kotlin.Promise
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class AndroidTelephonyModule : Module() {
  @SuppressLint("NewApi")
  override fun definition() = ModuleDefinition {
    Name("AndroidTelephony")

    // Defines a JavaScript synchronous function that runs the native code on the JavaScript thread.
    Function("getALlCellInfo") {
      val androidTelephony = AndroidTelephony()
      val result = androidTelephony.getAllCellInfo(appContext.reactContext)
      result
    }
    AsyncFunction("execute") { action: String, promise: Promise ->
      val androidTelephony = AndroidTelephony()
      val result = androidTelephony.execute(appContext.reactContext, action, promise)
      result
    }
  }
}
