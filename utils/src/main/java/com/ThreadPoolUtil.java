package com.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author torvalds on 2018/10/3 11:57.
 * @version 1.0
 */
public class ThreadPoolUtil {
    private static Logger logger = LoggerFactory.getLogger(ThreadPoolUtil.class);
    private static List<ExecutorService> executorServices = new ArrayList<ExecutorService>();


    /**
     * 一般情况推荐使用参数
     *
     * @param threadNamePrefix 线程前缀
     * @return
     */
    public static ExecutorService createThreadPool(String threadNamePrefix) {
        ExecutorService executorService = new ThreadPoolExecutor(10, 15, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(), new DefaultThreadFactory(threadNamePrefix));
        executorServices.add(executorService);
        return executorService;
    }

    public static ExecutorService createThreadPool(int corePoolSize,
                                                   int maximumPoolSize,
                                                   long keepAliveTime,
                                                   TimeUnit unit,
                                                   BlockingQueue<Runnable> workQueue, String threadNamePrefix) {
        ExecutorService executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new DefaultThreadFactory(threadNamePrefix));
        executorServices.add(executorService);
        return executorService;
    }

    public static ExecutorService createThreadPool(int corePoolSize,
                                                   int maximumPoolSize,
                                                   long keepAliveTime,
                                                   TimeUnit unit,
                                                   BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        ExecutorService executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        executorServices.add(executorService);
        return executorService;
    }

    public static ExecutorService createThreadPool(int corePoolSize,
                                                   int maximumPoolSize,
                                                   long keepAliveTime,
                                                   TimeUnit unit,
                                                   BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        ExecutorService executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        executorServices.add(executorService);
        return executorService;
    }

    public static ExecutorService createThreadPool(int corePoolSize,
                                                   int maximumPoolSize,
                                                   long keepAliveTime,
                                                   TimeUnit unit,
                                                   BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        ExecutorService executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        executorServices.add(executorService);
        return executorService;
    }

    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(corePoolSize);
        executorServices.add(scheduledThreadPoolExecutor);
        return scheduledThreadPoolExecutor;
    }

    public static ScheduledExecutorService newScheduledThreadPool(
            int corePoolSize, ThreadFactory threadFactory) {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(corePoolSize, threadFactory);
        executorServices.add(scheduledThreadPoolExecutor);
        return scheduledThreadPoolExecutor;
    }

    /**
     * 关闭应用时调用，关闭其他线程池资源
     */
    public static void shutdown() {
        for (ExecutorService executorService : executorServices) {
            try {
                if (!executorService.isShutdown()) {
                    executorService.shutdown();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.info("Success to close all user thread pool");
    }

    static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory(String threadNamePrefix) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                    poolNumber.getAndIncrement() + "-" + threadNamePrefix +
                    "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
}
