package best.ccg.JbAdminApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import best.ccg.JbAdminApp.service.ChartsDataService;
import best.ccg.JbAdminApp.service.JBServerProductUtilizationService;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping("/charts")
public class UtilizationChart {
    @Autowired
    ChartsDataService chartsDataService;
    @Autowired
    JBServerProductUtilizationService jbServerProductUtilizationService;

    @RequestMapping(value = "/index")
    public String dynachechchart2(Model model,
                                  @RequestParam(name = "licensename", required = false, defaultValue = "GoLand Toolbox") String licensename) {

        List<String> products = jbServerProductUtilizationService.getAllNames();
        Map<String, Long> data = chartsDataService.getLicenseUtilizationByHour(licensename, 2);
        Map<String, Long> treeMap = new TreeMap<String, Long>(data);

        Map<String, Long> dataDay = chartsDataService.getLicenseUtilizationByDay(licensename);
        Map<String, Long> treeMapDay = new TreeMap<String, Long>(dataDay);


        model.addAttribute("data", toString(treeMap, 0));
        model.addAttribute("dataDay", toString(treeMapDay, 1));
        model.addAttribute("productlist", products);
        model.addAttribute("product", licensename);

        return "charts/index";
    }

    public String toString(Map<String, Long> data, Integer a) {
        Iterator<Map.Entry<String, Long>> i = data.entrySet().iterator();
        if (!i.hasNext())
            return "{}";

        StringBuilder sb = new StringBuilder();
        for (; ; ) {
            Map.Entry<String, Long> e = i.next();
            String key = e.getKey();
            Long value = e.getValue();
            sb.append("{x:");
            sb.append('\'');
            sb.append(key);
            if (a == 0)
                sb.append(":00:00");
            sb.append("',");
            sb.append(" y:");
            sb.append(value);
            sb.append("}");
            if (!i.hasNext())
                return sb
                        .toString();
            sb.append(", ");
        }
    }

}