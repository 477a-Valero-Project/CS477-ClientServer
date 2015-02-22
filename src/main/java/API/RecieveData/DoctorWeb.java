package API.RecieveData;

import API.OAuthProtectedResource;
import API.Status;
import Logic.Database.Database;
import Logic.Database.User;
import Logic.Database.Users;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.util.Map;

/**
 * Created by Martin on 2/10/2015.
 * Makes the doctor with basic permission.
 */
public class DoctorWeb extends OAuthProtectedResource {
    public String getSecretKey(String id)
    {
        return "thisdoesnotmatterreallyithinkhopefullyalwayscanchangeitlater";
    }
    @Get
    public Object getRes()
    {
        return "Doctor Page";
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
        if(!map.containsKey("password"))
        {
            builder.append("status", "bad");
            return builder.build().toString();
        }
        builder.append("status", "good");
        User u = Database.makeDefaultDoctor(User.MD5(map.get("password")));
        builder.append("id", String.valueOf(u.getId()));
        Users.addUser(u.getId(), u.getPassword());
        return builder.build().toString();
    }
}
