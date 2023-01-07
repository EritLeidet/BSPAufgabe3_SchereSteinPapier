package ssp;

import java.util.Random;

public class Spieler extends Thread { //ERZEUGER

    private TischVorlage tisch;

    public Spieler(TischVorlage t) {
        this.tisch = t;
    }

    @Override
    public void run() {
        Random rand = new Random();
        Spielobjekt[] spielobjekte = Spielobjekt.values();
        try {
            while (!isInterrupted()) {
                this.tisch.enter(new Spielzug(this, spielobjekte[rand.nextInt(spielobjekte.length)]));
            }
            throw new InterruptedException();
        } catch (InterruptedException e) {
            System.out.println(getName() + " wurde unterbrochen.");
        }
    }
}
