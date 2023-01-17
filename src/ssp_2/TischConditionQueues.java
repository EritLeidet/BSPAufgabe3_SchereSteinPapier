package ssp_2;


import ssp_2.util.Zug;

import java.util.LinkedList;
import java.util.concurrent.locks.*;

public class TischConditionQueues implements TischInterface{

    private LinkedList<Zug> buffer;
    private Lock mutex = new ReentrantLock();
    private final Condition notFull = mutex.newCondition();
    private final Condition full = mutex.newCondition();

    private final int bufferMaxSize = 2;


    public TischConditionQueues() {
        buffer = new LinkedList<Zug>();
    }

    public void enter(Zug item) throws InterruptedException {
        mutex.lockInterruptibly();

        try {
            buffer.add(item);
            if (this.buffer.size() >= bufferMaxSize) full.signal();
            notFull.await();
        }finally {
            mutex.unlock();
        }

    }

    public Zug[] remove() throws InterruptedException {
        Zug[] item = new Zug[2];
        mutex.lockInterruptibly();
        try {
            while (buffer.size() < bufferMaxSize) full.await();
            item[0] = buffer.removeFirst();
            item[1] = buffer.removeFirst(); //Remove 2 items
        }finally {
        notFull.signalAll();
        mutex.unlock();
        }
        return item;
    }

}
