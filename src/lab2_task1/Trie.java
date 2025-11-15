package lab2_task1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

/**
 * An object of class Trie represents a prefix tree
 * @author Maria Khismatullova
 */
public class Trie {
    /** Constant number of english keyboard letters, digits and symbols ' and - */
    private static final int KEYBOARD_SIZE = 64; // 26 + 26 + 10 + 1 + 1
    
    /** Indexes of apostrophe and hyphen in child nodes array */
    private static final int APOSTROPHE_INDEX = 62;
    private static final int HYPHEN_INDEX = 63;

    /**
     * Return the place of the character in an array of child nodes 
     * Precondition: character must be from latin alphabet or digit/hyphen/apostrophe
     * @param character character to convert
     * @return index position in child nodes array
     */
    private static int findPlace(char character) {
	// Calculate position of the character
	if (character >= 'a' && character <= 'z') {
	    return character - 'a'; // 0-25
	}
	else if (character >= 'A' && character <= 'Z') {
	    return character - 'A' + 26; // 26-51
	}
	else if (character >= '0' && character <= '9') {
	    return character - '0' + 52; // 52-61
	}
	else if (character == '\'') {
	    return APOSTROPHE_INDEX;
	}
	else if (character == '-') {
	    return HYPHEN_INDEX;
	}
	else {
	    throw new IllegalArgumentException("Invalid index");
	}
    }
    
    /**
     * Return char according to the position on element in array 
     * Precondition: index in 0..63
     * @param index index in child nodes array
     * @return character at this position
     */
    private static char indexToChar(int index) {
	if (index >= 0 && index <= 25) {
	    return (char)(index + 'a'); // a-z
	}
	else if (index >= 26 && index <= 51) {
	    return (char)(index + 'A' - 26); // A-Z
	}
	else if (index >= 52 && index <= 61) {
	    return (char)(index + '0' - 52); // 0-9
	}
	else if (index == APOSTROPHE_INDEX) {
	    return '\'';
	}
	else if (index == HYPHEN_INDEX) {
	    return '-'; 
	}
	else {
	    throw new IllegalArgumentException("Invalid character");
	}
    }
    
    /**
     * Check character for validity
     * @param character character to check
     * @return true if valid, false if not
     */
    private static boolean isValid(char character) {	
	if ((character >= 'a' && character <= 'z') 
		|| (character >= 'A' && character <= 'Z') 
		|| (character >= '0' && character <= '9') 
		|| character == '\'' 
		|| character == '-') {
	    return true;
	}
	else {
	    return false;
	}
    }
    
    /**
     * Clear word from all characters except for letters, digits, hyphen and apostrophe
     * Precondition: word consists of valid characters only
     * @param word word to clean
     * @return word which consists from letters only
     */
    private static String cleanWord(String word) {
	// Delete all characters except for letters, hyphen and apostrophe
	return word.replaceAll("[^a-zA-Z0-9-']", "");	 
    }
    
    /** Open the main window */
    public static void main(String args[]) {
	window();
    }
    
    /**
     * Set font for Java Swing object
     * @param obj component to set the font on
     * @param isBold true for bold font, false for usual
     */
    private static void setFont(JComponent obj, boolean isBold) {
	// Handle custom font setting with system font on fallback
	try {
	    // { font files exist }
	    Font fontUsual = Font.createFont(Font.TRUETYPE_FONT, new File("ui_files/DejaVuSansMono.ttf"));
	    Font fontBold = Font.createFont(Font.TRUETYPE_FONT, new File("ui_files/DejaVuSansMono-Bold.ttf"));
	    // { Fonts created successfuly }
	    
	    // Set font sizes
	    Font sizedFontUsual = fontUsual.deriveFont(13f);
	    Font sizedFontBold = fontBold.deriveFont(17f);
	
	    // Apply certain font based on isBold parameter
	    if (isBold) {
		obj.setFont(sizedFontBold);
		// {obj has custom bold font at 17pt }
	    }
	    else {
		obj.setFont(sizedFontUsual);
		// {obj has custom usual font at 13pt }
	    }
	}
	catch (FontFormatException | IOException e) {
	    // Create fallback fonts using system one
	    Font fallFontUsual = new Font("Arial", Font.PLAIN, 13);
	    Font fallFontHead = new Font("Arial", Font.PLAIN, 17);
	    
	 // Apply certain font based on isBold parameter
	    if (isBold) {
		obj.setFont(fallFontHead);
		// {obj has system bold font at 17pt }
	    }
	    else {
		obj.setFont(fallFontUsual);
		// {obj has system usual font at 13pt }
	    }
	}
    }
    
