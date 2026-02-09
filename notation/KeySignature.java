package notation;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import musicEvent.Alteration;
import musicEvent.Modus;
import musicEvent.Note;
import musicInterface.MusicObject;

public class KeySignature extends MusicObject {

    private int numberOfAlterations;
    private int typeOfAlterations; // 1=SHARP, -1=FLAT
    private Modus modus;
    private Map<Integer, Alteration> alteredNotesMap; // midiClass -> Alteration

    private static final int[] MAJOR_SHARPS = { 0, 7, 2, 9, 4, 11, 6, 13 };
    private static final int[] MAJOR_FLATS = { 0, 5, 10, 3, 8, 1, 6, 11 };
    private static final int[] MINOR_SHARPS = { 0, 9, 4, 11, 6, 1, 8, 3 };
    private static final int[] MINOR_FLATS = { 5, 2, 6, 3, 7, 4, 8, 5, 9 };

    private static final int[] SHARP_SEQUENCE = { 1, 5, 2, 6, 3, 7, 4 };
    private static final int[] FLAT_SEQUENCE = { 4, 0, 3, 6, 2, 5, 1 };

    public KeySignature(int numberOfAlterations, int typeOfAlterations, Modus modus) {
        this.numberOfAlterations = numberOfAlterations;
        this.typeOfAlterations = typeOfAlterations;
        this.modus = modus;
        alteredNotesMap = new HashMap<>();
        init();
    }

    private void init() {
        int tonic = getTonicMidi(numberOfAlterations, typeOfAlterations, modus);
        alteredNotesMap = buildScale(tonic); // buildScale deve restituire Map<Integer, Alteration>
        buildChromaticScale(alteredNotesMap);
    }

    public Alteration getAlteration(Note n) {
        return getAlteration(n.getMidiNumber());
    }

    public Alteration getAlteration(int midi) {
        return alteredNotesMap.get(midi % 12); // restituisce null se la nota non ha alterazione scritta
    }

	/**
	 * Restituisce il numero MIDI della tonica di una scala maggiore o minore nella
	 * quarta ottava, dato il numero e il tipo di alterazioni.
	 * 
	 * @param numberOfAlterations il numero di alterazioni (diesis o bemolle)
	 *                            presenti nella tonalità (0 = nessuna, 1 = una,
	 *                            ecc.)
	 * @param typeOfAlterations   tipo di alterazioni: 1 = diesis (#), -1 = bemolle
	 *                            (b), 0 = nessuna
	 * @param modus               il tipo di scala: Modus.MAJOR_SCALE o
	 *                            Modus.MINOR_SCALE
	 * @return il numero MIDI della tonica più basso possibile (C = 0)
	 * 
	 *         Esempi:
	 * 
	 *         <pre>
	 * getTonicMidi(0, 0, Modus.MAJOR_SCALE) = 0  // C Major
	 * getTonicMidi(1, 1, Modus.MAJOR_SCALE) = 7  // G Major
	 * getTonicMidi(0, 0, Modus.MINOR_SCALE) = 9  // A minor
	 *         </pre>
	 */
	private int getTonicMidi(int numberOfAlterations, int typeOfAlterations, Modus modus) {
		int tonic = 0;

		if (typeOfAlterations == 1) { // diesis
			tonic = (modus == Modus.MAJOR_SCALE) ? MAJOR_SHARPS[numberOfAlterations]
					: MINOR_SHARPS[numberOfAlterations];
		} else if (typeOfAlterations == -1) { // bemolle
			tonic = (modus == Modus.MAJOR_SCALE) ? MAJOR_FLATS[numberOfAlterations] : MINOR_FLATS[numberOfAlterations];
		} else { // nessuna alterazione
			tonic = (modus == Modus.MAJOR_SCALE) ? 0 : 9; // C major o A minor
		}

		return  tonic; // tonic in semitoni sopra C
	}

	// getter
	public int getNumberOfAlterations() {
		return numberOfAlterations;
	}

	public int getTypeOfAlterations() {
		return typeOfAlterations;
	}

	public Modus getModus() {
		return modus;
	}
	
