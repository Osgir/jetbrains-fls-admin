package best.ccg.JbAdminApp;

import best.ccg.JbAdminApp.entity.JBServerProductBlacklist;
import best.ccg.JbAdminApp.entity.JBServerProductUtilization;
import best.ccg.JbAdminApp.service.JBServerProductBlacklistService;
import best.ccg.JbAdminApp.service.JBServerProductUtilizationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JBServerProductUtilizationServiceTest {

    @Autowired
    JBServerProductUtilizationService jbServerProductUtilizationService;
    @Autowired
    JBServerProductBlacklistService jbServerProductBlacklistService;

    @Test
    void checkEmployeeComputerRelationsTest() {
        Set<String> stringList = new HashSet<>(Arrays.asList("ReSharper Ultimate + Rider Toolbox;RRR:2021.3"
                , "DataGrip 2020.3;DB:2020.3"
                , "CLion Toolbox;CL:2021.3"
                , "new name;NN:2021.3"));

        Set<String> productBLList = new HashSet<>(Arrays.asList("RRR"
                , "DB"
                , "CL"
                , "NN"));
        HashMap<String, JBServerProductBlacklist> mapProductBlack = jbServerProductBlacklistService.getHashMapProductBlacklistBySetNames(productBLList);
        HashMap<String, JBServerProductUtilization> test = jbServerProductUtilizationService.getByListNames(stringList, mapProductBlack);

        assertThat(test.size() == 3).isTrue();
    }


}
