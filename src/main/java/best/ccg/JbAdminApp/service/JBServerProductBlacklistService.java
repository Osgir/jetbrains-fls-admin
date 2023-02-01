package best.ccg.JbAdminApp.service;

import best.ccg.JbAdminApp.entity.JBServerProductBlacklist;
import best.ccg.JbAdminApp.repository.JBServerProductBlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class JBServerProductBlacklistService {

    private JBServerProductBlacklistRepository jbServerProductBlacklistRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public void setJbServerProductBlacklistRepository(JBServerProductBlacklistRepository jbServerProductBlacklistRepository) {
        this.jbServerProductBlacklistRepository = jbServerProductBlacklistRepository;
    }

    public JBServerProductBlacklist save(JBServerProductBlacklist jbServerProductBlacklist) {
        return jbServerProductBlacklistRepository.saveAndFlush(jbServerProductBlacklist);
    }

    public JBServerProductBlacklist findByValueAddifNotExist(String value) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<JBServerProductBlacklist> cq = cb.createQuery(JBServerProductBlacklist.class);
        Root<JBServerProductBlacklist> root = cq.from(JBServerProductBlacklist.class);
        cq.select(root).where(cb.equal(root.get("valueP"), value));
        List<JBServerProductBlacklist> jbServerProductBlacklist = entityManager.createQuery(cq).getResultList();
        if (!jbServerProductBlacklist.isEmpty()) return jbServerProductBlacklist.get(0);

        return jbServerProductBlacklistRepository.saveAndFlush(new JBServerProductBlacklist(value, value));
    }

    public JBServerProductBlacklist getByID(Long jbServerProductBlacklist_id_) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<JBServerProductBlacklist> cq = cb.createQuery(JBServerProductBlacklist.class);
        Root<JBServerProductBlacklist> root = cq.from(JBServerProductBlacklist.class);
        cq.select(root).where(cb.equal(root.get("id"), jbServerProductBlacklist_id_));

        return entityManager.createQuery(cq).getSingleResult();
    }

    public HashMap<String, JBServerProductBlacklist> findAlltoHashmapValue() {
        HashMap<String, JBServerProductBlacklist> productBlacklistHashMap = new HashMap<>();
        jbServerProductBlacklistRepository.findAll().forEach(p -> {
            productBlacklistHashMap.put(p.getValueP(), p);
        });
        return productBlacklistHashMap;
    }

    public List<JBServerProductBlacklist> findIdValue() {
        return jbServerProductBlacklistRepository.findIdValue();
    }


    public List<JBServerProductBlacklist> findAll() {
        return jbServerProductBlacklistRepository.findAll();
    }


    public HashMap<String, JBServerProductBlacklist> getHashMapProductBlacklistBySetNames(Set<String> stringList) {

        List<JBServerProductBlacklist> items = getListProductBlacklistsbySetpValue(stringList);

        HashMap<String, JBServerProductBlacklist> hashMap = new HashMap<>();
        items.forEach(c -> hashMap.put(c.getValueP(), c));

        if (stringList.size() == items.size()) {
            return hashMap;
        }

        List<JBServerProductBlacklist> newValue = new ArrayList<>();

        for (String strComp : stringList) {
            if (!hashMap.containsKey(strComp)) newValue.add(new JBServerProductBlacklist(strComp, strComp));
        }
        List<JBServerProductBlacklist> newValue_ = jbServerProductBlacklistRepository.saveAllAndFlush(newValue);
        newValue_.forEach(c -> hashMap.put(c.getValueP(), c));

        return hashMap;
    }

    private List<JBServerProductBlacklist> getListProductBlacklistsbySetpValue(Set<String> stringList) {
        TypedQuery<JBServerProductBlacklist> query = entityManager.createQuery("SELECT item FROM JBServerProductBlacklist item WHERE item.valueP IN (?1)", JBServerProductBlacklist.class);
        query.setParameter(1, stringList);
        return query.getResultList();
    }

}