	/**
	 * Dice se la nota midi passata come argomento corrisponde a un tasto bianco del pianoforte
	 * @param midi
	 * @return
	 */
	private boolean isWhiteKey(int midi) {
	    int pc = midi % 12;
	    return pc == 0 || pc == 2 || pc == 4 || pc == 5 || pc == 7 || pc == 9 || pc == 11;
	}

	private Map<Integer, Alteration> buildScale(int tonic) {

	    Map<Integer, Alteration> map = new LinkedHashMap<>();

	    for (int i = 0; i < 12; i++)
	        map.put((i + tonic) % 12, null);

	    if (!isWhiteKey(tonic))
	        map.put(tonic % 12, new Alteration(typeOfAlterations));

	    int[] halfTones = (modus == Modus.MAJOR_SCALE) ?
	            new int[] { 2, 2, 1, 2, 2, 2, 1 } :
	            new int[] { 2, 1, 2, 2, 1, 2, 2 };

	    int[] alterationsAdded = null;
	    int[] alterationsAdded_major_sharp = {6, 2, 5, 1, 4, 0, 3};
	    int[] alterationsAdded_major_flat  = {3, 0, 4, 1, 5, 2, 6};
	    int[] alterationsAdded_minor_sharp = {1, 4, 0, 3, 6, 2, 5};
	    int[] alterationsAdded_minor_flat  = {5, 2, 6, 3, 0, 4, 1};

	    if (modus == Modus.MAJOR_SCALE && typeOfAlterations == 1)
	        alterationsAdded = alterationsAdded_major_sharp;
	    else if (modus == Modus.MAJOR_SCALE && typeOfAlterations == -1)
	        alterationsAdded = alterationsAdded_major_flat;
	    else if (modus == Modus.MINOR_SCALE && typeOfAlterations == 1)
	        alterationsAdded = alterationsAdded_minor_sharp;
	    else if (modus == Modus.MINOR_SCALE && typeOfAlterations == -1)
	        alterationsAdded = alterationsAdded_minor_flat;

	    Alteration[] scaleAlterations = new Alteration[7];
	    for (int i = 0; i < 7; i++) scaleAlterations[i] = new Alteration(0);

	    for (int i = 0; i < numberOfAlterations; i++) {
	        int pos = alterationsAdded[i];
	        scaleAlterations[pos] = new Alteration(typeOfAlterations);
	    }

	    int midiN = tonic;
	    for (int i = 0; i < halfTones.length; i++) {
	        map.put(midiN % 12, scaleAlterations[i]);
	        midiN += halfTones[i];
	    }

	    return buildChromaticScale(map);
	}

	private Map<Integer, Alteration> buildChromaticScale(Map<Integer, Alteration> diatonicScale) {
	    int[] chromaticMajorScale = {-1, -1, 1, -1, 1};
	    int[] chromaticMinorScale = {-1, 1, 1, 1, 1};
	    int[] chromatic = (modus == Modus.MAJOR_SCALE) ? chromaticMajorScale : chromaticMinorScale;

	    int i = 0;
	    for (Integer key : diatonicScale.keySet()) {
	        if (diatonicScale.get(key) != null) continue;
	        diatonicScale.put(key, new Alteration(chromatic[i++]));
	    }

	    return diatonicScale;
	}
	
	public int getAlterationForStaffPosition(Note n) {
		int pos = Math.floorMod(n.getStaffPosition(), 7);
	    int[] sequence = typeOfAlterations == 1 ? SHARP_SEQUENCE : FLAT_SEQUENCE;

	    for (int i = 0; i < numberOfAlterations; i++) {
	        if (pos == sequence[i]) {
	            return typeOfAlterations; // 1 per sharp, -1 per flat
	        }
	    }
	    return 0; // naturale
	}
	
	public boolean requiresAccidental(Note n) {
	    return getAlterationForStaffPosition(n) != 0;
	}
	
	private void printMap(Map<Integer, Integer> map) {
	    System.out.print("{");
	    boolean first = true;
	    for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
	    	 if (entry.getValue() == null) continue;
		        if (!first) System.out.print(",\t");
		        System.out.print(entry.getKey() + "=" + entry.getValue());
	        first = false;
	    }
	    System.out.println("}");
	}
}