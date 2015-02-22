package Logic.Database;

import java.util.Vector;

/**
 * Created by Martin on 2/9/2015.
 */
@Deprecated
public class Doctor implements AuthenticationModule {
    private String key;
    private int doctorId;
    private String password;

    public int getId()
    {
        return doctorId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Doctor(){}
    public Doctor(String nKey, int nDoctorId, String nPassword)
    {
        key = nKey;
        doctorId = nDoctorId;
        password = nPassword;
    }
    public Doctor(String nPassword)
    {
        password = nPassword;
    }

    public String toString()
    {
        return "Doctor: " + doctorId + " password: " + password;
    }

}
