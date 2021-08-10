public class LinkedListPlus2<T> extends A2LList {
    // Default constructor simply calls super()
	public LinkedListPlus2()
	{
		super();
	}
	
	// Copy constructor.  This is a "deepish" copy so it will make new
	// Node objects for all of the nodes in the old list.  However, it
	// is not totally deep since it does NOT make copies of the objects
	// within the Nodes -- rather it just copies the references.  The
	// idea of this method is as follows:  If oldList has at least one
	// Node in it, create the first Node for the new list, then iterate
	// through the old list, appending a new Node in the new list for each
	// Node in the old List.  At the end, link the Nodes around to make sure
	// that the list is circular.
	public LinkedListPlus2(LinkedListPlus2<T> oldList)
	{
		super();
		if (oldList.getLength() > 0)
		{
			// Special case for first Node since we need to set the
			// firstNode instance variable.
			Node temp = oldList.firstNode;
			Node newNode = new Node(temp.data);
			firstNode = newNode;
			
			// Now we traverse the old list, appending a new Node with
			// the correct data to the end of the new list for each Node
			// in the old list.  Note how the loop is done and how the
			// Nodes are linked.
			Node currNode = firstNode;
			temp = temp.next;
			rec_constructor(oldList, temp, currNode);
		}			
    }

    private void rec_constructor(LinkedListPlus2<T> oldList, Node oldNode, Node newNode){
        if (oldNode != oldList.firstNode)
		{
			Node temp = new Node(oldNode.data);
			newNode.next = temp;
			temp.prev = newNode;
			newNode = newNode.next;
			oldNode = oldNode.next;
			rec_constructor(oldList, oldNode, newNode);
			
		}
		else{
			newNode.next = firstNode;
			firstNode.prev = newNode;
			numberOfEntries = oldList.numberOfEntries;
		}
	}

	// Make a StringBuilder then traverse the nodes of the list, appending the
	// toString() of the data for each node to the end of the StringBuilder.
	// Finally, return the StringBuilder as a String.  Note that since the list
	// is circular, we cannot look for null.  Rather we must count the Nodes as
	// we progress down the list.
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		Node curr = firstNode;
		
		if (curr != null)
		{
			b.append(curr.getData());  
			b.append(" ");
			return rec_string(curr.getNextNode(), b);  
										 
		}
		else return b.toString();
	}

	private String rec_string(Node curr, StringBuilder b){
		if (curr != firstNode)
		{
			b.append(curr.getData());
			b.append(" ");
			return rec_string(curr.getNextNode(), b);
		} else
			return b.toString();
	}
	
	// Remove num items from the front of the list
	public void leftShift(int num)
	{	if ((num > 0) && (num< numberOfEntries)){
			remove(1);
			leftShift(num-1);
			
		}else if(num >= numberOfEntries){
			clear();
		}
	}

	// Remove num items from the end of the list
	public void rightShift(int num)
	{
		if ((num > 0) && (num< numberOfEntries)){
			remove(numberOfEntries);
			rightShift(num-1);
		}else if(num >= numberOfEntries){
			clear();
		}
	}

	// Rotate to the left num locations in the list.  No Nodes
	// should be created or destroyed.
	public void leftRotate(int num)
	{
		if (num > 0){
			leftRotate((num % numberOfEntries)-1);
			firstNode = firstNode.getNextNode();
		}else if( num == 0){
			firstNode = firstNode;
		}else{
			leftRotate(((num % numberOfEntries)+numberOfEntries)-1);		
			firstNode = firstNode.getNextNode();
		}
	}

	// Rotate to the right num locations in the list.  No Nodes
	// should be created or destroyed.
	public void rightRotate(int num)
	{
		if (num > 0){
			rightRotate((num % numberOfEntries)-1);
			firstNode = firstNode.getPrevNode();
		}else if( num == 0){
			firstNode = firstNode;
		}else{
			rightRotate(((num % numberOfEntries)+numberOfEntries)-1);		
			firstNode = firstNode.getPrevNode();
		}
	}
	
}
