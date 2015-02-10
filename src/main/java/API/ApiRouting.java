package API;

import Logic.Database.Users;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.ext.oauth.AccessTokenServerResource;
import org.restlet.ext.oauth.AuthorizationServerResource;
import org.restlet.ext.oauth.GrantType;
import org.restlet.ext.oauth.internal.ClientManager;
import org.restlet.ext.oauth.internal.TokenManager;
import org.restlet.routing.Router;

import API.RecieveData.*;

/**
 * Created by Martin on 10/24/2014.
 */
public class ApiRouting extends Application {

    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());

        getContext().getAttributes().put(ClientManager.class.getName(), Users.getClientManager());
        getContext().getAttributes().put(TokenManager.class.getName(), Users.getTokenManager());

        router.attach("/receive/", ReceiveData.class);
        router.attach("/authorize", AuthorizationServerResource.class);

        return router;
    }
}
