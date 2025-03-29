package com.liwei.ruiyi.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.service.ImageService;
import com.liwei.ruiyi.utils.DateUtils;
import com.liwei.ruiyi.utils.ReadProUtils;
import com.liwei.ruiyi.utils.StringUtils;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/image")
public class ImageController {

    private static String imageTempPath = ReadProUtils.ReadProperties("imageTempPath");

    @Autowired
    ImageService imageService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ServletContext servletContext;

    private String imageServiceUrl = ReadProUtils.ReadProperties("imageServiceUrl");

    @RequestMapping("/goImagePage")
    public String goImagePage() {
        request.setAttribute("imageUrl", imageServiceUrl);
        return "/page/image/image";
    }

    @RequestMapping("/uploadImageToCreateOrderImage")
    @ResponseBody
    public String uploadImageToCreateOrderImage(@RequestParam("file") MultipartFile file, String name, String number) {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            list.add(i);
        }
        int addNumber = 1;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(number)) {
            try {
                addNumber = Integer.parseInt(number);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if (!list.contains(addNumber)) {
            list.add(addNumber);
        }


        JSONObject rj = new JSONObject();
        //检测文件夹是否存在，如果不存在就创建，并且删除其他文件夹
        String tempPath = checkAndDeleteFile();
        if (!file.isEmpty()) {
            try {
                // 保存文件到指定目录
                String fileFirstName = StringUtils.getRandomString();
                //获取上传的file文件类型
                String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                String fileName = fileFirstName + fileSuffix;

                File convertFile = new File(tempPath, fileName);
                file.transferTo(convertFile);
                //通知UploadExcelServer，告知浏览器，文件上传成功
                JSONObject json = imageService.createOrderImage(convertFile, name, list);
                return json.toJSONString();
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

    private String checkAndDeleteFile() {
        File tempFile = new File(imageTempPath);
        String today = DateUtils.getSystemDate();
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        } else {
            //删除文件夹下所有文件
            File[] files = tempFile.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    if (!f.getName().equals(today)) {
                        try {
                            FileUtils.deleteDirectory(f); // 一键删除非空目录
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        File file = new File(imageTempPath + today);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }
}
