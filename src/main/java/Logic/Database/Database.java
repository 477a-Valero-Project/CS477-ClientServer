package Logic.Database;

import java.util.List;

/**
 * Created by Martin on 10/25/2014.
 */
public class Database {

    static private boolean started = false;

    //make table
    static public void initDatabase()
    {
        if(!started)
        {
            started = true;
        }
    }

    //Add file to database
    static synchronized public boolean addDatabase(int doctorID, int patientID, String location)
    {

        return true;
    }

    //get files from database
    static synchronized public List<String> getRecords(int patientID)
    {

        return null;
    }
}
