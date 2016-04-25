package lab6;

import javax.swing.border.CompoundBorder;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.util.concurrent.TimeUnit;

/**
 * Class that maintains a graphical user interface that
 * shows the progress of the word search app as the
 * word finder algorithm is run.
 * @author taylor
 * @version 2014.04.03
 */
public class UI extends JFrame {
    /**
     * The delay time (in milliseconds) if Slow Mo button
     * is depressed
     */
    private static final int SLEEP_TIME = 100;

    /**
     * Small font to be used in the GUI
     */
    private static final Font SMALL_FONT = new Font("Segoe UI Light", Font.TRUETYPE_FONT, 12);

    /**
     * Large font to be used in the GUI
     */
    private static final Font LARGE_FONT = new Font("Segoe UI Light", Font.TRUETYPE_FONT, 18);

    /**
     * Panel containing the game board
     */
    private JPanel gameBoardPanel;

    /**
     * Area for reporting statistics on the current word search
     */
    private JTextArea statistics;

    /**
     * Area containing all of the words on the game board that
     * are found in the dictionary
     */
    private JTextArea foundWords;

    /**
     * Button for selecting slow motion
     */
    private JToggleButton slowMoButton;

    /**
     * The number of words have been looked up in the dictionary
     */
    private int numberOfWordsSearched = 0;

    /**
     * The number of words have been found in the dictionary
     */
    private int numberOfWordsFound = 0;

    /**
     * Total execution time
     */
    private long totalTime = 0;

    /**
     * Time when a new game piece has been visited
     */
    private long startTime;

    /**
     * The current word being looked up in the dictionary
     */
    private String currentWord;

    /**
     * Number of rows on the game board
     */
    private int rows;

    /**
     * Number of columns on the game board
     */
    private int cols;

    /**
     * Row of the game piece currently being visited
     */
    private int currentRow;

    /**
     * Column of the game piece currently being visited
     */
    private int currentCol;

    /**
     * Maximum length of words to be searched for
     */
    private int maxLength;

    /**
     * A list of panels containing a game piece
     */
    private List<JPanel> gamePiecePanels;

