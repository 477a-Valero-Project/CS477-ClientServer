package Logic.Database;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.io.File;

/**
 * Created by Martin on 2/9/2015.
 */
public class ConfigurationManager {
    private static Configuration config;
    private static ServiceRegistry serviceRegistry;
    private static SessionFactory factory;

    public static void init()
    {
        config = new Configuration().configure(new File("src/main/java/Logic/Resources/hibernate.cfg.xml"))
                .addDirectory(new File("src/main/java/Logic/Resources/entities"));
        serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();
        factory = config.buildSessionFactory(serviceRegistry);
    }

    public static SessionFactory getSessionFactory()
    {
        if(factory == null)
        {
            init();
        }
        return factory;
    }
}
