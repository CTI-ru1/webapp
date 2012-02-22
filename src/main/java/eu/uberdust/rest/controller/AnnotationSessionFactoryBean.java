package eu.uberdust.rest.controller;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.springframework.context.ApplicationContextException;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

import java.util.ArrayList;
import java.util.List;

public class AnnotationSessionFactoryBean extends LocalSessionFactoryBean {
    AnnotationSessionFactoryBean() {
        super();
        LOG.info("constructor");
        annotatedClasses_ = new ArrayList<String>();

    }

    private static final Logger LOG = Logger.getLogger(AnnotationSessionFactoryBean.class);

    private List<String> annotatedClasses_;

    /**
     * @return the classes.
     */
    public List getAnnotatedClasses() {
        return annotatedClasses_;
    }

    /**
     * @param classes The classes to set.
     */
    public void setAnnotatedClasses(List<String> classes) {
        LOG.info("setting the annotated classes");
        for (String oneclass : classes) {
            LOG.info(oneclass);
        }
        annotatedClasses_ = classes;
    }

    @Override
    protected void postProcessConfiguration(Configuration config) throws HibernateException {
        super.postProcessConfiguration(config);

        if (!(config instanceof AnnotationConfiguration)) {
            throw new ApplicationContextException("The configuration must be AnnotationConfiguration.");
        }

        if (annotatedClasses_ == null) {
            LOG.info("No annotated classes to register with Hibernate.");
            return;
        }

        for (String className : annotatedClasses_) {
            try {
                Class clazz = config.getClass().getClassLoader().loadClass(className);
                ((AnnotationConfiguration) config).addAnnotatedClass(clazz);

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Class " + className + " added to Hibernate config.");
                }
            } catch (MappingException e) {
                throw new ApplicationContextException("Unable to register class " + className, e);
            } catch (ClassNotFoundException e) {
                throw new ApplicationContextException("Unable to register class " + className, e);
            }
        }
    }

    @Override
    protected SessionFactory buildSessionFactory() throws Exception {
        
        SessionFactory z = null;

        try {
            z = super.buildSessionFactory();
            LOG.info("mapped entities" + z.getStatistics().getEntityNames().length);
        } catch (Exception e) {
            LOG.fatal(e);
            e.printStackTrace();
        }
        return z;    //To change body of overridden methods use File | Settings | File Templates.
    }
}
