package com.liwei.ruiyi.dao;

import com.liwei.ruiyi.bo.TSeller;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface TSellerDao {
    void saveOrUpdate(TSeller seller);

    List<TSeller> queryAllSellers();

    List<TSeller> getUserSellers(String id);

    List<TSeller> queryUnbindShop();
}
