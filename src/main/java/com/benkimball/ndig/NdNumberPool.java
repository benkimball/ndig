package com.benkimball.ndig;

import java.util.concurrent.ConcurrentSkipListSet;

public class NdNumberPool {
    private final ConcurrentSkipListSet<Number> numbers;

    public NdNumberPool(int size) {
        numbers = new ConcurrentSkipListSet();
        for (int ix = 0; ix < size; ix++) numbers.add(ix);
    }

    public Number acquire() {
        return numbers.pollFirst();
    }

    public void release(Number n) {
        numbers.add(n);
    }
}
