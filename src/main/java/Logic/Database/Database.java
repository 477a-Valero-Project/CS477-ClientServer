package Logic.Database;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created by Martin on 10/25/2014.
 */
public class Database {

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
        int recordId = makeRecord(1, 1, "nope");
        Record r = getRecord(recordId);
        System.out.println(r);
        updateRecord(recordId, "test2");
        r = getRecord(recordId);
        System.out.println(r);

        //System.out.println(getDoctor(1));
    }
}
