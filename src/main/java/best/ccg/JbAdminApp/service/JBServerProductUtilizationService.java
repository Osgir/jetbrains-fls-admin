package best.ccg.JbAdminApp.service;

import best.ccg.JbAdminApp.entity.JBServerProductBlacklist;
import best.ccg.JbAdminApp.entity.JBServerProductUtilization;
import best.ccg.JbAdminApp.repository.JBServerProductUtilizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class JBServerProductUtilizationService {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(JBServerProductUtilizationService.class);


    @Autowired
    JBServerProductUtilizationRepository jbServerProductUtilizationRepository;
    @Autowired
    JBServerProductBlacklistService jbServerProductBlacklistService;

    public JBServerProductUtilization saveAndFlush(JBServerProductUtilization jbServerProductUtilization) {
        return jbServerProductUtilizationRepository.saveAndFlush(jbServerProductUtilization);
    }

    public List<JBServerProductUtilization> findAll() {
        return jbServerProductUtilizationRepository.findAll();
    }

    public HashMap<String, JBServerProductUtilization> findAlltoHashmap() {
        HashMap<String, JBServerProductUtilization> jbServerProductErrorHashMap = new HashMap<>();
        jbServerProductUtilizationRepository.findAll().forEach(p -> {
            jbServerProductErrorHashMap.put(p.getLicense() + p.getVersion() + p.getJbServerProductBlacklist().getId(), p);
        });
        return jbServerProductErrorHashMap;
    }

    public void saveAll(Set<JBServerProductUtilization> jbServerProductUtilizationListNew) {
        jbServerProductUtilizationRepository.saveAll(jbServerProductUtilizationListNew);
    }

    public List<JBServerProductUtilization> saveAllAndFlush(List<JBServerProductUtilization> jbServerProductUtilizationListNew) {
        return jbServerProductUtilizationRepository.saveAllAndFlush(jbServerProductUtilizationListNew);
    }


    public List<String> getAllNames() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);
        Root<JBServerProductUtilization> root = cq.from(JBServerProductUtilization.class);
        cq.where(cb.equal(root.get("isActual"), true));
        cq.select(root.get("license")).orderBy(cb.desc(root.get("license"))).groupBy(root.get("license"));

        return entityManager.createQuery(cq).getResultList();
    }

    public JBServerProductUtilization getJbServerProductUtilization(JBServerProductBlacklist productBlacklist, String version, String license, String productstring) {

        List<JBServerProductUtilization> prodUtil = entityManager.createQuery("SELECT prodUtil from JBServerProductUtilization prodUtil where prodUtil.license = ?1 and prodUtil.version = ?2 and prodUtil.jbServerProductBlacklist = ?3").setParameter(1, license).setParameter(2, version).setParameter(3, productBlacklist).getResultList();
        if (!prodUtil.isEmpty()) {
            return prodUtil.get(0);

        }
        return jbServerProductUtilizationRepository.save(new JBServerProductUtilization(license, productBlacklist, version, productstring));

    }

    public HashMap<String, JBServerProductUtilization> getByListNames(Set<String> listProductUtilizationNames, HashMap<String, JBServerProductBlacklist> productBlack) {
        HashMap<String, JBServerProductUtilization> prodUtilMap = new HashMap<>();
        List<JBServerProductUtilization> prodUtilnew = new ArrayList<>();
        for (String strValue : listProductUtilizationNames) {

            if (productBlack.containsKey(getBLname(strValue))) {
                JBServerProductBlacklist productBL = productBlack.get(getBLname(strValue));

                String license = getLicensefromStr(strValue);
                String productstring = getProductstringfromStr(strValue);
                String version = getVersionfromStr(strValue);

                TypedQuery<JBServerProductUtilization> query = entityManager.createQuery("SELECT item FROM JBServerProductUtilization item WHERE " + "item.license = (?1) " + "and item.productstring = (?2) " + "and item.version = (?3) " + "and item.jbServerProductBlacklist = (?4) ", JBServerProductUtilization.class);
                query.setParameter(1, license);
                query.setParameter(2, productstring);
                query.setParameter(3, version);
                query.setParameter(4, productBL);
                List<JBServerProductUtilization> item = query.getResultList();
                if (item.size() > 0)
                    prodUtilMap.put(item.get(0).getLicense() + ":" + item.get(0).getVersion(), item.get(0));
                else prodUtilnew.add(new JBServerProductUtilization(license, productBL, version, productstring));


            } else {
                logger.error("no productBL found " + getBLname(strValue) + "; " + strValue);
            }
        }

        List<JBServerProductUtilization> newValue_ = saveAllAndFlush(prodUtilnew);
        newValue_.stream().forEach(c -> prodUtilMap.put(c.getLicense() + c.getVersion(), c));
        return prodUtilMap;

    }

    private String getVersionfromStr(String strValue) {
        String[] data = strValue.split(";");
        String[] data2 = data[1].split(":");
        return data2[1];
    }

    private String getProductstringfromStr(String strValue) {
        String[] data = strValue.split(";");
        return data[1];
    }

    private String getLicensefromStr(String strValue) {
        String[] data = strValue.split(";");
        return data[0];
    }

    static String getBLname(String strValue) {
        String[] data = strValue.split(";");
        String[] data2 = data[1].split(":");

        return data2[0].toUpperCase(Locale.ROOT);
    }
}
