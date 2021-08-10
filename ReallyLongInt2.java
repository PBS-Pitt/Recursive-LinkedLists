
// CS 0445 Spring 2021
// This is a partial implementation of the ReallyLongInt class.  You need to
// complete the implementations of the remaining methods.  Also, for this class
// to work, you must complete the implementation of the LinkedListPlus class.
// See additional comments below.

public class ReallyLongInt2 	extends LinkedListPlus2<Integer> 
							implements Comparable<ReallyLongInt2>
{
	private ReallyLongInt2()
	{
		super();
	}

	// Data is stored with the LEAST significant digit first in the list.  This is
	// done by adding all digits at the front of the list, which reverses the order
	// of the original string.  Note that because the list is doubly-linked and 
	// circular, we could have just as easily put the most significant digit first.
	// You will find that for some operations you will want to access the number
	// from least significant to most significant, while in others you will want it
	// the other way around.  A doubly-linked list makes this access fairly
	// straightforward in either direction.
	public ReallyLongInt2(String s)
	{
		super();
		int digit = -1;
		int count = 0;
		// Iterate through the String, getting each character and converting it into
		// an int.  Then make an Integer and add at the front of the list.  Note that
		// the add() method (from A2LList) does not need to traverse the list since
		// it is adding in position 1.  Note also the the author's linked list
		// uses index 1 for the front of the list.
		rec_string(s, count, digit);
	}

	public void rec_string(String s, int count, int digit){
		char c;
		if (count < s.length()){
			c = s.charAt(count);
			if (('0' <= c) && (c <= '9'))
			{
				digit = c - '0';
				// Do not add leading 0s
				if (!(digit == 0 && this.getLength() == 0)) 
					this.add(1, Integer.valueOf(digit));
			}
			else throw new NumberFormatException("Illegal digit " + c);
			rec_string(s, count + 1, digit);
		}
		if (digit == 0 && this.getLength() == 0)
			this.add(1, Integer.valueOf(digit));
	}
		

	

	// Copy constructor can just call super()
	public ReallyLongInt2(ReallyLongInt2 rightOp)
	{
		super(rightOp);
	}
	
	// Constructor with a long argument.  You MUST create the ReallyLongInt
	// digits by parsing the long argument directly -- you cannot convert to a String
	// and call the constructor above.  As a hint consider the / and % operators to
	// extract digits from the long value.
	public ReallyLongInt2(long X)
	{
		int length = String.valueOf(X).length();
		rec_long(X, length);
    }
    
    public void rec_long(long X, int length){
        
		long digit;
        if (length == 1){
            digit = X;
            this.add(1, (int) digit);
        } else if(length > 1){
		    digit = X / (long) Math.pow(10, (double) length - 1);
			this.add(1, (int) digit);
			X = X % (long) Math.pow(10, (double) length - 1);
            rec_long(X, length-1);
        }
    }
	
	// Method to put digits of number into a String.  Note that toString()
	// has already been written for LinkedListPlus, but you need to
	// override it to show the numbers in the way they should appear.
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		Node curr = firstNode;
		if (curr != null)
		{  
			return rec_toString(curr.getPrevNode(), sb);  							 
		}
		else {
			return sb.toString();
		}
	}

	private String rec_toString(Node curr, StringBuilder b){
		if (curr != firstNode)
		{
			b.append(curr.getData());
			return rec_toString(curr.getPrevNode(), b);
		} else{
			b.append(curr.getData());
			return b.toString();
		}
	}

	// See notes in the Assignment sheet for the methods below.  Be sure to
	// handle the (many) special cases.  Some of these are demonstrated in the
	// RLITest.java program.

	// Return new ReallyLongInt which is sum of current and argument
	public ReallyLongInt2 add(ReallyLongInt2 rightOp)
	{
		ReallyLongInt2 result = new ReallyLongInt2();
		Node curr1 = firstNode;
		Node curr2 = rightOp.firstNode;
		makeEqualLength(rightOp);
		rec_add(rightOp, result, 0, curr1, curr2, 0);
		
		return result;
	}

	private void rec_add(ReallyLongInt2 rightOp, ReallyLongInt2 result, int carry, Node curr1, Node curr2, int count){
		if(count < rightOp.getLength()){
			int sum = carry;
			carry = 0;
			int c1 = (int) curr1.getData();
			int c2 = (int) curr2.getData();
			sum += (c1 + c2);
			if(sum > 9){
				carry ++;
				sum = sum % 10;
			}
			result.add(sum);
			rec_add(rightOp, result, carry, curr1.getNextNode(), curr2.getNextNode(), count+1);
		} else if(count == rightOp.getLength()){
			result.add(carry);
			original(this);
			original(rightOp);
			original(result);
		}
		
	}

	// Returns the lists to their original lengths byt taking out leading 0
	public void original(ReallyLongInt2 a){
		Node curr = a.firstNode;
		rec_original(a, 0, curr.getPrevNode());
	}

	private void rec_original(ReallyLongInt2 a, int count, Node curr){
		if ((int) curr.getData() != 0){
			a.rightShift(count);
		} else if(count < a.getLength()){
			rec_original(a, count+1, curr.getPrevNode());
		}
	}

	// Makes both Lists equal lengths by adding 0 in front
	public void makeEqualLength(ReallyLongInt2 b){
		int difference;
		if (numberOfEntries < b.numberOfEntries){
			difference = b.numberOfEntries - numberOfEntries;
			rec_makeEqual(difference, 0, this);
		}else if(numberOfEntries > b.numberOfEntries){
			difference = numberOfEntries - b.numberOfEntries;
			rec_makeEqual(difference, 0, b);
		}
	}

	private void rec_makeEqual(int difference, int count, ReallyLongInt2 a){
		if (count < difference){
			a.add(0);
			rec_makeEqual(difference, count + 1, a);
		}
	}
	
	
	// Return new ReallyLongInt which is difference of current and argument
	public ReallyLongInt2 subtract(ReallyLongInt2 rightOp)
	{	
		ReallyLongInt2 result = new ReallyLongInt2();
		if (compareTo(rightOp) < 0){
			throw new ArithmeticException();
		} else if (compareTo(rightOp) == 0){
			result.add(0);
			return result;
		}
		Node curr1 = firstNode;
		Node curr2 = rightOp.firstNode;
		makeEqualLength(rightOp);
		rec_substract(rightOp, result, curr1, curr2, 0, 0);
		return result;
	}

	private void rec_substract(ReallyLongInt2 rightOp, ReallyLongInt2 result, Node curr1, Node curr2, int count, int carry){
		if (count < numberOfEntries){
			int c1 = (int) curr1.getData();
			int c2 = (int) curr2.getData() + carry;
			carry = 0;
			if (c1 < c2){
				c1 += 10;
				carry ++;
			}
			int sub = c1 - c2;
			result.add(sub);
			rec_substract(rightOp, result, curr1.getNextNode(), curr2.getNextNode(), count+1, carry);
		} else if(count == numberOfEntries){
			original(this);
			original(rightOp);
			original(result);
		}
		
	}

	// Return new ReallyLongInt which is product of current and argument
	public ReallyLongInt2 multiply(ReallyLongInt2 rightOp)
	{
		ReallyLongInt2 result = new ReallyLongInt2();
		result.add(0);
		ReallyLongInt2 subProduct = new ReallyLongInt2();
		Node curr1 = firstNode;
		Node curr2 = rightOp.firstNode;
		if (((rightOp.numberOfEntries == 1) && rightOp.contains(0)) || ((numberOfEntries == 1) && contains(0))){
			return result;
		}else{
			return rec_mult(rightOp, subProduct, result, curr1, curr2, 0, 0);
			
			
		}
	}

	public ReallyLongInt2 rec_mult(ReallyLongInt2 rightOp, ReallyLongInt2 subProduct, ReallyLongInt2 result, Node curr1, Node curr2, int count1, int carry){
		if (count1 < getLength()){
			int c1 = (int) curr1.getData();
			subProduct = rec_subProduct(rightOp, subProduct, curr2, 0, 0, c1);
			rec_add0(0, count1, subProduct);
			result = result.add(subProduct);
			subProduct.clear();
			return rec_mult(rightOp, subProduct, result, curr1.getNextNode(), curr2, count1+1, 0);
		} else {
			return result;
		}
	}

	public ReallyLongInt2 rec_subProduct (ReallyLongInt2 rightOp, ReallyLongInt2 subProduct, Node curr2, int carry, int count2, int c1){
		if (count2 < rightOp.getLength()){
			int mult = carry;
			carry = 0;
			int c2 = (int) curr2.getData();
			mult += (c1*c2);
			if(mult > 9){
				carry = mult / 10;
				mult = mult % 10;
			}
			subProduct.add(mult);
			return rec_subProduct(rightOp, subProduct, curr2.getNextNode(), carry,count2 +1, c1);
		} else {
			subProduct.add(carry);
			return subProduct;
		}
	}

	public ReallyLongInt2 rec_add0(int count, int limit, ReallyLongInt2 subProduct){
		if (count < limit){
			subProduct.add(1, 0);
			return rec_add0(count+1, limit, subProduct);
		} else{
			return subProduct;
		}
	}
	
	// Return -1 if current ReallyLongInt is less than rOp
	// Return 0 if current ReallyLongInt is equal to rOp
	// Return 1 if current ReallyLongInt is greater than rOp
	public int compareTo(ReallyLongInt2 rOp)
	{
		int result = 0;
		if (numberOfEntries < rOp.numberOfEntries){
			result = -1;
		}else if (numberOfEntries > rOp.numberOfEntries){
			result = 1;
		}else{
			Node curr1 = firstNode.getPrevNode();
			Node curr2 = rOp.firstNode.getPrevNode();
			result = rec_compare(curr1, curr2, 0);
		}
		return result;
	}

	private int rec_compare(Node curr1, Node curr2, int count){	
		if (count < numberOfEntries){
			int c1 = (int) curr1.getData();
			int c2 = (int) curr2.getData();
			if (c1 <  c2){
				return -1;
			}else if (c1 >  c2){
				return 1;
			}else{
				return rec_compare(curr1.getPrevNode(), curr2.getPrevNode(), count+1);
			}
		}else
			return 0;
}

	// Is current ReallyLongInt equal to rightOp?  Note that the argument
	// in this case is Object rather than ReallyLongInt.  It is written
	// this way to correctly override the equals() method defined in the
	// Object class.
	public boolean equals(Object rightOp)
	{
		ReallyLongInt2 temp = new ReallyLongInt2();
		temp = (ReallyLongInt2) rightOp;
		if (compareTo(temp) == 0){
			return true;
		}else{
			return false;
		}
	}
}
