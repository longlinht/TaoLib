package io.github.longlinht.library.concurrent;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义ThreadPool，可以监听task执行状态
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */
public class TaoThreadPoolExecutor extends ThreadPoolExecutor {

    public TaoThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public TaoThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public TaoThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public TaoThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);

        for (TaskExecWatcher listener : taskEndListeners) {
            listener.onBeforeExecute(t, r);
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);

        for (TaskExecWatcher listener : taskEndListeners) {
            listener.onAfterExecute(r, t);
        }
    }

    private final List<TaskExecWatcher> taskEndListeners = new CopyOnWriteArrayList<>();

    public void addTaskExecWatcher(@NonNull TaskExecWatcher listener) {
        taskEndListeners.add(listener);
    }

    public void removeTaskExecWatcher(@NonNull TaskExecWatcher listener) {
        taskEndListeners.remove(listener);
    }

    /**
     * 任务执行监听
     */
    public interface TaskExecWatcher {

        void onAfterExecute(Runnable task, Throwable throwable);

        void onBeforeExecute(Thread t, Runnable r);
    }
}
