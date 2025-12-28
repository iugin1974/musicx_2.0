package notation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import Measure.Bar;
import Measure.Partial;
import Measure.TimeSignature;
import musicEvent.MusicEvent;
import musicEvent.Note;
import musicEvent.NoteEvent;
import musicEvent.Rest;
import musicInterface.MusicObject;
import notation.ScoreEvent.Type;

public class Score implements Serializable, Iterable<Staff> {

	private ArrayList<ScoreListener> listeners;
	private ArrayList<Staff> staffList;
	private TimeSignature time;
	private Partial partial;

	public Score() {
		staffList = new ArrayList<>();
		listeners = new ArrayList<>();
	}

	public Staff getStaff(int n) {
		return staffList.get(n);
	}

	/** crea uno staff */
	public void addStaff() {
		Staff s = new Staff();
		staffList.add(s);
		ScoreEvent e = new ScoreEvent(ScoreEvent.Type.STAFF_ADDED, s, staffList.size() - 1);
		fireScoreEvent(e);
	}

	public void setTimeSignature(TimeSignature time) {
		this.time = time;
		for (Staff s : staffList) {
			s.setTimeSignature(time);
		}
	}

	public void setPartial(Partial p) {
		this.partial = p;
		for (Staff s : staffList) {
			s.setPartial(p);
		}
	}

	public Partial getPartial() {
		return partial;
	}

	/**
	 * @return il tempo in chiave
	 */
	public TimeSignature getTimeSignature() {
		return time;
	}

	public int getStanzasNumber(int staffIndex, int voiceIndex) {
		Voice v = getStaff(staffIndex).getVoice(voiceIndex);
		int s = 0;
		for (NoteEvent g : v.getNotes()) {
			if (g.getNumberOfStanzas() > s) {
				s = g.getNumberOfStanzas();
			}
		}
		return s;
	}

	/** Aggiunge un oggetto allo staff e alla voce indicata */
	public void addObject(MusicObject obj, int staffIndex, int voiceIndex) {
		// se non ci sono abbastanza voci per lo staff, vengono create
		Staff s = staffList.get(staffIndex);
		int v = s.getNumberOfVoices();
		for (int i = v; i <= voiceIndex; i++) {
			s.addVoice();
		}
		if (obj instanceof MusicEvent)
			obj.setVoiceIndex(voiceIndex);
		else
			obj.setVoiceIndex(0);
		obj.setStaff(staffIndex);
		s.getVoice(voiceIndex).addObject(obj);
		ScoreEvent e = null;
		if (obj instanceof Note) {
			e = new ScoreEvent(ScoreEvent.Type.NOTE_ADDED, (Note) obj, staffIndex, voiceIndex);
		} else if (obj instanceof Rest) {
			e = new ScoreEvent(ScoreEvent.Type.REST_ADDED, (Rest) obj, staffIndex, voiceIndex);
		} else if (obj instanceof Bar) {
			e = new ScoreEvent(ScoreEvent.Type.BARLINE_ADDED, (Bar) obj, staffIndex, 0);
		} else if (obj instanceof Clef) {
			e = new ScoreEvent(ScoreEvent.Type.CLEF_ADDED, (Clef) obj, staffIndex, 0);
		}

		fireScoreEvent(e);
	}

	/** Restituisce la lista degli oggetti di uno staff e voce specifici */
	public List<MusicObject> getObjects(int staffNumber, int voiceNumber) {
		if (staffNumber < 0 || staffNumber >= staffList.size()) {
			return null;
		}
		return staffList.get(staffNumber).getVoice(voiceNumber).getObjects();
	}

	/** Restituisce la lista degli oggetti di uno staff e voce specifici */
	public List<MusicObject> getObjects(Staff staff, int voiceNumber) {
		return staff.getVoice(voiceNumber).getObjects();
	}

	/**
	 * Restituisce tutti gli oggetti di uno staff, di tutti i layer. Restituisce una
	 * lista vuota se lo staffNumber non è valido.
	 */
	public List<MusicObject> getObjects(int staffNumber) {
		if (staffNumber < 0 || staffNumber >= staffList.size()) {
			return List.of(); // lista immutabile vuota
		}

		Staff staff = staffList.get(staffNumber);
		List<MusicObject> all = new ArrayList<>();

		for (Voice layer : staff.getVoices()) {
			all.addAll(layer.getObjects());
		}

		return all;
	}

