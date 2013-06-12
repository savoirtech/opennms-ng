package org.opennms.ng.persistence.dao.jpa;

import org.opennms.ng.persistence.dao.VersionDao;

import javax.persistence.Query;
import java.util.List;

public class VersionJpaDao extends GenericJpaDao<String,String> implements VersionDao {

    public VersionJpaDao() {
        super(String.class);
    }

    public String getVersionNumber(){
        Query q = em.createQuery("select v.version from Version v");
        List results = q.getResultList();
        if (results.size() > 0)
            return (String)results.get(0);

        return null;
    }
}
