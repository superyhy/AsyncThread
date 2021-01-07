package com.yhy.elasticsearch.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 */
@Configuration
@EnableAsync   //开启异步调用
@Slf4j
public class ThreadExecutorConfig {
    //核心线程数
    private int corePoolSize = 10;
    //最大线程数
    private int maxNumPoolSize = 100;
    //队列数
    private int workQueue = 10;


    @Bean
    public ExecutorService threadExecutor() {
        log.debug("开始创建线程池");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxNumPoolSize);
        executor.setQueueCapacity(workQueue);
        executor.setThreadNamePrefix("test-thread-server-");
        //当pool达到max size时，如何处理新任务
        //该处理程序在execute方法的调用线程中直接运行被拒绝的任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化操作
        executor.initialize();
        return executor.getThreadPoolExecutor();
    }

}
