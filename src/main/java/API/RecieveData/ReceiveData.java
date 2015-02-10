package API.RecieveData;

import API.OAuthProtectedResource;
import Logic.Database.Database;
import Logic.Database.Patient;
import Logic.Database.Users;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 10/24/2014.
 */
public class ReceiveData extends OAuthProtectedResource {

    public String getSecretKey(String id)
    {
        return Users.getPassword(id);
    }
    @Get
    public Object getRes()
    {
        return "this page is where you send files";
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
        Representation rep = null;
        API.Status.StatusBuilder stats = new API.Status.StatusBuilder();
        if (entity != null) {
            if (MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(),
                    true)) {
                Map<String, String> map = getQuery().getValuesMap();
                if(!map.containsKey("filename") || !map.containsKey("patientid"))
                {
                    stats.append("status", "bad");
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
                    Patient p = Database.getPatient(Integer.valueOf(map.get("patientid")));
                    int doctor = !map.containsKey("doctorid") ? p.getDoctorId() : Integer.valueOf(map.get("doctorid"));
                    stats.append("fileid", String.valueOf(Database.makeRecord(doctor, p.getPatientId(), fileName)));
                    return stats.build().toString();
                } else {
                    stats.append("status", "bad");
                    return stats.build().toString();
                }
            }
        } else {
            stats.append("status", "bad");
            return stats.build().toString();
        }
        //should not reach here
        return null;
    }
}
