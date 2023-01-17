package ssp_2;

import ssp_2.playingthreads.Player;
import ssp_2.playingthreads.Spielleiter;

import java.util.concurrent.TimeUnit;

public class Game {

    public static void main(String[] args) throws InterruptedException {
        //CHOOSE YOUR Tisch
        TischInterface tisch = new TischConditionQueues();
        //TischInterface tisch = new TischMonitor();

        //Spieler
        Player spieler1 = new Player(tisch);
        spieler1.setName("Spieler 1");

        Player spieler2 = new Player(tisch);
        spieler2.setName("Spieler 2");

        //Schiedsrichter
        Spielleiter schieri = new Spielleiter(tisch);
        schieri.setName("Schiedsrichter");
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
}
