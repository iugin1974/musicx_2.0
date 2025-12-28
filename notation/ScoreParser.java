package notation;

import java.util.ArrayList;
import java.util.List;

import musicInterface.MusicObject;

public class ScoreParser {

	 private final Score score;

	    public ScoreParser(Score score) {
	        this.score = score;
	    }
	    
	    public List<ParsedStaff> parse() {
	        VoiceMixer mixer = new VoiceMixer(score);
	        List<ParsedStaff> result = new ArrayList<>();

	        for (Staff staff : score.getAllStaves()) {
	            List<List<MusicObject>> voices = mixer.mixStaff(staff);
	            result.add(new ParsedStaff(staff, voices));
	        }

	        return result;
	    }
}
