package Logic.Database;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Vector;

/**
 * Created by Martin on 2/12/2015.
 * role = permission level
 * group = where user belongs
 * Basic idea is that the api will seperate people into different roles.
 */
public class User implements AuthenticationModule {
    private Role role;
    private String name, username, password, override, diagnosis, irb, consentLocation,
            consentInvestigator, study;
    private int groupId;
    private int id;
    private int owner;


    public User() {}

    public User(String nPassword) {
        password = nPassword;
    }

    @Enumerated(EnumType.STRING)
    public Role getRole() {
        return role;
    }

    public User setRole(Role role) {
        this.role = role;
        return this;
    }

    public boolean canModify(User other)
    {
        if(other.role == Role.ADMINISTRATOR || other.id == id)
        {
            return true;
        }
        else if((other.role == Role.ORGANIZATION_HEAD || other.role == Role.SUPER_DOCTOR) &&
                getGroupId() > 0 && other.getGroupId() == getGroupId())
        {
            return true;
        }
        else if(other.role != Role.PATIENT && owner == other.getId())
        {
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    /*
    This is assuming the setter has the permission to modify this record.
    Uses reflection to access fields.
 */
    public static boolean setField(User user, String field, String input)
    {
        Method[] methods = user.getClass().getMethods();
        String compare = "set" + field.toLowerCase();
        if(compare.contains("role") || compare.contains("id") || compare.contains("owner") || compare.contains("groupid"))
        {
            return false;
        }
        for(Method m : methods)
        {
            String name = m.getName().toLowerCase();
            if(name.equals(compare))
            {
                try {
                    m.invoke(user, input);
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

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getOverride() {
        return override;
    }

    public User setOverride(String override) {
        this.override = override;
        return this;
    }

    public int getGroupId() {
        return groupId;
    }

    public User setGroupId(int groupId) {
        this.groupId = groupId;
        return this;
    }

    public int getId() {
        return id;
    }

    public User setId(int id) {
        this.id = id;
        return this;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getJSON()
    {
        return this.toString();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getIrb() {
        return irb;
    }

    public void setIrb(String irb) {
        this.irb = irb;
    }

    public String getConsentLocation() {
        return consentLocation;
    }

    public void setConsentLocation(String consentLocation) {
        this.consentLocation = consentLocation;
    }

    public String getConsentInvestigator() {
        return consentInvestigator;
    }

    public void setConsentInvestigator(String consentInvestigator) {
        this.consentInvestigator = consentInvestigator;
    }

    public String getStudy() {
        return study;
    }

    public void setStudy(String study) {
        this.study = study;
    }

    //taken from: http://stackoverflow.com/questions/415953/how-can-i-generate-an-md5-hash
    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    //tests to see if able to add a user
    public static void main(String args[])
    {
        User u = new User("test");
        u.setRole(Role.BASIC_DOCTOR);
        Session session = ConfigurationManager.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(u);
        tx.commit();
        System.out.println(u.getJSON());
    }

    /**
     * Create a toString using reflection.
     * @return String JSON format
     */
    public String toString()
    {
        Field[] fields = this.getClass().getDeclaredFields();
        System.out.println(fields.length);
        boolean first = true;
        StringBuilder str = new StringBuilder();
        str.append("{");
        for(Field f : fields)
        {
            if(f.getName().contains("password") || f.getName().contains("override"))
            {
                continue;
            }
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
                else
                {
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
