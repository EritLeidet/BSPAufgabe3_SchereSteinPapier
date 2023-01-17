package ssp_2.playingthreads;

import ssp_2.TischInterface;
import ssp_2.util.Geste;
import ssp_2.util.Zug;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static ssp_2.util.Geste.*;

public class Spielleiter extends Thread{
    //VERBRAUCHER

    //Counter für Endauswertung.
    public static int rounds = 0;
    public static Map<String, Integer> wins = new HashMap<>();

    public static final Geste[][] spielregeln = new Geste[][]{
            //hand1, hand2, winner
            {STEIN, STEIN, null},
            {STEIN, SCHERE, STEIN},
            {STEIN, PAPIER, PAPIER},
            {SCHERE, STEIN, STEIN},
            {SCHERE, SCHERE, null},
            {SCHERE, PAPIER, SCHERE},
            {PAPIER, STEIN, PAPIER},
            {PAPIER, SCHERE, SCHERE},
            {PAPIER, PAPIER, null}
    };

    private TischInterface tisch;

    public Spielleiter(TischInterface t) {
        this.tisch = t;
    }
    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                auswerten(tisch.remove());
            }
        } catch (InterruptedException e) {
            System.out.println(getName() + " wurde unterbrochen.");
        } finally {
            System.out.printf(getName() + " ist fertig." + gesamtauswertung());
        }

    }

    public static void auswerten(Zug[] spielzuege) {
        if (spielzuege.length != 2) throw new IllegalArgumentException("Nur exakt zwei Spielzüge pro Spiel erlaubt!");
        System.out.println();

        //Gewinner aussuchen
        String gewinner;
        if (!(spielzuege[0].geste == spielzuege[1].geste)) { //"Wenn beide Spieler NICHT das selbe Spielobjekt haben"
            int i = 0; //i-te Zeile von den Spielregeln
            //Relevante Spielregel suchen:
            while (!(spielregeln[i][0] == spielzuege[0].geste && spielregeln[i][1] == spielzuege[1].geste)) {i++;}

            //"Wenn (laut Spielregeln) Spieler 1 das gewinnende Spielobjekt hat"
            if (spielregeln[i][2] == spielzuege[0].geste) {
                gewinner = spielzuege[0].player.getName();

            } else {
                //"sonst gewinnt Spieler 2"
                gewinner = spielzuege[1].player.getName();}

        } else { //"Wenn beide Spieler das selbe Spielobjekt haben"
            gewinner = "Niemand";
        }

        //wins-Map und Runden-Zähler aktualisieren.
        int count = wins.containsKey(gewinner) ? wins.get(gewinner) : 0;
        wins.put(gewinner, count + 1);
        rounds++;
        System.out.println(wins.toString());
        System.out.printf("%s%n%s gewinnt.%n", Arrays.toString(spielzuege), gewinner);
    }

    public static String gesamtauswertung(){
        String sieger = wins.entrySet()
                .stream()
                .filter(e -> e.getKey() != "Niemand")
                .max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
        String verlierer = wins.entrySet()
                .stream()
                .filter(e -> e.getKey() != "Niemand")
                .min((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
        return String.format("%n%n######Gesamtauswertung######%n%n" +
                "Anzahl Runden: %d%nSieger ist %s mit %d gewonnenen Runden.%nVerlierer ist %s mit %d gewonnenen Runden.%nUnentschieden: %d%n", rounds, sieger, wins.get(sieger), verlierer, wins.get(verlierer), wins.get("Niemand"));
    }
}
