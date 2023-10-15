package course.concurrency.m2_async.executors.spring;

import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class AsyncClassTest {

    private static final Logger log = LoggerFactory.getLogger(AsyncClassTest.class);

    @Autowired
    public ApplicationContext context;

    @Autowired
    @Qualifier("applicationTaskExecutor")
    private ThreadPoolTaskExecutor executor;

    @Async
    public void runAsyncTask() {
        log.warn(() -> "*** RUN ***");
        log.warn(() -> "Prefix - " + executor.getThreadNamePrefix());
        log.warn(() -> "Class - " + executor.getClass());
        log.warn(() -> "Active count - " + executor.getActiveCount());
        log.warn(() -> "Core pool size - " + executor.getCorePoolSize());
        log.warn(() -> "Max pool size" + executor.getMaxPoolSize());
        log.warn(() -> "Keep alive - " + executor.getKeepAliveSeconds());
        log.warn(() -> "Pool size - " + executor.getPoolSize());
        log.warn(() -> "Executor type - " + executor.getThreadPoolExecutor());
        log.warn(() -> "Thread group - " + executor.getThreadGroup());
        log.warn(() -> "Thread priority - " + executor.getThreadPriority());
        log.warn(() -> "runAsyncTask: " + Thread.currentThread().getName());
    }
    @Async
    public void internalTask() {
        log.warn(() -> "*** ASYNC ***");
        log.warn(() -> "Prefix - " + executor.getThreadNamePrefix());
        log.warn(() -> "Class - " + executor.getClass());
        log.warn(() -> "Active count - " + executor.getActiveCount());
        log.warn(() -> "Core pool size - " + executor.getCorePoolSize());
        log.warn(() -> "Max pool size" + executor.getMaxPoolSize());
        log.warn(() -> "Keep alive - " + executor.getKeepAliveSeconds());
        log.warn(() -> "Pool size - " + executor.getPoolSize());
        log.warn(() -> "Executor type - " + executor.getThreadPoolExecutor());
        log.warn(() -> "Thread group - " + executor.getThreadGroup());
        log.warn(() -> "Thread priority - " + executor.getThreadPriority());
        log.warn(() -> "internalTask: " + Thread.currentThread().getName());
    }
}
