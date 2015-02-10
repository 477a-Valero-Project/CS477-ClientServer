package Logic.Database;

/**
 * Created by Martin on 2/9/2015.
 */
public class Patient implements AuthenticationModule {
    private String key;
    private int patientId;
    private String password;

    public Patient() {}
    public Patient(String nPassword)
    {
        password = nPassword;
    }

    public String getId()
    {
        return "patient:" + patientId;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String toString()
    {
        return "Patient: " + patientId + " password: " + password;
    }
}
