package com.clj.blesample.service.utils;

import java.util.Arrays;

public class RingBuffer<T> {

    private final static int DEFAULT_SIZE = 16;
    private Object[] buffer;
    private int head = 0;
    private int tail = 0;
    private int bufferSize;

    public RingBuffer() {
        this.bufferSize = DEFAULT_SIZE;
        this.buffer = new Object[bufferSize];
    }

    public RingBuffer(int initSize) {
        this.bufferSize = initSize;
        this.buffer = new Object[bufferSize];
    }

    private Boolean empty() {
        return head == tail;
    }

    private Boolean full() {
        return (tail + 1) % bufferSize == head;
    }

    public void clear() {
        Arrays.fill(buffer, null);
        this.head = 0;
        this.tail = 0;
    }

    public Boolean put(T v) {
        if (full()) {
            return false;
        }
        buffer[tail] = v;
        tail = (tail + 1) % bufferSize;
        return true;
    }

    public T get() {
        if (empty()) {
            return null;
        }
        Object result = buffer[head];
        head = (head + 1) % bufferSize;
        return (T) result;
    }

    public T[] getAll() {
        if (empty()) {
            return (T[]) (new Object[0]);
        }
        int copyTail = tail;
        int cnt = head < copyTail ? copyTail - head : bufferSize - head + copyTail;
        Object[] result = new Object[cnt];
        if (head < copyTail) {
            for (int i = head; i < copyTail; i++) {
                result[i - head] = buffer[i];
            }
        } else {
            for (int i = head; i < bufferSize; i++) {
                result[i - head] = buffer[i];
            }
            for (int i = 0; i < copyTail; i++) {
                result[bufferSize - head + i] = buffer[i];
            }
        }
        head = copyTail;
        return (T[]) result;
    }

}
