package lab2_task1;

/**
 * An object of class Node represents a single trie node 
 * @author Maria Khismatullova
 */
public class Node {
    /** Constant number of english keyboard letters, digits and symbols ' and - */
    private static final int KEYBOARD_SIZE = 64; // 26 + 26 + 10 + 1 + 1
    /** children[0..KEYBOARD_SIZE-1] are the child nodes */
    private Node[] children;

    /** An indicator of whether it is an ending letter */
    private boolean terminal;

    /** Constructor: an instance with a potential keyboard-size number of children and supposedly non-terminal */
    public Node() {
	this.children = new Node[KEYBOARD_SIZE];
	this.terminal = false;
    }

    /** Return child nodes */
    public Node[] getChildren() {
	return children;
    }

    /**
     * Return a certain child node 
     * Precondition: index in 0..63     
     * @param index index of child node in array
     */
    public Node getChild(int index) {
	return children[index];
    }

    /** Return true if character is ending and false if not */
    public boolean isTerminal() {
	return terminal;
    }

    /**
     * Add new child node at certain index 
     * Precondition: index in 0..63     
     * @param index index of child node in array
     */
    public void setChild(int index) {
	this.children[index] = new Node();
    }

    /**
     * Set the flag indicating whether character is ending or not     
     * @param terminal true if node ends a word, false otherwise
     */
    public void setTerminal(boolean terminal) {
	this.terminal = terminal;
    }
}
