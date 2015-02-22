package Logic.Database;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Created by Largo Usagi on 10/31/2014.
 * © Usagisoft LLC 2014
 */
public class LiquibaseManager {

    private String liquibseDirectory;

    public LiquibaseManager() {
        this.liquibseDirectory = "liquibase\\db.master.xml";
    }

    public void update(Connection connection) {

        try {
            Liquibase liquibase = getLiquibase(connection);

            liquibase.update("");

        } catch (LiquibaseException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.close();
                } catch (SQLException e) {
                    //nothing to do
                }
            }
        }

    }

    public Connection getConnection() {

        Connection connection = null;

        try {

            Properties connectionProperties = new Properties();
            connectionProperties.setProperty("user", "root");
            connectionProperties.setProperty("password", "root");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/dev", connectionProperties);
        } catch (SQLException e) {

            e.printStackTrace();

        }

        return connection;

    }


    public void purge(Connection connection) {

        try {
            Liquibase liquibase = getLiquibase(connection);

            liquibase.dropAll();

        } catch (LiquibaseException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.close();
                } catch (SQLException e) {
                    //nothing to do
                }
            }
        }

    }

    private Liquibase getLiquibase(Connection connection) throws LiquibaseException {
        Liquibase liquibase;
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        liquibase = new Liquibase(this.liquibseDirectory, new ClassLoaderResourceAccessor(), database);
        return liquibase;
    }

}
