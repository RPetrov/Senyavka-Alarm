package rpetrov.senyavkaspeakingalarmclock.providers

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Handler
import android.os.Vibrator
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.support.v7.app.AppCompatActivity
import android.util.Log
import java.util.*
import android.media.AudioManager
import android.preference.PreferenceManager


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
            providers.filter {
                try {
                    return@filter it.prepare()
                } catch(e: Exception) {
                    return@filter false
                }
            }.map { it.getText() }


    override fun onPostExecute(result: List<String>) {

        // Vibrate for 500 milliseconds
        val vibrator = context.getSystemService(AppCompatActivity.VIBRATOR_SERVICE)
        if (vibrator is Vibrator) {
            vibrator.vibrate(longArrayOf(700, 300, 700, 300, 700, 300, 700, 300, 700, 1500), -1)
        }

        val items = result

        Handler().postDelayed({
            tts = TextToSpeech(context, object : TextToSpeech.OnInitListener {
                override fun onInit(status: Int) {
                    if (status == TextToSpeech.SUCCESS) {

                        val locale = Locale("ru")

                        val result = tts?.setLanguage(locale)

                        tts?.setOnUtteranceProgressListener(object: UtteranceProgressListener() {
                            override fun onDone(utteranceId: String?) {
                                val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
                                playSearchArtist(sp.getString("playlist", null))
                            }

                            override fun onError(utteranceId: String) {

                            }

                            override fun onStart(utteranceId: String) {

                            }

                        })

                        for (i in 0..items.size - 1) {
                            val myHashAlarm = HashMap<String, String>()
                            myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_ALARM.toString())
                            if(i == items.size - 1)
                                myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "LAST MESSAGE!")
                            tts?.speak(items[i], TextToSpeech.QUEUE_ADD, myHashAlarm)
                        }

                    } else {
                        Log.e("TTS", "Ошибка!")
                    }
                }
            })


        }, 6000)

    }

    fun playSearchArtist(playlist: String?) {

        playlist ?: return

        val intent = Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH)
        intent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS,
                MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE)
        intent.putExtra(MediaStore.EXTRA_MEDIA_PLAYLIST, playlist)
        intent.putExtra(SearchManager.QUERY, playlist)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    fun cancel() {

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