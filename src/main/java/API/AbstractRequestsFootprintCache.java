package API;

/**
 * This abstract class represents a datastore that keeps tuples of (timestamp, nonce, publicKey).
 * The data is used by OAuth to prevent replay attacks.
 * from http://enrico.sartorello.org/blog/2013/10/implement-2-legged-oauth-server-with-java-restlet/
 */
public abstract class AbstractRequestsFootprintCache {

    public abstract boolean isCachedOrPutInCache(String timestamp, String nonce, String publicKey);

    String getKey(String timestamp, String nonce, String publicKey) {
        return new StringBuffer()
                .append(timestamp).append("_")
                .append(nonce).append("_")
                .append(publicKey)
                .toString();
    }
}