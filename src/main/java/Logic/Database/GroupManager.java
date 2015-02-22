package Logic.Database;

/**
 * Created by Martin on 2/13/2015.
 */
public class GroupManager {

    public Group getGroupById(int id)
    {
        return Database.getGroupById(id);
    }

    public Group getGroupByName(String name)
    {
        return Database.getGroupByName(name);
    }


}
