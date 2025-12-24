package notation;

import musicEvent.Modus;
import musicInterface.MusicObject;

public class KeySignature extends MusicObject {

	private int numberOfAlterations;
	private int typeOfAlterations;
	private Modus modus = Modus.MAJOR_SCALE;

	public KeySignature(int numberOfAlterations, int typeOfAlterations, Modus modus) {
		this.numberOfAlterations = numberOfAlterations;
		this.typeOfAlterations = typeOfAlterations;
		this.modus = modus;
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
