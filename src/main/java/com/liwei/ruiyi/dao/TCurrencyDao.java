package com.liwei.ruiyi.dao;

import com.liwei.ruiyi.bo.TCurrencyInfo;
import com.liwei.ruiyi.bo.TExchangeRate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TCurrencyDao {
    List<TCurrencyInfo> getCurrencyInfo();

    void saveOrUpdateRate(TExchangeRate exchangeRate);

    boolean isGetCurrency();
}
