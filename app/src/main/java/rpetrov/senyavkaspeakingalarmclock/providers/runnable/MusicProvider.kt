package rpetrov.senyavkaspeakingalarmclock.providers.text

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.provider.MediaStore
import rpetrov.senyavkaspeakingalarmclock.R


/**
 * Created by Roman Petrov
 */
class MusicProvider : BaseProvider, rpetrov.senyavkaspeakingalarmclock.providers.IRunnableProvider {
    constructor(context: Context) : super(context)


    override fun run() {
        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (sp.getBoolean("checkBoxMusic.isChecked", false))
            playSearchArtist(sp.getString("playlist", null))
    }

    private fun playSearchArtist(playlist: String?) {

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

    override fun getConfigLayout(): Int = R.xml.music

    override fun getPermissions(): Array<String> = emptyArray()

    override fun getName(): String = "Музыкальный плеер"

    override fun getDescription(): String = "Запускает музыкальный плеер"

    override fun isConfigurable(): Boolean = true
}