    /**
     * Highlight words starting with prefix in the text
     * @param textArea field containing text
     * @param text text to search in
     * @param prefix prefix to search for
     * @param foundWords array of found words
     */
    private static void highlightWords(JTextArea textArea, String text, String prefix, String[] foundWords) {
	// Create new highlighter and clear previous ones
	Highlighter highlighter = textArea.getHighlighter();
	highlighter.removeAllHighlights();	  
	    
	// Search through the text for suitable words
	for (int i = 0; i < text.length();) {
	    // Skip invalid characters
	    // { i <= current position in the text }
	    while (i < text.length() && !isValid(text.charAt(i))) {
	        i++;
	    }
	    
	    // Finish search if reached end
	    if (i >= text.length()) {
	        break;
	    }
	        
	    // Find word bounds
	    int start = i;
	    while (i < text.length() && isValid(text.charAt(i))) {
	        i++;
	    }
	    int end = i;
	        
	    // Extract the word
	    String originalWord = text.substring(start, end);
	    String cleanedWord = cleanWord(originalWord);
	        
	    // Check whether word should be highlighted
	    // { cleanedWord contains valid characters only }
	    boolean shouldHighlight = cleanedWord.startsWith(prefix);	        
	    if (!shouldHighlight) {
	        for (String foundWord : foundWords) {
	            if (cleanedWord.equals(foundWord)) {
	                shouldHighlight = true;
	                break;
	            }
	        }
	    }
	        
	    // Highlight the word if it is suitable
	    if (shouldHighlight && !cleanedWord.isEmpty()) {
	        try {
	            highlighter.addHighlight(start, end, new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW));
		} 
	        catch (BadLocationException e) {
	            // In case of error all highlights are removed
	            highlighter.removeAllHighlights();
	            e.printStackTrace();
		}
	    }
	}
    }

    /**
     * Create search button for the window
     * Precondition: textArea and wordField contain valid characters only
     * @param textArea contents of text area to search through
     * @param wordField contents of word field containing search prefix
     * @param frame main window frame for dialog placement
     * @return button with action listener
     */
    private static JButton createButton(JTextArea textArea, JTextField wordField, JFrame frame) {
	// Create button with settings
	JButton button = new JButton();
	button.setPreferredSize(new Dimension(30, 30));
	
	// Set button appearance
	ImageIcon icon = new ImageIcon("ui_files/icons8-loupe-30.png"); // Does not require try-catch because the absence of icon will not cause an exception
	Image scaledIcon = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
	button.setIcon(new ImageIcon(scaledIcon));
	button.setBackground(new Color(255, 255, 255));
	button.setCursor(new Cursor(Cursor.HAND_CURSOR));
	
	// Add action listener for button to function
	button.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {	
		// Initialize new trie root on each call
		Node root = new Node();
		    
		// Trim input text
		String text = textArea.getText().trim();
		String word = wordField.getText().trim();
		   
		// Truthify both field are not empty
		if (text.isEmpty() || word.isEmpty()) {
		    JOptionPane.showMessageDialog(frame, "Both fields must be filled in!", "Warning", JOptionPane.WARNING_MESSAGE);
		    return;
		}
		// { both fields must not be empty at this point }
		else {
		    // Clean search word
		    String cleanedSearchWord = cleanWord(word);
		    if (!cleanedSearchWord.equals(word)) {
			JOptionPane.showMessageDialog(frame, "Please enter valid prefix!", "Warning", JOptionPane.WARNING_MESSAGE);
	                return;
		    }
	                
	            // Build trie with each text word
		    String[] textWords = text.split("\\s+");
		    // { trie containt all processed words }
	            for (String textWord : textWords) {
	        		// Insert cleaned word
	        		insert(root, textWord);
	                assert contains(root, cleanWord(textWord));
	            }
	            // { trie contains all cleaned words from text }
	                
	            // Search for words by cleaned prefix
	            String[] foundWords = getByPrefix(root, cleanedSearchWord);
	            highlightWords(textArea, text, cleanedSearchWord, foundWords);
		}
	    } 
	});
	
	return button;
    }
    
    /**
     * Create left panel for instruction placement
     * @return first panel for main window
     */
    private static JPanel instructionPanel() {
	// Create panel with settings
	JPanel leftPanel = new JPanel();
	leftPanel.setPreferredSize(new Dimension(250, 400));
	leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
	leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	
	// Create instruction title
	JLabel insLabel = new JLabel("Instruction");
		
	// Create instruction contents with settings	
	JTextArea insArea = new JTextArea("Welcome to the Word Finder!\n"
		+ "1. Write your text in the text field\n"
		+ "2. Write a prefix you want to find words with in your text\n"
		+ "3. Press the button with loupe to proceed\n\n"
		+ "All found words will be highlighted in the text\n\n"
		+ "Rules:\n"
		+ "Text and prefix can consist from letters, digits, hyphen and apostrophe only");
	// Highlight word "highlighted" as an example
	Highlighter highlighter = insArea.getHighlighter();
	try {
	    highlighter.addHighlight(insArea.getText().indexOf("highlighted"), 
	    	insArea.getText().indexOf("highlighted") + "highlighted".length(), 
	    	new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW));
	} 
	catch (BadLocationException e) {
	    e.printStackTrace();
	}
		
	insArea.setAlignmentX(Component.LEFT_ALIGNMENT);
	insArea.setPreferredSize(new Dimension(230, 350));
		
	insArea.setEditable(false);
	insArea.setLineWrap(true);
	insArea.setWrapStyleWord(true);
		
	insArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	insArea.setBackground(Color.decode("#FFFFCC"));
	
	// Set fonts
	setFont(insLabel, true);
	setFont(insArea, false);
	
	// Set border
	Border sharedBorder = BorderFactory.createLineBorder(Color.GRAY, 1);
	insArea.setBorder(sharedBorder);
	
	// Assemble left panel
	leftPanel.add(insLabel);
	leftPanel.add(insArea);
	
	return leftPanel;
    }
    
    /**
     * Create right panel for text and word inputs
     * @param window main window frame for createButton function
     * @return second panel for main window
     */
    private static JPanel inputPanel(JFrame window) {
	// Create righ panel with settings
	JPanel rightPanel = new JPanel();
	rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
	rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
	// Create text input title with settings
	JLabel textLabel = new JLabel("Text: ");
	textLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
	// Create scrollable text input area with settings
	JTextArea textArea = new JTextArea();
	textArea.setAlignmentX(Component.LEFT_ALIGNMENT);
	textArea.setLineWrap(true);
	textArea.setWrapStyleWord(true);
		
	JScrollPane textScrollPane = new JScrollPane(textArea);
	textScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
	textScrollPane.setPreferredSize(new Dimension(380, 200));	
		
	// Create word input title with settings
	JLabel wordLabel = new JLabel("Prefix: ");
	wordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
	
	// Create word input area with settings
	JTextField wordField = new JTextField();	
	wordField.setPreferredSize(new Dimension(350,30));
	wordField.setAlignmentX(Component.LEFT_ALIGNMENT);
	
	// Create button
	JButton findButton = createButton(textArea, wordField, window);
	
	// Assemble word input area and button in a row
	JPanel rowPanel = new JPanel(new BorderLayout(0, 0));
	rowPanel.setMaximumSize(new Dimension(380, 30));
	rowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
	
	rowPanel.add(wordField, BorderLayout.CENTER);
	rowPanel.add(findButton, BorderLayout.EAST);
	
	// Set fonts
	setFont(textLabel, true);
	setFont(textArea, false);
	setFont(wordLabel, true);
	setFont(wordField, false);
	
	// Set borders
	Border sharedBorder = BorderFactory.createLineBorder(Color.GRAY, 1);
	textScrollPane.setBorder(sharedBorder);
	wordField.setBorder(sharedBorder);
	findButton.setBorder(sharedBorder);	
	
	// Assemble right panel
	rightPanel.add(textLabel);
	rightPanel.add(textScrollPane);
	rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
	rightPanel.add(wordLabel);
	rightPanel.add(rowPanel);
	rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));	
	
	return rightPanel;
    }
    
    /** Create UI */
    private static void window () {
	// Create window frame with settings and icon
	JFrame window = new JFrame("Word Finder");
	window.setSize(650, 400);
	window.setResizable(false);
	window.setLocationRelativeTo(null);
	window.setLayout(new BorderLayout());
	window.setIconImage(new ImageIcon("ui_files/icons8-loupe-30.png").getImage());	
	
	// Create panels
	JPanel leftPanel = instructionPanel();
	JPanel rightPanel = inputPanel(window);
	
	// Assemble frame
	window.add(leftPanel, BorderLayout.WEST);
	window.add(rightPanel, BorderLayout.CENTER);
	
	window.setVisible(true);
    }
    
    /**
     * Insert a word into a trie 
     * Precondition: word consists of valid characters only (ASCII 39, 45, 65-90, 97-122)  
     * @param root trie root
     * @param word word to insert
     */
    private static void insert(Node root, String word) {
	// Clean word before insertion
	String cleanedWord = cleanWord(word);
	if (cleanedWord.isEmpty()) {
	    return;
	}
	
	// Form a branch by processing each character in a cleaned word
	for (int i = 0; i < cleanedWord.length(); i++) {
	    char ch = cleanedWord.charAt(i);
	    int index = findPlace(ch);

	    // Create new node if path does not exist yet
	    if (root.getChild(index) == null) {
		root.setChild(index);
	    }

	    // Move to the next character
	    root = root.getChild(index);
	}

	// Mark last character as ending
	root.setTerminal(true);
    }

    /**
     * Find a word in the trie 
     * Precondition: word consists of valid characters only (ASCII 39, 45, 65-90, 97-122)     
     * @param root trie root
     * @param word word to find
     * @return true if word was found, false otherwise
     */
    private static boolean contains(Node root, String word) {
	// Find the word in a branch by processing each character
	for (int i = 0; i < word.length(); i++) {
	    char ch = word.charAt(i);
	    int index = findPlace(ch);

	    // Return false if next character does not exist in a branch
	    if (root.getChild(index) == null) {
		return false;
	    }

	    // Move to next the character
	    root = root.getChild(index);
	}

	// Word exists if the final node is terminal only
	return root.isTerminal();
    }

    /**
     * Check if any words in the trie start with the given prefix 
     * Precondition: prefix consists of valid characters only (ASCII 39, 45, 65-90, 97-122)     
     * @param root trie root
     * @param prefix prefix to search for
     * @return true if at least one word was found, false otherwise
     */
    private static boolean startsWith(Node root, String prefix) {
	// Find the word by prefix in a branch by processing each character
	for (int i = 0; i < prefix.length(); i++) {
	    char ch = prefix.charAt(i);
	    int index = findPlace(ch);

	    // Return false if next character does not exist in a branch
	    if (root.getChild(index) == null) {
		return false;
	    }

	    // Move to next the character
	    root = root.getChild(index);
	}

	// Word with prefix exists regardless of node terminality
	return true;
    }
    
    /**
     * Get all words with given prefix
     * Precondition: prefix consists of valid characters only (ASCII 39, 45, 65-90, 97-122)      
     * @param root trie root
     * @param prefix prefix to search for
     * @return array with found words
     */
    private static String[] getByPrefix(Node root, String prefix) {
	// If there are no words with the prefix, return empty array
	if (!startsWith(root, prefix)) {
	    return new String[0];
	}
	else {
	    // Otherwise traverse prefix first
	    for (int i = 0; i < prefix.length(); i++) {
		char ch = prefix.charAt(i);
		int index = findPlace(ch);
		root = root.getChild(index);
	    }
	    // { v - points to the last prefix character }
	    
	    // Traverse child branches of v and collect words
	    String[] result = new String[0];
	    if (root.isTerminal()) {
	        result = new String[]{prefix};
	    }
	    for (int i = 0; i < KEYBOARD_SIZE; i++) {
		if (root.getChild(i) != null) {
		    char word = indexToChar(i);
		    String[] words = preOrder(root.getChild(i), prefix + word, prefix);
		    result = concatArrays(result, words);
		}
	    }
	    // { result contains whole subtree }	  
	    
	    return result;
	}	
    }
    
    /**
     * Traverse all child branches of given vertex
     * Precondition: node represents a valid trie node     
     * @param node current given vertex
     * @param word current formed word
     * @param prefix the original prefix
     * @return array of found words
     */
    private static String[] preOrder(Node node, String word, String prefix) {
	String[] result = new String[0];
	
	// If word ending node is met, add word to result
	if (node.isTerminal() && !word.isEmpty()) {
	    String[] wordToAdd = new String[1];
	    wordToAdd[0] = word;
	    result = concatArrays(result, wordToAdd);
	}
	// { result contains all found words at this point }
	
	// Traverse child branches recursively to build words
	for (int i = 0; i < KEYBOARD_SIZE; i++) {
	    if (node.getChild(i) != null) {
		char ch = indexToChar(i);
		String[] children = preOrder(node.getChild(i), word + ch, prefix);
		result = concatArrays(result, children);
	    }
	}
	// { result contains all found words in the subtree }
	
	return result;
    }
    
    /**
     * Concatenate two given arrays
     * @param array1 first array
     * @param array2 second array
     * @return one array which contains elements from both given ones
     */
    private static String[] concatArrays(String[] array1, String[] array2) {
	// Create array with combined length of both arrays
	String[] mergedArray = new String[array1.length + array2.length];
	
	// Copy elements from both arrays into the new one
	System.arraycopy(array1, 0, mergedArray, 0, array1.length);
	System.arraycopy(array2, 0, mergedArray, array1.length, array2.length);
	// { mergedArray contains elements from array1, followed by elements of array2 }
	
	return mergedArray;
    }
}