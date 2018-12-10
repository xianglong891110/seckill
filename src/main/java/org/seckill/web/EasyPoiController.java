package org.seckill.web;

import cn.afterturn.easypoi.entity.vo.MapExcelConstants;
import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author whitedot
 * @date 2018/12/6
 * @description
 **/
@Controller
@RequestMapping(value = "easyPoi")
public class EasyPoiController {

    @RequestMapping("/MapExportExcel")
    public String exportMerchantProfitQuery(ModelMap modelMap, HttpServletRequest request) {
        List<ExcelExportEntity> entityList = new ArrayList<ExcelExportEntity>();
        entityList.add(new ExcelExportEntity("用户ID", "id", 35));
        entityList.add(new ExcelExportEntity("用户名", "name", 15));
        entityList.add(new ExcelExportEntity("用户年龄", "age", 15));
        List<Map<String, String>> dataResult = getData();
        modelMap.put(MapExcelConstants.ENTITY_LIST, entityList);
        modelMap.put(MapExcelConstants.MAP_LIST, dataResult);
        modelMap.put(MapExcelConstants.FILE_NAME, "商户利润");
        Date now = new Date();
        modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("商户利润详情", "创建时间" + now.toLocaleString(), "商户"));
        return MapExcelConstants.EASYPOI_MAP_EXCEL_VIEW;
    }

    private List<Map<String, String>> getData() {
        List<Map<String, String>> dataResult = new ArrayList<Map<String, String>>();
        Map<String, String> u1 = new LinkedHashMap<String, String>();
        u1.put("id", "1");
        u1.put("name", "cyf");
        u1.put("age", "21");
        Map<String, String> u2 = new LinkedHashMap<String, String>();
        u2.put("id", "2");
        u2.put("name", "cy");
        u2.put("age", "22");
        dataResult.add(u1);
        dataResult.add(u2);
        return dataResult;
    }


}
