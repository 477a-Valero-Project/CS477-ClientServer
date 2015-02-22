package API.RecieveData;

import API.ActionManager;
import API.OAuthProtectedResource;
import API.Status;
import Logic.Database.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.restlet.data.MediaType;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 2/21/2015.
 */
public class ActionWeb extends OAuthProtectedResource {
    @Override
    public String getSecretKey(String id) {
        return Users.getPassword(id);
    }

    @Get
    public Object acceptGet(Representation entity)
    {
        Status.StatusBuilder builder = new Status.StatusBuilder();
        Map<String, String> map = getQuery().getValuesMap();
        if(!map.containsKey("action"))
        {
            builder.append("status", "bad");
            return builder.build().toString();
        }
        User u = Database.getUserById(Integer.valueOf(map.get("ID")));
        switch(map.get("action")) {
            case "list":
                builder.append("status", "good");
                builder.append("list", ActionManager.list(Database.getUserById(Integer.valueOf(map.get("ID")))));
                break;
            case "modifySelf":
                builder.append("status", "good");
                if (map.containsKey("password"))
                    u.setPassword(User.MD5(map.get("password")));
                if (map.containsKey("name"))
                    u.setName(map.get("name"));
                Database.saveUser(u);
                break;
            case "modifyPatient":
                User other = Database.getUserById(Integer.valueOf(map.get("patient")));
                if (other.canModify(u)) {
                    builder.append("status", "good");
                    if (map.containsKey("password"))
                        other.setPassword(User.MD5(map.get("password")));
                    if (map.containsKey("name"))
                        other.setName(map.get("name"));
                    Database.saveUser(other);
                } else {
                    builder.append("status", "bad");
                }
                break;
            case "makePatient":
                if (u.getRole() != Role.PATIENT) {
                    builder.append("status", "good");
                    User p = Database.makeDefaultPatient(u);
                    builder.append("patientId", String.valueOf(p.getId()));
                } else {
                    builder.append("status", "bad");
                }
                break;
            case "getRecordRaw":
                if (!map.containsKey("recordId"))
                {
                    builder.append("status", "bad");
                }
                else
                {
                    Record r = Database.getRecord(Integer.valueOf(map.get("recordId")));
                    User other2 = Database.getUserById(r.getPatientId());
                    if(!other2.canModify(u))
                    {
                        builder.append("status", "bad");
                    }
                    else
                    {
                        return new FileRepresentation(r.getFilePathRaw(), MediaType.MULTIPART_FORM_DATA);
                    }
                }
                break;
            case "getRecordProcessed":
                if (!map.containsKey("recordId"))
                {
                    builder.append("status", "bad");
                }
                else
                {
                    Record r = Database.getRecord(Integer.valueOf(map.get("recordId")));
                    User other2 = Database.getUserById(r.getPatientId());
                    if(!other2.canModify(u))
                    {
                        builder.append("status", "bad");
                    }
                    else
                    {
                        return new FileRepresentation(r.getFilePathProcess(), MediaType.MULTIPART_FORM_DATA);
                    }
                }
                break;
        }
        return builder.build().toString();
    }

    @Post
    public Object posted(Representation entity) throws Exception
    {
        Representation rep = null;
        API.Status.StatusBuilder stats = new API.Status.StatusBuilder();
        if (entity != null) {
            if (MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(),
                    true)) {
                Map<String, String> map = getQuery().getValuesMap();
                if(!map.containsKey("filename") || !map.containsKey("patientid"))
                {
                    stats.append("status", "bad");
                    stats.append("extra", "missing stuff");
                    return stats.build().toString();
                }
                String fileName = "J:\\temp\\" + map.get("filename");

                // The Apache FileUpload project parses HTTP requests which
                // conform to RFC 1867, "Form-based File Upload in HTML". That
                // is, if an HTTP request is submitted using the POST method,
                // and with a content type of "multipart/form-data", then
                // FileUpload can parse that request, and get all uploaded files
                // as FileItem.

                // 1/ Create a factory for disk-based file items
                DiskFileItemFactory factory = new DiskFileItemFactory();
                factory.setSizeThreshold(1000240);

                // 2/ Create a new file upload handler based on the Restlet
                // FileUpload extension that will parse Restlet requests and
                // generates FileItems.
                RestletFileUpload upload = new RestletFileUpload(factory);
                List<FileItem> items;

                // 3/ Request is parsed by the handler which generates a
                // list of FileItems
                items = upload.parseRequest(getRequest());


                // Process only the uploaded item called "fileToUpload" and
                // save it on disk
                boolean found = false;
                boolean owner = false;
                for (final Iterator<FileItem> it = items.iterator(); it
                        .hasNext()
                        && (!found || !owner);) {
                    FileItem fi = it.next();
                    if (fi.getFieldName().equals("fileToUpload")) {
                        found = true;
                        File file = new File(fileName);
                        System.out.println(fi.getContentType());
                        fi.write(file);
                    }
                }

                // Once handled, the content of the uploaded file is sent
                // back to the client.
                if (found) {
                    stats.append("status", "good");
                    User u = Database.getUserById(Integer.valueOf(map.get("patientid")));
                    int doctor = !map.containsKey("doctorid") ? u.getOwner() : Integer.valueOf(map.get("doctorid"));
                    stats.append("fileid", String.valueOf(Database.makeRecord(doctor, u.getId(), fileName)));
                    return stats.build().toString();
                } else {
                    stats.append("status", "bad");
                    stats.append("extra", "file not found");
                    return stats.build().toString();
                }
            }
        } else {
            stats.append("status", "bad");
            stats.append("extra", "entity missing?");
            return stats.build().toString();
        }
        //should not reach here
        return null;
    }
}
