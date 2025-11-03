package lab2_task1;

/** An object of class Node represents a single trie node
 * @author Maria Khismatullova
 */
public class Node {
    /** Constant number of latin, russian letters and numbers */
    private static final int ALPHABET_SIZE = 69;
    /** children[0..ALPHABET_SIZE-1] are the child nodes */
    private Node[] children;
    
    /** An indicator of whether it is an ending letter */
    private boolean terminal;
    
    /** Constructor: an instance with a potential alphabet-size number of children and supposedly non-terminal
     */
    public Node() {
	this.children = new Node[ALPHABET_SIZE];
	this.terminal = false;
    }
    
    /** Return child nodes */
    public Node[] getChildren() {
	return children;
    }
    
    /** Return a certain child node
     * Precondition: index in 0..68
     * @param index index of child node in array
     */
    public Node getChild(int index) {
	return children[index];
    }
    
    /** Return whether letter is ending */
    public boolean isTerminal() {
	return terminal;
    }
}
