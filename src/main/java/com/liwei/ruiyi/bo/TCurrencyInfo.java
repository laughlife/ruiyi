package com.liwei.ruiyi.bo;

import lombok.*;
import java.math.BigDecimal;
import java.util.*;

@Data
public class TCurrencyInfo {
    private String code;
    private String name;
    private String symbol;
    private Boolean minorUnits;
    private Boolean isActive;
}