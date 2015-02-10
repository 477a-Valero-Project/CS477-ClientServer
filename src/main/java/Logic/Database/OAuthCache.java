package Logic.Database;

import java.util.Date;

/**
 * Created by Martin on 2/10/2015.
 */
public class OAuthCache {
    private int authId;
    private String timestamp;
    private String nonce;
    private String publicKey;
    private Date accessed;

    public OAuthCache() {}
    public OAuthCache(String nTimestamp, String nNonce, String nPublicKey)
    {
        accessed = new Date();
        timestamp = nTimestamp;
        nonce = nNonce;
        publicKey = nPublicKey;
    }

    public Date getAccessed() {
        return accessed;
    }

    public void setAccessed(Date accessed) {
        this.accessed = accessed;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public int getAuthId() {
        return authId;
    }

    public void setAuthId(int authId) {
        this.authId = authId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

}
