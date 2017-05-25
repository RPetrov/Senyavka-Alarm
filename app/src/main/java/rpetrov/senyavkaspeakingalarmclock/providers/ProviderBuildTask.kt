package rpetrov.senyavkaspeakingalarmclock.providers

import android.os.AsyncTask

/**
 * Created by Roman Petrov
 */

class ProviderBuildTask : AsyncTask<IProvider, Void, List<String>> {

    constructor() : super()

    override fun doInBackground(vararg providers: IProvider): List<String> =
            providers.filter { it.prepare() }.map { it.getText() }


    override fun onPostExecute(result: List<String>?) {
        super.onPostExecute(result)
    }
}