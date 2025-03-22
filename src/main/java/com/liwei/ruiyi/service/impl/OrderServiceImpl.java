package com.liwei.ruiyi.service.impl;

import com.liwei.ruiyi.service.OrderService;
import com.liwei.ruiyi.dao.TOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("orderService")
public class OrderServiceImpl implements OrderService {
    @Autowired
    private TOrderDao orderDao;
}
