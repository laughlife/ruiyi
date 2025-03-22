package com.liwei.ruiyi.bo;

import lombok.*;
import java.math.BigDecimal;
import java.util.*;

@Data
public class TExchangeRate {
    private String baseCurrency;
    private String targetCurrency;
    private String date;
    private BigDecimal officialRate;
    private BigDecimal customRate;
    private String lastUpdated;
}