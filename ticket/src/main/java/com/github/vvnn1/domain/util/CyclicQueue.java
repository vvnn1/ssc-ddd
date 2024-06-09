package com.github.vvnn1.domain.util;

import java.util.*;

/**
 * @author vvnn1
 * @since 2024/5/25 14:36
 */
public class CyclicQueue<T> extends LinkedList<T> {
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
