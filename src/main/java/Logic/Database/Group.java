package Logic.Database;

/**
 * Created by Martin on 2/13/2015.
 */
public class Group {
    private int id;
    private String name;
    private String joinCode;

    public Group() {};
    public Group(String nName, String nJoinCode)
    {
        name = nName;
        joinCode = nJoinCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJoinCode() {
        return joinCode;
    }

    public void setJoinCode(String joinCode) {
        this.joinCode = joinCode;
    }
}
