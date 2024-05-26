package com.github.vvnn1.domain.util;

import java.util.*;

/**
 * @author vvnn1
 * @since 2024/5/25 14:36
 */
public class CyclicQueue<T> extends AbstractQueue<T> {
    private final Queue<T> queue;
    private final int initialCapacity;

    public CyclicQueue(int initialCapacity) {
        this.queue = new LinkedList<>();
        this.initialCapacity = initialCapacity;
    }

    @Override
    public Iterator<T> iterator() {
        return queue.iterator();
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public boolean offer(T t) {
        if (queue.size() == initialCapacity){
            return false;
        }

        return queue.offer(t);
    }

    @Override
    public T poll() {
        return queue.poll();
    }

    @Override
    public T peek() {
        return queue.peek();
    }
}
