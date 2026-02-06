package notation;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import musicEvent.Modus;
import musicEvent.Note;
import musicInterface.MusicObject;

public class KeySignature extends MusicObject {

	private int numberOfAlterations;
	private int typeOfAlterations; // 1=SHARP, -1=FLAT
	private Modus modus;
	private Map<Integer, Integer> alteredNotesMap; // midiClass -> alteration

	// Sequenze delle tonalità in base al numero di diesis/bemolli
	private static final int[] MAJOR_SHARPS = { 0, 7, 2, 9, 4, 11, 6, 13}; // C, G, D, A, E, B, F#
	private static final int[] MAJOR_FLATS = { 0, 5, 10, 3, 8, 1, 6, 11 }; // C, F, Bb, Eb, Ab, Db, Gb
	private static final int[] MINOR_SHARPS = { 0, 9, 4, 11, 6, 1, 8, 3 }; // A, E, B, F#, C#, G#, D#
	private static final int[] MINOR_FLATS = { 5, 2, 6, 3, 7, 4, 8, 5, 9 }; // A, D, G, C, F, Bb, Eb

	private static final int[] SHARP_SEQUENCE = { 6, 1, 8, 3, 10, 5, 0 }; // F C G D A E B
	private static final int[] FLAT_SEQUENCE = { 10, 3, 8, 1, 6, 11, 4 }; // B E A D G C F 
	public KeySignature(int numberOfAlterations, int typeOfAlterations, Modus modus) {
		this.numberOfAlterations = numberOfAlterations;
		this.typeOfAlterations = typeOfAlterations;
		this.modus = modus;
		alteredNotesMap = new HashMap<>();
		init();
	}

	private void init() {
		int tonic = getTonicMidi(numberOfAlterations, typeOfAlterations, modus);
		alteredNotesMap = buildScale(tonic);
		buildChromaticScale(alteredNotesMap);
		
	}
	
	public int getAlteration(Note n) {
		return getAlteration(n.getMidiNumber());
	}
	
