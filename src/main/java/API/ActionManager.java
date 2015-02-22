package API;

import Logic.Database.Database;
import Logic.Database.User;

/**
 * Created by Martin on 2/21/2015.
 */
public class ActionManager {
    public static String list(User user)
    {
        StringBuilder ret = new StringBuilder();
        switch (user.getRole())
        {
            case ADMINISTRATOR:
            case ORGANIZATION_HEAD:
                addAction(ret, "listGroup");
                addAction(ret, "modifyDoctor");
            case SUPER_DOCTOR:
                addAction(ret, "listGroupPatients");
            case BASIC_DOCTOR:
                addAction(ret, "addPatient");
                addAction(ret, "deletePatient");
                addAction(ret, "modifyPatient");
                addAction(ret, "removeRecord");
                addAction(ret, "listPatients");
            case PATIENT:
                addAction(ret, "addRecord");
                addAction(ret, "getRecordRaw");
                addAction(ret, "getRecordProcessed");
                addAction(ret, "modifySelf");
        }
        ret.append("]");
        return ret.toString();
    }

    private static void addAction(StringBuilder ret, String action)
    {
        if(ret.length() > 1)
        {
            ret.append(",");
        }
        ret.append(action);
    }
}
