package musicEvent;

import java.util.HashMap;

import Utilities.ReadProperties;

public class NamedNote extends Note {

	private static HashMap<Integer, String> mapNoteName;
	private static String[] exception;
	private static String[] alterations;
	private static String language = "DE";

	public NamedNote(int midi) {
		super(midi);
	}

	public NamedNote(int midi, Alteration alteration) {
		super(midi, alteration);
		if (mapNoteName == null) {
			mapNoteName = new HashMap<Integer, String>();
			loadNoteName(language);
		}
		if (exception == null) {
			exception = new ReadProperties().readException(language);
		}
		if (alterations == null) {
			alterations = new ReadProperties().readAlterations(language);
		}
	}

	public NamedNote(int midi, Alteration alteration, int duration) {
		super(midi, alteration, duration);
		if (mapNoteName == null) {
			mapNoteName = new HashMap<Integer, String>();
			loadNoteName(language);
		}
		if (exception == null) {
			exception = new ReadProperties().readException(language);
		}
		if (alterations == null) {
			alterations = new ReadProperties().readAlterations(language);
		}
	}
	
	public NamedNote(Note note) {
		super(note.getMidiNumber(), note.getAlteration());
		setDuration(note.getDuration());
		setDots(note.getDots());
		if (mapNoteName == null) {
			mapNoteName = new HashMap<Integer, String>();
			loadNoteName(language);
		}
		if (exception == null) {
			exception = new ReadProperties().readException(language);
		}
		if (alterations == null) {
			alterations = new ReadProperties().readAlterations(language);
		}
	}


	private static void loadNoteName(String lang) {
		String[] notes = new ReadProperties().readNoteName(language);
		mapNoteName.put(0, notes[0]);
		mapNoteName.put(2, notes[1]);
		mapNoteName.put(4, notes[2]);
		mapNoteName.put(5, notes[3]);
		mapNoteName.put(7, notes[4]);
		mapNoteName.put(9, notes[5]);
		mapNoteName.put(11, notes[6]);
	}

	@Override
	public String toString() {
		return getName() + " [" + midiNumber + "; " + alteration + "]";
	}

	public String getInfo() {
		return " [" + midiNumber + "; " + alteration + "]";
	}
	public String getName() {
		String name = null;
		/*
		 * FIXME
		 * Le prime due righe sono perché ogni tanto usciva null.
		 * TODO controlla se puoi correggere matematicamente senza if-else
		 * Ha sicuramente a che fare col fatto che nella classe Scale
		 * in insertChromaticNote un controllo c'è che guarda che la
		 * nota sia >0 o <127. Così la nota 0 non viene calcolata o qualcosa del genere...
		 */

		// gestione casi particolari per Do♭ e Si♯
		if (midiNumber % 12 == 0 && alteration != null && alteration.getValue() == 1) {
		    name = mapNoteName.get(11); // Si
		} else if (midiNumber % 12 == 11 && alteration != null && alteration.getValue() == -1) {
		    name = mapNoteName.get(0); // Do
		} else {
		    int alterValue = (alteration != null) ? alteration.getValue() : 0;
		    int key = (midiNumber % 12 - alterValue + 12) % 12; // +12 per evitare negativi
		    name = mapNoteName.get(key);
		}

		if (name == null) return null;

		String alt = "";
		if (alteration != null) {
		    switch (alteration.getValue()) {
		        case Alteration.DOUBLE_FLAT:  alt = alterations[0]; break;
		        case Alteration.FLAT:         alt = alterations[1]; break;
		        case Alteration.SHARP:        alt = alterations[2]; break;
		        case Alteration.DOUBLE_SHARP: alt = alterations[3]; break;
		        case Alteration.NATURAL:      alt = alterations[4]; break; // opzionale, se hai ♮
		    }
		}

		name = name + alt;
		name = checkException(name);
		return name;
	}
	
	public boolean exists() {
		return getName() != null;
	}
	private String checkException(String name) {
		if (exception == null) return name;
		for (int i = 0; i < exception.length; i++) {
			String[] ex = exception[i].split("/");
			if (name.equals(ex[0])) {
				return ex[1];
			}
		}
		return name;
	}
	
	public static void setLanguage(String lang) {
		language = lang;
	}
}
