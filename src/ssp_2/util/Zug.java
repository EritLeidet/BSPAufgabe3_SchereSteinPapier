package ssp_2.util;

import ssp_2.util.Geste;

public class Zug {
    public final Thread player;
    public final Geste geste;

    public Zug(Thread player, Geste geste) {
        this.player = player;
        this.geste = geste;
    }

    @Override
    public String toString() {
        return String.format("(%s:%s)", player.getName(), geste.toString());
    }
}
