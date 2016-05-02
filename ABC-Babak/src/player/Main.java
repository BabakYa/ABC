package player;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import sound.*;

/**
 * Main entry point of your application.
 */
public class Main {
	
	public static void main(String[] args) {
		
		/**
		 * GUI for selecting one of the sample abc music files
		 */
		Select _music = new Select();
		Path filePath = Paths.get("sample_abc"+"/"+ _music.getMusic()+".abc");
		
		
		/**
		 * reads the information in the header section 
		 */
		header _header = new header();
		try {
			_header.readHeader(filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/**
		 * reads and analyzes the notes in abc music file 
		 */
		body _body = null;
		try {
			_body = new body(_header.getTempo(), _header.getNoteLines());
		} catch (MidiUnavailableException | InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/**
		 * plays the music 
		 */
		SequencePlayer _player = _body.makeSequence();
		try {
			_player.play();
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

