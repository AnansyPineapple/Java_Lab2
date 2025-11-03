package lab2_task1;

/** An object of class Node represents a single trie node
 * @author Maria Khismatullova
 */
public class Node {
    /** Constant number of english keyboard letters, digits and symbols */
    private static final int KEYBOARD_SIZE = 94;
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
    
    /** Return a certain child node
     * Precondition: index in 0..93
     * @param index index of child node in array
     */
    public Node getChild(int index) {
	return children[index];
    }
    
    /** Return whether letter is ending */
    public boolean isTerminal() {
	return terminal;
    }
    
    public void setChild(int index) {
	this.children[index] = new Node();
    }
    
    public void setTerminal(boolean terminal) {
	this.terminal = terminal;
    }
}
