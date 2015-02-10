package API;

import API.ApiRouting;
import Logic.Database.ConfigurationManager;
import Logic.Database.Database;
import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.ext.oauth.GrantType;
import org.restlet.resource.ServerResource;

/**
 * Created by Martin on 10/24/2014.
 */
public class App extends ServerResource {

    public static void main(String args[]) throws Exception
    {
        Component component = new Component();
        Server server = component.getServers().add(Protocol.HTTP, 8182);
        server.getContext().getParameters().add("useForwardedForHeader", "true");
        ConfigurationManager.init();
        Database.init();
        component.getDefaultHost().attach(new ApiRouting());
        component.start();
    }
}
