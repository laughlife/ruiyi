package com.liwei.ruiyi.controller;

import com.liwei.ruiyi.bo.TFont;
import com.liwei.ruiyi.service.TFontService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/font")
public class FontController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    TFontService fontService;

    @RequestMapping("/font_list")
    public String fontList(){
        List<TFont> fontList = fontService.getAllFont();
        request.setAttribute("fontList",fontList);
        return "page/icon/fontawesome";
    }
}
