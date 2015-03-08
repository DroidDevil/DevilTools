package com.droiddevil.deviltools.network;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.Process;

/**
 * An abstract class that provides networking capability in its own asynchronous
 * thread pool.
 * 
 * @author tperrien
 * 
 */
public abstract class AsyncNetworkTask implements Runnable {

    private static final int CORE_POOL_SIZE = 5;

    private static final int MAXIMUM_POOL_SIZE = 128;

    private static final int KEEP_ALIVE = 1;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "DDToolsNetworkTask #"
                    + mCount.getAndIncrement());
        }
    };

    private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(
            10);

    public static final ExecutorService THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
            sPoolWorkQueue, sThreadFactory);

    private Future<?> mFuture;

    private boolean mCancelled;

    /**
     * Adds this task to the task executor service for processing.
     * 
     * @return A handle back to this task.
     */
    public AsyncNetworkTask execute() {
        Runnable taskWrapper = new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                AsyncNetworkTask.this.run();
            }
        };
        mFuture = THREAD_POOL_EXECUTOR.submit(taskWrapper);
        return this;
    }

    public void cancel() {
        mCancelled = true;
        if (mFuture != null) {
            mFuture.cancel(true);
        }
    }

}
