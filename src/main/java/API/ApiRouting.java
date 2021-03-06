package API;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import API.RecieveData.*;

/**
 * Created by Martin on 10/24/2014.
 */
public class ApiRouting extends Application {

    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());

        router.attach("/action/", ActionWeb.class);
        router.attach("/doctor/", DoctorWeb.class);

        return router;
    }
}
