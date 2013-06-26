
package javacop.initrules;


import com.sun.tools.javac.comp.AbstractFlowFacts;
import com.sun.tools.javac.comp.FlowFacts;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;

import constrainer.AbstractConstraints;

public class InitFlowFacts extends AbstractFlowFacts<String>  {
    
    public InitFlowFacts(AbstractConstraints jcop){
    	super(true);
    }
    
    InitFlowFacts(){
    	super(true);
    }

    
    public boolean testMeth(JCTree t){
    	//System.out.println("INNNN " + this);
    	//System.out.println("t: " + t + " : " + t.getClass());
    	return true;
    }
    
    public boolean hasBeenInitialized(JCTree t){
		//System.out.println("IN2!: " + t);
		//System.out.println(this);
		
		return false;
    }
    
    public boolean hasBeenInitialized(String s){
    	return contains(s);
    }
    
    public FlowFacts dup() {
	InitFlowFacts ff = new InitFlowFacts();
	ff.makeCloneOf(this);
	return ff;
    }
	
	
    /** TODO 
     * Consider re-coding this to use the FieldReadExp class from the uniqueness 
     * checker.  In particular, the function below will not work if you use the
     * syntax "MyClass.this.f" to initialize a field.  Also, ensure
     * that this behaves soundly in the presence of nested classes -- though
     * it probably already does. Consider the vardecl case of the genSet
     * function in the presence of class nesting as well.   
     * **/
    // if we're assigning to a field of "this", then return its name.
    // otherwise return null
    protected static String getThisFieldNameOrNull(JCTree lhs) {
	// check if we've got a field access of the form "this.f"
	if (lhs instanceof JCFieldAccess) {
	    JCFieldAccess fa = (JCFieldAccess) lhs;
	    if (fa.selected instanceof JCIdent && 
		((JCIdent)fa.selected).name.toString().equals("this")) {
		return fa.name.toString();
	    }
	}
		
	// also need to handle the case of an implicit this on a field access
	else if (lhs instanceof JCIdent) {
	    JCIdent id = (JCIdent) lhs;
	    if (!(id.sym.isLocal() || id.sym.isStatic())) {
		return id.name.toString();
	    }
	}

	return null;
    }
	
    public FlowFacts genSet(JCTree tree){
	InitFlowFacts gen = new InitFlowFacts();
	if (tree instanceof JCAssign){
	   
	}
	else if (tree instanceof JCVariableDecl){
	    JCVariableDecl vd = ((JCVariableDecl) tree);	
	    //System.out.println("VarDecl: " + vd.name + " " + vd.init);
	    String fieldName = vd.name.toString();
	    // add a variable declaration if it includes
	    // an initializer
	    if (vd.init != null){
		//System.out.println("Adding: " + vd.name.toString() );
		gen.add(fieldName);
	    }
	}
	//System.out.println(gen);
	return gen;
    }

       

    // nothing kills a definite assignment
    public FlowFacts killSet(JCTree tree){ return new InitFlowFacts(); }

	
    // we don't need path sensitivity
    public boolean condDistinguished(JCTree tree){
	return false;
    }
	
    // these are never used by our analysis, since it is not path-sensitive
    public FlowFacts genSetTrue(JCTree tree) { return null; }
    public FlowFacts genSetFalse(JCTree tree) { return null; }
    public FlowFacts killSetTrue(JCTree tree) { return null; }
    public FlowFacts killSetFalse(JCTree tree) { return null; }
	
    
    /*System.out.println("ED");
    JCAssign a = ((JCAssign)tree);
    JCTree lhs = TreeInfo.skipParens(a.lhs);
    String fieldName = getThisFieldNameOrNull(lhs);
    if (fieldName != null) {
    //System.out.println("Adding an assigned field " + fieldName + " to the set.");
    gen.add(fieldName);
    }*/
}
