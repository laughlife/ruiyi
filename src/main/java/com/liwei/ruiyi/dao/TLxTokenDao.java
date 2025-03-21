package com.liwei.ruiyi.dao;

import com.liwei.ruiyi.bo.TLxToken;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface TLxTokenDao {
    int insertToken(TLxToken token);
    int updateToken(TLxToken token);
    TLxToken getToken();

    int getTokenCount();
}
