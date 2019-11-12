

public class Node<T> 
{
	  /**
    The Node class stores a list element
    and a reference to the next node.
	   */
	T value;   // Value of a list element
    Node next;      // Next node in the list
    Node prev;      // Previous element in the list
	  
    /**
       Constructor.            
       @param val The element to be stored in the node.
       @param n The reference to the successor node.
       @param p The reference to the predecessor node.
    */
	  
    Node(T val, Node n, Node p)
    {
        value = val;
        next = n;
        prev = p;
    }
	   
    /**
       Constructor. 
       @param val The element to be stored in the node.
    */
	  
    Node(T val)
    {
       // Just call the other (sister) constructor
       this(val, null, null);            
    }
}
