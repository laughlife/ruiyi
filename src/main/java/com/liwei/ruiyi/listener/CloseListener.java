package com.liwei.ruiyi.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class CloseListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        //执行清理操作，将正在执行中的任务修改为执行中断。
        System.out.println("应用上下文关闭，执行清理操作。");
    }
}
