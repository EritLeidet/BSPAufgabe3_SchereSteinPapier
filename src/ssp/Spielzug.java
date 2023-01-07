package ssp;

public class Spielzug {
    public final Thread spieler;
    public final Spielobjekt spielobjekt;

    public Spielzug(Thread spieler, Spielobjekt spielobjekt) {
        this.spieler = spieler;
        this.spielobjekt = spielobjekt;
    }

    @Override
    public String toString() {
        return String.format("(%s:%s)", spieler.getName(), spielobjekt.toString());
    }
}
