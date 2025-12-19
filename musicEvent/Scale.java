package musicEvent;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Crea una scala musicale in base ai tasti bianchi e neri del pianoforte. Per
 * ogni tasto stabilisce se il tasto nero successivo è il tasto precedente più
 * semitono o quello successivo meno un semitono.
 */
public class Scale {

	
	private Note[] fullChromaticScale = new Note[128];
	/*
	 * L'uso dei diesis e bemolli nella scala maggiore o minore. Stabilisce, per
	 * esempio, se in do maggiore la nota dopo il do è un do# o un reb. Gli 0
	 * rappresentano le note della scala diatonica.
	 */
	private int[] intervalsMajorScale = { 0, -1, 0, -1, 0, 0, 1, 0, -1, 0, -1,
			0 };

	private int[] intervalMinorScale = { 0, -1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1 };
	private Note tonic;
	private Modus modus;
	
	public Scale(Note tonic, Modus modus) {
		this.tonic = tonic;
		this.modus = modus;
		createFullChromaticScale();
	}

	public Note getTonic() {
		return tonic;
	}
	
	public Modus getModus() {
		return modus;
	}
	
	public Note getNote(int midi) {
		return fullChromaticScale[midi];
	}

	public ArrayList<Note> getDiatonicScale() {
		ArrayList<Note> tones = new ArrayList<>();
		int[] majorIntervals = new int[]{2, 2, 1, 2, 2, 2, 1};
		int[] minorIntervals = new int[]{2, 1, 2, 2, 1, 2, 2};
		int[] intervals = null;
		if (modus == Modus.MAJOR_SCALE) intervals = majorIntervals;
		else if (modus == Modus.MINOR_SCALE) intervals = minorIntervals;
		KeyAlteration ka = new KeyAlteration();
		ArrayList<Note> keyAlt = ka.getAlteredNotes(tonic, modus);
		tones.add(tonic);
		
		/*
		 * Il modo migliore per riempire la scala è
		 * - aggiungere la tonica
		 * - riempire da quel punto fino alla fine della scala (midi 128)
		 * - rimepire da quel punto a ritroso fino a 0
		 * 
		 */
		int i=0;
		int midi = tonic.getMidiNumber();
		while (midi < 128) {
			int arrayIntervall = ArraysUtils.getElementAt(intervals, i++);
			midi += arrayIntervall;
			boolean addedFromKey = false;
			
			for (Note keyNote: keyAlt) {
				if (keyNote.getMidiNumber() == midi%12) {
					tones.add(new Note(midi, keyNote.getAlteration()));
	     				addedFromKey = true;
					break;
				}
			}
			if (!addedFromKey) tones.add(new Note(midi, 0));
		}
		
		i=intervals.length-1;
		midi = tonic.getMidiNumber();
		while (midi > 0) {
			int arrayIntervall = ArraysUtils.getElementAt(intervals, i--);
			if (i<0) i = intervals.length-1;
			midi -= arrayIntervall;
			boolean addedFromKey = false;
			
			for (Note keyNote: keyAlt) {
				if (keyNote.getMidiNumber() == midi%12) {
					tones.add(new Note(midi, keyNote.getAlteration()));
	     				addedFromKey = true;
					break;
				}
			}
			if (!addedFromKey) tones.add(new Note(midi, 0));
		}
		Collections.sort(tones);
		return tones;
	}
	
