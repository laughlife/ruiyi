package com.liwei.ruiyi.bo;

import lombok.*;
import java.math.BigDecimal;
import java.util.*;

@Data
public class TProHistory {
    private Integer mid;
    private String country;
    private Integer sid;
    private String sellerName;
    private String asin;
    private String sellerSku;
    private String queryDate;
    private String isEur;
    private String localSku;
    private String localName;
    private String isDelete;
    private Integer volume;
    private String productPicUrl;
    private String smallImageUrl;
    private BigDecimal price;
    private BigDecimal sourceRate;
    private Integer status;
    private Integer cid;
    private String addDate;
}