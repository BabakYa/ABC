import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import sound.SequencePlayer;
import sound.body;
import sound.header;

public class Test {

	@org.junit.Test
	public void test() throws MidiUnavailableException, InvalidMidiDataException, IOException {
		
		testKeySignature();
		testSequenceLoaderPlaysASeriesOfNotes();
		
		//fail("Not yet implemented");		
	}
	
	public void testKeySignature() throws IOException  {
		Path _path = Paths.get("sample_abc/scale.abc");
		header _header = new header();
		_header.readHeader(_path);

		assertEquals(_header.getKeySignature(), "C");
	}
	
	/**
	 * this test needs a change in code 
	 * requires to make AnalyzeLine Public first
	 */
	public void basicHeaderTest() {
        // Tests only the basic header necessities
		header h = new header();
		
        String input = "X: 1 \n T:Piece No.1 \n K:C \n";
        String[] a = input.split("\n");
        for(String s : a){
        	System.out.println(s.trim());
        	
        	//h.AnalyzeLine(s);
        }
         
        System.out.println(h.getIndexNumber());
        //System.out.println(h.getTitle());
        //System.out.println(h.getKeySignature());
    }
	
	public void testSequenceLoaderPlaysASeriesOfNotes() throws MidiUnavailableException, InvalidMidiDataException {
		ArrayList noteLines = new ArrayList<>();
		noteLines.add("a/2 C");
		body loader = new body(0, noteLines);
		SequencePlayer player = loader.makeSequence();		

		System.out.println(player);
		assertTrue(player.toString().contains("Event: NOTE_ON  Pitch: 67") && player.toString().contains("Event: NOTE_ON  Pitch: 60") &&
		player.toString().contains("Event: NOTE_ON  Pitch: 64"));
		
		
		ArrayList noteLines2 = new ArrayList<>();
		noteLines2.add("C/2 a");
		body loader2 = new body(0, noteLines2);
		SequencePlayer player2 = loader2.makeSequence();		

		System.out.println(player2);
		assertTrue(player.toString().contains("Event: NOTE_ON  Pitch: 67") && player.toString().contains("Event: NOTE_ON  Pitch: 60") &&
		player.toString().contains("Event: NOTE_ON  Pitch: 64"));

		}


}
