package ssp_2.playingthreads;

import ssp_2.TischInterface;
import ssp_2.util.Geste;
import ssp_2.util.Zug;


import java.util.Random;

public class Player extends Thread{//ERZEUGER

    private TischInterface tisch;

    public Player(TischInterface t) {
        this.tisch = t;
    }

    @Override
    public void run() {
        Random rand = new Random();
        Geste[] gesten = Geste.values();
        try {
            while (!isInterrupted()) {
                this.tisch.enter(new Zug(this, gesten[rand.nextInt(gesten.length)]));
            }
            throw new InterruptedException();
        } catch (InterruptedException e) {
            System.out.println(getName() + " wurde unterbrochen.");
        }
    }

}
