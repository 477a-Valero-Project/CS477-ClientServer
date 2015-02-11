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
        List[] list = Database.getUsers();
        for(List l : list)
        {
            for(Object o : l)
            {
                AuthenticationModule a = (AuthenticationModule)o;
                table.put(a.getId(), a.getPassword());
            }
        }
    }

    public static boolean validate(String id, String password)
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
