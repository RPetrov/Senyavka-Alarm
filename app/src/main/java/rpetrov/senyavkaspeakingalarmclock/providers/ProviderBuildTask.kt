package rpetrov.senyavkaspeakingalarmclock.providers

import android.content.Context
import android.os.AsyncTask
import android.os.Vibrator
import android.speech.tts.TextToSpeech
import android.support.v7.app.AppCompatActivity
import android.util.Log
import java.util.*

/**
 * Created by Roman Petrov
 */

class ProviderBuildTask : AsyncTask<IProvider, Void, List<String>> {

    private val context: Context
    constructor(context: Context) : super() {
        this.context = context
    }

    private var tts: TextToSpeech? = null




    override fun doInBackground(vararg providers: IProvider): List<String> =
            providers.filter { try {
                return@filter it.prepare()
            } catch(e: Exception) {
                return@filter false
            }
            }.map { it.getText() }


    override fun onPostExecute(result: List<String>) {

        // Vibrate for 500 milliseconds
        val vibrator = context.getSystemService(AppCompatActivity.VIBRATOR_SERVICE)
        if(vibrator is Vibrator){
            vibrator.vibrate(longArrayOf(500, 500, 500, 500, 500, 500, 500, 500, 1000), 1)
        }

        val items = result

        tts = TextToSpeech(context, object: TextToSpeech.OnInitListener {
            override fun onInit(status: Int) {
                if (status == TextToSpeech.SUCCESS) {

                    val locale = Locale("ru")

                    val result = tts?.setLanguage(locale)

                    for(item in items){
                        tts?.speak(item, TextToSpeech.QUEUE_ADD, null)
                    }

                } else {
                    Log.e("TTS", "Ошибка!")
                }
            }
        })
    }

    fun cancel(){

        tts?.stop()

    }


    fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {

            val locale = Locale("ru")

            val result = tts?.setLanguage(locale)

        } else {
            Log.e("TTS", "Ошибка!")
        }

    }
}