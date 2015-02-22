package Logic.Database;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by Martin on 2/10/2015.
 */
public class Users {
    private static Hashtable<Integer, String> table;

    public static synchronized void init()
    {
        table = new Hashtable<>();
        List list = Database.getUsers();
        for(Object o : list)
        {
            AuthenticationModule a = (AuthenticationModule)o;
            table.put(a.getId(), a.getPassword());
        }
    }

    public static synchronized void addUser(int id, String password)
    {
        table.put(id, password);
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

    public static String getPassword(String id)
    {
        if(table == null)
        {
            init();
        }
        return table.get(id);
    }

    public static void main(String args[])
    {
        System.out.println(getPassword("doctor:1"));
    }
}
