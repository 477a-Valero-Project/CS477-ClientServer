package Logic.Database;

import liquibase.resource.ClassLoaderResourceAccessor;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.io.File;

/**
 * Created by Martin on 2/9/2015.
 */
public class ConfigurationManager {
    private Configuration config;
    private ServiceRegistry serviceRegistry;
    private SessionFactory factory;
    private static ConfigurationManager configer;

    public static void init()
    {
        if(configer != null)
        {
            return;
        }
        configer = new ConfigurationManager();
        configer.config = new Configuration().configure(configer.getClass().getClassLoader().getResource("hibernate.cfg.xml"))
                .addInputStream(configer.getClass().getClassLoader().getResourceAsStream("entities/Group.hbm.xml"))
                .addInputStream(configer.getClass().getClassLoader().getResourceAsStream("entities/OAuthCache.hbm.xml"))
                .addInputStream(configer.getClass().getClassLoader().getResourceAsStream("entities/Record.hbm.xml"))
                .addInputStream(configer.getClass().getClassLoader().getResourceAsStream("entities/User.hbm.xml"));
        configer.serviceRegistry = new ServiceRegistryBuilder().applySettings(configer.config.getProperties()).buildServiceRegistry();
        configer.factory = configer.config.buildSessionFactory(configer.serviceRegistry);
    }

    public static SessionFactory getSessionFactory()
    {
        if(configer == null)
        {
            init();
        }
        return configer.factory;
    }
}
