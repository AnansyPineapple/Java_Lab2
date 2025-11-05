package lab2_task1;

/**
 * An object of class Trie represents a prefix tree
 * 
 * @author Maria Khismatullova
 */
public class Trie {
    /** Constant number of english keyboard letters, digits and symbols */
    private static final int KEYBOARD_SIZE = 94;

    /** Root of a trie */
    private Node root = new Node();

    /**
     * Return the place of the character in an array of child nodes 
     * Precondition: character must be from latin alphabet or digit or keyboard symbol (ASCII 33-126)
     * 
     * @param character character to convert
     */
    private static int findPlace(char character) {
	// Define ascii code of character
	int code = character;

	// Ensure the character is on the keyboard
	int exc = '!'; // The smallest ascii - 33
	int tilde = '~'; // The biggest ascii - 126
	assert code >= exc && code <= tilde;

	// Calculate position of the character
	code -= exc;
	return code;
    }

    public static void main(String args[]) {
	Trie trie = new Trie();
	trie.insert("a");
	trie.insert("ce");
	trie.insert("cho");
	trie.insert("chea");
	trie.insert("cher");
	trie.insert("che");
	
	printWords(trie.root, new StringBuilder());
	
	//trie.getByPrefix("ch");
	pw(trie.getByPrefix("ch"));
    }
    
    private static void pw(String[] array) {
	for (int i = 0; i < array.length; i++) {
	    if (i > 0) System.out.print(", ");
	    System.out.print(array[i]);
	}
    }
    
    private static void printWords(Node node, StringBuilder currentWord) {
	    if (node.isTerminal()) {
	        System.out.println("  " + currentWord.toString());
	    }
	    
	    for (int i = 0; i < 94; i++) {
	        Node child = node.getChild(i);
	        if (child != null) {
	            char ch = (char) (i + '!');
	            currentWord.append(ch);
	            printWords(child, currentWord);
	            currentWord.deleteCharAt(currentWord.length() - 1);
	        }
	    }
	}
    /**
     * Insert a word into a trie 
     * Precondition: word consists of valid characters only
     * 
     * @param word word to insert
     */
    public void insert(String word) {
	// Start from root
	Node v = root;

	// Form a branch by processing each character in a word
	for (int i = 0; i < word.length(); i++) {
	    char ch = word.charAt(i);
	    int index = findPlace(ch);

	    // Create new node if path does not exist yet
	    if (v.getChild(index) == null) {
		v.setChild(index);
	    }

	    // Move to the next character
	    v = v.getChild(index);
	}

	// Mark last character as ending
	v.setTerminal(true);
    }

    /**
     * Find a word in the trie 
     * Precondition: word consists of valid characters only
     * 
     * @param word word to find
     * @return true if word was found, false otherwise
     */
    public boolean contains(String word) {
	// Start from root
	Node v = root;

	// Find the word in a branch by processing each character
	for (int i = 0; i < word.length(); i++) {
	    char ch = word.charAt(i);
	    int index = findPlace(ch);

	    // Return false if next character does not exist in a branch
	    if (v.getChild(index) == null) {
		return false;
	    }

	    // Move to next the character
	    v = v.getChild(index);
	}

	// Word exists if the final node is terminal only
	return v.isTerminal();
    }

    /**
     * Check if any words in the trie start with the given prefix 
     * Precondition: prefix consists of valid characters only
     * 
     * @param prefix prefix to search for
     * @return true if at least one word was found, false otherwise
     */
    public boolean startsWith(String prefix) {
	// Start from root
	Node v = root;

	// Find the word by prefix in a branch by processing each character
	for (int i = 0; i < prefix.length(); i++) {
	    char ch = prefix.charAt(i);
	    int index = findPlace(ch);

	    // Return false if next character does not exist in a branch
	    if (v.getChild(index) == null) {
		return false;
	    }

	    // Move to next the character
	    v = v.getChild(index);
	}

	// Word with prefix exists regardless of node terminality
	return true;
    }
    
    /**
     * Get all words with given prefix
     * Precondition: prefix consists of valid characters only
     * 
     * @param prefix prefix to search for
     * @return array with found words
     */
    public String[] getByPrefix(String prefix) {
	// Start traversal from the root
	Node v = root;
	
	// If there are no words with the prefix, return empty array
	if (!startsWith(prefix)) {
	    return new String[0];
	}
	else {
	    // Otherwise traverse prefix first
	    for (int i = 0; i < prefix.length(); i++) {
		char ch = prefix.charAt(i);
		int index = findPlace(ch);
		v = v.getChild(index);
	    }
	    // { v - points to the last prefix character }
	    
	    // Traverse child branches of v and collect words
	    String[] result = new String[0];
	    int exc = '!';
	    for (int i = 0; i < KEYBOARD_SIZE; i++) {
		if (v.getChild(i) != null) {
		    char word = (char)(i + exc);
		    String[] words = preOrder(v.getChild(i), prefix + word, prefix);
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
     * 
     * @param node current given vertex
     * @param word current formed word
     * @param prefix the original prefix
     * @return array of found words
     */
    public String[] preOrder(Node node, String word, String prefix) {
	String[] result = new String[0];
	
	// If word ending node is met, add word to result
	if (node.isTerminal()) {
	    String[] wordToAdd = new String[1];
	    wordToAdd[0] = word;
	    result = concatArrays(result, wordToAdd);
	}
	// { result contains all found words at this point }
	
	// Traverse child branches recursively to build words
	for (int i = 0; i < KEYBOARD_SIZE; i++) {
	    if (node.getChild(i) != null) {
		int exc = '!';
		char ch = (char)(i + exc);
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
    public String[] concatArrays(String[] array1, String[] array2) {
	// Create array with combined length of both arrays
	String[] mergedArray = new String[array1.length + array2.length];
	
	// Copy elements from both arrays into the new one
	System.arraycopy(array1, 0, mergedArray, 0, array1.length);
	System.arraycopy(array2, 0, mergedArray, array1.length, array2.length);
	// { mergedArray contains elements from array1, followed by elements of array2 }
	
	return mergedArray;
    }
}