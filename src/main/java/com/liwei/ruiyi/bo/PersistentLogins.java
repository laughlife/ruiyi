package com.liwei.ruiyi.bo;

import lombok.*;
import java.math.BigDecimal;
import java.util.*;

@Data
public class PersistentLogins {
    private String username;
    private String series;
    private String token;
    private String lastUsed;
    private String deviceInfo;
    private String ipAddress;
}