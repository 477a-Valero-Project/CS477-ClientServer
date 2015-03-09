package Logic.Database;

import org.hibernate.Query;
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
        LiquibaseManager manager = new LiquibaseManager();
        manager.update(manager.getConnection());
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

    static synchronized public Group getGroupById(int id)
    {
        Session session = ConfigurationManager.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List list = session.createQuery("FROM Group g WHERE g.id=" + id).list();
        tx.commit();
        if(list.size() == 0)
            return null;
        return (Group)list.get(0);
    }

    static synchronized public Group getGroupByName(String name)
    {
        Session session = ConfigurationManager.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Query q = session.createQuery("FROM Group g WHERE g.name=:name");
        List list = q.setString("name", name).list();
        tx.commit();
        if(list.size() == 0)
            return null;
        return (Group)list.get(0);
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
        Query q = session.createQuery("FROM OAuthCache a WHERE a.timestamp=:timestamp");
        List l = q.setString("timestamp", timestamp).list();
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

    static synchronized public String getRecordsJSON(int patientID) {
        List<Record> list = getRecords(patientID);
        StringBuilder ret = new StringBuilder();
        ret.append("[");
        for(Record r : list)
        {
            if(ret.length() > 1)
            {
                ret.append(",");
            }
            ret.append(r.getRecordId());
        }
        ret.append("]");
        return ret.toString();
    }

    static synchronized public Record getRecord(int recordId)
    {
        Session session = ConfigurationManager.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List list = session.createQuery("FROM Record r WHERE r.recordId=" + recordId).list();
        tx.commit();
        if(list.size() == 0)
            return null;
        return (Record)list.get(0);
    }

    static synchronized public User makeDefaultDoctor(String password) {
        User u = new User(password);
        u.setOwner(-1);
        u.setRole(Role.BASIC_DOCTOR);
        Session session = ConfigurationManager.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(u);
        tx.commit();
        return u;
    }

    static synchronized public User makeDefaultPatient(User user) {
        User u = new User();
        u.setOwner(user.getId());
        u.setRole(Role.PATIENT);
        Session session = ConfigurationManager.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(u);
        tx.commit();
        return u;
    }

    static synchronized public User getUserById(int id)
    {
        Session session = ConfigurationManager.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List list = session.createQuery("FROM User u WHERE u.id = " + id).list();
        tx.commit();
        if(list.size() == 0)
            return null;
        return (User) list.get(0);
    }

    static synchronized public void saveUser(User user)
    {
        Session session = ConfigurationManager.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(user);
        tx.commit();
    }

    /**
     * Takes in a user and attempts to also update the groups of any users that this
     * user owns.
     * @param user
     * @param groupId
     */
    static synchronized public void updateUserGroup(User user, int groupId) {
        user.setGroupId(groupId);
        Session session = ConfigurationManager.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(user);
        List l = session.createQuery("From User u WHERE u.owner=" + user.getId()).list();
        for(Object o : l)
        {
            User temp = (User)o;
            temp.setGroupId(groupId);
            session.save(temp);
        }
        tx.commit();
    }

    static synchronized public String getUsersByOwner(User user)
    {
        Session session = ConfigurationManager.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(user);
        List l = session.createQuery("From User u WHERE u.owner=" + user.getId()).list();
        StringBuilder build = new StringBuilder();
        build.append("[");
        for(Object o : l)
        {
            User temp = (User)o;
            build.append(temp.getJSON());
        }
        build.append("]");
        tx.commit();
        return build.toString();
    }

    private static void addAction(StringBuilder ret, String action)
    {
        if(ret.length() > 1)
        {
            ret.append(",");
        }
        ret.append(action);
    }


    /**
     * Give back all ids and passwords.  This is to initialize the usermanger.
     *
     * @return should be an array of size 2 with username and password.
     */
    static synchronized public List getUsers() {
        Session session = ConfigurationManager.getSessionFactory().openSession();
        List ret = session.createQuery("FROM User").list();
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
