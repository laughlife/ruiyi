package com.liwei.ruiyi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public interface DeepseekService {

    void sendMessage(String uid,String message);

}
