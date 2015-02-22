package Logic.Database;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Vector;

/**
 * Created by Martin on 2/12/2015.
 * role = permission level
 * group = where user belongs
 * Basic idea is that the api will seperate people into different roles.
 */
public class User implements AuthenticationModule {
    private Role role;
    private String name;
    private String password;
    private String override;
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
        return "{\"id\":" + getId() + ",\"name\":\"" + getName() + "\",\"role\":\"" + getRole() + "\"" +
                ",\"groupId\":" + getGroupId() + "}";
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
}
