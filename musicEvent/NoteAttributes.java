package musicEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * Una classe rappresentate un evento musicale che può essere o una nota
 * o un accordo.
 * @author eugenio
 *
 */
public abstract class NoteAttributes extends MusicEvent {

	protected int midiNumber = -1;
	protected int alteration = 0;
	public static final int BEGIN = 0;
	public static final int END = 1;
	private int slurType = -1;
	private boolean tied;
	protected HashMap<Integer, String> mapNoteName;
	
	public NoteAttributes() {
		super();
		loadNoteName("DE");
	}

	private void loadNoteName(String lang) {
		if (mapNoteName == null) {
			mapNoteName = new HashMap<Integer, String>();
		}
		Properties prop = new Properties();
		InputStream input = null;
	 
		try {
	 
			input = this.getClass().getResourceAsStream("language");
	 
			// load a properties file
			prop.load(input);	
			String n = prop.getProperty(lang);
			String[] notes = n.split(",");
			mapNoteName.put(0, notes[0]);
			mapNoteName.put(2, notes[1]);
			mapNoteName.put(4, notes[2]);
			mapNoteName.put(5, notes[3]);
			mapNoteName.put(7, notes[4]);
			mapNoteName.put(9, notes[5]);
			mapNoteName.put(11, notes[6]);
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 
		
	}


	/**
	 * @return -1 se il numero MIDI non è stato settato.
	 */
	public int getMidiNumber() {
		return midiNumber;
	}

	public int getOctave() {
		return midiNumber / 12;
	}

	public void setAlteration(int alteration) {
		this.alteration = alteration;
	}

	public int getAlteration() {
		return alteration;
	}

	public void addFlat() {
		if (alteration <= -2)
			return;
		alteration--;
		midiNumber--;
	}

	public void addSharp() {
		if (alteration >= 2)
			return;
		alteration++;
		midiNumber++;
	}

	public void setTie(boolean tied) {
		this.tied = tied;
	}
	
	public boolean isTied() {
		return tied;
	}
	/**
	 * Crea una legatura di valore per l'evento del tipo
	 * scelto tra NoteEvent.BEGIN
	 * @param type
	 */
	public void setSlur(int type) {
		this.slurType = type;
	}
	
	public int getSlur() {
		return slurType;
	}
}
