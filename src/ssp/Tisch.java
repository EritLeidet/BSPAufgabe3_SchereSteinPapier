package ssp;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Tisch implements TischVorlage {
    private LinkedList<Spielzug> buffer;
    private Semaphore sem_E;
    private Semaphore sem_V;

    public Tisch() {
        buffer = new LinkedList<Spielzug>();
        sem_E = new Semaphore(2);
        sem_V = new Semaphore(0);
    }

    public static void main(String[] args) {
        //Tisch
        TischVorlage tisch = new TischAlternative();

        //Spieler
        Spieler spieler1 = new Spieler(tisch);
        spieler1.setName("Spieler 1");

        Spieler spieler2 = new Spieler(tisch);
        spieler2.setName("Spieler 2");

        //Schiedrichter
        Schiedsrichter schieri = new Schiedsrichter(tisch);
        schieri.setName("Schiedrichter");
        //Spielstart
        spieler1.start();
        spieler2.start();
        schieri.start();

        //warte
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {}

        System.out.printf("%nSpielschluss...%n");
        spieler1.interrupt();
        spieler2.interrupt();
        schieri.interrupt();
    }

    public void enter(Spielzug item) throws InterruptedException {
        sem_E.acquire();
        synchronized (this) {
            buffer.add(item);
            if (sem_E.availablePermits() > 0) {
                wait();
            } else {
                notifyAll();
            }
        }//sync
        sem_V.release();
    }

    public Spielzug[] remove() throws InterruptedException {
        Spielzug[] item = new Spielzug[2];

        sem_V.acquire(2);
        synchronized (this) {
            item[0] = buffer.removeFirst();
            item[1] = buffer.removeFirst(); //Remove 2 items
        }
        sem_E.release(2);

        return item;
    }
}
