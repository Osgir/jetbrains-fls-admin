package best.ccg.JbAdminApp.service;

import best.ccg.JbAdminApp.entity.*;
import best.ccg.JbAdminApp.repository.JBServerEmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class JBServerEmployeeService {


    private static final Logger logger = LoggerFactory.getLogger(JBServerEmployeeService.class);
    JBServerEmployeeRepository jbServerEmployeeRepository;
    JBServerBlacklistEmployeeService jbServerBlacklistEmployeeService;
    JBServerLogErrorService jbServerLogErrorService;
    JBServerLogUtilizationService jbServerLogUtilizationService;
    JBServerProductBlacklistService jbServerProductBlacklistService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public void setJbServerProductBlacklistService(JBServerProductBlacklistService jbServerProductBlacklistService) {
        this.jbServerProductBlacklistService = jbServerProductBlacklistService;
    }

    @Autowired
    public void setJbServerBlacklistEmployeeService(JBServerBlacklistEmployeeService jbServerBlacklistEmployeeService) {
        this.jbServerBlacklistEmployeeService = jbServerBlacklistEmployeeService;
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
    public void setJbServerEmployeeRepository(JBServerEmployeeRepository jbServerEmployeeRepository) {
        this.jbServerEmployeeRepository = jbServerEmployeeRepository;
    }

    public JBServerEmployee getByID(long employee_id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<JBServerEmployee> cq = cb.createQuery(JBServerEmployee.class);
        Root<JBServerEmployee> root = cq.from(JBServerEmployee.class);
        cq.select(root).where(cb.equal(root.get("id"), employee_id));
        return entityManager.createQuery(cq).getSingleResult();
    }

    public Map<String, JBServerEmployee> findAlltoHashmapEntity() {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<JBServerEmployee> cq = cb.createQuery(JBServerEmployee.class);
        Root<JBServerEmployee> root = cq.from(JBServerEmployee.class);
        root.fetch("jbServerBlacklistProductSet", JoinType.LEFT);
        root.fetch("uniqErrorProduct", JoinType.LEFT);
        root.fetch("uniqUtilProduct", JoinType.LEFT);
        root.fetch("jbServerComputers", JoinType.LEFT);
//        cq.multiselect(root.get("name"), root);
        List<JBServerEmployee> employees1 = entityManager.createQuery(cq).getResultList();
        Set<JBServerEmployee> foo = new LinkedHashSet<>(employees1);
        List<JBServerEmployee> employees = new LinkedList<>(foo);

        return employees
                .stream()
                .collect(Collectors.toMap(JBServerEmployee::getName, tuple -> tuple));
    }

    public void saveAll(Set<JBServerEmployee> jbServerEmployeeListNew) {
        logger.debug("start jbServerEmployeeListNew save");

        try {
            jbServerEmployeeRepository.saveAll(jbServerEmployeeListNew);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        logger.debug("finish jbServerEmployeeListNew save");
    }

    @Deprecated
    public Page<TransientlistJBServerEmployee> findAll2(String name, Pageable paging, int sort) {

        if (name == null) name = "";
        List<TransientlistJBServerEmployee> curPageNewEmployee = new ArrayList<>();
//        List<TransientEmployeeIdName> employees = findAllIdNameByNameContains(name);
        List<JBServerEmployee> employees = jbServerEmployeeRepository.findAllByNameContains(name);
        if (!CollectionUtils.isEmpty(employees)) {

            List<TransientlistJBServerEmployee> newEmployees = new ArrayList<>();
            employees.forEach(r -> {
                TransientlistJBServerEmployee c = new TransientlistJBServerEmployee(r.getId(), r.getName(), r.getLastseenutildate(), r.getLastseenerrordate());
                newEmployees.add(c);
            });

//            Map<Long, LocalDateTime> jbServerLogUtilizationList = jbServerLogUtilizationService.getMaxDatetimeGroupByEmployeeId();
//            if (!CollectionUtils.isEmpty(jbServerLogUtilizationList)) {
//                newEmployees.forEach(employee1 -> {
//                    employee1.setDateLastUtil(jbServerLogUtilizationList.get(employee1.getId()));
//                });
//            }
//
//            Map<Long, LocalDateTime> jbServerLogErrorList = jbServerLogErrorService.getMaxDatetimeGroupByEmployeeId();
//            if (!CollectionUtils.isEmpty(jbServerLogErrorList)) {
//                newEmployees.forEach(employee1 -> {
//                    employee1.setDateLastError(jbServerLogErrorList.get(employee1.getId()));
//                });
//            }
            if (sort == 1) newEmployees.sort(new TransientlistJBServerEmployee.ComparatorDateLastError());
            else newEmployees.sort(new TransientlistJBServerEmployee.ComparatorDateLastUtil());

            int start = (int) paging.getOffset();
            int finish = (int) paging.getOffset() + paging.getPageSize();
            if (employees.size() < finish) {
                finish = employees.size();
            }
            curPageNewEmployee = newEmployees.subList(start, finish);

            List<Long> ids = curPageNewEmployee.stream().map(TransientlistJBServerEmployee::getId).collect(Collectors.toList());
            List<TransientJbServerEmployeeGroupProduct> reportJbServerEmployeeGroupProducts
                    = jbServerLogUtilizationService.getGroupByEmployeeProduct(ids);

            Map<Long, Set<JBServerProductBlacklist>> logUtilizationMap = new HashMap<>();
            getProductsfromDB(logUtilizationMap, reportJbServerEmployeeGroupProducts);

            Map<Long, Set<JBServerProductBlacklist>> logErrorMap = new HashMap<>();
            reportJbServerEmployeeGroupProducts
                    = jbServerLogErrorService.getGroupByEmployeeProduct(ids);
            getProductsfromDB(logErrorMap, reportJbServerEmployeeGroupProducts);

            Map<Long, Set<JBServerProductBlacklist>> logBlacklistMap = new HashMap<>();
            reportJbServerEmployeeGroupProducts = jbServerBlacklistEmployeeService.getGroupByEmployeeProduct(ids);
            getProductsfromDB(logBlacklistMap, reportJbServerEmployeeGroupProducts);

            addProductstonewEmployeeList(curPageNewEmployee, logUtilizationMap, logErrorMap, logBlacklistMap);

            return new PageImpl<>(curPageNewEmployee, paging, employees.size());
        }
        return new PageImpl<>(curPageNewEmployee, paging, 0);
    }

    @Deprecated
    public Page<JBServerEmployee> findAll3(String name, Pageable paging) {
        return jbServerEmployeeRepository.findAllByNameContains(name, paging);
    }

    @Deprecated
    public Page<JBServerEmployee> findAll4(String filterValue, Pageable paging, Integer hideBlock) {

        List<JBServerEmployee> curPageLicense;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<JBServerEmployee> cq = builder.createQuery(JBServerEmployee.class);
        Root<JBServerEmployee> root = cq.from(JBServerEmployee.class);
        Predicate s1 = builder.like(root.get("name"), "%" + filterValue + "%");
//        Join<License, SoftwareGroupName> join2 = root2.join("softwareGroupNames");
        Join<JBServerEmployee, JBServerBlacklistEmployee> jbServerBlacklistProductSet = root.join("jbServerBlacklistProductSet", JoinType.LEFT);
        root.fetch("jbServerBlacklistProductSet", JoinType.LEFT);
        root.fetch("uniqErrorProduct", JoinType.LEFT);
        root.fetch("uniqUtilProduct", JoinType.LEFT);
        if (hideBlock == 1) {
            Predicate a1 = builder.and(s1, jbServerBlacklistProductSet.isNull());
            cq.where(a1);
        } else {
            cq.where(s1);
        }
        cq.select(root).orderBy(QueryUtils.toOrders(paging.getSort(), root, builder));
        List<JBServerEmployee> employees1 = entityManager.createQuery(cq).getResultList();
        Set<JBServerEmployee> foo = new LinkedHashSet<>(employees1);
        List<JBServerEmployee> employees = new LinkedList<>(foo);

        int start = (int) paging.getOffset();
        int finish = (int) paging.getOffset() + paging.getPageSize();
        if (employees.size() < finish) finish = employees.size();
        curPageLicense = employees.subList(start, finish);

        return new PageImpl<>(curPageLicense, paging, employees.size());

    }


    public Page<JBServerEmployee> findAll5(String filterValue, Pageable paging, Integer hideBlock) {

        FindData findData = getEmployeeIds(filterValue, paging, hideBlock);
        List<Long> idList = findData.getListData();


        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<JBServerEmployee> cq = builder.createQuery(JBServerEmployee.class);
        Root<JBServerEmployee> root = cq.from(JBServerEmployee.class);
        root.fetch("jbServerBlacklistProductSet", JoinType.LEFT);
        root.fetch("uniqErrorProduct", JoinType.LEFT);
        root.fetch("uniqUtilProduct", JoinType.LEFT);

        cq.select(root)
                .where(root.get("id").in(idList))
                .orderBy(QueryUtils.toOrders(paging.getSort(), root, builder));
        List<JBServerEmployee> employees1 = entityManager.createQuery(cq).getResultList();
        Set<JBServerEmployee> foo = new LinkedHashSet<>(employees1);

        List<JBServerEmployee> curPageLicense = new LinkedList<>(foo);

        return new PageImpl<>(curPageLicense, paging, findData.getSize());
    }

    public FindData getEmployeeIds(String filterValue, Pageable paging, Integer hideBlock) {
        FindData findData = new FindData();
        String srt = paging.getSort().toString();
        StringBuilder sb = new StringBuilder();
        sb.append("select e.id " +
                "from JBServerEmployee e ");

        if (hideBlock == 1) {
            sb.append("left join JBServerBlacklistEmployee eBL on eBL.jbServerEmployee = e " +
                    " where e.name like :name and eBL is null ");
        } else
            sb.append(" where e.name like :name ");
        sb.append(" order by ").append(srt.replace(":", ""));

        List idList = entityManager.createQuery(
                        sb.toString())
                .setParameter("name", "%" + filterValue + "%")
                .getResultList();

        if (idList.size() > 0) {
            int start = (int) paging.getOffset();
            int finish = (int) paging.getOffset() + paging.getPageSize();
            if (idList.size() < finish) finish = idList.size();
            findData.setListData(idList.subList(start, finish));
            findData.setSize(idList.size());
            return findData;
        }
        return null;

    }


    public void addProductstonewEmployeeList(List<TransientlistJBServerEmployee> curPageNewEmployee, Map<Long, Set<JBServerProductBlacklist>> logUtilizationMap, Map<Long, Set<JBServerProductBlacklist>> logErrorMap, Map<Long, Set<JBServerProductBlacklist>> logBlacklistMap) {
        curPageNewEmployee.forEach(emp -> {
            addProducttoEmployeeList(logUtilizationMap, "setSetUtilizationlist", emp);
            addProducttoEmployeeList(logBlacklistMap, "setSetBlacklist", emp);
            addProducttoEmployeeList(logErrorMap, "setSetErrorlist", emp);

        });
    }

    private void addProducttoEmployeeList(Map<Long, Set<JBServerProductBlacklist>> logUtilizationMap, String type, TransientlistJBServerEmployee emp) {
        for (Map.Entry<Long, Set<JBServerProductBlacklist>> entry : logUtilizationMap.entrySet()) {
            Long empId = entry.getKey();
            Set<JBServerProductBlacklist> value = entry.getValue();
            if (emp.getId().equals(empId)) switch (type) {
                case ("setSetUtilizationlist"):
                    emp.setSetUtilizationlist(value);
                    break;
                case ("setSetBlacklist"):
                    emp.setSetBlacklist(value);
                    break;
                case ("setSetErrorlist"):
                    emp.setSetErrorlist(value);
                    break;
                default:
                    break;
            }
        }
    }

    private void getProductsfromDB(Map<Long, Set<JBServerProductBlacklist>> logUtilizationMap, List<TransientJbServerEmployeeGroupProduct> reportJbServerEmployeeGroupProducts) {
        reportJbServerEmployeeGroupProducts.forEach(uniqProduct -> {
            if (logUtilizationMap.containsKey(uniqProduct.getId())) {
                logUtilizationMap.get(uniqProduct.getId()).add(uniqProduct.getProductBlacklist());
            } else {
                Set<JBServerProductBlacklist> s1 = new HashSet<>();
                s1.add(uniqProduct.getProductBlacklist());
                logUtilizationMap.put(uniqProduct.getId(), s1);
            }
        });
        reportJbServerEmployeeGroupProducts.clear();
    }

    public void checkEmployeeComputerRelations(Set<JBServerEmployee> employeeComputerRelations
            , JBServerComputer jbServerComputer
            , JBServerEmployee jbServerEmployee) {
        try {
            if (jbServerEmployee.getJbServerComputers() == null) {
                jbServerEmployee.setJbServerComputers(new HashSet<>(List.of(jbServerComputer)));
                employeeComputerRelations.add(jbServerEmployee);
            } else {
                jbServerEmployee.getJbServerComputers().add(jbServerComputer);
                employeeComputerRelations.add(jbServerEmployee);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    public void save(JBServerEmployee employee) {
        jbServerEmployeeRepository.save(employee);
    }

    public Page<JBServerComputer> findAllComputersByEmployee_Id(long empid, Pageable paging) {
        return jbServerEmployeeRepository.findAllComputersByEmployee_Id(empid, paging);
    }

    public void blockUnidentifierEmployee() {
        //TODO: Add to EmployeeBlackList unindentifier employee
    }

    public JBServerEmployee getEmployeebyUsername(String userName, JBServerComputer jbServerComputer, JBServerProductBlacklist jbServerProductBlacklist) {
        List<JBServerEmployee> emp = entityManager.createQuery("SELECT employee from JBServerEmployee employee where employee.name = ?1")
                .setParameter(1, userName)
                .getResultList();
        JBServerEmployee employee;
        if (!emp.isEmpty()) {
            employee = emp.get(0);
        } else {
            employee = jbServerEmployeeRepository.save(new JBServerEmployee(userName));
        }

        checkEmployeeComputerRelations(employee, jbServerComputer);
        employee.setUniqUtilProduct(new HashSet<>(List.of(jbServerProductBlacklist)));
        return employee;
    }

    private void checkEmployeeComputerRelations(JBServerEmployee jbServerEmployee, JBServerComputer jbServerComputer) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<JBServerEmployee> cq = cb.createQuery(JBServerEmployee.class);
        Root<JBServerEmployee> root = cq.from(JBServerEmployee.class);
        Join comp = root.join("jbServerComputers");
        cq.where(cb.and(
                cb.equal(root.get("id"), jbServerEmployee.getId()),
                cb.equal(comp.get("name"), jbServerComputer.getName())
        ));
        List<JBServerEmployee> employees1 = entityManager.createQuery(cq).getResultList();
        if (employees1.isEmpty()) {
            Set<JBServerComputer> pc = new HashSet<>(List.of(jbServerComputer));
            jbServerEmployee.setJbServerComputers(pc);
            jbServerEmployeeRepository.save(jbServerEmployee);
        }
    }

    public HashMap<String, JBServerEmployee> getByListNames(Set<String> employeeArr) {

        Query query = entityManager.createQuery("SELECT item FROM JBServerEmployee item WHERE item.name IN (?1)");
        query.setParameter(1, employeeArr);
        List<JBServerEmployee> items = query.getResultList();

        HashMap<String, JBServerEmployee> empMap = new HashMap<>();
        items.forEach(c -> empMap.put(c.getName(), c));

        if (employeeArr.size() == items.size()) {
            return empMap;
        }

        List<String> diffEmp = new ArrayList<>();
        for (String strEmp : employeeArr) {
            if (!empMap.containsKey(strEmp))
                diffEmp.add(strEmp);
        }

        List<JBServerEmployee> newEmp = new ArrayList<>();
        for (String c : diffEmp) {
            newEmp.add(new JBServerEmployee(c));
        }
        List<JBServerEmployee> newComp_ = jbServerEmployeeRepository.saveAllAndFlush(newEmp);
        newComp_.forEach(c -> empMap.put(c.getName(), c));

        return empMap;
    }

    @Deprecated
    public JBServerEmployee updateEmployeeProduct(Long id, JBServerComputer comp, JBServerProductBlacklist bl) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<JBServerEmployee> cq = cb.createQuery(JBServerEmployee.class);
        Root<JBServerEmployee> root = cq.from(JBServerEmployee.class);
        root.fetch("uniqUtilProduct", JoinType.LEFT);
        root.fetch("jbServerComputers", JoinType.LEFT);

        cq.where(cb.equal(root.get("id"), id));
        List<JBServerEmployee> computers1 = entityManager.createQuery(cq).getResultList();

        computers1.get(0).getUniqUtilProduct().add(bl);
        computers1.get(0).getJbServerComputers().add(comp);

        return computers1.get(0);
    }

    public static class FindData {
        List<Long> listData;
        int size;

        public void setListData(List<Long> listData) {
            this.listData = listData;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public List<Long> getListData() {
            return listData;
        }

        public int getSize() {
            return size;
        }
    }
}