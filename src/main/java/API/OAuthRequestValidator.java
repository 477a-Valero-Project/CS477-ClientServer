package API;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Martin on 2/10/2015.
 * modified from:
 * http://enrico.sartorello.org/blog/2013/10/implement-2-legged-oauth-server-with-java-restlet/
 */
public class OAuthRequestValidator {
    static final int TIMESTAMP_VALIDITY_IN_SECS = 120;

    private static final OAuthRequestValidator instance = new OAuthRequestValidator();

    private Logger log;
    private AbstractRequestsFootprintCache requestsCache;

    private OAuthRequestValidator() {
        requestsCache = LocalRequestsFootprintCache.getInstance();
        log = LoggerFactory.getLogger(OAuthRequestValidator.class);
    }

    OAuthRequestValidator(AbstractRequestsFootprintCache requestsCache, Logger log) {
        this.requestsCache = requestsCache;
        this.log = log;
    }

    public static final OAuthRequestValidator getInstance() {
        return instance;
    }

    public boolean validate(Verb verb, String url, String timestamp, String nonce, String signatureMethod,
                            String version, String publicKey, String secretKey, String requestSignature) {

        DefaultApi10a api = new DummyAPIProvider();

        // Check if the request is too old
        long seconds = (System.currentTimeMillis() / 1000L) - Long.valueOf(timestamp);
        if (seconds < 0 || seconds > TIMESTAMP_VALIDITY_IN_SECS) {
            log.warn("Received old request:\n" +
                    formatRequest(verb, url, timestamp, nonce, signatureMethod, version, publicKey, requestSignature));
            return false;
        }

        // Check if the request has already been sent once
        boolean replayedRequest;
        try {
            replayedRequest = requestsCache.isCachedOrPutInCache(timestamp, nonce, publicKey);
        } catch (Exception e) {
            // If the cache is not working properly, consider the request as not replayed
            replayedRequest = false;
        }
        if (replayedRequest) {
            log.warn("Received replayed request:\n" +
                    formatRequest(verb, url, timestamp, nonce, signatureMethod, version, publicKey, requestSignature));
            return false;
        }

        OAuthRequest request = new OAuthRequest(verb, url);
        // add the necessary parameters
        request.addOAuthParameter(OAuthConstants.TIMESTAMP, timestamp);
        request.addOAuthParameter(OAuthConstants.NONCE, nonce);
        request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, publicKey);
        request.addOAuthParameter(OAuthConstants.SIGN_METHOD, signatureMethod);
        request.addOAuthParameter(OAuthConstants.VERSION, version);

        String baseString = api.getBaseStringExtractor().extract(request);
        // Passing an empty token, as the 2-legged OAuth doesn't require it
        String realSignature = api.getSignatureService().getSignature(baseString, secretKey, "");

        return (requestSignature.compareTo(realSignature) == 0);
    }

    private String formatRequest(Verb verb, String url, String timestamp, String nonce, String signatureMethod,
                                 String version, String publicKey, String requestSignature) {

        return new StringBuffer()
                .append(verb.name()).append(" ").append(url)
                .append("\n\ttimestamp: ").append(timestamp)
                .append("\n\tnonce: ").append(nonce)
                .append("\n\tsignature method: ").append(signatureMethod)
                .append("\n\tversion: ").append(version)
                .append("\n\tpublic key: ").append(publicKey)
                .append("\n\trequest signature: ").append(requestSignature)
                .toString();
    }
}