	public int getAlteration(int midi) {
		return alteredNotesMap.get(midi%12);
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

	private Map<Integer,Integer> buildScale(int tonic) {

	    /*
	     * Crea una mappa che rappresenta un'ottava della scala maggiore o minore 
	     * partendo dalla tonica. La chiave è il numero MIDI della nota, il valore 
	     * è l'alterazione della nota (-1=bemolle, 0=naturale, 1=#).
	     */
	    Map<Integer, Integer> map = new LinkedHashMap<>();

	    // 1) Inizializza 12 semitoni consecutivi a partire dalla tonica, tutti null
	    for (int i = 0; i < 12; i++)
	        map.put((i + tonic)%12, null);

	    /*
	     * 2) Controlla se la tonica è un tasto nero:
	     *    Se lo è, applica l'alterazione della chiave alla tonica stessa.
	     *    Questo garantisce che la tonica abbia l'alterazione corretta.
	     */
	    if (!isWhiteKey(tonic))
	        map.put(0, typeOfAlterations);

	    /*
	     * 3) Definisce gli intervalli diatonici per la scala maggiore o minore.
	     *    Questi intervalli servono per selezionare le note della scala 
	     *    rispetto alla tonica.
	     */
	    int[] halfTones = (modus == Modus.MAJOR_SCALE) ?
	            new int[] { 2, 2, 1, 2, 2, 2, 1 } :   // maggiore
	            new int[] { 2, 1, 2, 2, 1, 2, 2 };    // minore

	    int midiN = tonic; // nota corrente in costruzione

	    /*
	     * 4) Definisce l'ordine in cui entrano le alterazioni (diesis o bemolle)
	     *    per ogni tonalità con alterazioni. Questi array indicano la posizione
	     *    nella scala dove ciascun # o b va applicato, in ordine progressivo.
	     */
	    int[] alterationsAdded_major_sharp = {6, 2, 5, 1, 4, 0, 3 };
	    int[] alterationsAdded_major_flat  = {3, 0, 4, 1, 5, 2, 6 };
	    int[] alterationsAdded_minor_sharp = {1, 4, 0, 3, 6, 2, 5 };
	    int[] alterationsAdded_minor_flat  = {5, 2, 6, 3, 0, 4, 1 };
	    int[] alterationsAdded = null;

	    // 5) Seleziona l'array corretto in base al tipo di scala e tipo di alterazioni
	    if (modus == Modus.MAJOR_SCALE && typeOfAlterations == 1)
	        alterationsAdded = alterationsAdded_major_sharp;
	    else if (modus == Modus.MAJOR_SCALE && typeOfAlterations == -1)
	        alterationsAdded = alterationsAdded_major_flat;
	    else if (modus == Modus.MINOR_SCALE && typeOfAlterations == 1)
	        alterationsAdded = alterationsAdded_minor_sharp;
	    else if (modus == Modus.MINOR_SCALE && typeOfAlterations == -1)
	        alterationsAdded = alterationsAdded_minor_flat;

	    /*
	     * 6) Costruisce un array con le alterazioni dei gradi della scala diatonica.
	     *    Inizialmente tutte le note sono naturali (0). Poi, in base al numero di
	     *    alterazioni della tonalità, vengono impostati # o b nelle posizioni corrette.
	     */
	    int[] scaleAlterations = new int[7]; // 7 gradi della scala
	    for (int i = 0; i < numberOfAlterations; i++) {
	        int pos = alterationsAdded[i];  // posizione nella scala dove inserire il diesis/bemolle
	        scaleAlterations[pos] = typeOfAlterations;
	    }

	    /*
	     * 7) Assegna le alterazioni alla mappa delle note.
	     *    Scorre i gradi della scala diatonica usando gli intervalli halfTones e
	     *    assegna a ciascuna nota l'alterazione calcolata precedentemente.
	     */
	    for (int i = 0; i < halfTones.length; i++) {
	        map.put(midiN%12, scaleAlterations[i]);
	        midiN += halfTones[i]; // passa alla nota successiva nella scala
	    }

	    // 8) Ritorna la mappa completa di un'ottava con le note diatoniche e le alterazioni
	    return map;
	}

	private Map<Integer,Integer> buildChromaticScale(Map<Integer,Integer> diatonicScale) {
		int[] chromaticMajorScale = {-1, -1, 1, -1, 1};
		int[] chromaticMinorScale = {-1, 1, 1, 1, 1};
		int[] chromatic = (modus.equals(Modus.MAJOR_SCALE)) ?
				chromaticMajorScale : chromaticMinorScale;
		int i = 0;
		for (Integer key : diatonicScale.keySet()) {
			if (diatonicScale.get(key) != null) continue;
			diatonicScale.put(key,chromatic[i++]);
		}
		
		
		return diatonicScale;
	}
	
	public boolean requiresAccidental(int midi, int alt) {
	    midi = midi % 12;
	    int[] sequence = typeOfAlterations == 1 ? SHARP_SEQUENCE : FLAT_SEQUENCE;

	    for (int i = 0; i < numberOfAlterations; i++) {
	        int alteredNote = sequence[i]; // nota alterata dalla tonalità
	        int naturalNote = (alteredNote - typeOfAlterations + 12) % 12; // nota naturale corrispondente

	        // Caso 1: la nota è quella alterata dalla tonalità con la stessa alterazione
	        if (midi == alteredNote && alt == typeOfAlterations) {
	            return false; // nessun simbolo
	        }

	        // Caso 2: la nota è quella alterata ma con alterazione diversa (incluso naturale)
	        if (midi == alteredNote && alt != typeOfAlterations) {
	            return true; // serve simbolo
	        }

	        // Caso 3: la nota è la naturale corrispondente alla nota alterata (es. Do naturale in C#)
	        if (midi == naturalNote && alt == 0) {
	            return true; // serve bequadro
	        }
	    }

	    // Caso 4: nota non alterata dalla tonalità
	    return alt != 0; // serve simbolo solo se non naturale
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