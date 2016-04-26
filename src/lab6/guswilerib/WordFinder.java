package lab6.guswilerib;

import lab6.GamePiece;
import lab6.UI;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

/**
 * This is the powerhouse of the application. This is where all the recursing
 * and calculating takes place. This class also
 * contains methods to recurse through, access, and manipulate the search grid
 * of GridEntries.
 * @author taylor [based on a similar class by hornick]
 * @version 2015.04.21
 */
public class WordFinder {
    /**
     * Controls whether or not the GUI is displayed.
     */
    public static final boolean GUI_ENABLED = false;
    // TODO: you may wish to disable the GUI during benchmarking

    /**
     * Maximum word length
     */
    public static final int MAX_WORD_LENGTH = 15;

    /**
     * Minimum word length
     */
    public static final int MIN_WORD_LENGTH = 3;

    /**
     * Dictionary of words known to be spelled correctly
     */
    private final Dictionary dictionary;

    /**
     * List of game pieces that compose the game board
     */
    private final List<GamePiece<Character>> gameBoard;

    /**
     * Words found on the game board that are in the dictionary
     */
    private final Collection<String> foundWords;

    /**
     * Number of rows on the game board
     */
    private final int rows;

    /**
     * Number of columns on the game board
     */
    private final int cols;

    /**
     * The word that is currently being built by the word finder
     */
    private String currWord = "";

    /**
     * The graphical user interface that shows the progress of the search
     */
    private UI gui;

    /**
     * Constructor for this WordFinder
     * @param gameBoard The game board to be searched
     * @param dictionary A collection of correctly spelled words
     * @param rows Number of rows in game board
     * @param cols Number of columns in game board
     */
    public WordFinder(List<GamePiece<Character>> gameBoard, Dictionary dictionary, int rows, int cols) {
        this.gameBoard = gameBoard;
        this.dictionary = dictionary;
        foundWords = new TreeSet<>();
        this.rows = rows;
        this.cols = cols;
        if(GUI_ENABLED) {
            gui = new UI(gameBoard, rows, cols, 15);
        }
    }

//**********************************************************************
//  TODO: MODIFY ONLY THE FOLLOWING METHOD (and the call to it
//  TODO: identified by the TODO below.
//**********************************************************************
    /**
     * This is the method that calls itself repeatedly to wander it's way through
     * the game board using the specified search pattern, creating every possibly
     * letter combination and checking it against a dictionary. If the word is
     * found in the dictionary, it gets added to a collection of found words.
     * @param row Current row position of cursor
     * @param col Current column position of cursor
     * @param do8WaySearch <tt>true</tt> if eight way search is desired,
     *                     otherwise a four way search is performed
     */
    private void recursiveSearch(int row, int col, boolean do8WaySearch) {
        // TODO: Remove this line , since it will likely run MANY times
        //System.out.println("Doing recursive " + (do8WaySearch ? "eight" : "four") + " way search");

        // 1. Ensure we should visit this game piece:
        //  a. row is valid
        //  b. col is valid
        //  c. will not exceeded maximum length of word
        //  d. game piece has not been visited
        if(row >= 0 && row < rows && col >= 0 && col < cols
                && !getGamePiece(row,col).hasBeenVisited() && currWord.length() < MAX_WORD_LENGTH) {


            // 2. Now we're on a game piece we haven't yet visited on this pass through.
            //    Grab the character on the game piece and append it to the current word
            //    we're building.
            currWord += getGamePiece(row,col).getElement();

            // 3. Mark piece as visited. (use method in this class)
            setVisitedFlag(row,col);

            // 4. If word is at least three characters, lookup up the
            //    word in the dictionary and add it if found.
            if(currWord.length() >= MIN_WORD_LENGTH){
                if(dictionary.contains(currWord.toLowerCase())) {
                    validWord(currWord);
                }
            }

            // 5. Recursively call this method on the neighboring game pieces
            //    (either 4 neighbors or eight neighbors, depending on do8WaySearch).
            recursiveSearch(row, col + 1, do8WaySearch);
            recursiveSearch(row, col - 1, do8WaySearch);
            recursiveSearch(row + 1, col, do8WaySearch);
            recursiveSearch(row - 1, col, do8WaySearch);
            if(do8WaySearch){
                recursiveSearch(row + 1, col + 1, do8WaySearch);
                recursiveSearch(row - 1, col - 1, do8WaySearch);
                recursiveSearch(row + 1, col - 1, do8WaySearch);
                recursiveSearch(row - 1, col + 1, do8WaySearch);
            }

            // 6. Start back-tracking after the recursive calls
            //    are finished... Unmark piece to indicate it has
            //    not been visited. (use method in this class)
            clearVisitedFlag(row, col);

            // 7. Strip off the last character added to the word
            currWord = currWord.substring(0, currWord.length()-1);
        }
    }

