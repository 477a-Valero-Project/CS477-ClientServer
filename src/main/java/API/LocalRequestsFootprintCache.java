package API;

import Logic.Database.Database;

/**
 * Created by Martin on 2/10/2015.
 * modified from http://enrico.sartorello.org/blog/2013/10/implement-2-legged-oauth-server-with-java-restlet/
 */
public class LocalRequestsFootprintCache extends AbstractRequestsFootprintCache {
    private static final AbstractRequestsFootprintCache instance = new LocalRequestsFootprintCache();
    public static AbstractRequestsFootprintCache getInstance() {
        return instance;
    }


    public boolean isCachedOrPutInCache(String timestamp, String nonce, String publicKey)
    {
        return Database.tryPutOAuth(timestamp, nonce, publicKey);
    }
}
