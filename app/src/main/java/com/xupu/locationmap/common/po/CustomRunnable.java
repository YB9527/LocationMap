package com.xupu.locationmap.common.po;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static androidx.constraintlayout.widget.Constraints.TAG;

public  class CustomRunnable implements Runnable {

    private CountDownLatch countDownLatch;
    private String name;
    private int delayTime;

    public CustomRunnable(CountDownLatch countDownLatch, String name, int delayTime) {
        this.countDownLatch = countDownLatch;
        this.name = name;
        this.delayTime = delayTime;
    }

    @Override
    public void run() {


        try {
            TimeUnit.SECONDS.sleep(delayTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }
}
