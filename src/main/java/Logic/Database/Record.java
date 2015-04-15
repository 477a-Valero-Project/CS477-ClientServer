package Logic.Database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
    private String filePathProcess, date, time, location, subjectType, conditionOfTrial, investigator,
            softwareVersion, calibrationFileName, samplingRate, filterSettings, roomTemperature, limb, side, limbState,
            comments, extra1, extra2, extra3, notes1, notes2, notes3;

    public static void main(String args[])
    {
        Record r = new Record();
        System.out.println(r.getDate());
        setField(r, "date", "test");
        System.out.println(r.getDate());
        System.out.println(r.toString());
    }
    public Record(){}

    public Record(int nPatientId, int nDoctorId, String nRawPath, String nFilePathProcess)
    {
        patientId = nPatientId;
        doctorId = nDoctorId;
        filePathRaw = nRawPath;
        filePathProcess = nFilePathProcess;
    }

    /*
        This is assuming the setter has the permission to modify this record.
        Uses reflection to access fields.
     */
    public static boolean setField(Record record, String field, String input)
    {
        Method[] methods = record.getClass().getMethods();
        String compare = "set" + field.toLowerCase();
        if(compare.contains("recordid") || compare.contains("patientid") || compare.contains("doctorid"))
        {
            return false;
        }
        for(Method m : methods)
        {
            String name = m.getName().toLowerCase();
            if(name.equals(compare))
            {
                try {
                    m.invoke(record, input);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getConditionOfTrial() {
        return conditionOfTrial;
    }

    public void setConditionOfTrial(String conditionOfTrial) {
        this.conditionOfTrial = conditionOfTrial;
    }

    public String getInvestigator() {
        return investigator;
    }

    public void setInvestigator(String investigator) {
        this.investigator = investigator;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getCalibrationFileName() {
        return calibrationFileName;
    }

    public void setCalibrationFileName(String calibrationFileName) {
        this.calibrationFileName = calibrationFileName;
    }

    public String getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(String samplingRate) {
        this.samplingRate = samplingRate;
    }

    public String getFilterSettings() {
        return filterSettings;
    }

    public void setFilterSettings(String filterSettings) {
        this.filterSettings = filterSettings;
    }

    public String getRoomTemperature() {
        return roomTemperature;
    }

    public void setRoomTemperature(String roomTemperature) {
        this.roomTemperature = roomTemperature;
    }

    public String getLimb() {
        return limb;
    }

    public void setLimb(String limb) {
        this.limb = limb;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getLimbState() {
        return limbState;
    }

    public void setLimbState(String limbState) {
        this.limbState = limbState;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getExtra1() {
        return extra1;
    }

    public void setExtra1(String extra1) {
        this.extra1 = extra1;
    }

    public String getExtra2() {
        return extra2;
    }

    public void setExtra2(String extra2) {
        this.extra2 = extra2;
    }

    public String getExtra3() {
        return extra3;
    }

    public void setExtra3(String extra3) {
        this.extra3 = extra3;
    }

    public String getNotes1() {
        return notes1;
    }

    public void setNotes1(String notes1) {
        this.notes1 = notes1;
    }

    public String getNotes2() {
        return notes2;
    }

    public void setNotes2(String notes2) {
        this.notes2 = notes2;
    }

    public String getNotes3() {
        return notes3;
    }

    public void setNotes3(String notes3) {
        this.notes3 = notes3;
    }

    /**
     * Create a toString using reflection.
     * @return String JSON format
     */
    public String toString()
    {
        Field[] fields = this.getClass().getDeclaredFields();
        boolean first = true;
        StringBuilder str = new StringBuilder();
        str.append("{");
        for(Field f : fields)
        {
            if(!first)
            {
                str.append(",");
            }
            else
            {
                first = false;
            }
            str.append("\"");
            str.append(f.getName());
            str.append("\":");
            try {
                if (f.getType().equals("".getClass())) {
                    str.append("\"");
                    str.append(f.get(this));
                    str.append("\"");
                }
                else {
                    str.append(f.get(this));
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        str.append("}");
        return str.toString();
    }
}
