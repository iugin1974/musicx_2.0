package notation;

import musicEvent.Modus;
import musicInterface.MusicObject;

public class KeySignature extends MusicObject {

	private int numberOfAlterations;
	private int typeOfAlterations;
	private Modus modus = Modus.MAJOR_SCALE;

	private static final int[] SHARPS_ORDER = { 8, 5, 9, 6, 3, 7, 4 };
    private static final int[] FLATS_ORDER  = { 4, 7, 3, 6, 2, 5, 1 };
    
    public KeySignature(int numberOfAlterations, int typeOfAlterations, Modus modus) {
		this.numberOfAlterations = numberOfAlterations;
		this.typeOfAlterations = typeOfAlterations;
		this.modus = modus;
	}
    
    public int[] getSharpsIndex() {
        return SHARPS_ORDER;
    }

    public int[] getFlatsIndex() {
        return FLATS_ORDER;
    }

	public int getNumberOfAlterations() {
		return numberOfAlterations;
	}

	public int getTypeOfAlterations() {
		return typeOfAlterations;
	}

	public Modus getModus() {
		return modus;
	}
}
