package com.liwei.ruiyi.dao;

import com.liwei.ruiyi.bo.TDeepseekLog;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface TDeepseekLogDao {
    int insert(TDeepseekLog entity);
    int update(TDeepseekLog entity);
}
