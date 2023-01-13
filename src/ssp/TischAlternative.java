package ssp;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class TischAlternative implements TischVorlage{
    private LinkedList<Spielzug> buffer;
    private Lock mutex = new ReentrantLock();;

    private final Condition notFull = mutex.newCondition();
    private final Condition full = mutex.newCondition();
    private final Condition otherPlayer = mutex.newCondition();
    private int occupiedSpaces = 0;

    public TischAlternative() {
        buffer = new LinkedList<Spielzug>();
    }

    private void waitUntilFull() throws InterruptedException {
        while (this.occupiedSpaces < 2) {
            full.await();
        }
        this.occupiedSpaces = 0;
    }

    private void signalIfFull() {
        this.occupiedSpaces++;
        if (this.occupiedSpaces > 1) {
            full.signal();
        }
    }

    public void enter(Spielzug item) throws InterruptedException {
        final int BUFFER_MAX_SIZE = 2;
        mutex.lockInterruptibly();
        //System.out.println(Thread.currentThread().getName() + " in");
        try {
            buffer.add(item);
            signalIfFull();
            notFull.await();

            //System.out.println(Thread.currentThread().getName() + " out");

        } finally {
            mutex.unlock();
        }

    }

    public Spielzug[] remove() throws InterruptedException {
        Spielzug[] item = new Spielzug[2];
        mutex.lockInterruptibly();

        try {
            //System.out.println(Thread.currentThread().getName() + " waiting until full");
            waitUntilFull();
            //System.out.println(Thread.currentThread().getName() + " in");
            //Kritischer Abschnitt
            item[0] = buffer.removeFirst();
            item[1] = buffer.removeFirst(); //Remove 2 items
            //Ende - Kritischer Abschnitt

            notFull.signalAll();

        } finally {
            //System.out.println(Thread.currentThread().getName() + " out");
            mutex.unlock();

        }
        return item;
    }
}
