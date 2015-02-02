package Logic.Database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Martin on 2/1/2015.
 */
public class Record {
    private int recordId;
    private int patientId;
    private int doctorId;
    private String filePathRaw;
    private String filePathProcess;

    public Record(){}

    public Record(int nRecordId, int nPatientId, int nDoctorId, String nRawPath, String nFilePathProcess)
    {
        recordId = nRecordId;
        patientId = nPatientId;
        doctorId = nDoctorId;
        filePathRaw = nRawPath;
        filePathProcess = nFilePathProcess;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getRecordId() {

        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getFilePathProcess() {
        return filePathProcess;
    }

    public void setFilePathProcess(String filePathProcess) {
        this.filePathProcess = filePathProcess;
    }

    public String getFilePathRaw() {
        return filePathRaw;
    }

    public void setFilePathRaw(String filePathRaw) {
        this.filePathRaw = filePathRaw;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String toString()
    {
        return "record id: " + recordId + " patient id: " + patientId + " doctor id: " + doctorId
                + " path1 " + filePathRaw + " path2 " + filePathProcess;
    }

    public static void main(String args[])
    {
        //Record r = new Record(-1, 1, 0, "hello", "world");
        Record r = null;
        Configuration cfg = new Configuration().configure(new File("src/main/java/Logic/Resources/hibernate.cfg.xml"))
                .addDirectory(new File("src/main/java/Logic/Resources/entities"));
        SessionFactory factory = cfg.buildSessionFactory();
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        //session.save(r);
        List records = session.createQuery("FROM Record").list();
        for(Iterator iterator = records.iterator(); iterator.hasNext();)
        {
            r = (Record)iterator.next();
            System.out.println(r);
        }
        tx.commit();


        session.close();

    }
}
