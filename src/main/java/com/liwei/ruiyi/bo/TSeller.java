package com.liwei.ruiyi.bo;

import lombok.*;
import java.math.BigDecimal;
import java.util.*;

@Data
public class TSeller {
    private Integer sid;
    private Integer mid;
    private String name;
    private String sellerId;
    private String accountName;
    private Integer sellerAccountId;
    private String region;
    private String country;
    private Integer hasAdsSetting;
    private String marketplaceId;
    private Integer status;
}