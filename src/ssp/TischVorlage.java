package ssp;

public interface TischVorlage {
    public void enter(Spielzug item) throws InterruptedException;
    public Spielzug[] remove() throws InterruptedException;

}
