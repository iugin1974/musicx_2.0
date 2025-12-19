package notation;

import java.util.ArrayList;
import java.util.List;

import Measure.Partial;
import Measure.TimeSignature;
import musicInterface.MusicObject;

public class Staff {

	private List<Voice> voices;
	private TimeSignature timeSignature;
	private Partial partial;
	
	public Staff() {
		voices = new ArrayList<>();
		voices.add(new Voice(0));
		voices.add(new Voice(1));
	}
	
	public void addVoice() {
		int i = voices.size();
		voices.add(new Voice(i));
		System.out.println("Voice added. Now " + voices.size() + "voices");
	}

	public Voice getVoice(int n) {
		return voices.get(n);
	}
	
	public int getNumberOfVoices() {
		return voices.size();
	}
	
	public List<MusicObject> getObjects(int voiceNumber) {
		return getVoice(voiceNumber).getObjects();
	}
	
	public List<Voice> getVoices() {
		return voices;
	}
	
	public List<Voice> getVoicesWithMusic() {
		List<Voice> voiceWithMusic = new ArrayList<>();
		for (int i = 1; i < voices.size(); i++) {
			if (!voices.get(i).isEmpty()) voiceWithMusic.add(voices.get(i));
		}
		return voiceWithMusic;
	}
	
	public boolean removeObject(MusicObject obj) {
		 if (obj == null) 
		        return false;
		 
		for (Voice v : voices) {
			if (v.removeObject(obj))
				return true;
		}
		return false;
	}

	public KeySignature getLastKeySignature() {
		Voice v = voices.get(0);
		for (int i = v.size() - 1; i >= 0; i--) {
			if (v.get(i) instanceof KeySignature) {
				return ((KeySignature)v.get(i));
			}
		}
		return null;
	}
	
	public Clef getLastClef() {
		Voice v = voices.get(0);
		for (int i = v.size() - 1; i >= 0; i--) {
			if (v.get(i) instanceof Clef) {
				return ((Clef)v.get(i));
			}
		}
		return null;
	}
	
	public void clearVoice(int voiceNumber) {
		getVoice(voiceNumber).clear();
	}

	public TimeSignature getTimeSignature() {
		return timeSignature;
	}
	
	public void setTimeSignature(TimeSignature t) {
		timeSignature = t;
	}

	public void setPartial(Partial p) {
		partial = p;
		
	}
	
}
