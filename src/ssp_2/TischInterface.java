package ssp_2;

import ssp_2.util.Zug;

public interface TischInterface {
    public void enter(Zug z) throws InterruptedException;
    public Zug[] remove() throws InterruptedException;
}
