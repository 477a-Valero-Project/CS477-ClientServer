package Logic.Database;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by Martin on 2/10/2015.
 */
public class Users {
    private static Hashtable<String, String> table;

    public static synchronized void init()
    {
        table = new Hashtable<>();
        List list = Database.getUsers();
        for(Object o : list)
        {
            AuthenticationModule a = (AuthenticationModule)o;
            if(a.getPassword() != null)
                table.put(String.valueOf(a.getId()), a.getPassword());
        }
    }

    public static synchronized boolean addUser(int id, String password)
    {
        if(table == null)
        {
            init();
            return true;
        }
        if(password == null)
        {
            return false;
        }
        return table.put(String.valueOf(id), password) == null;
    }

    public static synchronized void removeUser(int id)
    {
        if(table == null)
        {
            init();
            return;
        }
        table.remove(id);
    }

    public static boolean validate(int id, String password)
    {
        if(table == null)
        {
            init();
        }
        String str = table.get(id);
        return str.equals(password);
    }

    public static synchronized String getPassword(String id)
    {
        if(table == null)
        {
            init();
        }
        String ret = table.get(id);
        return ret == null || ret.isEmpty() ? null : ret;
    }

    public static void main(String args[])
    {
        System.out.println(getPassword("doctor:1"));
    }
}
