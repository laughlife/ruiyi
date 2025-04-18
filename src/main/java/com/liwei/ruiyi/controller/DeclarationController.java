package com.liwei.ruiyi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.CDeclaration;
import com.liwei.ruiyi.bo.CSupplier;
import com.liwei.ruiyi.bo.TSeller;
import com.liwei.ruiyi.bo.TUser;
import com.liwei.ruiyi.service.DeclarationService;
import com.liwei.ruiyi.service.SellerService;
import com.liwei.ruiyi.utils.DateUtils;
import com.liwei.ruiyi.utils.PageUtils;
import com.liwei.ruiyi.utils.ReadProUtils;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/declaration")
public class DeclarationController {
    @Autowired
    HttpServletRequest request;

    @Autowired
    DeclarationService declarationService;

    @Autowired
    SellerService sellerService;

    private static final long MAX_SIZE = 1024 * 1024 * 20;

    @RequestMapping("/goDeclaration")
    public String goDeclaration() {
        return "page/c_declaration/my_declaration";
    }

    @RequestMapping("/goCreateDeclaration")
    public String goCreateDeclaration() {
        TUser user = (TUser) request.getSession().getAttribute("user");
        List<TSeller> sellerList = new ArrayList<>();
        if (user.getIsAdmin() == 1) {
            sellerList = sellerService.getAllSellerList();
        }else if (user.getIsLadder() == 1) {
            sellerList = sellerService.queryShopByDepartmentCode(user.getId() + "");
        }else{
            sellerList = sellerService.getOwnSellerList(user.getId() + "");
        }
        request.setAttribute("sellerList", sellerList);
        return "page/c_declaration/create_declaration";
    }

    @RequestMapping("/cgsq")
    public String cgsq() {
        return "page/cgsq/cgsq";
    }
    @RequestMapping("/goEditDeclaration")
    public String goEditDeclaration(String id) {
        TUser user = (TUser) request.getSession().getAttribute("user");
        List<TSeller> sellerList = new ArrayList<>();
        if (user.getIsAdmin() == 1) {
            sellerList = sellerService.getAllSellerList();
        }else if (user.getIsLadder() == 1) {
            sellerList = sellerService.queryShopByDepartmentCode(user.getId() + "");
        }else{
            sellerList = sellerService.getOwnSellerList(user.getId() + "");
        }
        request.setAttribute("sellerList", sellerList);

        String imageServiceUrl = ReadProUtils.ReadProperties("imageServiceUrl", "conf.properties");
        request.setAttribute("imageServiceUrl", imageServiceUrl);

        CDeclaration declaration = declarationService.queryDeclarationById(id);
        request.setAttribute("dec", declaration);

        return "page/c_declaration/edit_declaration";
    }

