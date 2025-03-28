package com.liwei.ruiyi.service;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public interface ImageService {
    /**
     * 生成订单图片，返回图片地址
     * @param convertFile 图片文件
     * @param name 商品名称
     * @param list 商品数量
     * @return
     */
    JSONObject createOrderImage(File convertFile, String name, List<Integer> list);
}
