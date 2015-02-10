package API;

import Logic.Database.Users;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.ext.oauth.AccessTokenServerResource;
import org.restlet.ext.oauth.ClientVerifier;
import org.restlet.routing.Router;

import API.RecieveData.*;
import org.restlet.security.ChallengeAuthenticator;

/**
 * Created by Martin on 10/24/2014.
 */
public class ApiRouting extends Application {

    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());




        router.attach("/receive/", ReceiveData.class);
        //router.attach("/authorize", au);

        return router;
    }
}
