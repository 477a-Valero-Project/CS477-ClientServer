package Logic.Database;

import org.restlet.ext.oauth.internal.ClientManager;
import org.restlet.ext.oauth.internal.TokenManager;
import org.restlet.ext.oauth.internal.memory.MemoryClientManager;
import org.restlet.ext.oauth.internal.memory.MemoryTokenManager;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by Martin on 2/10/2015.
 */
public class Users {
    private static Hashtable<String, String> table;
    private static ClientManager clientManager;
    private static TokenManager tokenManager;

    public static ClientManager getClientManager() {
        return clientManager;
    }

    public static TokenManager getTokenManager() {
        return tokenManager;
    }

    public static void init()
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
        clientManager = new MemoryClientManager();
        tokenManager = new MemoryTokenManager();
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
}
