package notation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Measure.Partial;
import Measure.TimeSignature;
import musicInterface.MusicObject;

public class Staff implements Iterable<Voice> {

	private List<Voice> voices;
	private TimeSignature timeSignature;
	private Partial partial;
	
	protected Staff() {
		voices = new ArrayList<>();
		voices.add(new Voice(0));
		voices.add(new Voice(1));
	}
	
	protected void addVoice() {
		int i = voices.size();
		voices.add(new Voice(i));
		System.out.println("Voice added. Now " + voices.size() + " voices");
	}

	protected Voice getVoice(int n) {
		return voices.get(n);
	}
	
	protected int getNumberOfVoices() {
		return voices.size();
	}
	
	protected List<MusicObject> getObjects(int voiceNumber) {
		return getVoice(voiceNumber).getObjects();
	}
	
	protected List<Voice> getVoices() {
		return voices;
	}
	
	protected List<Voice> getVoicesWithMusic() {
		List<Voice> voiceWithMusic = new ArrayList<>();
		for (int i = 1; i < voices.size(); i++) {
			if (!voices.get(i).isEmpty()) voiceWithMusic.add(voices.get(i));
		}
		return voiceWithMusic;
	}
	
	protected boolean removeObject(MusicObject obj) {
		 if (obj == null) 
		        return false;
		 
		for (Voice v : voices) {
			if (v.removeObject(obj))
				return true;
		}
		return false;
	}

	protected KeySignature getLastKeySignature() {
		Voice v = voices.get(0);
		for (int i = v.size() - 1; i >= 0; i--) {
			if (v.get(i) instanceof KeySignature) {
				return ((KeySignature)v.get(i));
			}
		}
		return null;
	}
	
	protected Clef getLastClef() {
		Voice v = voices.get(0);
		for (int i = v.size() - 1; i >= 0; i--) {
			if (v.get(i) instanceof Clef) {
				return ((Clef)v.get(i));
			}
		}
		return null;
	}
	
	protected void clearVoice(int voiceNumber) {
		getVoice(voiceNumber).clear();
	}

	protected TimeSignature getTimeSignature() {
		return timeSignature;
	}
	
	protected void setTimeSignature(TimeSignature t) {
		timeSignature = t;
	}

	protected void setPartial(Partial p) {
		partial = p;
		
	}

	@Override
	public Iterator<Voice> iterator() {
		return voices.iterator();
	}
	
}
