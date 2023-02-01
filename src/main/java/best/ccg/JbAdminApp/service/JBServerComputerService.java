package best.ccg.JbAdminApp.service;

import best.ccg.JbAdminApp.entity.JBServerComputer;
import best.ccg.JbAdminApp.entity.JBServerProductBlacklist;
import best.ccg.JbAdminApp.repository.JBServerComputerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JBServerComputerService {

    private static final Logger logger
            = LoggerFactory.getLogger(JBServerComputerService.class);

    private JBServerComputerRepository jbServerComputerRepository;
    JBServerLogErrorService jbServerLogErrorService;
    JBServerLogUtilizationService jbServerLogUtilizationService;
    JBServerProductBlacklistService jbServerProductBlacklistService;
    JBServerBlacklistComputerService jbServerBlacklistComputerService;
    JBServerEmployeeService jbServerEmployeeService;
    IdentifiedComputersService identifiedComputersService;

    @Autowired
    public void setIdentifiedComputersService(IdentifiedComputersService identifiedComputersService) {
        this.identifiedComputersService = identifiedComputersService;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public void setJbServerEmployeeService(JBServerEmployeeService jbServerEmployeeService) {
        this.jbServerEmployeeService = jbServerEmployeeService;
    }

    @Autowired
    public void setJbServerBlacklistComputerService(JBServerBlacklistComputerService jbServerBlacklistComputerService) {
        this.jbServerBlacklistComputerService = jbServerBlacklistComputerService;
    }

    @Autowired
    public void setJbServerLogErrorService(JBServerLogErrorService jbServerLogErrorService) {
        this.jbServerLogErrorService = jbServerLogErrorService;
    }

    @Autowired
    public void setJbServerLogUtilizationService(JBServerLogUtilizationService jbServerLogUtilizationService) {
        this.jbServerLogUtilizationService = jbServerLogUtilizationService;
    }

    @Autowired
    public void setJbServerProductBlacklistService(JBServerProductBlacklistService jbServerProductBlacklistService) {
        this.jbServerProductBlacklistService = jbServerProductBlacklistService;
    }

    @Autowired
    public void setComputerJBServerRepository(JBServerComputerRepository JBServerComputerRepository) {
        this.jbServerComputerRepository = JBServerComputerRepository;
    }

    public Map<String, JBServerComputer> findAlltoHashmap() {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<JBServerComputer> cq = cb.createQuery(JBServerComputer.class);
        Root<JBServerComputer> root = cq.from(JBServerComputer.class);
        root.fetch("jbServerBlacklistProductSet", JoinType.LEFT);
        root.fetch("uniqErrorProduct", JoinType.LEFT);
        root.fetch("uniqUtilProduct", JoinType.LEFT);
        List<JBServerComputer> computers1 = entityManager.createQuery(cq).getResultList();
        Set<JBServerComputer> foo = new LinkedHashSet<>(computers1);
        List<JBServerComputer> computers = new LinkedList<>(foo);

        return computers
                .stream()
                .collect(Collectors.toMap(JBServerComputer::getName, tuple -> tuple));

    }

    public void saveAll(Set<JBServerComputer> jbServerComputerListNew) {
        logger.debug("start comp1 save");
        try {
            jbServerComputerRepository.saveAllAndFlush(jbServerComputerListNew);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        logger.debug("finish comp1 save");
    }

    public Page<JBServerComputer> findAll3(long empid, Pageable paging) {
        return jbServerEmployeeService.findAllComputersByEmployee_Id(empid, paging);
    }

    public JBServerComputer getByID(Long computerid) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<JBServerComputer> cq = cb.createQuery(JBServerComputer.class);
        Root<JBServerComputer> root = cq.from(JBServerComputer.class);
        cq.select(root).where(cb.equal(root.get("id"), computerid));
        return entityManager.createQuery(cq)
                .getSingleResult();
    }

    public JBServerComputer getComputerbyHostname(String hostName) {

        List<JBServerComputer> comps = jbServerComputerRepository.findByName(hostName);
        if (!comps.isEmpty()) {
            return comps.get(0);
        } else {
            try {
                return jbServerComputerRepository.save(new JBServerComputer(hostName));
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return null;
    }

    public HashMap<String, JBServerComputer> getByListNames(Set<String> stringList) {

        Query query = entityManager.createQuery("SELECT item FROM JBServerComputer item WHERE item.name IN (?1)");
        query.setParameter(1, stringList);
        List<JBServerComputer> items = query.getResultList();

        HashMap<String, JBServerComputer> hashMap = new HashMap<>();
        items.forEach(c -> hashMap.put(c.getName(), c));

        if (stringList.size() == items.size()) {
            return hashMap;
        }

        List<JBServerComputer> newComp = new ArrayList<>();
        for (String strComp : stringList) {
            if (!hashMap.containsKey(strComp))
                newComp.add(new JBServerComputer(strComp));
        }

        List<JBServerComputer> newComp_ = jbServerComputerRepository.saveAllAndFlush(newComp);
        newComp_.forEach(c -> hashMap.put(c.getName(), c));

        return hashMap;
    }

    public JBServerComputer updateComputerProduct(Long jbServerComputerId, JBServerProductBlacklist bl) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<JBServerComputer> cq = cb.createQuery(JBServerComputer.class);
        Root<JBServerComputer> root = cq.from(JBServerComputer.class);
        root.fetch("uniqUtilProduct", JoinType.LEFT);
        cq.where(cb.equal(root.get("id"), jbServerComputerId));
        List<JBServerComputer> computers1 = entityManager.createQuery(cq).getResultList();

        computers1.get(0).getUniqUtilProduct().add(bl);
        return computers1.get(0);
    }
}
