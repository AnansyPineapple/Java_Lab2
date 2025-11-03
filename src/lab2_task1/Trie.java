package lab2_task1;

/**
 * An object of class Trie represents a prefix tree
 * @author Maria Khismatullova
 */
public class Trie {
    /** Constant number of english keyboard letters, digits and symbols */
    private static final int KEYBOARD_SIZE = 94;
    
    /** Root of a trie */
    private Node root = new Node();
    
    /** Return the place of letter in an array of child nodes
     * Precondition: letter must be from latin alphabet or digit or keyboard symbol (ASCII 33-126)
     * @param letter letter to convert
     */
    private static int findPlace(char letter) {
	// Define ascii code of letter
	int code = letter;
	
	// Ensure the letter is on the keyboard
	int exc = '!'; // The smallest ascii - 33
	int tilde = '~'; // The biggest ascii - 126
	assert code >= exc && code <= tilde;
	
	// Calculate position of the letter
	code -= exc;
	return code;
    }
    
    public static void main(String args[]) {
	
    }
    
    /** Insert a word into a tree
     * Precondition: word consists of latin letters, digits and keyboard symbols only
     * @param word word to insert
     */
    public void insert(String word) {
	// Start from root
	Node v = root;
	
	// Form a branch by processing each letter in a word
	for (int i = 0; i < word.length(); i++) {
	    char ch = word.charAt(i);
	    int index = findPlace(ch);
	    // Create new node if path does not exist yet
	    if (v.getChild(index) == null) {
		v.setChild(index);
	    }
	    // Do the same for the next letter
	    v = v.getChild(index);
	}
	// Mark last letter as ending
	v.setTerminal(true);
    }
}