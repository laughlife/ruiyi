package com.liwei.ruiyi.service.impl;

import com.liwei.ruiyi.service.WaybillService;
import com.liwei.ruiyi.dao.CWaybillDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("waybillService")
public class WaybillServiceImpl implements WaybillService {
    @Autowired
    private CWaybillDao waybillDao;
}
