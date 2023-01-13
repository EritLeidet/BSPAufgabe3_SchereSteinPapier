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
    private final int maxSizeKarten = 2;

    public Tisch() {
        buffer = new LinkedList<Spielzug>();
        sem_E = new Semaphore(2);
        sem_V = new Semaphore(0);
    }

    public static void main(String[] args) throws InterruptedException {
        //Tisch
        TischVorlage tisch = new Tisch();

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
        spieler1.join();
        spieler2.join();
        schieri.interrupt();
        schieri.join();
    }

//    public void enter(Spielzug item) throws InterruptedException {
//        sem_E.acquire();
//        synchronized (this) {
//            buffer.add(item);
//            if (sem_E.availablePermits() > 0) {
//                wait();
//            } else {
//                notifyAll();
//            }
//        }//sync
//        sem_V.release();
//    }

    public synchronized void enter (Spielzug item) throws InterruptedException {
            while (buffer.size() >= maxSizeKarten) { /* Puffer ist voll */
                this.wait(); // Ausführenden Thread blockieren
            }
            buffer.add(item); // Datenpaket in den Puffer legen
            this.notifyAll();
    }

    public synchronized Spielzug[] remove() throws InterruptedException {
        Spielzug[] item = new Spielzug[2];
        while (buffer.size() < maxSizeKarten) { /* Puffer noch nicht voll */
            this.wait(); // Ausführenden Thread blockieren
        }
        /* Datenpaket aus dem Puffer holen */
        item[0] = buffer.removeFirst();
        item[1] = buffer.removeFirst(); //Remove 2 items
        this.notifyAll();
        return item;
    }

//    public Spielzug[] remove() throws InterruptedException {
//        Spielzug[] item = new Spielzug[2];
//
//        sem_V.acquire(2);
//        synchronized (this) {
//            item[0] = buffer.removeFirst();
//            item[1] = buffer.removeFirst(); //Remove 2 items
//        }
//        sem_E.release(2);
//
//        return item;
//    }
}
