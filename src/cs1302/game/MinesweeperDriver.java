package cs1302.game; 

import java.io.FileNotFoundException;

/**
 * MinesweeperDriver contains the main method
 * and makes it so that the program can read
 * in command-line arguments from the args.
 */
public class MinesweeperDriver {

    public static void main(String[] args) {
        String seedFile = " ";
  
        if (args[0].equals("--seed")) {
            seedFile = args[1];
        } else {
            System.out.println("Cannot understand command-line arguments.");
            System.exit(1);
        }
        
        if (seedFile.length() > 0) {
            MinesweeperGame ms = new MinesweeperGame(seedFile);
            ms.play();
        }
    }   
    
}
