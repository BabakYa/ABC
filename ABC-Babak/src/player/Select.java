package player;

import javax.swing.JOptionPane;

public class Select {


	public String SelectedMusic ; 
	
	public String getMusic(){
		return SelectedMusic;
	}
	
    public Select() {
        String[] choices = new String[]{"fur_elise","scale","piece1","piece2","paddy","little_night_music","invention","paddy","debussy","testing","sample1","sample2","sample3"};
        SelectedMusic = askUser(choices);
        //System.out.println("selected: " + choice);
    }

    static String askUser(String[] choices) {
        String s = (String) JOptionPane.showInputDialog(
                null,
                "Select one of the sample abc musics",
                "Music Selection",
                JOptionPane.PLAIN_MESSAGE,
                null,
                choices,
                choices[0]);
        return s;
    }
}