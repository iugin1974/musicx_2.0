package notation;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import Measure.Partial;
import Measure.TimeSignature;
import musicEvent.Modus;
import musicEvent.NoteEvent;
import musicInterface.MusicObject;

public class Score implements Serializable, Iterable<Staff>, Observable {

	private List<ScoreListener> listeners;
	private List<Staff> staffList;
	private final List<CurvedConnection> curvedConnections = new ArrayList<>();
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
	public Staff addStaff() {
		Staff s = new Staff();
		staffList.add(s);
		ScoreEvent e = new ScoreEvent(ScoreEvent.Type.STAFF_ADDED, s, staffList.size() - 1);
		fireScoreEvent(e);
		return s;
	}

//	public void setTimeSignature(TimeSignature time) {
//		this.time = time;
//		for (Staff s : staffList) {
//			s.setTimeSignature(time);
//		}
//	}

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
		Staff s = staffList.get(staffIndex);

		// se non ci sono abbastanza voci per lo staff, vengono create
		int v = s.getNumberOfVoices();
		for (int i = v; i <= voiceIndex; i++) {
			s.addVoice();
		}

		obj.setStaff(staffIndex);
		obj.setVoiceIndex(voiceIndex);
		s.getVoice(voiceIndex).addObject(obj);

		ScoreEvent e = new ScoreEvent(ScoreEvent.Type.OBJECT_ADDED, obj, staffIndex, voiceIndex);
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

	/** restituisce una lista ordinata in base ai tick con tutti gli oggetti di tutti gli staves */
	public List<MusicObject> getAllObjects() {
		List<MusicObject> all = new ArrayList<>();
		for (Staff staff : staffList) {
			for (Voice v : staff.getVoices()) {
				all.addAll(v.getObjects());
			}
		}
		all.sort(new CompareTick());
		return all;
	}

	/**
	 * Rimuove un oggetto dalla score. Se l'azione ha avuto successo viene lanciato
	 * uno ScoreEvent e ritorna true.
	 * 
	 * @param obj
	 * @return
	 */
	public void removeObject(MusicObject obj) {

		if (obj == null)
			return;

		// 1. CurvedConnection (tie, slur, ecc.)
		if (obj instanceof CurvedConnection c) {
			removeCurvedConnection(c);
			int staffIndex = c.getStaffIndex();
			int voiceIndex = c.getStart().getVoiceIndex();
			updateLyrics(staffIndex, voiceIndex);
		}

		// 2. Nota
		if (obj instanceof NoteEvent note) {
			removeNote(note);
		}

		// 3. Altri MusicObject (bar, clef, rest, ecc.)
		removeGenericObject(obj);
	}

	private void removeGenericObject(MusicObject obj) {

		for (int s = 0; s < staffList.size(); s++) {
			Staff staff = staffList.get(s);
			int voiceIndex = staff.removeObject(obj);

			if (voiceIndex != -1) {
				ScoreEvent ev = new ScoreEvent(ScoreEvent.Type.OBJECT_REMOVED, obj, s, voiceIndex);
				fireScoreEvent(ev);
			}
		}
	}

