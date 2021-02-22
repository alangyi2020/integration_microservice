package com.dxc.hibernate;

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

/**
 * Home object for domain model class RequestParameters.
 * @see com.dxc.hibernate.RequestParameters
 * @author Hibernate Tools
 */
public class RequestParametersHome {

    private static final Log log = LogFactory.getLog(RequestParametersHome.class);

    private final SessionFactory sessionFactory = getSessionFactory();

    protected SessionFactory getSessionFactory() {
        try {
            return (SessionFactory) new InitialContext().lookup("SessionFactory");
        } catch (Exception e) {
            log.error("Could not locate SessionFactory in JNDI", e);
            throw new IllegalStateException("Could not locate SessionFactory in JNDI");
        }
    }

    public void persist(RequestParameters transientInstance) {
        log.debug("persisting RequestParameters instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void attachDirty(RequestParameters instance) {
        log.debug("attaching dirty RequestParameters instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(RequestParameters instance) {
        log.debug("attaching clean RequestParameters instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(RequestParameters persistentInstance) {
        log.debug("deleting RequestParameters instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public RequestParameters merge(RequestParameters detachedInstance) {
        log.debug("merging RequestParameters instance");
        try {
            RequestParameters result = (RequestParameters) sessionFactory.getCurrentSession().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public RequestParameters findById(com.dxc.hibernate.RequestParametersId id) {
        log.debug("getting RequestParameters instance with id: " + id);
        try {
            RequestParameters instance = (RequestParameters) sessionFactory.getCurrentSession()
                    .get("com.dxc.hibernate.RequestParameters", id);
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

    public List findByExample(RequestParameters instance) {
        log.debug("finding RequestParameters instance by example");
        try {
            List results = sessionFactory.getCurrentSession()
                    .createCriteria("com.dxc.hibernate.RequestParameters").add(Example.create(instance))
                    .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }
}

