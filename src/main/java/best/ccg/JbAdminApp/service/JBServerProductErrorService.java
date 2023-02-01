package best.ccg.JbAdminApp.service;

import best.ccg.JbAdminApp.entity.JBServerProductError;
import best.ccg.JbAdminApp.repository.JBServerProductErrorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class JBServerProductErrorService {

    @PersistenceContext
    private EntityManager entityManager;


    JBServerProductErrorRepository jbServerProductErrorRepository;

    @Autowired
    public void setJbServerProductErrorRepository(JBServerProductErrorRepository jbServerProductErrorRepository) {
        this.jbServerProductErrorRepository = jbServerProductErrorRepository;
    }

    public JBServerProductError saveAndFlush(JBServerProductError jbServerProductError) {
        return jbServerProductErrorRepository.saveAndFlush(jbServerProductError);
    }

    public HashMap<String, JBServerProductError> findAlltoHashmap() {
        HashMap<String, JBServerProductError> jbServerProductErrorHashMap = new HashMap<>();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<JBServerProductError> cq = cb.createQuery(JBServerProductError.class);
        Root<JBServerProductError> root = cq.from(JBServerProductError.class);
        root.fetch("jbServerProductBlacklist", JoinType.INNER);
        cq.select(root);
        List<JBServerProductError> jbServerProductError = entityManager.createQuery(cq).getResultList();

        jbServerProductError.forEach(p -> jbServerProductErrorHashMap.put(p.getValueP(), p));
        return jbServerProductErrorHashMap;

    }

    public void saveAll(Set<JBServerProductError> jbServerProductErrorList) {
        jbServerProductErrorRepository.saveAll(jbServerProductErrorList);
    }

    public List<JBServerProductError> findAll() {

        return jbServerProductErrorRepository.findAll();
    }
}
