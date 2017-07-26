package rpetrov.senyavkaspeakingalarmclock.database.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public class DBOpenHelper extends OrmLiteSqliteOpenHelper {


    private Map<Class, Dao> daos = new HashMap<>();


    public DBOpenHelper(Context context, String databaseName) {
        super(context, databaseName, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    @SuppressWarnings("unchecked")
    public <D extends Dao<T, ?>, T> D getDaoForClass(Class<D> clazz) {

        Dao dao = daos.get(clazz);

        if (dao == null) {
            try {
                dao = clazz.getConstructor(ConnectionSource.class, Class.class).
                        newInstance(getConnectionSource(), ((ParameterizedType) (clazz.getGenericSuperclass())).getActualTypeArguments()[0]);
                //dao = clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            daos.put(clazz, dao);
        }

        return (D) dao;
    }
}
