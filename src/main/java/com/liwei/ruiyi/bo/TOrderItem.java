package com.liwei.ruiyi.bo;

import lombok.*;
import java.math.BigDecimal;
import java.util.*;

@Data
public class TOrderItem {
    private String id;
    private String orderId;
    private String asin;
    private Integer quantityOrdered;
    private String sellerSku;
    private String localSku;
    private String localName;
}