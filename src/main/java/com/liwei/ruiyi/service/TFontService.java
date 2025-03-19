package com.liwei.ruiyi.service;

import com.liwei.ruiyi.bo.TFont;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TFontService {

    List<TFont> getAllFont();
}