	public List<MusicObject> getStaffWideObjects(int staffNumber) {
		Staff staff = getStaff(staffNumber);
		return staff.getObjects(0);
	}

	public List<MusicObject> getStaffWideObjects(Staff staff) {
		return staff.getObjects(0);
	}

	public int getNumberOfVoices(Staff s) {
		return s.getNumberOfVoices();
	}

	public int getNumberOfVoices(int staffIndex) {
		Staff s = getStaff(staffIndex);
		return s.getNumberOfVoices();
	}

	/**
	 * Restituisce tutte le note di uno staff specifico e di una voce specifica. Per
	 * il layer STAFF_WIDE ritorna una lista vuota.
	 */
	public List<NoteEvent> getNotes(int staffNumber, int voiceNumber) {

		// controlli di sicurezza
		if (staffNumber < 0 || staffNumber >= staffList.size())
			return List.of(); // lista vuota immutabile

		Staff staff = staffList.get(staffNumber);

		Voice layer = staff.getVoice(voiceNumber);
		if (layer == null)
			return List.of();

		// STAFF_WIDE non contiene note → ritorna lista vuota
		if (layer.getVoiceType() == 0)
			return List.of();

		List<NoteEvent> notes = new ArrayList<>();

		for (MusicObject o : layer.getObjects()) {
			if (o instanceof NoteEvent note) {
				notes.add(note);
			}
		}

		return notes;
	}

	/** Restituisce lo staffList completo */
	public List<Staff> getAllStaves() {
		return staffList;
	}

	/** restituisce una lista con tutti gli oggetti di tutti gli staves */
	public List<MusicObject> getAllObjects() {
		List<MusicObject> all = new ArrayList<>();
		for (Staff staff : staffList) {
			for (Voice v : staff.getVoices()) {
				all.addAll(v.getObjects());
			}
		}
		return all;
	}

	/**
	 * Rimuove un oggetto dalla score. Se l'azione ha avuto successo viene lanciato
	 * uno ScoreEvent e ritorna true.
	 * 
	 * @param obj
	 * @return
	 */
	public boolean removeObject(MusicObject obj) {
		if (obj == null)
			return false;

		for (int s = 0; s < staffList.size(); s++) {
			Staff staff = staffList.get(s);
			int voiceIndex = staff.removeObject(obj);

			if (voiceIndex != -1) {
				ScoreEvent ev = new ScoreEvent(ScoreEvent.Type.OBJECT_REMOVED, obj, s, voiceIndex);
				fireScoreEvent(ev); // o come gestisci gli eventi
				return true;
			}
		}
		return false;
	}

	/** Rimuove tutti gli oggetti da uno staff */
	public void clearVoice(int staffNumber, int voiceNumber) {
		if (staffNumber >= 0 && staffNumber < staffList.size()) {
			staffList.get(staffNumber).clearVoice(voiceNumber);
		}
	}

	/** Numero di staff presenti */
	public int getStaffCount() {
		return staffList.size();
	}

	/**
	 * Restituisce la nota successiva nella stessa voce dello stesso staff, oppure
	 * null se è l'ultima.
	 */
	public NoteEvent getNextNote(NoteEvent note) {
		if (note == null)
			return null;

		Voice layer = getVoiceOf(note);
		if (layer == null)
			return null;

		List<MusicObject> objs = layer.getObjects();
		int index = objs.indexOf(note);

		for (int i = index + 1; i < objs.size(); i++) {
			if (objs.get(i) instanceof NoteEvent next)
				return next;
		}

		return null;
	}

	/**
	 * Restituisce la nota precedente nella stessa voce dello stesso staff, oppure
	 * null se è la prima.
	 */
	public NoteEvent getPrevNote(NoteEvent note) {
		if (note == null)
			return null;

		Voice layer = getVoiceOf(note);
		if (layer == null)
			return null;

		List<MusicObject> objs = layer.getObjects();
		int index = objs.indexOf(note);

		for (int i = index - 1; i >= 0; i--) {
			if (objs.get(i) instanceof NoteEvent prev)
				return prev;
		}

		return null;
	}

	/** Controlla se n1 e n2 sono consecutive **/
	public boolean areNotesConsecutive(NoteEvent n1, NoteEvent n2) {
		return getNextNote(n1) == n2;
	}

