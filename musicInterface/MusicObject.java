package musicInterface;


public class MusicObject {

	private int voice = 1;
	
	/**
	 * Indica la voce alla quale l'oggetto appartiene.
	 * Per Note, Pause e Accordi può essere 1, 2 ecc.
	 * Per oggetti Staff-Wide (chiavi, stanghette di battuta, ecc.) deve essere 0
	 * @param v
	 */
	public void setVoice(int v) {
		voice = v;
	}
	
	/**
	 * Indica la voce alla quale l'oggetto appartiene.
	 * Per Note, Pause e Accordi può essere 1, 2 ecc.
	 * Per oggetti Staff-Wide (chiavi, stanghette di battuta, ecc.) deve essere 0
	 * @param v
	 */
	public int getVoice() {
		return voice;
	}
}
