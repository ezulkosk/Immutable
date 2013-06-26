//import javacop.annotations.Nonnull;
package test;

import javacop.annotations.Committed;
import javacop.annotations.Free;
import javacop.annotations.Nonnull;
import javacop.annotations.Mutable;
import javacop.annotations.Immutable;

public class List {

    Node root = new EmptyNode();
    @Mutable @Immutable @Free @Committed int field;
    int b;
    //Node r = this.root;
    public void insert(int t) {
	int a = 1;
	
	int c = b;
	@Free int d = 0;
	int e = d;
	field = 2;
	Object o = new Object();
	
	root = root.insert(t);
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

    public Node insert(int t) {
	return new DataNode(t, this);
    }

}