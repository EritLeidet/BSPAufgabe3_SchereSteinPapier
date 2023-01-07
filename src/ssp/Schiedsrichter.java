package ssp;
import java.util.Arrays;

import static ssp.Spielobjekt.*;

public class Schiedsrichter extends Thread{ //VERBRAUCHER

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
                System.out.println(getName() + " ist fertig.");
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
        System.out.printf("%s%n%s gewinnt.%n", Arrays.toString(spielzuege), gewinner);
    }

}
