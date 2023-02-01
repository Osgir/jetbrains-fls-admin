package best.ccg.JbAdminApp.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

public class TransientlistJBServerEmployee {
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private LocalDateTime dateLastUtil;
    @Getter
    @Setter
    private LocalDateTime dateLastError;

    @Getter
    @Setter
    private Set<JBServerProductBlacklist> setBlacklist;
    @Getter
    @Setter
    private Set<JBServerProductBlacklist> setErrorlist;
    @Getter
    @Setter
    private Set<JBServerProductBlacklist> setUtilizationlist;

    public TransientlistJBServerEmployee(Long id, String name
            , Set<JBServerProductBlacklist> setUtilizationlist
            , Set<JBServerProductBlacklist> setErrorlist
            , Set<JBServerProductBlacklist> setBlacklist) {
        this.id = id;
        this.name = name;
        this.setBlacklist = setBlacklist;
        this.setErrorlist = setErrorlist;
        this.setUtilizationlist = setUtilizationlist;
    }

    public TransientlistJBServerEmployee(Long id, String name, LocalDateTime dateLastUtil, LocalDateTime dateLastError) {
        this.id = id;
        this.name = name;
        this.dateLastUtil = dateLastUtil;
        this.dateLastError = dateLastError;
    }

    public TransientlistJBServerEmployee() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransientlistJBServerEmployee that = (TransientlistJBServerEmployee) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public static class ComparatorDateLastError implements Comparator<TransientlistJBServerEmployee> {
        @Override
        public int compare(TransientlistJBServerEmployee o1, TransientlistJBServerEmployee o2) {
            if (o1.getDateLastError() != null) {
                if (o2.getDateLastError() != null) {
                    return o1.getDateLastError().isBefore(o2.getDateLastError())
                            ? 1 : o1.getDateLastError().isEqual(o2.getDateLastError()) ? 0 : -1;
                } else {
                    return -1;
                }
            } else {
                if (o2.getDateLastError() != null)
                    return 1;
                else
                    return 0;
            }
        }
    }

    public static class ComparatorDateLastUtil implements Comparator<TransientlistJBServerEmployee> {
        @Override
        public int compare(TransientlistJBServerEmployee o1, TransientlistJBServerEmployee o2) {
            if (o1.getDateLastUtil() != null) {
                if (o2.getDateLastUtil() != null) {
                    return o1.getDateLastUtil().isBefore(o2.getDateLastUtil())
                            ? 1 : o1.getDateLastUtil().isEqual(o2.getDateLastUtil()) ? 0 : -1;
                } else {
                    return -1;
                }
            } else {
                if (o2.getDateLastUtil() != null)
                    return 1;
                else
                    return 0;

            }
        }
    }


}

