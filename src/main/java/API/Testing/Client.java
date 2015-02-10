package API.Testing;

import API.DummyAPIProvider;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

/**
 * Created by Martin on 2/10/2015.
 * For testing application.
 */
public class Client {
    public static void main(String args[]) {
        OAuthService service = new ServiceBuilder().provider(DummyAPIProvider.class)
                .apiKey("apples").apiSecret("peanuts")
                .signatureType(SignatureType.QueryString).build();
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://127.0.0.1:8182/receive/");
        service.signRequest(OAuthConstants.EMPTY_TOKEN, request);
        request.addQuerystringParameter("filename", "test");
        Response response = request.send();
        System.out.println(response.getBody());
    }
}
