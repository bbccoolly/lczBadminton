package com.lcz.bm.util;

import android.content.Context;

/**
 * Created by oyty on 11/14/17.
 */

public class RefreshStatusUtil {

    private Thread mStatusTask;
    private OnRefreshStatusListener listener;
    private long startPriceTime;


    public RefreshStatusUtil(Context context, OnRefreshStatusListener listener) {
        this.listener = listener;
    }

    public void start() {
        startQueryPrice();
    }

    private void startQueryPrice() {
        stopQueryPrice();
        mStatusTask = new Thread(QueryRunnable);
        mStatusTask.start();
    }

    private final Runnable QueryRunnable = new Runnable() {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    queryStatus();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                if (System.currentTimeMillis() - startPriceTime > 1000) {
//                    startPriceTime = System.currentTimeMillis();
//                    try {
//                        Thread.sleep(30000);
//                        queryStatus();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        }
    };

    private void queryStatus() {
        if (listener != null) {
            listener.onRefreshStatus();
        }
    }

    private void stopQueryPrice() {
        if (mStatusTask != null && !mStatusTask.isInterrupted()) {
            mStatusTask.interrupt();
            mStatusTask = null;
        }
    }

    public void stop() {
        stopQueryPrice();
    }

    public void release() {
        stop();
        listener = null;
    }

    public interface OnRefreshStatusListener {
        void onRefreshStatus();
    }

}
