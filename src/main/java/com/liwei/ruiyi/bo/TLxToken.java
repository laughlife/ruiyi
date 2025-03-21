package com.liwei.ruiyi.bo;

import lombok.*;
import java.math.BigDecimal;
import java.util.*;

@Data
public class TLxToken {
    private Integer id;
    private String accessToken;
    private String refreshToken;
    private long saveTime;
    private long expiresTime;
}