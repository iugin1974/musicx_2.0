package notation;

public interface ScoreListener {
	 /**
     * Chiamato quando il modello Score cambia.
     */
    public void scoreChanged(ScoreEvent e);
}