    private void setVisitedFlag(int row, int col) {
        getGamePiece(row, col).setVisitedFlag();
        if(GUI_ENABLED) {
            gui.setVisitedFlag(row, col, currWord, currWord.length());
        }
    }

    /**
     * Removes the visited flag from the game piece at row, col on
     * the board.
     * @param row Row in which the game piece is located
     * @param col Column in which the game piece is located
     */
    private void clearVisitedFlag(int row, int col) {
        getGamePiece(row, col).clearVisitedFlag();
        if(GUI_ENABLED) {
            gui.clearVisitedFlag(row, col);
        }
    }

    /**
     * Record word as a valid word
     * @param word The word to be recorded as valid
     */
    private void validWord(String word) {
        foundWords.add(word);
        if(GUI_ENABLED) {
            gui.foundWord(word);
        }
    }

    /**
     * This is the starting point for beginning the search. It contains some
     * parameters to track the time and progress of the search on top of what
     * it's really meant for: starting the recursion over for the next letter
     * once recursion has finished for the one before it.
     */
    public void startSearching() {
        double totalTime = 0.0;
        DecimalFormat time = new DecimalFormat("00");
        DecimalFormat timeS = new DecimalFormat("00.####");
        for (int r=0; r<rows; ++r) {
            for (int c=0; c<cols; ++c) {
                if(!GUI_ENABLED) {
                    System.out.print("Searching from " + getGamePiece(r, c).getElement()
                            + " (" + r + ", " + c + ")... ");
                }
                long beginTime = System.currentTimeMillis();
                clearVisitedFlags();

                // TODO: In call below, true: 8-way search, false: 4-way search
                recursiveSearch(r, c, true);

                totalTime += (System.currentTimeMillis() - beginTime);
                if(!GUI_ENABLED) {
                    System.out.println(time.format(100.0*(r * cols + c + 1)
                            / gameBoard.size()) + "% done!");
                }
            }
        }
        if(!GUI_ENABLED) {
            totalTime = totalTime/1000;
            float seconds = (float) (totalTime % 60);
            int minutes = (int) (totalTime % 3600 / 60);
            int hours = (int) (totalTime / 3600);
            System.out.println("\nTotal recursion time: " + time.format(hours)
                    + ":" + time.format(minutes) + ":" + timeS.format(seconds));
        }
    }

    /**
     * Returns the game piece at the specified position
     * @param row Row of the game board
     * @param col Column of the game board
     * @return Returns the requested GamePiece
     */
    private GamePiece<Character> getGamePiece(int row, int col) {
        return gameBoard.get(row * cols + col);
    }

    /**
     * Clears visited flags for all of the game pieces on the game board.
     */
    private void clearVisitedFlags() {
        for (GamePiece<Character> g : gameBoard) {
            g.clearVisitedFlag();
        }
    }

    /**
     * Returns the collection of words to the driver application so they can be
     * printed to a file and displayed to the user.
     * @return The collection of words found during the search
     */
    public Collection<String> getResults() {
        return foundWords;
    }

}
