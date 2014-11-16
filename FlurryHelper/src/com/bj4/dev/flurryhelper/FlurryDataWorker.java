
package com.bj4.dev.flurryhelper;

import java.util.ArrayList;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

public class FlurryDataWorker {
    private static final boolean DEBUG = true;

    private static final String TAG = "QQQQ";

    private volatile static int sDebugCounter = 0;

    private static final HandlerThread sWorkerThread = new HandlerThread("launcher-loader");
    static {
        sWorkerThread.start();
    }

    public static final Handler sWorker = new Handler(sWorkerThread.getLooper());

    private static ArrayList<DelayedRunnable> sQueueTasks = new ArrayList<DelayedRunnable>();

    public synchronized static void schedulePost(Runnable runnable) {
        if (runnable == null)
            return;
        synchronized (sQueueTasks) {
            final boolean runImmediately = sQueueTasks.isEmpty();
            sQueueTasks.add(new DelayedRunnable(runnable));
            if (runImmediately) {
                runNextTask();
            }
        }
    }

    public synchronized static void clearAllTasks() {
        sWorker.removeCallbacksAndMessages(null);
        synchronized (sQueueTasks) {
            sQueueTasks.clear();
        }
    }

    private synchronized static void runNextTask() {
        synchronized (sQueueTasks) {
            if (sQueueTasks.isEmpty())
                return;
            sWorker.post(sQueueTasks.get(0));
        }
    }

    private static class DelayedRunnable implements Runnable {
        private Runnable mTask;

        private int mCounter = ++sDebugCounter;

        public DelayedRunnable(Runnable runnable) {
            mTask = runnable;
            if (DEBUG)
                Log.v(TAG, "build counter: " + mCounter);
        }

        @Override
        public void run() {
            if (DEBUG)
                Log.d(TAG, "run counter: " + mCounter);
            mTask.run();
            FlurryDataWorker.runNextTask();
        }
    }
}
