package com.liwei.ruiyi.service.impl;

import com.liwei.ruiyi.bo.TFont;
import com.liwei.ruiyi.dao.TFontDao;
import com.liwei.ruiyi.service.TFontService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("fontService")
public class TFontServiceImpl implements TFontService {
    @Autowired
    private TFontDao fontDao;

    @Override
    public List<TFont> getAllFont() {

        return fontDao.getAllFont();
    }
}