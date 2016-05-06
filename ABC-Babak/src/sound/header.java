package sound;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;


public class header {
	
	/**
	 * Each line in the header corresponds to a field, and begins with a single uppercase letter
	 */
	private String composer; 		
	private String keySignature;    
	private String noteDuration;	
	private String meter; 			
	private int tempo;				
	private String title; 			
	private int indexNumber; 		
	
	private static ArrayList<String> noteLines;
	
	public  header (){
		this.composer = "";
		this.keySignature = "";
		this.noteDuration = "";
		this.meter = "";
		this.tempo = 100;
		this.title = "";
		this.indexNumber = 0;		
		noteLines = new ArrayList<String>();
	}
			
	public  void readHeader(Path fileName) throws IOException{
		InputStream in = Files.newInputStream(fileName);
		
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
		String _line;
		while ((_line = bufferedReader.readLine()) != null) {
			
			AnalyzeLine(_line);
			
		}
	}

	/**
	 * is public only because of testing!!!!!!
	 * this is method Analyzes the header
	 * @param _line is each line of header which starts with one of [CKLMQTXV]
	 * modifies Composer, keySignature , other header info
	 */
	private void AnalyzeLine(String _line){
		if (_line.matches("^[CKLMQTXV]:.*$")) {
			
			/**
			 * Initialize the details of Song Header
			 */
			switch (_line.charAt(0)) {
			case 'C':
				this.composer = _line.substring(2).trim();
				break;
			case 'K':
				this.keySignature = _line.substring(2).trim();
				break;
			case 'L':
				this.noteDuration = _line.substring(2).trim();
				break;
			case 'M':
				this.meter = _line.substring(2).trim();
				break;
			case 'Q':
				this.tempo = Integer.parseInt(_line.substring(2).trim());;
				break;
			case 'T':
				this.title = _line.substring(2).trim();
				break;
			case 'X':
				this.indexNumber = Integer.parseInt(_line.substring(2).trim());;
				break;
			default:
				break;
			}
			
			// Adds Notes to our NoteLine file
			addNoteLine(_line);				
		} 
		// Adds Notes to our NoteLine file
		else if (_line.matches("[a-gzA-G0-9,=:|\\( \\[\\]\\/_^']*")) {
			addNoteLine(_line);
		} 
	}
	
	
	public String getComposer() {
		return composer;
	}
	public String getKeySignature() {
		return keySignature;
	}
	public String getNoteDuration() {
		return noteDuration;
	}
	public String getMeter() {
		return meter;
	}
	public int getTempo() {
		return tempo;
	}
	public String getTitle() {
		return title;
	}
	public int getIndexNumber() {
		return indexNumber;
	}
	public ArrayList<String> getNoteLines() {
		return noteLines;
	}
	
	
	public static void addNoteLine(String val) {
		noteLines.add(val); 
	}
}
