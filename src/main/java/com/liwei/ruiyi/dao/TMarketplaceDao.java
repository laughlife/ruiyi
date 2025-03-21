package com.liwei.ruiyi.dao;

import com.liwei.ruiyi.bo.TMarketplace;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface TMarketplaceDao {
    void saveOrUpdate(TMarketplace m);
}
