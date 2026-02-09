package notation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Measure.Bar;
import musicEvent.Note;
import musicEvent.NoteEvent;
import musicInterface.MusicObject;

public class AccidentalEngine implements ScoreListener {

	private Score score;
	private Map<Integer, Integer> accidentalState = new HashMap<>();

	public AccidentalEngine(Score score) {
		this.score = score;
	}

	@Override
	public void scoreChanged(ScoreEvent e) {
		if (!e.isMusicObjectEvent())
			return;

		MusicObject mo = e.getMusicObject();
		if (!(mo instanceof Note n))
			return;

		int staffIndex = n.getStaffIndex();
		int tick = n.getTick();

		// 1. Recupera i tick di inizio e fine della misura
		Bar barStart = score.getPreviousObjectOfType(staffIndex, tick, Bar.class);
		Bar barEnd = score.getNextObjectOfType(staffIndex, tick, Bar.class);
		int lastTick = score.getLastObjectOfType(staffIndex, tick, Note.class).getTick() + 1;
		int tickStart = (barStart == null) ? 0 : barStart.getTick();
		int tickEnd = (barEnd == null) ? lastTick : barEnd.getTick();

		// 2. Calcola le alterazioni effettive e imposta needsAccidental
		calculateAccidentals(staffIndex, tickStart, tickEnd);

		// 3. Aggiorna lo stato della nota nella mappa (ma non tocca alteration)
		// checkForAlteration(n);
	}

//	 
//	public void calculateAccidentals(int staffIndex, int tick1) {
//		List<NoteEvent> list = score.getAllNotesAfter(staffIndex, tick1);
//		if (list.isEmpty()) return;
//		int tick2 = list.get(list.size() - 1).getTick();
//		calculateAccidentals(staffIndex, tick1, tick2);
//	}
//	
	public void calculateAccidentals(int staffIndex, int tick1, int tick2) {
		reset();

		List<NoteEvent> list = score.getAllNotesBetween(staffIndex, tick1, tick2);
		if (list.isEmpty())
			return;

		for (NoteEvent ev : list) {
			if (!(ev instanceof Note))
				continue;
			Note n = (Note) ev;

			// 1. Chiave valida per questa nota
			KeySignature ks = score.getLastObjectOfType(staffIndex, n.getTick(), KeySignature.class);

			int pos = n.getStaffPosition();

			// 2. Alterazione di riferimento
			int refAlteration;
			if (accidentalState.containsKey(pos)) {
				refAlteration = accidentalState.get(pos);
			} else {
				refAlteration = (ks != null) ? ks.getAlterationForStaffPosition(n) : 0;
			}

			// 3. Alterazione effettiva della nota
			int effectiveAlteration = n.getAlteration().getValue();
			n.getAlteration().setVisible(effectiveAlteration != refAlteration);

			// 5. Aggiorna stato della battuta
			accidentalState.put(pos, effectiveAlteration);
		}
	}

	public void reset() {
		accidentalState = new HashMap<>();
	}
}
