package sound;
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import sound.SongNote;

public class body {
	private int timeBegin;
	private int timeRepeat;
	private int noteLength;
	private SequencePlayer song;
	private ArrayList<String> noteLines;
	private ArrayList<SongNote> matchNote;
	private boolean hasAccidentals;
	

	public body(int tempo, ArrayList<String> _noteLines) throws MidiUnavailableException, InvalidMidiDataException {
		timeBegin = 1;
		timeRepeat = timeBegin;
		noteLength = 0;		
		song = new SequencePlayer(tempo, 12);		
		noteLines = _noteLines;
		matchNote = new ArrayList<SongNote>();
		hasAccidentals = false;		
	}	
	
	
	public SequencePlayer makeSequence() {
		for (String noteLine : noteLines) {
			if (noteLine.matches("^[CKLMQTXV]:.*$")) {
				if (noteLine.matches("^[vV]: *1")) {
					timeRepeat = timeBegin;
				} else if (noteLine.matches("^[vV]:.*"))
					timeBegin = timeRepeat;
			} 
			else if (noteLine.matches("[a-gzA-G0-9,=:| \\[\\]\\/_^\\(']*")) {				
				String[] seperateNotes = noteLine.split(" ");
				for (int i = 0; i < seperateNotes.length; i++) {
					String noteSection = seperateNotes[i];
					analyzeNoteSection(noteSection);
				}
			} 
		}
		return this.song;
	}


	private void analyzeNoteSection(String _noteSection) {

		boolean inChord = false;
		int _tuplet = 1;
		int _tupletCount = 0;
		ArrayList<String> preparedNotes = prepareSubNote(_noteSection);
		
		for (String noteCode : preparedNotes) {
			
			// in a chord, beginTime doesn't change
			if (noteCode.matches("\\[")) {
				inChord = true;
				timeBegin += noteLength;
			}
			if (noteCode.matches("\\]")) {
				inChord = false;
			}

			//tuplets are being handled
			if (noteCode.matches("\\(\\d")) {
				_tupletCount = Integer.parseInt(noteCode.substring(1));
				_tuplet = _tupletCount;
			}
			if (noteCode.matches("|")) {
				matchNote = new ArrayList<SongNote>();
				hasAccidentals = false;
			}
			
			
			if (noteCode.matches(".*[A-Ga-gzZ].*")) {				
				SongNote note = new SongNote(noteCode);
				if (hasAccidentals) {
					int location = -1;
					for (int i = matchNote.size() - 1; i >= 0; i--) {
						if (matchNote.get(i).compareInstances(note) == 0) {
							location = i;
							break;
						}
					}
					if (location > -1) {
						note.setPitch(matchNote.get(location).getPitch());
					}
				}
				
				noteLength = note.getDuration() / _tuplet;
				
				if (_tupletCount > 0) 
					_tupletCount--;
				else 
					_tuplet = 1;
				
				int pitch = note.getPitch();
				if (!inChord) 
					timeBegin += noteLength;
				
				String noteLetter = note.getContent() + "";
				if (!noteLetter.matches("[zZ]")) 
					song.addNote(pitch, timeBegin, noteLength);
				
				matchNote.add(note);
				if (note.hasAccidental()) {
					hasAccidentals = true;
				}
				
				//we can  print processed note
				//printNote();
				//System.out.println(note.getContent() + " pitch: " + note.getPitch() + "\tOctave:  " + note.getOctave() + "\t NumTicks: " + noteLength + "\tstartTick: " + timeBegin);

			}
		}
	}

	/**
	 * Separating symbols of notes' sequences
	 * @param _noteSection
	 * @return noteList
	 */
	private ArrayList<String> prepareSubNote(String _noteSection) {		
		ArrayList<String> noteList = new ArrayList<>();
		int location = 0;
		int length = _noteSection.length();

		while (location < length) {
			boolean valid = false;
			boolean symbol = false;
			String currentNote = "";
			
			while (location < length && _noteSection.substring(location, location + 1).matches("[|:\\[\\]]")) {
				currentNote += _noteSection.substring(location, location + 1);
				location++;
				symbol = true;
				valid = true;
			}
			if (location < length - 1 && _noteSection.substring(location, location + 2).matches("\\(\\d")) {
				currentNote += _noteSection.substring(location, location + 2);
				location += 2;
				symbol = true;
				valid = true;
			}
			
			while (!symbol && location < length && _noteSection.substring(location, location + 1).matches("[_^=]")) {
				currentNote += _noteSection.substring(location, location + 1);
				location++;
				valid = true;
			}		
			if (!symbol && location < length && _noteSection.substring(location, location + 1).matches("[A-Ga-gzZ]")) {
				currentNote += _noteSection.substring(location, location + 1);
				location++;
				valid = true;
			}
			while (!symbol && location < length && _noteSection.substring(location, location + 1).matches("[',0-9/]")) {
				currentNote += _noteSection.substring(location, location + 1);
				location++;
				valid = true;
			}
			if (!valid) {
				location++;
			}
			noteList.add(currentNote);
		}
		return noteList;
	}


}
