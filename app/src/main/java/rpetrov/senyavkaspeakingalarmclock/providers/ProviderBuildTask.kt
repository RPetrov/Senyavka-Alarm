package rpetrov.senyavkaspeakingalarmclock.providers

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.AsyncTask
import android.os.Handler
import android.os.Vibrator
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.support.v7.app.AppCompatActivity
import android.util.Log
import rpetrov.senyavkaspeakingalarmclock.AlarmService
import java.util.*


/**
 * Created by Roman Petrov
 */

class ProviderBuildTask : AsyncTask<Void, Void, List<String>> {

    private val context: Context
    private val vibrator: Vibrator

    private var cancel: Boolean = false
    private val textProviders: List<ITextProvider>
    private val runnableProviders: List<IRunnableProvider>


    constructor(context: Context, textProviders: List<ITextProvider>, runnableProviders: List<IRunnableProvider>) : super() {
        this.context = context
        //    this.textProviders = textProviders
        //     this.runnableProviders = runnableProviders

        this.textProviders = textProviders.filter {
            try {
                return@filter it.isEnable()
            } catch(e: Exception) {
                return@filter false
            }
        }

        this.runnableProviders = runnableProviders.filter {
            try {
                return@filter it.isEnable()
            } catch(e: Exception) {
                return@filter false
            }
        }


        vibrator = context.getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator
    }

    private var tts: TextToSpeech? = null


    override fun doInBackground(vararg voids: Void): List<String> =
            textProviders.filter {
                try {
                    return@filter it is ITextProvider && it.prepare()
                } catch(e: Exception) {
                    return@filter false
                }
            }.map { it.getText() }


    override fun onPostExecute(result: List<String>) {

        if (cancel) return

        vibrator.vibrate(longArrayOf(700, 300, 700, 300, 700, 700), -1)

        val items = result

        Handler().postDelayed({


            tts = TextToSpeech(context, object : TextToSpeech.OnInitListener {
                override fun onInit(status: Int) {

                    if (cancel) return

                    if (status == TextToSpeech.SUCCESS) {

                        val locale = Locale("ru")

                        val result = tts?.setLanguage(locale)

                        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                            override fun onDone(utteranceId: String?) {
                                if (cancel) return
                                for (i in runnableProviders) {
                                    i.run()
                                }

                                // stop service
                                val stopServiceIntent = Intent(context, AlarmService::class.java)
                                stopServiceIntent.putExtra("command", "stop")
                                context.startService(stopServiceIntent)
                                tts?.shutdown()
                            }

                            override fun onError(utteranceId: String) {

                            }

                            override fun onStart(utteranceId: String) {

                            }

                        })

                        for (i in 0..items.size - 1) {
                            val myHashAlarm = HashMap<String, String>()
                            myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_ALARM.toString())
                            if (i == items.size - 1)
                                myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "LAST MESSAGE!")
                            tts?.speak(items[i], TextToSpeech.QUEUE_ADD, myHashAlarm)
                        }

                        if(items.isEmpty()){
                            for (i in runnableProviders) {
                                i.run()
                            }
                        }

                    } else {
                        Log.e("TTS", "Ошибка!")
                    }
                }
            })


        }, 5000)

    }


    fun cancel() {
        cancel = true
        vibrator.cancel()
        tts?.stop()
        tts?.shutdown()

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