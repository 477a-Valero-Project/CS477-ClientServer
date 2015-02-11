package API.RecieveData;

import API.OAuthProtectedResource;
import API.Status;
import Logic.Database.Database;
import Logic.Database.Users;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.util.Map;
import java.util.Random;

/**
 * Created by Martin on 2/10/2015.
 */
public class PatientWeb extends OAuthProtectedResource {
    public String getSecretKey(String id)
    {
        //if a patient decides to try to make a patient
        //their secret key will be random
        //probably shouldn't ever authenticate
        //probably
        if(!id.startsWith("doctor"))
        {
            return String.valueOf(new Random().nextInt());
        }
        return Users.getPassword(id);
    }
    @Get
    public Object getRes()
    {
        return "Patient page";
    }

    /**
     * Goal for method: accept file via post
     *                  send back id
     *                  start process to process data
     * @param entity
     * @return
     * @throws Exception
     */
    @Post
    public Object accept(Representation entity) throws Exception
    {
        Status.StatusBuilder builder = new Status.StatusBuilder();
        Map<String, String> map = getQuery().getValuesMap();
        if(!map.containsKey("password") || !map.containsKey("doctorid"))
        {
            builder.append("status", "bad");
            return builder.build().toString();
        }
        builder.append("status", "good");
        builder.append("patientid", String.valueOf(Database.makePatient(map.get("password"),
                Integer.valueOf(map.get("doctorid")))));
        Users.init();
        return builder.build().toString();
    }
}
