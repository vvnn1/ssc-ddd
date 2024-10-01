package indi.melon.ssc.context.ticket.thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * @author vvnn1
 * @since 2024/9/22 11:39
 */
public class ParallelThread extends Thread{
    public ParallelThread(CyclicBarrier cyclicBarrier, CountDownLatch countDownLatch, Runnable target) {
        super(() -> {

            try {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }

                target.run();

            } finally {
                countDownLatch.countDown();
            }
        });
    }
}