	private void createFullChromaticScale() {

		int alterationType = ScaleType.getScaleAlterationType(tonic, modus);
		ArrayList<Integer> keyAlteration = getKeyAlteration(tonic, modus);

		// la tonica più bassa
		int lowestTonic = tonic.getMidiNumber() % 12;
		int[] intervals = null;
		if (modus == Modus.MAJOR_SCALE)
			intervals = intervalsMajorScale;
		else if (modus == Modus.MINOR_SCALE)
			intervals = intervalMinorScale;

		/*
		 * Un iteratore che scorre l'array con gli intervalli della scala. Il
		 * puntatore parte dalla posizione corrispondente alla nota 0. Se voglio
		 * una scala di si maggiore, lowestTonic sarà 11, e il puntatore partirà
		 * dalla posizione 1.
		 */
		int arrayIterator = 12 - lowestTonic;
		for (int i = 0; i < 128; i++) {

			/*
			 * Controlla se il numero nell'array degli intervalli è 0. Se è 0,
			 * vuol dire che la nota è una nota della scala e viene inserita,
			 * altrimenti viene inserito null.
			 */
			int interval = intervals[arrayIterator++ % intervals.length];
			if (interval == 0)
				insertDiatonicNote(keyAlteration, alterationType, i);
		}

		arrayIterator = 12 - lowestTonic;

		/*
		 * Sostituisce tutti i posti vuoti (null) con le note cromatiche secondo
		 * l'array intervals
		 */
		for (int i = 0; i < 128; i++) {
			if (fullChromaticScale[i] == null) {
				// interval è necessariamente o -1 o 1
				int interval = intervals[arrayIterator % intervals.length];
				insertChromaticNote(interval, i);
			}
			arrayIterator++;
		}

	}

	/**
	 * Inserisce una nuova nota in <i>fullChromaticScale</i>
	 * 
	 * @param keyAlteration
	 *            L'arrayList con le alterazioni di chiave
	 * @param alterationType
	 *            il tipo di alterazione: <i>SHARP</i> o <i>FLAT</i>
	 * @param noteNumber
	 *            il numero della nota nella scala
	 */
	private void insertDiatonicNote(ArrayList<Integer> keyAlteration,
			int alterationType, int noteNumber) {

		Note nn = null;

		/*
		 * Se la nota è compresa nelle alterazioni di chiave, viene inserita con
		 * alterazioni.
		 */
		if (contains(keyAlteration, noteNumber % 12)) {
			nn = new Note(noteNumber, alterationType);
		} else {
			nn = new Note(noteNumber, 0);
		}
		fullChromaticScale[noteNumber] = nn;
	}

	/**
	 * Inserisce una nuova nota in <i>fullChromaticScale</i>
	 * 
	 * @param keyAlteration
	 *            L'arrayList con le alterazioni di chiave
	 * @param alterationType
	 *            il tipo di alterazione: <i>SHARP</i> o <i>FLAT</i>
	 * @param noteNumber
	 *            il numero della nota nella scala
	 */
	private void insertChromaticNote(int interval, int noteNumber) {

		Note nn = null;

		/*
		 * interval può solo essere -1 o 1. Se è 1 la nota precedente a
		 * noteNumber viene innalzata. Se -1 quella seguente abbassata. Così in
		 * do maggiore, la posizione 6 dell'array interval corrisponde a 1. Vuol
		 * dire che la nota 6 (fa#) è und fa innalzato di 1
		 */
		if (interval == -1) interval = -1 + fullChromaticScale[(noteNumber+1)%128].getAlteration();
		if (interval == 1 && noteNumber > 0) interval = 1 + fullChromaticScale[noteNumber-1].getAlteration();
		//TODO se l'intervallo è -1 prende l'alterazione della nota successiva -1
		// FIXME non funziona ancora. In do e re si, ma in reb no. La nota 11 è null
		nn = new Note(noteNumber, interval);
		fullChromaticScale[noteNumber] = nn;
	}


	private boolean contains(ArrayList<Integer> l, int i) {
		for (int x = 0; x < l.size(); x++) {
			if (l.get(x) == i)
				return true;
		}
		return false;
	}

	private ArrayList<Integer> getKeyAlteration(Note tonic, Modus modus) {
		KeyAlteration ka = new KeyAlteration();
		return ka.getAlteredNoteNumbers(tonic, modus);
	}

	
	
}
