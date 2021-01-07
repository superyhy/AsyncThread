package com.yhy.elasticsearch.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
public class AsyncService {

    @Resource(name = "threadExecutor")
    ExecutorService asyncService;


    public List<String> myThreads() {

        //在业务中使用多线程
        Future<String> query1 = asyncService.submit(() -> {
            Integer a = 1 / 0;
            return "qwqw";
        });

        Future<String> query2 = asyncService.submit(() -> {
            return "线程2";
        });

        Future<String> query3 = asyncService.submit(() -> {
            return "线程3";
        });

        String string1 = null, string2 = null, string3 = null;

        try {
            string1 = query1.get();
            string2 = query2.get();
            string3 = query3.get();
        } catch (Exception e) {

        }
        List<String> stringList = new ArrayList<>();
        stringList.add(string1);
        stringList.add(string2);
        stringList.add(string3);

        return stringList;
    }


    public void myThread2() {
        //使用Future方式执行多次任务
        //生成一个集合
        List<Future> futures = new ArrayList<>();

        //并发处理
        for (int i = 0; i < 10; i++) {
            Integer finalI = i;
            Future<?> future = asyncService.submit(() -> {
                return finalI;
            });
            futures.add(future);
        }

        //查询任务执行结果
        for (Future<?> future : futures) {
            //CPU高速轮询，每个future都并发轮询，判断完成状态后获取结果
            while (true) {
                //获取CPU完成状态
                if (future.isDone() && future.isCancelled()) {
                    Integer i = new Integer(0);
                    try {
                        i = (Integer) future.get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "任务：" + i + "完成");
                    break;  //当前future获取结果完毕，跳出while
                } else {
                    //每次轮询休息1秒钟，避免CPU高速轮询
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }


    /**
     * execute()方法用于提交不需要返回值的任务，所以无法判断任务是否被线程池执行成功与否；
     * submit()方法用于提交需要返回值的任务。线程池会返回一个 Future 类型的对象，通过这个 Future 对象可以判断任务是否执行成
     * 功，并且可以通过 Future 的 get()方法来获取返回值，get()方法会阻塞当前线程直到任务完成，而使用 get（long timeout，
     * TimeUnit unit）方法则会阻塞当前线程一段时间后立即返回，这时候有可能任务没有执行完。
     */
    public void myThread3() {

        int a=2;
        asyncService.submit(new Runnable() {
            @Override
            public void run() {
                int result=a+2;
                System.out.println("异步线程：" + Thread.currentThread().getName() + "开始执行"+result);

            }
        });

        asyncService.submit(new Runnable() {
            @Override
            public void run() {
                int result=a/0;
                System.out.println("异步线程：" + Thread.currentThread().getName() + "开始执行"+result);

            }
        });
    }


}
