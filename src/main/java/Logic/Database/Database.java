package Logic.Database;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.List;

/**
 * Created by Martin on 10/25/2014.
 */
public class Database {

    /**
     * Starts a loop to kill off old oauth entries.
     * Plans it to run around once every 2.5 min
     */
    public static void init()
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    removeOldOAuth();
                    try {
                        Thread.sleep(150000);
                    }
                    catch(Exception e)
                    {
                        //doesn't really matter
                    }
                }
            }
        });
    }

    /**
     * Update the processed location of file
     * @param recordId the record id
     * @param location the processed location
     * @return whether it was able to update or not
     */
    static synchronized public boolean updateRecord(int recordId, String location)
    {
        Session session = ConfigurationManager.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Record r = (Record)session.createQuery("FROM Record r WHERE r.recordId=" + recordId).list().get(0);
        if(r == null)
        {
            return false;
        }
        r.setFilePathProcess(location);
        session.update(r);
        tx.commit();
        return true;
    }

    static synchronized public void removeOldOAuth()
    {
        Session session = ConfigurationManager.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List l = session.createQuery("FROM OAuthCache").list();
        //300000 ms = 5 minutes
        Date curr = new Date(new Date().getTime() - 300000);
        for(Object o : l)
        {
            OAuthCache cached = (OAuthCache)o;
            if(cached.getAccessed().before(curr))
            {
                session.delete(cached);
            }
            else
            {
                break;
            }
        }
        tx.commit();
    }

    static synchronized public boolean tryPutOAuth(String timestamp, String nonce, String publicKey)
    {
        Session session = ConfigurationManager.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List l = session.createQuery("FROM OAuthCache a WHERE a.timestamp='" + timestamp + "'").list();
        for(Object o : l)
        {
            OAuthCache cached = (OAuthCache)o;
            if(cached.getNonce().equals(nonce) && cached.getPublicKey().equals(publicKey))
            {
                return true;
            }
        }
        OAuthCache o = new OAuthCache(timestamp, nonce, publicKey);
        session.save(o);
        tx.commit();
        return false;
    }

    //Add file to database
    static synchronized public int makeRecord(int doctorID, int patientID, String location) {
        Record r = new Record(doctorID, patientID, location, "not processed");
        Session session = ConfigurationManager.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(r);
        tx.commit();
        return r.getRecordId();
    }

    //get files from database
    static synchronized public List getRecords(int patientID) {
        Session session = ConfigurationManager.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List list = session.createQuery("FROM Record r WHERE r.patientId=" + patientID).list();
        tx.commit();
        return list;
    }

    static synchronized public Record getRecord(int recordId)
    {
        Session session = ConfigurationManager.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List list = session.createQuery("FROM Record r WHERE r.recordId=" + recordId).list();
        tx.commit();
        return (Record)list.get(0);
    }

    static synchronized public int makeDoctor(String password) {
        Doctor d = new Doctor(password);
        Session session = ConfigurationManager.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(d);
        tx.commit();
        return d.getDoctorId();
    }

    static synchronized public int makePatient(String password) {
        Patient d = new Patient(password);
        Session session = ConfigurationManager.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(d);
        tx.commit();
        return d.getPatientId();
    }

    static synchronized public Doctor getDoctor(int id) {
        Session session = ConfigurationManager.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List list = session.createQuery("FROM Doctor d WHERE d.doctorId = " + id).list();
        tx.commit();
        return (Doctor) list.get(0);
    }

    /**
     * Give back all ids and passwords.  This is to initialize the usermanger.
     *
     * @return should be an array of size 2 with username and password.
     */
    static synchronized public List[] getUsers() {
        List[] ret = new List[2];
        Session session = ConfigurationManager.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        ret[0] = session.createQuery("FROM Doctor").list();
        ret[1] = session.createQuery("FROM Patient").list();
        tx.commit();
        return ret;
    }

    /**
     * For testing parts of database.
     * @param args
     */
    public static void main(String args[])
    {
        System.out.println(tryPutOAuth("a", "b", "d"));

        //System.out.println(getDoctor(1));
    }
}
