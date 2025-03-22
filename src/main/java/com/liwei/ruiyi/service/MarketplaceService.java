package com.liwei.ruiyi.service;

import org.springframework.stereotype.Service;

@Service
public interface MarketplaceService {

    boolean refreshMarketplace();

    boolean checkAllMarketplaceWorldState();
}