	private void removeNote(NoteEvent note) {

		// 1. Rimuovi tutte le connessioni collegate

		for (int i = curvedConnections.size() - 1; i >= 0; i--) {
			removeCurvedConnection(curvedConnections.get(i));
		}

		// 2. Rimuovi la nota dallo staff / voice
		for (int s = 0; s < staffList.size(); s++) {
			Staff staff = staffList.get(s);
			int voiceIndex = staff.removeObject(note);

			if (voiceIndex != -1) {
				ScoreEvent ev = new ScoreEvent(ScoreEvent.Type.OBJECT_REMOVED, note, s, voiceIndex);
				fireScoreEvent(ev);
			}
		}
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

	public int getStaffIndex(Staff s) {
		return staffList.indexOf(s);
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

	public MusicObject getNextObject(MusicObject o) {
		Voice layer = getVoiceOf(o);
		if (layer == null)
			return null;

		List<MusicObject> objs = layer.getObjects();
		int index = objs.indexOf(o);
		// se o è l'ultimo oggetto restituisce null
		if (index == objs.size() - 1)
			return null;
		// restituisce l'oggetto
		return objs.get(index + 1);
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
		MusicObject mo = getNextObject(n1);
		if (mo == null)
			return false;
		return mo == n2;
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
		System.out.println("Remove Lyrics on Staff " + staffIndex + ", voice " + voiceNumber + ", stanza " + stanza);
		Staff s = getStaff(staffIndex);
		Voice v = s.getVoice(voiceNumber);
		for (NoteEvent n : v.getNotes()) {
			n.removeLyric(stanza); // rimuove la lyric dalla nota
		}
	}

	private void updateLyrics(int staffIndex, int voiceNumber) {
	    int stanzasNumber = getStanzasNumber(staffIndex, voiceNumber);

	    for (int stanza = 0; stanza < stanzasNumber; stanza++) {
	        List<String> lyrics = getLyricsFor(staffIndex, voiceNumber, stanza);

	        if (lyrics.isEmpty()) {
	            removeLyrics(staffIndex, voiceNumber, stanza);
	            continue;
	        }

	        removeLyrics(staffIndex, voiceNumber, stanza);
	        addLyrics(lyrics, staffIndex, voiceNumber, stanza);
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
		System.out.println("Add Lyrics on Staff " + staffIndex + ", voice " + voiceNumber + ", stanza " + stanza);
		Voice v = s.getVoice(voiceNumber);
		List<NoteEvent> notes = v.getNotes();
		int syllableIndex = 0;
		int noteIndex = 0;

		NoteEvent lastLyricNote = null;

		while (syllableIndex < syllables.size() && noteIndex < notes.size()) {
			String token = syllables.get(syllableIndex);
			System.out.print(token+" ");
			NoteEvent note = notes.get(noteIndex);
			boolean underSlur = isNoteUnderAnySlur(note, false, true);
			
			if (underSlur) {
				note.setSkipText(true);
				noteIndex++;
				continue;
			}

			switch (token) {
			case "_":
				note.setSkipText(true);
				noteIndex++; // avanziamo la nota
				syllableIndex++; // anche il token deve essere consumato
				break;

			case "--":
				if (lastLyricNote != null) {
					lastLyricNote.setSyllableDivision(true);
				}
				syllableIndex++; // consumiamo il token di controllo
				break;

			case "__":
				if (lastLyricNote != null) {
					note.setLyricExtender(true);
				}
				noteIndex++; // avanza la nota
				syllableIndex++; // e il token
				break;

			default:
				// Token normale: sillaba da assegnare alla nota corrente
				Syllable syl = new Syllable(token);
				new Lyric(syl, note, staffIndex, voiceNumber, stanza);
				lastLyricNote = note;
				noteIndex++;
				syllableIndex++;
				break;
			}
		}
System.out.println();
	}

	public boolean isNoteUnderAnySlur(NoteEvent note, boolean includeFirstNote, boolean includeLastNote) {
	    for (CurvedConnection c : curvedConnections) {
	        if (c instanceof Slur slur) {
	            List<NoteEvent> notes = getNotesUnderSlur(slur, includeFirstNote, includeLastNote);
	            if (notes.contains(note)) {
	                return true;
	            }
	        }
	    }
	    return false;
	}

	/**
	 * Ritorna una lista di note sotto una slur, tralasciando la prima e l'utlima nota,
	 * che sono le "ancore" della slur.
	 * @param slur
	 * @return
	 */
	public List<NoteEvent> getNotesUnderSlur(Slur slur, boolean includeFirstNote, boolean includeLastNote) {
		List<NoteEvent> result = new ArrayList<>();

	    Voice voice = getVoiceOf(slur.getStart());
	    if (voice == null) return result;

	    boolean inside = false;

	    for (MusicObject obj : voice.getObjects()) {

	        if (obj == slur.getStart()) {
	            inside = true;

	            if (includeFirstNote && obj instanceof NoteEvent note) {
	                result.add(note);
	            }
	            continue;
	        }

	        if (obj == slur.getEnd()) {
	            if (includeLastNote && obj instanceof NoteEvent note) {
	                result.add(note);
	            }
	            break;
	        }

	        if (inside && obj instanceof NoteEvent note) {
	            result.add(note);
	        }
	    }

	    return result;
	}


	public List<String> getLyricsFor(int staffIndex, int voiceNumber, int stanza) {
		Staff s = getStaff(staffIndex);
		Voice v = s.getVoice(voiceNumber);
		List<String> result = new ArrayList<>();
		for (NoteEvent n : v.getNotes()) {
			Lyric l = n.getLyric(stanza);
			if (l != null) {
				result.add(l.getSyllable().getText());
				if (n.hasSyllableDivision()) {
					result.add("--");
				} else if (n.hasLyricExtender()) {
					result.add("__");
				}
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

	public void fireScoreEvent(ScoreEvent e) {
		for (ScoreListener l : listeners) {
			l.scoreChanged(e);
		}
	}

	public List<CurvedConnection> getCurveList() {
		return curvedConnections;
	}

	public void removeCurvedConnection(CurvedConnection c) {

		NoteEvent n1 = c.getStart();
		NoteEvent n2 = c.getEnd();

		n1.removeCurvedConnection(c);
		n2.removeCurvedConnection(c);

		curvedConnections.remove(c);
		int staffIndex = c.getStaffIndex();

		System.out.println("Connection removed");
		fireScoreEvent(new ScoreEvent(ScoreEvent.Type.OBJECT_REMOVED, c, staffIndex, 0));
	}

	/**
	 * Restituisce il gruppo di {@link NoteEvent} connessi alla nota di partenza
	 * tramite connessioni curve del tipo specificato (ad es. {@link Tie} o
	 * {@link Slur}).
	 * <p>
	 * Il gruppo viene calcolato navigando il grafo delle connessioni a partire
	 * dalla nota iniziale, seguendo esclusivamente le {@link CurvedConnection} che
	 * sono istanza della classe {@code type}. La visita è transitiva: se una nota è
	 * connessa a un'altra che a sua volta è connessa a una terza tramite
	 * connessioni dello stesso tipo, tutte le note verranno incluse nel risultato.
	 * </p>
	 *
	 * <p>
	 * Il metodo non assume una struttura lineare (catena): supporta anche
	 * configurazioni più complesse (ramificazioni), anche se nel caso di tie
	 * musicali ci si aspetta normalmente una sequenza lineare.
	 * </p>
	 *
	 * <p>
	 * L'ordine delle note nel risultato corrisponde all'ordine di visita e non è
	 * garantito essere musicale o temporale. Se è necessario un ordinamento (ad
	 * esempio per posizione nella voice), questo va applicato separatamente.
	 * </p>
	 *
	 * @param start la nota di partenza da cui iniziare l'esplorazione; deve
	 *              appartenere allo {@link Score}
	 * @param type  il tipo di {@link CurvedConnection} da seguire durante
	 *              l'esplorazione (ad esempio {@code Tie.class} o
	 *              {@code Slur.class})
	 *
	 * @return una lista contenente tutte le {@link NoteEvent} raggiungibili dalla
	 *         nota di partenza tramite connessioni del tipo specificato, inclusa la
	 *         nota iniziale
	 *
	 * @throws NullPointerException se {@code start} o {@code type} sono
	 *                              {@code null}
	 */

	public List<NoteEvent> getConnectionGroup(NoteEvent start, Class<? extends CurvedConnection> type) {
		List<NoteEvent> result = new ArrayList<>();
		Set<NoteEvent> visited = new HashSet<>();
		Deque<NoteEvent> stack = new ArrayDeque<>();

		stack.push(start);

		while (!stack.isEmpty()) {
			NoteEvent current = stack.pop();

			if (!visited.add(current))
				continue;

			result.add(current);

			for (CurvedConnection c : current.getCurvedConnections()) {
				if (!type.isInstance(c))
					continue;

				NoteEvent other = c.getOther(current);
				if (!visited.contains(other)) {
					stack.push(other);
				}
			}
		}
		return result;
	}

//	public void tie(List<NoteEvent> notes) {
//		if (notes == null || notes.size() < 2)
//			return;
//
//		for (int i = 0; i < notes.size() - 1; i++) {
//			NoteEvent n1 = notes.get(i);
//			NoteEvent n2 = notes.get(i+1);
//			CurvedConnection c = Tie.createIfValid(this, n1, n2);
//			listCurve.add(c);
//		}
//		fireScoreEvent(new ScoreEvent(ScoreEvent.Type.OBJECT_ADDED));
//	}
//
//	public void slur(List<NoteEvent> notes) {
//		if (notes == null || notes.size() < 2)
//			return;
//
//		for (int i = 0; i < notes.size() - 1; i++) {
//			NoteEvent n1 = notes.get(i);
//			NoteEvent n2 = notes.get(i+1);
//			CurvedConnection c = new Slur(n1, n2);
//			listCurve.add(c);
//		}
//		fireScoreEvent(new ScoreEvent(ScoreEvent.Type.OBJECT_ADDED));
//	}
//
	public void addCurvedConnection(CurvedConnection c) {
		curvedConnections.add(c);
		NoteEvent n1 = c.getStart();
		NoteEvent n2 = c.getEnd();
		if (n1.getVoiceIndex() != n2.getVoiceIndex()) {
			System.out.println("Connection not possible: different voice.");
			return;
		}

		n1.addCurvedConnection(c);
		n2.addCurvedConnection(c);
		updateLyrics(c.getStaffIndex(), n1.getVoiceIndex());
		fireScoreEvent(new ScoreEvent(ScoreEvent.Type.OBJECT_ADDED, c, c.getStaffIndex(), 0));
	}

	public void changeTick(MusicObject o, int newTick) {
		int staffIndex = o.getStaffIndex();
		int voiceIndex = o.getVoiceIndex();
		getStaff(staffIndex).getVoice(voiceIndex).changeTick(o, newTick);
		System.out.println("Change Tick: StaffIndex " + staffIndex+", voiceIndex: "+ voiceIndex + ", Object: " +o + ", Tick: " + newTick);
		
	}
	
	/**
	 * Trova la chiave valida nel tick <i>tick</i>.
	 * Se non vi è una chiave, restituisce <i>null</i>
	 * @param staffIndex
	 * @param atTick
	 * @return
	 */
	public KeySignature getKeySignature(int staffIndex, int atTick) {
		List<MusicObject> objs = getStaffWideObjects(getStaff(staffIndex));
		KeySignature lastKeySignature = new KeySignature(0, 0, Modus.MAJOR_SCALE); // fallback
	    // scorrere all’indietro
	    for (int i = objs.size() - 1; i >= 0; i--) {
	        MusicObject obj = objs.get(i);
	        if (obj.getTick() <= atTick && obj instanceof KeySignature) {
	        	lastKeySignature = (KeySignature) obj;
	            break; // trovato l’ultima chiave valida
	        }
	    }

	    return lastKeySignature;
	}
	
	/**
	 * Trova la chiave valida nel tick <i>tick</i>.
	 * Se non vi è una chiave, restituisce <i>null</i>
	 * @param staffIndex
	 * @param atTick
	 * @return
	 */
	public Clef getClef(int staffIndex, int atTick) {
		List<MusicObject> objs = getStaffWideObjects(getStaff(staffIndex));
		Clef lastClef = null;
	    // scorrere all’indietro
	    for (int i = objs.size() - 1; i >= 0; i--) {
	        MusicObject obj = objs.get(i);
	        if (obj.getTick() <= atTick && obj instanceof Clef) {
	            lastClef = (Clef) obj;
	            break; // trovato l’ultima chiave valida
	        }
	    }

	    return lastClef;
	}
	
	public void save() {
		ScoreToXML s = new ScoreToXML(this);
		try {
			s.parse();
		} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
			// TODO Auto-generated catch block
		}
	}

	public void load() {
		ScoreToXML s = new ScoreToXML(this);
		try {
			s.load();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
	}
}
