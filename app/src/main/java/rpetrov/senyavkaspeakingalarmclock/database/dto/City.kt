package rpetrov.senyavkaspeakingalarmclock.database.dto

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

/**
 * Created by Roman Petrov
 */
@DatabaseTable(tableName = "cities")
data class City(@DatabaseField() val city: String,
                @DatabaseField() val region: String,
                @DatabaseField() val lat: Float,
                @DatabaseField() val lon: Float,
                @DatabaseField() val id: Int,
                @DatabaseField() val area: String){

    constructor() : this("", "", 0f, 0f, 0, "")

    override fun toString(): String {
        return "$city ($region, $area)"
    }


}



