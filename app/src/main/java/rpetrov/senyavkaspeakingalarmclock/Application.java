package rpetrov.senyavkaspeakingalarmclock;

import java.sql.SQLException;

/**
 * Created by Roman Petrov
 */

public class Application extends android.app.Application {

    /**
     * Single instance
     */
    private static Application instance;


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
    }
}
