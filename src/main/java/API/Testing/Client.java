package API.Testing;

import API.DummyAPIProvider;
import Logic.Database.User;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by Martin on 2/10/2015.
 * For testing application.
 */
public class Client {
    public static void main(String args[]) {
        int doctor = makeDoctor();
        System.out.println(doctor);
        int patient = makePatient(doctor);
        System.out.println(patient);
        int r = makeRecord(doctor, patient);
        System.out.println(r);
    }

    public static int makeRecord(int id1, int id2)
    {
        OAuthService service = new ServiceBuilder().provider(DummyAPIProvider.class)
                .apiKey("apples").apiSecret(User.MD5("test"))
                .signatureType(SignatureType.QueryString).build();
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://127.0.0.1:8182/action/");
        service.signRequest(OAuthConstants.EMPTY_TOKEN, request);
        request.addQuerystringParameter("patientid", String.valueOf(id2));
        request.addQuerystringParameter("filename", "123.txt");
        request.addQuerystringParameter("ID", String.valueOf(id1));
        MultipartEntityBuilder build = MultipartEntityBuilder.create();
        build.addBinaryBody("fileToUpload", new File("J:/temp/file.txt"));
        HttpEntity entity = build.build();
        ByteArrayOutputStream out = new ByteArrayOutputStream((int)entity.getContentLength());
        try {
            entity.writeTo(out);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        request.addPayload(out.toByteArray());
        Header contentType = entity.getContentType();
        request.addHeader(contentType.getName(), contentType.getValue());
        Response response = request.send();
        System.out.println(response.getBody());
        try {
            return new JSONObject(response.getBody()).getInt("fileid");
        }
        catch(Exception e)
        {
            return -1;
        }
    }

    public static int makePatient(int id)
    {
        OAuthService service = new ServiceBuilder().provider(DummyAPIProvider.class)
                .apiKey("apples").apiSecret(User.MD5("test"))
                .signatureType(SignatureType.QueryString).build();
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://127.0.0.1:8182/action/");
        service.signRequest(OAuthConstants.EMPTY_TOKEN, request);
        request.addQuerystringParameter("action", "makePatient");
        request.addQuerystringParameter("ID", String.valueOf(id));
        Response response = request.send();
        System.out.println(response.getBody());
        try {
            return new JSONObject(response.getBody()).getInt("patientId");
        }
        catch(Exception e)
        {
            return -1;
        }
    }

    public static int makeDoctor()
    {
        OAuthService service = new ServiceBuilder().provider(DummyAPIProvider.class)
                .apiKey("makingDoctor").apiSecret("thisdoesnotmatterreallyithinkhopefullyalwayscanchangeitlater")
                .signatureType(SignatureType.QueryString).build();
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://127.0.0.1:8182/doctor/");
        service.signRequest(OAuthConstants.EMPTY_TOKEN, request);
        request.addQuerystringParameter("password", "test");
        request.addQuerystringParameter("ID", "null");
        Response response = request.send();
        System.out.println(response.getBody());
        try {
            return new JSONObject(response.getBody()).getInt("id");
        }
        catch(Exception e)
        {
            return -1;
        }
    }
}
