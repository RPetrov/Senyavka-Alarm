package rpetrov.senyavkaspeakingalarmclock

import android.content.Context
import ui.AutoCompletePreference
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * Created by Roman Petrov
 */
class Cities {
    companion object Cities {
        val cities: ArrayList<AutoCompletePreference.City> = ArrayList()

        fun init(c: Context) {
            var reader: BufferedReader? = null
            try {
                reader = BufferedReader(
                        InputStreamReader(c.assets.open("ru-list.csv"), "UTF-8"))

                // do reading, usually loop until end of file reading
                var mLine: String?

                while (true) {
                    mLine = reader.readLine()
                    if (mLine == null) break

                    val splitted = mLine.split(";")
                    cities.add(AutoCompletePreference.City(splitted[2], splitted[3].toFloat(), splitted[4].toFloat(), splitted[1] + ", " + splitted[0]))

                }

            } catch (e: IOException) {

            } finally {
                if (reader != null) {
                    try {
                        reader.close()
                    } catch (e: IOException) {
                    }
                }
            }
        }
    }
}