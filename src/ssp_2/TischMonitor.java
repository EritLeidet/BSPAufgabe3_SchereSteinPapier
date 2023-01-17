package ssp_2;

import ssp_2.util.Zug;

import java.util.LinkedList;

public class TischMonitor implements TischInterface{

    LinkedList<Zug> buffer;
    private static final int bufferMaxSize = 2;

    public TischMonitor(){
        this.buffer = new LinkedList<Zug>();
    }

    public synchronized void enter(Zug z) throws InterruptedException {
        while (buffer.size() >= bufferMaxSize) { /* Puffer ist voll */
            this.wait();  // Ausführenden Thread blockieren
        }
        buffer.add(z);
        this.notifyAll();
    }


    public synchronized Zug[] remove() throws InterruptedException {
        Zug[] item = new Zug[2];
        while (buffer.size() < bufferMaxSize) { /* Puffer noch nicht voll */
            this.wait(); // Ausführenden Thread blockieren
        }
        /* Datenpaket aus dem Puffer holen */
        item[0] = buffer.removeFirst();
        item[1] = buffer.removeFirst(); //Remove 2 items
        this.notifyAll();
        return item;
    }
}
