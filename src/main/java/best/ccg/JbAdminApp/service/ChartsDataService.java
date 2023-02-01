package best.ccg.JbAdminApp.service;

import best.ccg.JbAdminApp.entity.JBServerLogUtilization;
import best.ccg.JbAdminApp.entity.JBServerProductUtilization;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChartsDataService {

    @PersistenceContext
    private EntityManager entityManager;

    public Map<String, Long> getLicenseUtilizationByHour(String licensename, Integer weeks) {
        LocalDateTime filterWeek = LocalDateTime.now().minusWeeks(weeks);
        if (licensename == null || licensename.isEmpty()) {
            licensename = "GoLand Toolbox";
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<JBServerLogUtilization> root = cq.from(JBServerLogUtilization.class);
        Join<JBServerLogUtilization, JBServerProductUtilization> join = root.join("jbServerProductUtilization");
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.greaterThanOrEqualTo(root.get("dateTime"), filterWeek));
        predicates.add(cb.equal(join.get("license"), licensename));
        Predicate[] predicatesArray = {};
        Expression<String> lpadValue = cb.substring(root.get("dateTime").as(String.class), 1, 13);
        cq.multiselect(lpadValue, cb.countDistinct(root.get("jbServerEmployee")))
                .where(predicates.toArray(predicatesArray));
        cq.groupBy(lpadValue);
        return entityManager.createQuery(cq).getResultList()
                .stream()
                .collect(Collectors.toMap(tuple -> tuple.get(0, String.class), tuple -> tuple.get(1, Long.class)));
    }

    public Map<String, Long> getLicenseUtilizationByDay(String licensename) {
        Map<String, Long> list = getLicenseUtilizationByHour(licensename, 12);
        Map<String, Long> list_ = new HashMap<>();

        for (Map.Entry<String, Long> entry : list.entrySet()) {
            String key = entry.getKey().substring(0, 10);
            Long value = entry.getValue();
            if (list_.containsKey(key)) {
                if (list_.get(key) < value)
                    list_.put(key, value);
            } else
                list_.put(key, value);

        }
        return list_;
    }
}