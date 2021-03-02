package com.dxc.hibernate;

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

/**
 * Home object for domain model class ServiceProvider.
 * @see com.dxc.hibernate.ServiceProvider
 * @author Hibernate Tools
 */
public class ServiceProviderHome {

    private static final Log log = LogFactory.getLog(ServiceProviderHome.class);

    private final SessionFactory sessionFactory = getSessionFactory();

    protected SessionFactory getSessionFactory() {
        try {
            return (SessionFactory) new InitialContext().lookup("SessionFactory");
        } catch (Exception e) {
            log.error("Could not locate SessionFactory in JNDI", e);
            throw new IllegalStateException("Could not locate SessionFactory in JNDI");
        }
    }

    public void persist(ServiceProvider transientInstance) {
        log.debug("persisting ServiceProvider instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void attachDirty(ServiceProvider instance) {
        log.debug("attaching dirty ServiceProvider instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(ServiceProvider instance) {
        log.debug("attaching clean ServiceProvider instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(ServiceProvider persistentInstance) {
        log.debug("deleting ServiceProvider instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public ServiceProvider merge(ServiceProvider detachedInstance) {
        log.debug("merging ServiceProvider instance");
        try {
            ServiceProvider result = (ServiceProvider) sessionFactory.getCurrentSession().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public ServiceProvider findById(int id) {
        log.debug("getting ServiceProvider instance with id: " + id);
        try {
            ServiceProvider instance = (ServiceProvider) sessionFactory.getCurrentSession()
                    .get("com.dxc.hibernate.ServiceProvider", id);
            if (instance == null) {
                log.debug("get successful, no instance found");
            } else {
                log.debug("get successful, instance found");
            }
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List<?> findByExample(ServiceProvider instance) {
        log.debug("finding ServiceProvider instance by example");
        try {
            List<?> results = sessionFactory.getCurrentSession()
                    .createCriteria("com.dxc.hibernate.ServiceProvider").add(Example.create(instance))
                    .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }
}

