package notation;

import java.util.HashMap;
import java.util.Map;

import musicEvent.Note;
import musicInterface.MusicObject;

public class AccidentalContext implements ScoreListener {
	private Map<Integer, Integer> lastAlteration = new HashMap<>();
	private Score score;

	public AccidentalContext(Score score) {
		this.score = score;
	}

	private void checkForAlteration(Note n) {
		int midi = n.getMidiNumber();
		int alt = n.getAlteration();
		int tick = n.getTick();
		int staffIndex = n.getStaffIndex();

		// ottieni la KeySignature corrente per lo staff e il tick
		KeySignature ks = score.getPreviousObjectOfType(staffIndex, tick, KeySignature.class);
		// chiamo la logica di KeySignature
		boolean needsAccidental = ks.requiresAccidental(midi, alt);

		// controllo se la nota è già stata alterata nella misura
		Integer previousAlt = lastAlteration.get(midi);
		if (previousAlt != null && previousAlt == alt) {
			needsAccidental = false;
		}

		// aggiorno lo stato
		lastAlteration.put(midi, alt);

		n.needsAccidental(needsAccidental);
	}

	@Override
	public void scoreChanged(ScoreEvent e) {
		if (!e.isMusicObjectEvent())
			return; // solo note/music objects

		MusicObject mo = e.getMusicObject();
		if (!(mo instanceof Note n))
			return; // ignoriamo altre MusicObject
		checkForAlteration(n);
	}

	public void reset() {
		lastAlteration.clear();
	}
}
