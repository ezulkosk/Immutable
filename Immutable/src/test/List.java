//import javacop.annotations.Nonnull;
package test;

import javacop.annotations.Committed;
import javacop.annotations.Free;
import javacop.annotations.Immutable;
import javacop.annotations.Mutable;
import javacop.annotations.Nonnull;

import org.omg.CORBA.Environment;

public class List {

    Node root = new EmptyNode();
    @Mutable @Immutable @Free @Committed int field;
    @Immutable List testlist;
    int b = 2;
    int e = this.b;
    //Node r = this.root;
    @Free public void insert(int t) {
	int a = 1;
	
	
	int c = this.b;
	@Free int d = 0;
	int e = d;
	field = 2;
	Object o = new Object();
	
	root = root.insert(t);
    }
    
    public void testListAsParam(@Free List l){
    	Node n = l.root;
    	Object o = getClass();
    }
    
    public List(){
    	e = b;
    }
}

interface Node {
    Node insert(int t);
}

class DataNode implements Node {
    protected int value;
    @Nonnull protected Node next;
    int x = 1+2;
    public DataNode(int value, Node next) {
	int y = 2;
	int z = y;
	this.value = value;
	this.next = next;
    }

    public Node insert(int t) {
	next = next.insert(t);
	return this;
    }
}

class EmptyNode implements Node {

    @Free public Node insert(@Free int t) {
	return new DataNode(t, this);
    }

}