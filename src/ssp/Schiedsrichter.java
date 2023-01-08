package ssp;
import java.util.*;

import static ssp.Spielobjekt.*;

public class Schiedsrichter extends Thread{ //VERBRAUCHER

    //Counter für Endauswertung.
    public static int rounds = 0;
    public static Map<String, Integer> wins = new HashMap<>();

    public static final Spielobjekt[][] spielregeln = new Spielobjekt[][]{
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

    private TischVorlage tisch;

    public Schiedsrichter(TischVorlage t) {
        this.tisch = t;
    }
    @Override
    public void run() {
            try {
                while (!isInterrupted()) {
                    auswerten(tisch.remove());
                }
                System.out.printf(getName() + " ist fertig." + gesamtauswertung());
            } catch (InterruptedException e) {
                System.out.println(getName() + " wurde unterbrochen.");
            }

    }

    public static void auswerten(Spielzug[] spielzuege) {
        if (spielzuege.length != 2) throw new IllegalArgumentException("Nur exakt zwei Spielzüge pro Spiel erlaubt!");
        System.out.println();

        //Gewinner aussuchen
        String gewinner;
        if (!(spielzuege[0].spielobjekt == spielzuege[1].spielobjekt)) { //"Wenn beide Spieler NICHT das selbe Spielobjekt haben"
            int i = 0; //i-te Zeile von den Spielregeln
            //Relevante Spielregel suchen:
            while (!(spielregeln[i][0] == spielzuege[0].spielobjekt && spielregeln[i][1] == spielzuege[1].spielobjekt)) {i++;}

            //"Wenn (laut Spielregeln) Spieler 1 das gewinnende Spielobjekt hat"
            if (spielregeln[i][2] == spielzuege[0].spielobjekt) {
                gewinner = spielzuege[0].spieler.getName();

            } else {
                //"sonst gewinnt Spieler 2"
                gewinner = spielzuege[1].spieler.getName();}

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
