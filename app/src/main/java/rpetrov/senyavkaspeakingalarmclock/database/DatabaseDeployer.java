package rpetrov.senyavkaspeakingalarmclock.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import rpetrov.senyavkaspeakingalarmclock.Application;

/**
 * Created by Roman Petrov.
 */

public class DatabaseDeployer {


    public void deploy() {

        if(checkDatabase("Cities.sqlite")){
            //Logger.w("DATABASE ALREADY DEPLOYED");
        }  else {
            copyDataBase("Cities.sqlite", "Cities.sqlite");
        }


    }

    private boolean checkDatabase(String name){
        File file = Application.instance().getDatabasePath(name);
        return file.exists();
    }

    private boolean deleteDatabase(String name){
        File file = Application.instance().getDatabasePath(name);
        return file.delete();
    }


    private void copyDataBase(String name, String newName) {



        byte[] buffer = new byte[1024];
        OutputStream myOutput = null;
        int length;

        InputStream myInput = null;
        try {
            myInput = Application.instance().getAssets().open(name);

            File file = Application.instance().getDatabasePath(newName);

            file.getParentFile().mkdirs();
            myOutput = new FileOutputStream(file);
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }


           //  Logger.w("DATABASE " + newName + " COPIED!");


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(myOutput != null){
                    myOutput.flush();
                    myOutput.close();
                }

                if(myInput != null){
                    myInput.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
