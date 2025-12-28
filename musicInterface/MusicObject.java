package musicInterface;

import notation.Clef;

public class MusicObject {

	private int voiceIndex = -1;
	private int staffIndex = -1;
	
	
	/**
	 * Posizione temporale dell'oggetto all'interno dello staff.
	 * <p>
	 * Il valore {@code tick} rappresenta una coordinata temporale ordinabile,
	 * usata dall'editor per stabilire l'ordine orizzontale degli oggetti
	 * (note, pause, chiavi, armature, ecc.).
	 * </p>
	 * <p>
	 * Non è necessariamente legata a una durata musicale assoluta
	 * (battute, quarti, MIDI ticks) e può essere derivata dalla posizione
	 * grafica orizzontale durante l'inserimento o lo spostamento degli oggetti.
	 * </p>
	 * <p>
	 * Oggetti con {@code tick} minore sono considerati precedenti nel tempo.
	 * </p>
	 * <p>
	 * Questo campo serve come ponte tra rappresentazione grafica e modello
	 * musicale in un editor WYSIWYG libero.
	 * </p>
	 */
	protected int tick;

	public void setTick(int t) {
		tick = t;
	}
	
	public int getTick() {
		return tick;
	}
	
	/**
	 * Indica la voce alla quale l'oggetto appartiene.
	 * Per Note, Pause e Accordi può essere 1, 2 ecc.
	 * Per oggetti Staff-Wide (chiavi, stanghette di battuta, ecc.) deve essere 0
	 * @param v
	 */
	public void setVoiceIndex(int v) {
		voiceIndex = v;
	}
	
	/**
	 * Indica la voce alla quale l'oggetto appartiene.
	 * Per Note, Pause e Accordi può essere 1, 2 ecc.
	 * Per oggetti Staff-Wide (chiavi, stanghette di battuta, ecc.) deve essere 0
	 * @param v
	 */
	public int getVoiceIndex() {
		return voiceIndex;
	}
	
	  /**
     * Confronto musicale/logico tra oggetti.
     * NON riguarda l'identità dell'evento nello score.
     */
    public boolean musicallyEquals(MusicObject other) {
        return this == other;
    }
    
    public void setStaff(int staffIndex) {
    	this.staffIndex = staffIndex;
    }
    
    public int getStaffIndex() {
    	return staffIndex;
    }
}
