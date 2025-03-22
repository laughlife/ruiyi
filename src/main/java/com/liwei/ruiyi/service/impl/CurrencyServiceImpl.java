package com.liwei.ruiyi.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TCurrencyInfo;
import com.liwei.ruiyi.bo.TExchangeRate;
import com.liwei.ruiyi.service.CurrencyService;
import com.liwei.ruiyi.dao.TCurrencyDao;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("currencyService")
public class CurrencyServiceImpl implements CurrencyService {
    @Autowired
    private TCurrencyDao currencyDao;

    @Override
    public JSONObject analyseExcel(String filePath) {
        JSONObject rj = new JSONObject();
        File file = new File(filePath);
        String message = "汇率数据导入成功。";
        try {
            FileInputStream fis = new FileInputStream(file);
            // 使用XSSFWorkbook或SXSSFWorkbook来打开现有的文件
            boolean isXLSX = file.getAbsolutePath().endsWith(".xlsx");
            Workbook workbook = isXLSX ? new XSSFWorkbook(fis) : new HSSFWorkbook(fis);
            Sheet firstSheet = workbook.getSheetAt(0);

            /*
            * 2、人民币对墨西哥比索汇率中间价采取间接标价法，
            * 即100人民币折合多少外币。
            * 人民币对其它10种货币汇率中间价仍采取直接标价法，即100外币折合多少人民币。
            * */

            List<TCurrencyInfo> list = currencyDao.getCurrencyInfo();
            JSONObject cj = new JSONObject();
            for(TCurrencyInfo currencyInfo : list) {
                switch (currencyInfo.getName()){
                    case "墨西哥比索":
                        cj.put("比索",-1);
                        break;
                    default:
                        cj.put(currencyInfo.getName(),-1);
                        break;
                }
            }

            Row headerRow = firstSheet.getRow(0); // 获取表头
            List<String> titleList = new ArrayList<>();
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                titleList.add(headerRow.getCell(i).getStringCellValue());
            }
            // 通过表头获取列索引
            for(String title : titleList){
                if(cj.containsKey(title)){
                    cj.put(title,titleList.indexOf(title));
                }
            }

            for(int i = 1; i <= firstSheet.getLastRowNum(); i++){
                Row row = firstSheet.getRow(i);
                if(row == null){
                    continue;
                }
                String date = row.getCell(0).getStringCellValue();
                for(Map.Entry<String, Object> entry : cj.entrySet()){
                    if(entry.getValue() instanceof Integer){
                        int index = (int) entry.getValue();
                        Cell currency_cell = row.getCell(index);
                        if(currency_cell == null){
                            continue;
                        }
                        String key = entry.getKey();
                        BigDecimal decimalValue = new BigDecimal(0);
                        switch (currency_cell.getCellTypeEnum()){
                            case NUMERIC:
                                decimalValue = new BigDecimal(currency_cell.getNumericCellValue());
                                break;
                            case STRING:
                                decimalValue = BigDecimal.valueOf(Double.parseDouble(currency_cell.getStringCellValue()));
                                break;
                            default:
                                break;
                        }
                        saveOrUpdateCurrencyInfo(list,date,key,decimalValue);
                    }
                }

            }
            workbook.close();
            fis.close();
            file.delete();
        } catch (FileNotFoundException e) {
            message = "未查找到文件，文件不存在.";
            e.printStackTrace();
        } catch (IOException e) {
            message = "文件解析时候出错，清检查文件是否被占用.";
            e.printStackTrace();
        }
        rj.put("message", message);
        return rj;
    }

    private void saveOrUpdateCurrencyInfo(List<TCurrencyInfo> list, String date, String key, BigDecimal decimalValue) {
        TCurrencyInfo currencyInfo = new TCurrencyInfo();
        for(TCurrencyInfo info : list){
            if(key.equals("比索")){
                key = "墨西哥比索";
            }
            if(info.getName().equals(key)){
                currencyInfo = info;
                break;
            }
        }
        TExchangeRate exchangeRate = new TExchangeRate();
        BigDecimal result = decimalValue.divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);
        if(key.equals("墨西哥比索")){
            exchangeRate.setBaseCurrency(currencyInfo.getCode());
            exchangeRate.setTargetCurrency("CNY");
            exchangeRate.setDate(date);
            exchangeRate.setOfficialRate(result);
        }else{
            exchangeRate.setBaseCurrency("CNY");
            exchangeRate.setTargetCurrency(currencyInfo.getCode());
            exchangeRate.setDate(date);
            exchangeRate.setOfficialRate(result);
        }

        currencyDao.saveOrUpdateRate(exchangeRate);
    }
}
