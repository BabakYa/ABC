package sound;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SongNote  {
	private int length;
	private HashMap<String, Integer> noteInformation;

	public SongNote(String _note) {
		
		/**
		 * By default, every note has the length specified in the 'L' header field (and when this field is absent, a length of 1/8)
		 * The length of a particular note may be augmented (diminished) by appending a multiplicative factor to the note
		 * Calculate the Note Lengths
		 */
		length = calculateLength(_note);
		
		/**
		 * Notes may be in  higher octaves or below ones
		 * The effect of the key signature can be temporarily overridden using accidentals
		 * Acquiring the information of note
		 */ 
		noteInformation = extractInfo(_note);
	}
	

	/**
	 * Calculate the Note Lengths
	 */
	private static int calculateLength(String note) {
		
		/**
	     * Number of pitches in an octave = 12
	     */
		int assumedTotalLength = 12;
		
		Pattern pattern = Pattern.compile("([0-9]*\\/[0-9])");
		Matcher matcher = pattern.matcher(note);
		
		
		Pattern pattern2 = Pattern.compile("(.*)(\\d)(.*)");
		Matcher matcher2 = pattern2.matcher(note);
		
		
		if (matcher.find()) {
						 
			if (matcher.group(1).startsWith("/")){
				String P = matcher.group(1).substring(1);
				int denominator = Integer.parseInt(P);
				assumedTotalLength = 12 / denominator;
				
			}else{
				assumedTotalLength = (12 * Integer.parseInt(matcher.group(1).substring(0, 1))) / Integer.parseInt(matcher.group(1).substring(2));
			}									
		} else if (matcher2.find()) {			
			note = matcher2.group(1) + matcher2.group(3);
			assumedTotalLength = 12 * Integer.parseInt(matcher2.group(2));
		}
		return assumedTotalLength;
	}
	
	public int getAccidental() {
		return noteInformation.get("Accidental").intValue();
	}
	
	public int getOctave() {
		return noteInformation.get("octave").intValue();
	}
	
	public int getPitch() {
		return noteInformation.get("pitch").intValue();
	}
	
	public void setPitch(Integer value) {
		noteInformation.put("pitch", value);
	}
	public char getContent() {
		return (char) noteInformation.get("content").intValue();
	}

	public int getDuration() {
		return this.length;
	}


	/**
	 * Acquiring the information of note
	 */
	private static HashMap<String, Integer> extractInfo(String note) {
		
		HashMap<String, Integer> noteInformation = new HashMap<>();
		int pitch = 0;
		
		
		Pattern pattern = Pattern.compile("([A-Ga-gzZ])");
		Matcher matcher = pattern.matcher(note);
		
		String _content = "";
		
		if (matcher.find()) {
			_content = matcher.group(1);
			noteInformation.put("content", (int) _content.toUpperCase().charAt(0));

			/* 
			 * The note C in the next higher octave is denoted by a lower case C 
			 *  Notes in the higher octave are denoted by appending an appropriate number of apostrophes (') immediately	after their names
			 *  Notes in the octaves below the middle C can be denoted by appending	commas (,)
			 */
			
			int higherOctave = counting(note, '\'');
			int belowOctave = counting(note, ',');
			
			if (_content.matches("[c-g]")) {
				higherOctave++;
			} else if (_content.matches("[ab]")) {
				higherOctave += 2;
			}
			
			noteInformation.put("octave", higherOctave - belowOctave);
			int octaveTotal = noteInformation.get("octave") * Pitch.OCTAVE;
			
			/*
			 * An accidental can be a sharp (denoted by '^' in abc), a flat ('_'), or a natural ('=')
			 * A sharp causes a note to be played one semitone higher
			 * A flat one semitone lower
			 * A natural causes the note to be played as if the key were in C without any accidental
			 */

			int sharp = counting(note, '^');
			int flat = counting(note, '_') ;
			int natural = counting(note, '=');
			
			if (flat > 0 || natural > 0 || sharp > 0)
				noteInformation.put("Accidental", 1);
			else 
				noteInformation.put("Accidental", 0);
			
			noteInformation.put("Semitone", sharp - flat);
			
			if (_content.contains("z") ||  _content.contains("Z")) 
				noteInformation.put("pitch", -1);
			else 
				noteInformation.put("pitch", new Pitch((char) noteInformation.get("content").intValue()).transpose(noteInformation.get("Semitone") + octaveTotal).toMidiNote());
		} 
		
		else
			System.out.println("No note found!");
		return noteInformation;
	}

	private static int counting(String _note, char _char) {
		
		int stringLength = _note.length();
		int _count = 0;
		for (int i = 0; i < stringLength; i++) {
			if (_note.charAt(i) == _char)
				_count++;
		}

		return _count;
	}

	public boolean hasAccidental() {
		
		boolean result = false;
		
		if (noteInformation.get("Accidental").intValue() != 0) 
			result = true;
		
		return result;
	}
	
	
	public int compareInstances(SongNote other) {
		
		int result = 0;
		if (getOctave() > other.getOctave()) 
			result = 1;
		else if (getOctave() > other.getOctave()) 
			result = -1;
		else {
			if (getContent() > other.getContent()) result = 1;
			else if (getContent() < other.getContent()) result = -1;
		}
		return result;
	}
}