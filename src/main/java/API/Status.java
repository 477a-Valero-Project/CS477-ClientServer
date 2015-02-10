package API;

/**
 * Created by Martin on 2/10/2015.
 */
public class Status {
    private String status;

    private Status(String nStat) {status = nStat;}

    public String toString()
    {
        return status;
    }

    public static class StatusBuilder
    {
        private StringBuilder build;
        public StatusBuilder()
        {
            build = new StringBuilder();
            build.append("{");
        }

        public StatusBuilder append(String name, String key)
        {
            if(build.length() > 1)
            {
                build.append(",");
            }
            build.append("\"");
            build.append(name);
            build.append("\":\"");
            build.append(key);
            build.append("\"");
            return this;
        }

        public Status build()
        {
            build.append("}");
            Status ret = new Status(build.toString());
            build = null;
            return ret;
        }
    }
}
