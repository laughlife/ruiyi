package com.liwei.ruiyi.dao;

import com.liwei.ruiyi.bo.TWorldState;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface TWorldStateDao {
    void saveOrUpdate(TWorldState w);
}
