package com.liwei.ruiyi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.service.CurrencyService;
import com.liwei.ruiyi.utils.ReadProUtils;
import com.liwei.ruiyi.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/currency")
public class CurrencyController {

    @Autowired
    CurrencyService currencyService;

    private String excelSavePath = ReadProUtils.ReadProperties("excelSavePath");

    @RequestMapping("/uploadCurrency")
    @ResponseBody
    public String uploadCurrency(@RequestParam("file") MultipartFile file, String id) {
        JSONObject rj = new JSONObject();
        if (!file.isEmpty()) {
            try {
                // 保存文件到指定目录
                String fileFirstName = StringUtils.getRandomString();
                //获取上传的file文件类型
                String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                String fileName = fileFirstName + fileSuffix;
                File convertFile = new File(excelSavePath + fileName);
                file.transferTo(convertFile);
                rj.put("code", 0);
                rj.put("data", null);
                //通知UploadExcelServer，告知浏览器，文件上传成功
                JSONObject saveResult = currencyService.analyseExcel(convertFile.getAbsolutePath());
                rj.put("msg", saveResult.getString("message"));
                return rj.toJSONString();
            } catch (IOException e) {
                rj.put("code", -1);
                rj.put("msg", "Error uploading file: " + e.getMessage());
                return rj.toJSONString();
            }
        } else {
            rj.put("code", -1);
            rj.put("msg", "Please select a file to upload");
            return rj.toJSONString();
        }
    }
}
