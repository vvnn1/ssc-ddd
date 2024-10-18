package indi.melon.ssc.domain.ticket.util;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author vvnn1
 * @since 2024/5/25 14:36
 */
public class CyclicQueue<T> extends LinkedBlockingQueue<T> {
    private final int initialCapacity;

    public CyclicQueue(int initialCapacity) {
        this.initialCapacity = initialCapacity;
    }

    @Override
    public boolean offer(T t) {
        if (size() == initialCapacity){
            return false;
        }
        return super.offer(t);
    }
}
