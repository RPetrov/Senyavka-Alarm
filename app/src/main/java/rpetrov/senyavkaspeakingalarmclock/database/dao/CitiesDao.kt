package rpetrov.senyavkaspeakingalarmclock.database.dao


import com.j256.ormlite.dao.BaseDaoImpl
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.DatabaseTableConfig
import rpetrov.senyavkaspeakingalarmclock.database.dto.City
import java.sql.SQLException

/**
 * Created by Roman Petrov.
 */

class CitiesDao : BaseDaoImpl<City, Long> {
    @Throws(SQLException::class)
    constructor(dataClass: Class<City>) : super(dataClass)

    @Throws(SQLException::class)
    constructor(connectionSource: ConnectionSource, dataClass: Class<City>) : super(connectionSource, dataClass)

    @Throws(SQLException::class)
    constructor(connectionSource: ConnectionSource, tableConfig: DatabaseTableConfig<City>) : super(connectionSource, tableConfig)



}