    /**
     * Default constructor for the Visual Guide
     * @param gameBoard the grid to be displayed
     * @param rows the number of rows in the grid
     * @param cols the number of columns in the grid
     * @param maxLength the maximum number of characters that can be in one word
     */
    public UI(List<GamePiece<Character>> gameBoard, int rows, int cols, int maxLength) {
        super("Word Search");
        this.cols = cols;
        this.rows = rows;
        this.maxLength = maxLength;

        // Initialize the container of JPanels, one for each game piece
        gamePiecePanels = new ArrayList<>();
        for(GamePiece piece : gameBoard) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createLineBorder(new Color(244, 244, 244)));
            JLabel label = new JLabel(piece.getElement().toString(), SwingConstants.CENTER);
            label.setFont(LARGE_FONT);
            panel.add(label, BorderLayout.CENTER);
            gamePiecePanels.add(panel);
        }

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 400);

        // Build the GUI
        setLayout(new GridLayout(1, 2));
        add(buildLeftPanel());
        add(buildRightPanel());

        setVisible(true);
    }

    /**
     * Updates the GUI when a word has been found in the dictionary
     * @param word The correctly spelled word
     */
    public void foundWord(String word) {
        ++numberOfWordsFound;
        foundWords.setText(foundWords.getText() +  (foundWords.getText().length() != 0 ? "\n" : "") +  word);
        statistics.setText(generateStats());

        statistics.repaint();
        foundWords.repaint();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Highlights the just visited game piece and updates
     * the statistics panel.
     * @param row the row of the game piece
     * @param col the column of the game piece
     * @param currWord the word created by visiting this game piece
     * @param wordLength length of the currently constructed word
     */
    public void setVisitedFlag(int row, int col, String currWord, int wordLength) {
        // Delay if slow mo is selected
        if(slowMoButton.isSelected()) {
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Set color to Red if first letter, otherwise set to yellow
        // with increased transparency towards the end of the word.
        Color backgroundColor;
        if(wordLength == 1) {
            backgroundColor = Color.RED;
            currentRow = row;
            currentCol = col;
        } else {
            backgroundColor = new Color(255, 255, 0, (int)(255-240.0*wordLength/maxLength));
        }

        JPanel panel = gamePiecePanels.get(offset(row, col));
        panel.setBackground(backgroundColor);
        panel.setOpaque(true);

        startTime = System.currentTimeMillis();
        currentWord = currWord;
        ++numberOfWordsSearched;
        statistics.setText(generateStats());

        statistics.repaint();
        gameBoardPanel.repaint();
    }

    /**
     * Removes the highlight on most recently visited game piece and
     * updates the statistics panel
     * @param row the row of the game piece
     * @param col the column of the game piece
     */
    public void clearVisitedFlag(int row, int col) {
        gamePiecePanels.get(offset(row, col)).setOpaque(false);
        long timeEnd = System.currentTimeMillis();
        totalTime += timeEnd - startTime;
        statistics.setText(generateStats());

        statistics.repaint();
        gameBoardPanel.repaint();

    }

    /**
     * Builds the left half of the GUI which contains the game board
     * with a * "Slow Mo" toggle button below it.
     */
    private JPanel buildLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        gameBoardPanel = new JPanel(new GridLayout(rows, cols));
        gameBoardPanel.setBorder(createBorder("Game Board"));
        for(JPanel panel : gamePiecePanels) {
            gameBoardPanel.add(panel);
        }
        leftPanel.add(gameBoardPanel);
        JPanel slowMoPanel = new JPanel(new FlowLayout());
        slowMoButton = new JToggleButton("Slow Mo");
        slowMoButton.setFont(SMALL_FONT);
        slowMoButton.setToolTipText(SLEEP_TIME + " ms");
        slowMoPanel.add(slowMoButton);
        leftPanel.add(slowMoPanel, BorderLayout.PAGE_END);
        return leftPanel;
    }

     /**
     * Builds the right half of the GUI which contains two panels,
     * one for the found words and one for statistics.
     */
    private JPanel buildRightPanel() {
        JPanel rightPanel = new JPanel(new GridLayout(1,2));
        foundWords = new JTextArea();
        foundWords.setFont(SMALL_FONT);
        foundWords.setEditable(false);
        JScrollPane foundWordsPane = new JScrollPane();
        foundWordsPane.setBorder(createBorder("Found Words"));
        foundWordsPane.setViewportView(foundWords);
        rightPanel.add(foundWordsPane);
        statistics = new JTextArea(generateStats());
        statistics.setFont(SMALL_FONT);
        statistics.setBorder(createBorder("Statistics"));
        statistics.setEditable(false);
        rightPanel.add(statistics);
        return rightPanel;
    }

    /**
     * Generates a string containing all of the text for the
     * statistics panel
     * @return Summary of statistical information
     */
    private String generateStats() {
        DecimalFormat format = new DecimalFormat("###,###,###,###,###,###,###");
        DecimalFormat microFormat = new DecimalFormat("00.####");

        return "\nProgress: " + 100.0*(currentRow*cols+currentCol+1)/gamePiecePanels.size() + "%"
                + "\n\nAverage per word: "
                + microFormat.format(1000.0*totalTime/numberOfWordsSearched) + "\u00B5s"
                + "\n\nTotal time: " + formatTime(totalTime)
                + "\n\nWords Searched: " + format.format(numberOfWordsSearched)
                + "\n\nWords Found: " + numberOfWordsFound
                + "\n\nCurrent Word: \n\n" + currentWord;
    }

    /**
     * Format a time for display
     * @param theTime the time to display in milliseconds
     * @return a formatted representation String representation fo the time
     */
    private String formatTime(long theTime) {
        long hours = TimeUnit.MILLISECONDS.toHours(theTime);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(theTime) - TimeUnit.HOURS.toMinutes(hours);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(theTime) - TimeUnit.HOURS.toSeconds(hours)
                - TimeUnit.MINUTES.toSeconds(minutes);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Calculates the position in the array for an element
     * based on its position in the grid
     * @param row the row of the game piece
     * @param col the column of the game piece
     * @return the index of the element in the array
     */
    private int offset(int row, int col) {
        return row * cols + col;
    }

    /**
     * Creates a fancy border with a title
     * @param title Title for the border
     * @return The border
     */
    private CompoundBorder createBorder(String title) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder( null, title + ":",
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        LARGE_FONT),
        BorderFactory.createEmptyBorder(1, 1, 1, 1));
    }
}
