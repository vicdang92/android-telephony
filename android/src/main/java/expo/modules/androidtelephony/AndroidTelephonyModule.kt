package expo.modules.androidtelephony

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import org.json.JSONException
import org.json.JSONObject

class AndroidTelephonyModule : Module() {
    override fun definition() = ModuleDefinition {
        Name("AndroidTelephony")

        Function("getAllCellInfo") {
            try {
                val androidTelephony = AndroidTelephony()
                val result = androidTelephony.getAllCellInfo(appContext.reactContext)
                result.toString()
            } catch (e: JSONException) {
                val json = JSONObject()
                json.put("ERROR", e.message)
                json.toString()
            }
        }
        Function("execute") { action: String ->
            try {
                val androidTelephony = AndroidTelephony()
                val result = androidTelephony.execute(appContext.reactContext, action)
                result.toString()
            } catch (e: JSONException) {
                val json = JSONObject()
                json.put("ERROR", e.message)
                json.toString()
            }
        }
    }
}
