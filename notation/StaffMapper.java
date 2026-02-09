package notation;

import musicEvent.Alteration;
import musicEvent.Modus;
import notation.Clef;
import notation.KeySignature;
import notation.MidiPitch;

public class StaffMapper {

	private static final int[] SHARPS_ORDER = { 8, 5, 9, 6, 3, 7, 4 };
    private static final int[] FLATS_ORDER  = { 4, 7, 3, 6, 2, 5, 1 };
    
	public static int midiToStaffPosition(int pitch, Clef clef) {
		int delta = pitch - clef.getMidiOffset();
		int direction = Integer.signum(delta);

		int semitones = Math.abs(delta);
		int[] map = clef.getSemitoneMap();

		int octaveSize = 12;
		int diatonicStepsPerOctave = 7;

		int octaves = semitones / octaveSize;
		int remainder = semitones % octaveSize;

		int stepsInOctave = 0;
		while (stepsInOctave < map.length - 1 && map[stepsInOctave + 1] <= remainder) {
			stepsInOctave++;
		}

		int totalSteps = octaves * diatonicStepsPerOctave + stepsInOctave;

		return direction * totalSteps;
	}

	public static MidiPitch staffPositionToMidi(int staffPosition, Clef clef, KeySignature ks) {
	    // Se non c’è chiave di violino o basso, non possiamo calcolare il MIDI
	    if (clef == null) return null;

	    int[] scale = clef.getSemitoneMap();

	    // Posizione della nota sul pentagramma modulo 7 (0..6)
	    int notePosMod7 = Math.floorMod(staffPosition, 7);

	    // Numero di ottave da spostare (può essere negativo se sotto la riga base)
	    int octaveShift = Math.floorDiv(staffPosition, 7);

	    // Tipo di alterazioni della chiave: 1 = diesis, -1 = bemolle
	    int typeOfAlterations = ks.getTypeOfAlterations();

	    // Ordine delle alterazioni nella chiave
	    int[] keySignatureIndex = (typeOfAlterations == 1) ? SHARPS_ORDER : FLATS_ORDER;

	    // Calcola il numero MIDI di base della nota
	    int midiN = clef.getMidiOffset() + scale[notePosMod7] + (octaveShift * 12);

	    // Controlla se la nota è alterata dalla chiave
	    for (int i = 0; i < ks.getNumberOfAlterations(); i++) {
	        int keyPosMod7 = Math.floorMod(keySignatureIndex[i], 7);

	        if (keyPosMod7 == notePosMod7) {
	            // Applica l’alterazione della chiave e ritorna
	            midiN += typeOfAlterations;
	            return new MidiPitch(midiN, new Alteration(typeOfAlterations));
	        }
	    }

	    // Se la nota non è alterata dalla chiave, controlla eventuali alterazioni aggiuntive
	    Alteration alteration = ks.getAlteration(midiN);
	    return new MidiPitch(midiN, alteration);
	}


	public static void main(String[] args) {
		for (int i = -2; i < 10; i++)
			System.out.println(staffPositionToMidi(i, Clef.treble(), new KeySignature(4, -1, Modus.MINOR_SCALE)));
	}

}
