package API;

import java.util.Map;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;
import org.scribe.model.Verb;

/**
 * Created by Martin on 2/10/2015.
 * From http://enrico.sartorello.org/blog/2013/10/implement-2-legged-oauth-server-with-java-restlet/
 */
public abstract class OAuthProtectedResource extends ServerResource {
    static final String TIMESTAMP_QUERY_PARAM = "oauth_timestamp";
    static final String NONCE_QUERY_PARAM = "oauth_nonce";
    static final String SIGNATURE_METHOD_QUERY_PARAM = "oauth_signature_method";
    static final String VERSION_QUERY_PARAM = "oauth_version";
    static final String CONSUMER_KEY_QUERY_PARAM = "oauth_consumer_key";
    static final String SIGNATURE_QUERY_PARAM = "oauth_signature";
    static final String ID = "ID";

    static final String SECRET_KEY_HEADER = "3scale-app-key";

    private OAuthRequestValidator oauthValidator;

    public OAuthProtectedResource() {
        oauthValidator = OAuthRequestValidator.getInstance();
    }

    OAuthProtectedResource(OAuthRequestValidator oauthValidator) {
        this.oauthValidator = oauthValidator;
    }

    @Override
    protected Representation doHandle() throws ResourceException {
        doOAuthHMACValidation();
        return super.doHandle();
    }

    @Override
    protected Representation doHandle(Variant variant) throws ResourceException {
        doOAuthHMACValidation();
        return super.doHandle(variant);
    }

    private void doOAuthHMACValidation() {

        // Check that all the needed parameters are contained in the request
        Map<String,String> queryValues = getQuery().getValuesMap();
        if (!queryValues.containsKey(TIMESTAMP_QUERY_PARAM) ||
                !queryValues.containsKey(NONCE_QUERY_PARAM) ||
                !queryValues.containsKey(SIGNATURE_METHOD_QUERY_PARAM) ||
                !queryValues.containsKey(VERSION_QUERY_PARAM) ||
                !queryValues.containsKey(CONSUMER_KEY_QUERY_PARAM) ||
                !queryValues.containsKey(SIGNATURE_QUERY_PARAM) ||
                !queryValues.containsKey(ID))
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing OAuth credentials");

        // Validate the request
        boolean isValid = oauthValidator.validate(
                Verb.valueOf(getMethod().getName()),
                getReference().toString(false, false),
                getQuery().getFirstValue(TIMESTAMP_QUERY_PARAM),
                getQuery().getFirstValue(NONCE_QUERY_PARAM),
                getQuery().getFirstValue(SIGNATURE_METHOD_QUERY_PARAM),
                getQuery().getFirstValue(VERSION_QUERY_PARAM),
                getAccessKey(),
                getSecretKey(getQuery().getFirstValue(ID)),
                getQuery().getFirstValue(SIGNATURE_QUERY_PARAM));

        if (!isValid)
            throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "Wrong credentials");
    }

    /**
     * Extract the API secret key from the following request
     * @return the secret key of the request
     */
    public abstract String getSecretKey(String id);

    /**
     * Extract the API public key from the following request
     * @return the public key of the request
     */
    String getAccessKey() {
        return getQuery().getFirstValue(CONSUMER_KEY_QUERY_PARAM);
    }
}
