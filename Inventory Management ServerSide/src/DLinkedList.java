/**
   The DLinkedList class implements a doubly 
   Linked list. 
*/

class DLinkedList<T>
{
	
	private Node first;   // Head of the list
	private Node last;    // Last element on the list 
	 
	 /** 
	    Constructor.
	 */
	 
	 public DLinkedList()
	 {
	     first = null;
	     last = null;        
	 }

	 /**
	    The isEmpty method checks to see if the list
	    is empty.
	    @return true if list is empty, false otherwise.
	 */
	 
	 public boolean isEmpty()
	 {        
	     return first == null;
	 }
	 
	 /**
	    The size method returns the length of the list.
	    @return The number of elements in the list.
	 */
	 
	 public int size()
	 {
	    int count = 0;
	    Node p = first;   
	    while (p != null)
	    {
	        // There is an element at p
	        count ++;
	        p = p.next;
	    }
	    return count;
	 }
	 
	 /**
	    The add method adds to the end of the list.
	    @param e The value to add.       
	 */
	 
	 public void add(T e)
	 {
	   if (isEmpty()) 
	   {
	       last = new Node(e);
	       first = last;
	   }
	   else
	   {
	       // Add to end of existing list
	       last.next = new Node(e, null, last);
	       last = last.next;         
	   }      
	 }
	 
	 /**
	    This add method adds an element at an index.
	    @param e The element to add to the list.
	    @param index The index at which to add.
	    @exception IndexOutOfBoundsException 
			 When the index is out of bounds.
	 */
	 
	 public void add(int index, T e)
	 {
	      if (index < 0  || index > size()) 
	      {
	          String message = String.valueOf(index);
	          throw new IndexOutOfBoundsException(message);
	      }
	      
	      // Index is at least 0
	      if (index == 0)
	      {
	          // New element goes at beginning
	          Node p = first;            // Old first
	          first = new Node(e, p, null);
	          if (p != null)// WHEN the list is not empty
	              p.prev = first;             
	          if (last == null)//when the list is empty
	              last = first;
	          return;
	      }
	      
	      // pred will point to the predecessor
				// of the new node.
	      Node pred = first;       
	      for (int k = 1; k <= index - 1; k++)        
	      {
	         pred = pred.next;           
	      }
	      
	      // Splice in a node with the new element
	      // We want to go from  pred-- succ to 
	      // pred--middle--succ
	      Node succ = pred.next;
	      Node middle = new Node(e, succ, pred);
	      pred.next = middle;  
	      if (succ == null)             
	          last = middle;       
	      else            
	          succ.prev = middle;              
	 }
	 
	 /**
	    The toString method computes the string
	    representation of the list.
	    @return The string representation of the
	    linked list.
	 */
	 
	 public String toString()
	 {
	   StringBuilder strBuilder = new StringBuilder();
	   
	   // Use p to walk down the linked list
	   Node p = first;
	   while (p != null)
	   {
	      strBuilder.append(p.value + "\n"); 
	      p = p.next;
	   }      
	   return strBuilder.toString(); 
	 }
	 
	 
	 /**
	    The remove method removes the element
			 at a given position.
	    @param index The position of the element 
			 to remove. 
	    @return The element removed.   
	    @exception IndexOutOfBoundsException When		 
			 index is out of bounds.  
	 */
	 
	 public T remove(int index)
	 {
	    if (index < 0 || index >= size())  
	    {
	       String message = String.valueOf(index);
	       throw new IndexOutOfBoundsException(message);
	    }            
	   
	    // Locate the node targeted for removal
	    Node target = first;  
	    for (int k = 1; k <= index; k++)
	        target = target.next;
	    
	    T element = (T) target.value;  // Element to return
	    Node pred = target.prev;        // Node before the target
	    Node succ = target.next;        // Node after the target
	    
	    // Route forward and back pointers around
	    // the node to be removed
	    if (pred == null)       
	        first = succ;
	    else
	        pred.next = succ;
	    
	    if (succ == null)
	        last = pred;
	    else
	        succ.prev = pred;
	    
	    return element;        
	 }
	 
	 public T get(int index) 
	 {
		 if (index < 0 || index >= size())  
		    {
		       String message = String.valueOf(index);
		       throw new IndexOutOfBoundsException(message);
		    }  
		 Node target = first;  
		    for (int k = 1; k <= index; k++)
		        target = target.next;
		    
		    T element = (T) target.value;  // Element to return
		    
		    return element;
	 }
	 
	 public void set(int index,T newValue) 
	 {
		 if (index < 0 || index >= size())  
		    {
		       String message = String.valueOf(index);
		       throw new IndexOutOfBoundsException(message);
		    }  
		 Node target = first;  
		    for (int k = 1; k <= index; k++)
		        target = target.next;
		
		    target.value=newValue;
	 }  
}