package rpetrov.senyavkaspeakingalarmclock;

import java.sql.SQLException;

import rpetrov.senyavkaspeakingalarmclock.database.DatabaseDeployer;
import rpetrov.senyavkaspeakingalarmclock.database.dao.CitiesDao;
import rpetrov.senyavkaspeakingalarmclock.database.dto.City;
import rpetrov.senyavkaspeakingalarmclock.database.helpers.DBOpenHelper;

/**
 * Created by Roman Petrov
 */

public class Application extends android.app.Application {

    /**
     * Single instance
     */
    private static Application instance;



    // database helpers
    private DBOpenHelper dbOpenHelper;
    private CitiesDao citiesDao;



    /**
     *
     * @return single Application instance
     */
    public static Application instance() {
        return instance;
    }



    @Override
    public void onCreate() {


        instance = this;
        super.onCreate();


        new DatabaseDeployer().deploy();


        dbOpenHelper = new DBOpenHelper(this, "Cities.sqlite");
        try {
            citiesDao = new CitiesDao(dbOpenHelper.getConnectionSource(), City.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DBOpenHelper getDbOpenHelper() {
        return dbOpenHelper;
    }

    public CitiesDao getCitiesDao() {
        return citiesDao;
    }
}
