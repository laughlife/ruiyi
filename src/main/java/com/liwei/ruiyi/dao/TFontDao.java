package com.liwei.ruiyi.dao;

import com.liwei.ruiyi.bo.TFont;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TFontDao {

    List<TFont> getAllFont();
}