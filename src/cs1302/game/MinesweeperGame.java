package cs1302.game;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * MinesweeperGame class that contains
 * many methods needed to run and create
 * the minesweeper game.
 *
 */
public class MinesweeperGame {

    private double playerScore; //instance variable for the player's score.
    private boolean isGameOver; //instance variable for whether the game is over or  being played.
    private String[][] mineFieldArray; //instance variable array that shows the mine field.
    private boolean[][] booleanMineArray; //instance variable that shows array of mine locations.
    private int roundsCompleted; //instance variable that shows rounds completed.
    private int numberOfMines; //instance variable that shows how many mines there are in the game.
    private String[][] noFogArray; //instance variable array for the nofog command.

    /**
     * Constructor for the MinesweeperGame class.
     *
     *@param seed A seed file that is used to give information for
     *       creating the minesweeper game, such as the dimensions
     *       and number of mines.
     */
    public MinesweeperGame(String seed) {        
        try {
            int rows = 0;
            int columns = 0;
            int booleanMineRow;
            int booleanMineColumn;
            
            File configFile = new File(seed);
            Scanner keyboard = new Scanner(configFile);
            if (keyboard.hasNextInt()) {
                rows = keyboard.nextInt();
                if (keyboard.hasNextInt()) {
                    columns = keyboard.nextInt();
                    if (keyboard.hasNextInt()) {
                        this.numberOfMines = keyboard.nextInt();
                    }              
                } 
            } else {
                System.out.println("\nSeedfile Not Found Error: Cannot create game with " + seed
                                   + ", because it is not formatted correctly.");
                System.exit(1);
            }
            if ((rows < 5) || (columns < 5)) {
                System.out.println("\nSeedfile Value Error: Cannot "
                                   + " create a mine field with that many rows and/or columns!");
                System.exit(3);
            }
            //initilazing mineFieldArray and noFogArray
            mineFieldArray = new String[rows][columns];
            noFogArray = new String[rows][columns];
            //setting values inside arrays
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (columns > 10) {
                        mineFieldArray[i][j] = "    ";
                        noFogArray[i][j] = "    ";
                    } else {
                        mineFieldArray[i][j] = "   ";
                        noFogArray[i][j] = "   ";
                    }
                }
            }
            //initilaizing booleanMineArray and setting values
            booleanMineArray = new boolean[rows][columns];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    booleanMineArray[i][j] = false;
                }
            }        
            for (int i = 0; i < numberOfMines; i++) {
                booleanMineRow = keyboard.nextInt();
                booleanMineColumn = keyboard.nextInt();
                booleanMineArray[booleanMineRow][booleanMineColumn] = true;
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("Cannot create game with " + seed + ", because it "
                               + "cannot be be found or cannot be read due to permission.");
            System.exit(1);
        }
    } //Constructor
    
    /**
     * Prints welcome screen message.
     */
    public void printWelcome() {
        System.out.println("        _");
        System.out.println("  /\\/\\ (_)_ __   ___  _____      _____  ___ _ __   ___ _ __");
        System.out.println(" /    \\| | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\ / _ \\"
                           + " '__|");
        System.out.println("/ /\\/\\ \\ | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |");
        System.out.println("\\/    \\/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|");
        System.out.println("                 A L P H A   E D I T I O N |_| v2020.sp");
    } //printWelcome
    
    /**
     * Prints the Mine Field grid for the game output.
     */
    public void printMineField() {
        //prints the row numbers and the actual grid
        for (int i = 0; i < mineFieldArray.length; i++) {
            if (i < 10) {
                System.out.print(" ");
            } //if-statement
            System.out.print(i + " |");
            for (int j = 0; j < mineFieldArray[i].length; j++) {
                System.out.print(mineFieldArray[i][j]);
                if (j < mineFieldArray[i].length - 1) {
                    System.out.print("|");
                }
            } //for-loop
            System.out.println("|");
        }
        if (mineFieldArray[0].length <= 10) { 
            System.out.print("     ");
        } else {
            System.out.print("     ");
        }
        //prints the numbers at the bottom of the row
        for (int i = 0; i < mineFieldArray[0].length; i++) {
            if (mineFieldArray[0].length <= 10) {
                System.out.print(i + "  ");
            } else {
                System.out.print(i + "   ");
            }
            if (i < 10) {
                System.out.print(" ");
            }
        }
        System.out.println();
        System.out.println();
    } //printMineField

    /**
     * Asks the user for their command and changes arrays
     * or gives information if the command is valid.
     */
    public void promptUser() {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("minesweeper-alpha: ");
        String command = keyboard.next();
        
        boolean invalidCommand = false;
        int commandRow = 0;
        int commandColumn = 0;
       
        if (command.equalsIgnoreCase("r") || command.equalsIgnoreCase("reveal") ||
            command.equalsIgnoreCase("m") || command.equalsIgnoreCase("mark") ||
            command.equalsIgnoreCase("g") || command.equalsIgnoreCase("guess")) {       
            //checks whether there are numbers that till with coordinates to do the action to
            //if there is a missing number then prints an error message
            if (keyboard.hasNextInt()) {
                commandRow = keyboard.nextInt();   
                if (keyboard.hasNextInt()) {
                    commandColumn = keyboard.nextInt();
                } else {
                    System.out.println("\nInput Error: Command not recognized!");
                    invalidCommand = true;
                }
            } else {
                System.out.println("\nInput Error: Command not recognized!");
                invalidCommand = true;
            }
        }
        //if statement that prints error message if a coordinate is not inbound
        if (!(isInBounds(commandRow, commandColumn))) {
            System.out.println("\nInput Error: Number Not in Bounds!");
            invalidCommand = true;
        } 
        if (!invalidCommand) {
            if (command.equalsIgnoreCase("r") || command.equalsIgnoreCase("reveal")) {
                reveal(commandRow, commandColumn);
            } else if (command.equalsIgnoreCase("m") || command.equalsIgnoreCase("mark")) {
                mark(commandRow, commandColumn);        
            } else if (command.equalsIgnoreCase("g") || command.equalsIgnoreCase("guess")) {
                guess(commandRow, commandColumn);
            } else if (command.equalsIgnoreCase("h") || command.equalsIgnoreCase("help")) {
                roundsCompleted++;
                System.out.println("\nCommands Available...");
                System.out.println("- Reveal: r/reveal row col");
                System.out.println("-   Mark: m/mark   row col");
                System.out.println("-  Guess: g/guess  row col");
                System.out.println("-   Help: h/help");
                System.out.println("-   Quit: q/quit");
            } else if (command.equalsIgnoreCase("q") || command.equalsIgnoreCase("quit")) {
                System.out.println();
                System.out.println("Quitting the game...");
                System.out.println("Bye!");
                System.exit(0);
            } else if (command.equalsIgnoreCase("nofog")) {
                noFog(commandRow, commandColumn);
            } else {
                System.out.println("\nInput Error: Command not recognized!");
            }
        }
        System.out.println();
    } //promptUser
    
    /**
     * Method used to execute reveal command.
     *
     *@param commandRow the row that the user entered for the command 
     *@param commandColumn the column that the user entered for the command
     */
    public void reveal(int commandRow, int commandColumn) {
        //if statement that ends game if grid revealed is a mine
        if (booleanMineArray[commandRow][commandColumn] == true) {
            roundsCompleted++;
            printLoss();
        }
        if (isInBounds(commandRow, commandColumn)) {
            roundsCompleted++;
            
            //if statement that adds the reveal symbol to the array 
            if (mineFieldArray[0].length <= 10) {
                mineFieldArray[commandRow][commandColumn] = " "
                    + getNumAdjMines(commandRow, commandColumn) + " ";
            } else {
                mineFieldArray[commandRow][commandColumn] = "  "
                    + getNumAdjMines(commandRow, commandColumn) + " ";
            }   
        }
    } //reveal
    
    /**
     * Method used to execute mark command.
     *
     *@param commandRow the row that the user entered for the command 
     *@param commandColumn the column that the user entered for the command
     */
    public void mark(int commandRow, int commandColumn) {
        if (isInBounds(commandRow, commandColumn)) {
            roundsCompleted++;

            //if statement that adds the mark symbol to the array 
            if (mineFieldArray[0].length <= 10) {
                mineFieldArray[commandRow][commandColumn] = " F ";
            } else {
                mineFieldArray[commandRow][commandColumn] = "  F ";
            }
        }
    } //mark

    /**
     * Method used to execute guess command.
     *
     *@param commandRow the row that the user entered for the command 
     *@param commandColumn the column that the user entered for the command
     */
    public void guess(int commandRow, int commandColumn) {
        if (isInBounds(commandRow, commandColumn)) {
            roundsCompleted++;
            
            //if statement that adds the guess symbol to the array
            if (mineFieldArray[0].length <= 10) {
                mineFieldArray[commandRow][commandColumn] = " ? ";
            } else {
                mineFieldArray[commandRow][commandColumn] = "  ? ";
            }
        }
    } //guess

    /**
     * Uses conditions to check whether the user has won the game.
     *
     *@return true or false of whether the user has won the game
     */
    public boolean isWon() {
        boolean nonMines = true;
        boolean mines = true;
    
        for (int i = 0; i < mineFieldArray.length; i++) {
            for (int j = 0; j < mineFieldArray[0].length; j++) {
                if (booleanMineArray[i][j]) {
                    if (mineFieldArray[i][j] != (" F ")) {
                        mines = false;
                    }
                } else {
                    if (mineFieldArray[i][j] == "   " || mineFieldArray[i][j] == " F " ||
                        mineFieldArray[i][j] == " ? ") {
                        nonMines = false;
                    }
                }
            }
        }
        return nonMines && mines;
    } //isWon

    /**
     * Prints the win screen.
     */
    public void printWin() {
        System.out.println();
        System.out.println(" ░░░░░░░░░▄░░░░░░░░░░░░░░▄░░░░ \"So Doge\"");
        System.out.println(" ░░░░░░░░▌▒█░░░░░░░░░░░▄▀▒▌░░░");
        System.out.println(" ░░░░░░░░▌▒▒█░░░░░░░░▄▀▒▒▒▐░░░ \"Such Score\"");
        System.out.println(" ░░░░░░░▐▄▀▒▒▀▀▀▀▄▄▄▀▒▒▒▒▒▐░░░");
        System.out.println(" ░░░░░▄▄▀▒░▒▒▒▒▒▒▒▒▒█▒▒▄█▒▐░░░ \"Much Minesweeping\"");
        System.out.println(" ░░░▄▀▒▒▒░░░▒▒▒░░░▒▒▒▀██▀▒▌░░░");
        System.out.println(" ░░▐▒▒▒▄▄▒▒▒▒░░░▒▒▒▒▒▒▒▀▄▒▒▌░░ \"Wow\"");
        System.out.println(" ░░▌░░▌█▀▒▒▒▒▒▄▀█▄▒▒▒▒▒▒▒█▒▐░░");
        System.out.println(" ░▐░░░▒▒▒▒▒▒▒▒▌██▀▒▒░░░▒▒▒▀▄▌░");
        System.out.println(" ░▌░▒▄██▄▒▒▒▒▒▒▒▒▒░░░░░░▒▒▒▒▌░");
        System.out.println(" ▀▒▀▐▄█▄█▌▄░▀▒▒░░░░░░░░░░▒▒▒▐░");
        System.out.println(" ▐▒▒▐▀▐▀▒░▄▄▒▄▒▒▒▒▒▒░▒░▒░▒▒▒▒▌");
        System.out.println(" ▐▒▒▒▀▀▄▄▒▒▒▄▒▒▒▒▒▒▒▒░▒░▒░▒▒▐░");
        System.out.println(" ░▌▒▒▒▒▒▒▀▀▀▒▒▒▒▒▒░▒░▒░▒░▒▒▒▌░");
        System.out.println(" ░▐▒▒▒▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▒▄▒▒▐░░");
        System.out.println(" ░░▀▄▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▄▒▒▒▒▌░░");
        System.out.println(" ░░░░▀▄▒▒▒▒▒▒▒▒▒▒▄▄▄▀▒▒▒▒▄▀░░░ CONGRATULATIONS!");
        System.out.println(" ░░░░░░▀▄▄▄▄▄▄▀▀▀▒▒▒▒▒▄▄▀░░░░░ YOU HAVE WON!");
        System.out.println(" ░░░░░░░░░▒▒▒▒▒▒▒▒▒▒▀▀░░░░░░░░ SCORE: " + playerScore());
        System.out.println();
        System.exit(0);
    } //printWin

    /**
     * Uses conditions to check whether the user has lost the game.
     *
     *@return true or false of whether the user has lost the game
     */
    public boolean isLost() {
        int tester = 0;
        for (int i = 0; i < mineFieldArray.length; i++) {
            for (int j = 0; j < mineFieldArray.length; j++) {
                if (!((mineFieldArray[i][j] == " F " || mineFieldArray[i][j] == "  F ") &&
                      (booleanMineArray[i][j] == true))) {
                    tester = 0;
                }
            }
        }
        if (tester > 0) {
            return true;
        } else {
            return false;
        }
            
    } //isLost

    /**
     * Prints the loss screen.
     */
    public void printLoss() {
        System.out.println();
        System.out.println("Oh no... You revealed a mine!");
        System.out.println(" __ _  __ _ _ __ ___   ___    _____   _____ _ __");
        System.out.println("/ _` |/ _` | '_ ` _ \\ / _ \\  / _ \\ \\ / / _ \\ '__|");
        System.out.println("| (_| | (_| | | | | | |  __/ | (_) \\ V /  __/ |");
        System.out.println(" \\__, |\\__,_|_| |_| |_|\\___|  \\___/ \\_/ \\___|_|");
        System.out.println(" |___/");
        System.exit(0);
    } //printLoss

    /**
     * Provides the main game loop by invoking other methods.
     */
    public void play() {
        int i = 0;
        
        printWelcome();
        while (i == 0) {
            System.out.println(" Rounds Completed: " + roundsCompleted);
            System.out.println();

            printMineField();
            promptUser();

            if (isWon() == true) {
                printWin();
            } else if (isLost() == true) {
                printLoss();
            }
        }
    } //play

    /**
     * Returns the number of mines adjacent to the specified
     * square in the grid.
     *
     * @param row the row index of the square
     * @param col the column index of the square
     * @return the number of adjacent mines
     */
    public int getNumAdjMines(int row, int col) {
        int numAdjMines = 0;

        //checking each adjacent block, checking for a bomb
        for (int i = row - 1; i <= row + 1; i++) {
            if (!(i >= 0 && i < mineFieldArray.length)) {
                continue;
            }
            for (int j = col - 1; j <= col + 1; j++) {
                if ((i == row && j == col) || (!(j >= 0 && j < mineFieldArray[0].length))) {
                    continue;
                } else {
                    if (booleanMineArray[i][j] == true) {
                        numAdjMines++;
                    }
                }
            }
        }
        return numAdjMines;
    } //getNumAdjMines

    /**
     * Calulates the user's score.
     *
     * @return the user's score for the game.
     */
    public double playerScore() {
        double score;
        score = (100.0 * mineFieldArray.length * mineFieldArray[0].length) / roundsCompleted;
        return score;
    }
    
    /**
     * Indicates whether or not the square is in the game grid.
     *
     * @param row the row index of the square
     * @param col the column index of the square
     * @return true if the square is in the game grid; false otherwise
     */
    private boolean isInBounds(int row, int col) {
        //if statement checking on whether or not the row and column are in the grid
        if (row < mineFieldArray.length && row >= 0 && col < mineFieldArray[1].length && col >= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Prints the noFog grid when the noFog
     * method is called. The noFog grid shows
     * the location of all mines.
     *
     *@param commandRow the row that the user entered for the command
     *@param commandColumn the column that the user entered for the command
     */
    public void noFog(int commandRow, int commandColumn) {
        roundsCompleted++;
        System.out.println();

        //creates new grid for noFog command
        for (int i = 0; i < noFogArray.length; i++) {
            if (i < 10) {
                System.out.print(" ");
            }
            System.out.print(i + " |");
            for (int j = 0; j < noFogArray[0].length; j++) {
                if (booleanMineArray[i][j] == true) {
                    if (mineFieldArray[0].length <= 10) {
                        System.out.print("<" + mineFieldArray[i][j].substring(1,2) + ">");
                    } else {
                        System.out.print("<" + mineFieldArray[i][j].substring(1,3) + ">");
                    }
                } else {
                    System.out.print(mineFieldArray[i][j]);
                }
                if (j < mineFieldArray[0].length - 1) {
                    System.out.print("|");
                }
            }
            System.out.println("|");
        }
        System.out.print("    ");
        if (mineFieldArray[0].length <= 10) {
            System.out.print(" ");
        } else {
            System.out.print("  ");
        }
        for (int i = 0; i < mineFieldArray[0].length; i++) {
            if (mineFieldArray[0].length <= 10) {
                System.out.print(i + "  ");
            } else {
                System.out.print(i + "   ");
            }
            if (i < 9) {
                System.out.print(" ");
            }
        }
        System.out.println();
    } //noFog

} //MinesweeperGame
