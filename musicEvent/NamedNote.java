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

	public NamedNote(int midi, int alteration) {
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

	public NamedNote(int midi, int alteration, int duration) {
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
		if (midiNumber%12 == 0 && alteration ==1) name = mapNoteName.get(11);
		else if (midiNumber%12 == 11 && alteration == -1) name = mapNoteName.get(0);
		else name = mapNoteName.get(((midiNumber % 12) - alteration)%12);
		/*
		 * Qui sopra c'è due volte un %12, perché
		 * [70, -2] (cbb) ritornava 12, che è
		 * fuori dai valori della map
		 */
		if (name == null) return null;
		String alt = "";
		switch (getAlteration()) {
		case -2:
			alt = alterations[0];
			break;
		case -1:
			alt = alterations[1];
			break;
		case 1:
			alt = alterations[2];
			break;
		case 2:
			alt = alterations[3];
			break;
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