	/** Restituisce la VoiceLayer che contiene la nota, oppure null. */
	public Voice getVoiceOf(MusicObject startNote) {
		if (startNote == null)
			return null;

		for (Staff staff : staffList) {
			for (Voice layer : staff.getVoices()) {
				if (layer.getVoiceType() == 0)
					continue;

				if (layer.getObjects().contains(startNote)) {
					return layer;
				}
			}
		}
		return null;
	}

	public void removeLyrics(int staffIndex, int voiceNumber, int stanza) {
	    Staff s = getStaff(staffIndex);
	    Voice v = s.getVoice(voiceNumber);
	    for (NoteEvent n : v.getNotes()) {
	        n.removeLyric(stanza); // rimuove la lyric dalla nota
	    }
	}

	public void addLyrics(List<String> syllables, int staffIndex, int voiceNumber, int stanza) {
	    Staff s = getStaff(staffIndex);
	    if (voiceNumber < 0 || voiceNumber >= s.getVoices().size()) {
	        System.out.println("Voce selezionata non valida.");
	        return;
	    }

	    if (stanza < 0 || stanza >= 10) { // massimo 10 strofe
	        System.out.println("Stanza selezionata non valida.");
	        return;
	    }

	    // Rimuove eventuali vecchie lyrics della stanza
	    removeLyrics(staffIndex, voiceNumber, stanza);

	    Voice v = s.getVoice(voiceNumber);
	    List<NoteEvent> notes = v.getNotes();

	    int syllableIndex = 0;
	    int noteIndex = 0;
	    boolean connected = false;

	    while (syllableIndex < syllables.size() && noteIndex < notes.size()) {
	        NoteEvent note = notes.get(noteIndex);
	        String syllable = syllables.get(syllableIndex);

	        // Sillabe speciali
	        if ("_".equals(syllable)) {
	            syllableIndex++;
	            noteIndex++;
	            continue;
	        }
	        if ("--".equals(syllable) || "__".equals(syllable)) {
	            syllableIndex++;
	            continue;
	        }

	        // Assegna lyric se applicabile
	        if (shouldAssignLyric(note, connected)) {
	            Syllable syl = new Syllable(syllable);
	            new Lyric(syl, note, staffIndex, voiceNumber, stanza); // aggiunge automaticamente alla nota
	            syllableIndex++;

	            if (note.isCurveStart())
	                connected = true;
	        } else {
	            if (note.isCurveEnd())
	                connected = false;
	        }

	        noteIndex++;
	    }
	}

	private boolean shouldAssignLyric(NoteEvent note, boolean connected) {
	    if (!connected) {
	        return true; // nota singola o inizio curva
	    } else {
	        return false; // dentro curva → skip
	    }
	}

	public List<String> getLyricsFor(int staffIndex, int voiceNumber, int stanza) {
	    Staff s = getStaff(staffIndex);
	    Voice v = s.getVoice(voiceNumber);
	    List<String> result = new ArrayList<>();
	    for (NoteEvent n : v.getNotes()) {
	        Lyric l = n.getLyric(stanza);
	        if (l != null) {
	            result.add(l.getSyllable().getText());
	        }
	    }
	    return result;
	}


	@Override
	public Iterator<Staff> iterator() {
		return staffList.iterator();
	}

	public void addListener(ScoreListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ScoreListener listener) {
		listeners.remove(listener);
	}

	protected void fireScoreEvent(ScoreEvent e) {
		for (ScoreListener l : listeners) {
			l.scoreChanged(e);
		}
	}

	public void tie(List<NoteEvent> notes) {
		if (notes == null || notes.size() < 2)
			return;

		notes.get(0).tieStart();

		for (int i = 1; i < notes.size() - 1; i++) {
			NoteEvent n = notes.get(i);
			n.tieEnd();
			n.tieStart();
		}

		notes.get(notes.size() - 1).tieEnd();
	}

	public void slur(List<NoteEvent> notes) {
		if (notes == null || notes.size() < 2)
			return;

		notes.get(0).slurStart();

		for (int i = 1; i < notes.size() - 1; i++) {
			NoteEvent n = notes.get(i);
			n.slurEnd();
			n.slurStart();
		}

		notes.get(notes.size() - 1).slurEnd();
	}

	public void changeTick(MusicObject o, int newTick) {
		int staffIndex = o.getStaffIndex();
		int voiceIndex = o.getVoiceIndex();
		getStaff(staffIndex).getVoice(voiceIndex).changeTick(o, newTick);
	}
}