    @RequestMapping("/uploadImage")
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        boolean upload = false;
        String errorMessage = "";
        String savePath = checkFile();
        File saveDir = new File(savePath);
        if (!saveDir.exists() && !saveDir.mkdirs()) {
            errorMessage = "无法创建上传目录。";
        }
        // 检查文件大小
        if (file.getSize() > MAX_SIZE) {
            errorMessage = "上传文件大小超过限制。";
        }
        // 检查扩展名
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            errorMessage = "请选择文件。";
        }
        String fileExt = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
        if (!fileExt.equals("jpg") && !fileExt.equals("png") && !fileExt.equals("gif") && !fileExt.equals("bmp") && !fileExt.equals("jpeg")) {
            errorMessage = "上传文件扩展名不是jpg|png|gif|bmp|jpeg。";
        }

        String newFileName = com.liwei.ruiyi.utils.StringUtils.getRandomString() + "." + fileExt;
        File uploadedFile = new File(savePath, newFileName);
        JSONObject rj = new JSONObject();
        try {
            file.transferTo(uploadedFile);
            String month = DateUtils.getSystemMonth();
            rj.put("src", "/declaration/" + month + "/" + newFileName);
            rj.put("fileName", originalFileName);
            upload = true;
        } catch (IOException e) {
            errorMessage = "上传文件失败。";
        }
        rj.put("status", upload);
        rj.put("msg", upload ? originalFileName + "上传成功" : errorMessage);
        return rj.toJSONString();
    }

    private String checkFile() {
        String savePath = ReadProUtils.ReadProperties("declarationImagePath", "conf.properties");
        File tempFile = new File(savePath);
        String month = DateUtils.getSystemMonth();
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        File file = new File(savePath, month);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    @RequestMapping("/queryAllDeclaration")
    @ResponseBody
    public String queryAllDeclaration() {
        TUser user = (TUser) request.getSession().getAttribute("user");
        int nowPage = Integer.parseInt(request.getParameter("page"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        PageUtils pageUtils = new PageUtils(nowPage, limit);
        String key = request.getParameter("key");
        String date_start = request.getParameter("date_start");
        String date_end = request.getParameter("date_end");
        JSONObject params = new JSONObject();
        params.put("key", key);
        params.put("date_start", date_start);
        params.put("date_end", date_end);
        params.put("user_id", user.getId());
        params.put("is_admin", user.getIsAdmin());
        params.put("is_ladder", user.getIsLadder());
        params.put("departmentCode", user.getDepartmentCode());
        pageUtils.setSearchParams(params);

        PageUtils page = declarationService.queryMyDeclaration(pageUtils);
        JSONObject returnJson = new JSONObject();
        returnJson.put("code", 0);
        returnJson.put("msg", "操作成功");
        returnJson.put("count", page.getTotal());
        returnJson.put("data", page.getData());

        return returnJson.toJSONString();
    }

    @RequestMapping("/deleteDeclaration")
    @ResponseBody
    public String deleteDeclaration(String id) {
        JSONObject rj = new JSONObject();
        boolean status = declarationService.deleteDeclarationById(id);
        rj.put("status", status);
        rj.put("msg", status ? "删除成功" : "删除失败");
        return rj.toJSONString();
    }

    @RequestMapping("/createDeclaration")
    @ResponseBody
    public String createDeclaration(CDeclaration declaration) {
        TUser user = (TUser) request.getSession().getAttribute("user");
        declaration.setUserId(user.getId());
        declaration.setUserName(user.getName());
        declaration.setUserPhone(user.getPhone());

        boolean status = declarationService.createDeclaration(declaration);
        JSONObject rj = new JSONObject();
        rj.put("status", status);
        rj.put("msg", status ? "申报成功，在仓库未确认之前，尚可修改" : "申报失败，请联系开发人员反馈此消息。");//确认
        return rj.toJSONString();
    }
    @RequestMapping("/updateDeclaration")
    @ResponseBody
    public String updateDeclaration(CDeclaration declaration) {
        boolean status = declarationService.updateDeclaration(declaration);
        JSONObject rj = new JSONObject();
        rj.put("status", status);
        rj.put("msg", status ? "修改成功" : "修改失败");//确认
        return rj.toJSONString();
    }
    @RequestMapping("/queren")
    @ResponseBody
    public String queren(String id) {
        boolean status = declarationService.queren(id);
        JSONObject rj = new JSONObject();
        rj.put("status", status);
        rj.put("msg", status ? "已确认，采购信息已锁定" : "操作失败，请联系开发人员排查错误原因，错误码/declaration/queren");//确认
        return rj.toJSONString();
    }

    @RequestMapping("/chakan")
    public String chakan(String id) {
        String imageServiceUrl = ReadProUtils.ReadProperties("imageServiceUrl", "conf.properties");
        request.setAttribute("imageServiceUrl", imageServiceUrl);

        CDeclaration declaration = declarationService.queryDeclarationById(id);
        request.setAttribute("dec", declaration);

        return "page/cgsq/chakan";
    }
    @RequestMapping("/chuli")
    public String chuli(String id) {
        String imageServiceUrl = ReadProUtils.ReadProperties("imageServiceUrl", "conf.properties");
        request.setAttribute("imageServiceUrl", imageServiceUrl);

        CDeclaration declaration = declarationService.queryDeclarationById(id);
        request.setAttribute("dec", declaration);

        return "page/cgsq/chuli";
    }

    @RequestMapping("/buy")
    @ResponseBody
    public String buy(String id) {
        boolean status = declarationService.queren(id);
        JSONObject rj = new JSONObject();
        rj.put("status", status);
        rj.put("msg", status ? "已确认，采购信息已锁定" : "操作失败，请联系开发人员排查错误原因，错误码/declaration/queren");//确认
        return rj.toJSONString();
    }

}
