/*
 * Type system for object initialization, as described in:
 * "Freedom Before Commitment" by Alexander J. Summers and 
 * Peter Muller. 
 * 
 * Implemented in conjunction with an immutable type system.
 * 
 * Course Project: ECE750, University of Waterloo, Spring 2013
 * Author: Ed Zulkoski
 */

package javacop.immutability;


import java.util.List;

import com.sun.source.tree.LiteralTree;
import com.sun.tools.javac.code.Attribute.Compound;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.comp.AbstractFlowFacts;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.FlowFacts;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCConditional;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeInfo;
import com.sun.tools.javac.util.Name;

import constrainer.AbstractConstraints;

// Probably could do most of these analyses in the jcop file,
// but Eclipse makes working Java-side so much easier.
// Could benefit from a lot of refactoring...
public class ImmutabilityFlowFacts extends AbstractFlowFacts<String>  {

	
	// EITHER -- literals / return of constructor
	public static final int MUTABLE = 0, IMMUTABLE = 1, EITHER = 2;
	public static final int FREE = 3, COMMITTED = 4, UNCLASSIFIED = 5, UNDEFINED = 6, THIS = 7; 
	public String currLeft = "";
	
	
    public ImmutabilityFlowFacts(AbstractConstraints jcop){
    	super(true);
    }
    
    ImmutabilityFlowFacts(){
    	super(true);
    }
    
    /*----------------*/
    /* Helper Methods */
    /*----------------*/
    
    //method that checks if mods contains the annotation s
    public boolean hasAnnotation(JCModifiers mods, String s){
    	for(JCAnnotation a : mods.annotations)
    		if(a.annotationType.toString().equals(s))
    			return true;
    	return false;
    }
    
    public boolean hasAnnotation(List<Compound> mods, String s){
    	for(Compound a : mods)
    		if(a.toString().equals("@javacop.annotations." + s))
    			return true;
    	return false;
    }
    
    
    // Returns true if an arg is mutable 
    // At this point, we do not allow mutables to be passed into a constructor of an immutable object.
    public boolean checkConstructorArgs(JCTree t){
    	JCNewClass c = (JCNewClass)t;
    	for(JCExpression e : c.args)
    		if(immType(e) == 0)
    			return true;
    	return false;
    }
    
    public boolean constructorCheck(int l, boolean r){
    	if(l == 1 && r)
    		return false;
    	else 
    		return true;
    }
    
    
    /*--------------------*/
    /* Mutability Methods */
    /*--------------------*/
    
    public boolean assign_to_committed_immutable_check(int init, int imm){
    	return init == FREE || init == UNDEFINED || imm == MUTABLE; 
    }
    
    //ensure we're not putting an immutable in a mutable or vice versa, 
    //Note: EITHER only occurs on return from a constructor
    public boolean mutabilityCheck(int l, int r){
    	System.out.println(l + " " + r);
    	return(l == r || r == EITHER);
    }
    
    
    // A field is mutable until committed, at which point it is
    // immutable if it, or its enclosing object, is declared immutable.
    // So we need to check both the field itself and its parents for immutability.
    public int immType(JCFieldAccess fa){
    	System.out.println("FA: " + fa + " " + fa.sym.getAnnotationMirrors());
    	
    		
    	if(!hasAnnotation(fa.sym.getAnnotationMirrors(),"Mutable"))
    		return IMMUTABLE;
    	if(fa.selected.toString().equals("this"))
    		return MUTABLE;
    	System.out.println("imm: " + fa + " " + immType(fa.selected));
    	return immType(fa.selected);
    }
    
    public int immType(JCIdent id){
    	
    	Symbol s = id.sym;
    	System.out.println(id+ "  " + s.getAnnotationMirrors());
    	if(!hasAnnotation(s.getAnnotationMirrors(),"Mutable"))
    		return IMMUTABLE;
    	else
    		return MUTABLE;
    } 
    
    
    //TODO handle casts somewhere 
    //TODO handle call with free receiver from committed receiver
    //TODO handle the other rules for @Mutates (only one left is assign to mutable)
    //TODO change so that when accessing the fields of a free object the fields are unclassified, not free
   
   //A method returns an immutable object if it is not declared mutable
    public int immType(JCMethodInvocation mi){
    	JCTree sel = mi.getMethodSelect();
    	Symbol s = null;
    	Name name = null;
    	if(sel instanceof JCFieldAccess){
    		s = ((JCFieldAccess)sel).sym.owner;
    		name = ((JCFieldAccess)sel).name;
    	}
    	else if(sel instanceof JCIdent){
    		s = ((JCIdent)sel).sym.owner;
    		name = ((JCIdent)sel).name;
    	}
    	//XXX use mi.meth.type to get the return type and args
    	for(Symbol t : s.enclClass().getEnclosedElements()){
    		if(t instanceof MethodSymbol){
    			MethodSymbol ms = (MethodSymbol)t;
    			if(ms.name.toString().equals(name.toString())){
    				if(!hasAnnotation(ms.getAnnotationMirrors(),("Mutable")))
    					return IMMUTABLE;
    				else
    					return MUTABLE;
    			}
    		}
    	}
    	System.out.println("Shouldn't be reached.");
    	return IMMUTABLE;
    }
    
    //For now return EITHER, and let the variable its assigning into choose for us
    public int immType(JCNewClass nc ){
    	return EITHER;
    }
    
    public int immType(Symbol s ){
    	if(hasAnnotation(s.getAnnotationMirrors(), "Mutable"))
    		return MUTABLE;
    	return IMMUTABLE;
    }
    
    // Probably should use the visitor pattern, but seems like overkill for now.
    public int immType(JCTree t ){
    	//System.out.println(t +  " " + t.getClass());
    	int ret = IMMUTABLE;
    	if(t instanceof JCFieldAccess)
    		ret = immType((JCFieldAccess)t );
    	else if(t instanceof JCIdent)
    		ret = immType((JCIdent)t);
    	else if(t instanceof JCMethodInvocation)
    		ret = immType((JCMethodInvocation)t);
    	else if(t instanceof JCNewClass)
    		ret = immType((JCNewClass)t );
    	else if(t instanceof LiteralTree)
    		ret = EITHER;
    	else
    		System.out.println("Unhandled case in immType: " + t + " " + t.getClass());
    	return ret;
    }
    
    /*------------------------*/
    /* Initialization Methods */
    /*------------------------*/
    
    // determines if the enclosing method/constructor's receiver is Free 
    // (always true for constructors)
    public int receiverType(Symbol s){
    	//System.out.println("sym: " + s + " " + s.getAnnotationMirrors() + s.getClass());
    	
    	if(this.contains(FREE+"this"))
    		return FREE;
    	else if(this.contains(UNCLASSIFIED+"this"))
    		return UNCLASSIFIED;
    	else
    		return COMMITTED;
    	/*if(s instanceof MethodSymbol){
    		MethodSymbol m = (MethodSymbol)s;
	    	if(hasAnnotation(m.getAnnotationMirrors(), "Free") || m.isConstructor())
	    		return FREE;
	    	else if(hasAnnotation(m.getAnnotationMirrors(), "Unclassified") || m.isConstructor())
	    		return UNCLASSIFIED;
	    	else
	    		return COMMITTED;
    	}
    	else
    		return COMMITTED;
    		*/
    }
    
    public boolean initCheck(int l, int r){
    	//System.out.println(l + " " + r);
    	//System.out.println(currLeft);
    	return l == UNCLASSIFIED || l == UNDEFINED || l == r;
    }
    
    public int initType(JCFieldAccess fa){
    	System.out.println("FA " + fa  + " " + initType(fa.selected) + " " + this);
    	int init = initType(fa.selected);
    	if(init == FREE)
    		return UNCLASSIFIED;
    	else
    		return init;
    }
   
    
    public int checkInFlowFacts(Symbol owner, String id){
    	//System.out.println(owner + " " + id + " " + this);
    	if((owner instanceof MethodSymbol && (((MethodSymbol)owner).isConstructor()
    			|| hasAnnotation(((MethodSymbol)owner).getAnnotationMirrors(), "Free")))
    			|| owner instanceof ClassSymbol) {	
    		if(this.contains(COMMITTED+id.toString())){
    			currLeft = id.toString();
    			return COMMITTED; 
    		}
    		if(this.contains(UNCLASSIFIED+id.toString())){
    			currLeft = id.toString();
    			return UNCLASSIFIED; 
    		}
    		if(this.contains(FREE+id.toString())){
    			currLeft = id.toString();
    			return FREE; 
    		}
    		if(this.contains(UNDEFINED+id.toString())){
    			currLeft = id.toString();
    			return UNDEFINED;
    		}
    	}
    	return 0;
    }
    
    //tired code
    public int initType(JCIdent id){
    	//System.out.println(id);
    	//System.out.println("ID: " + id + " " + this.genSet(id) + " " + id.sym.owner.getClass());// + this.contains(new InitPair(id.toString(),1)));
    	Symbol owner = id.sym.owner;
    	
    	//System.out.println("owner: " + id + " " + owner);
    	if(id.name.toString().equals("this")){
    		//System.out.println("in" + id.sym); 
    		//return COMMITTED;
    		return receiverType(id.sym.owner);
    	}
    	int ff = checkInFlowFacts(owner, id.toString());
    	//System.out.println(ff);
    	if(ff != 0 )
    		return ff;
    	return initType(id.sym);
    } 
    
    //A method returns an immutable object if it is not declared mutable
    //TODO fix me like immtype
    public int initType(JCMethodInvocation mi ){
    	return initType(mi.getMethodSelect());
    }
    
    //If all actual parameters are non-committed, return committed
    //Else free
    public int initType(JCNewClass nc){
    	for(JCExpression e : nc.args){
    		int init = initType(e);
    		if(init != COMMITTED){
    			return FREE;
    		}
    	}
    	return COMMITTED;
    }
    
    public int initType(Symbol s ){
    	int ff = checkInFlowFacts(s.owner, s.name.toString());
    	if(ff != 0)
    		return ff;
    	if(hasAnnotation(s.getAnnotationMirrors(),"Free"))
    		return FREE;
    	else if(hasAnnotation(s.getAnnotationMirrors(),"Unclassified"))
    		return UNCLASSIFIED;
    	else
    		return COMMITTED;
    }
    
    public int initType(JCTree t ){
    	int ret = COMMITTED;
    	if(t instanceof JCFieldAccess)
    		ret = initType((JCFieldAccess)t);
    	else if(t instanceof JCIdent)
    		ret = initType((JCIdent)t );
    	else if(t instanceof JCMethodInvocation)
    		ret = initType((JCMethodInvocation)t );
    	else if(t instanceof JCNewClass)
    		ret = initType((JCNewClass)t );
    	else if(t instanceof JCConditional){
    		JCConditional c = (JCConditional)t;//TODO PICK UP HERE
    		int fval = initType(c.getFalseExpression());
    		int tval = initType(c.getTrueExpression());
    		System.out.println("COND: " + fval + " " + tval);
    		if(fval != tval)
    			return -1;
    		ret = fval;
    	}
    	else if(t instanceof LiteralTree)
    		ret = COMMITTED;
    	else
    		System.out.println("Unhandled case in initType: " + t + " " + t.getClass());
    	return ret;
    }
    
    public boolean hasBeenInitialized(JCTree t){
		return false;
    }
    
    public boolean hasBeenInitialized(String s){
    	return true;//contains(s);
    }
   
    
    /*-----------------*/
    /* Mutator Checker */
    /*-----------------*/
    
    public boolean is_mutator(JCMethodInvocation mi ){
    	//not dealing with super(), but shouldn't matter since it will be called in a constructor,
    	//where mutation is OK.
    	if(mi.meth.toString().equals("super"))
    		return false;
    	
    	JCTree sel = mi.getMethodSelect();
    	Symbol s = null;
    	Name name = null;
    	if(sel instanceof JCFieldAccess){
    		s = ((JCFieldAccess)sel).sym.owner;
    		name = ((JCFieldAccess)sel).name;
    	
    	}
    	else if(sel instanceof JCIdent){
    		s = ((JCIdent)sel).sym.owner;
    		name = ((JCIdent)sel).name;
    	}
    	for(Symbol t : s.enclClass().getEnclosedElements()){
    		if(t instanceof MethodSymbol){
    			MethodSymbol ms = (MethodSymbol)t;
    			//System.out.println("IN " + ms.name.toString() + ":" + name);
    			if(ms.name.toString().equals(name.toString())){
    				if(hasAnnotation(ms.getAnnotationMirrors(),("Mutates")))
    					return true;
    				else
    					return false;
    			}
    		}
    	}
    	System.out.println("Shouldn't be reached (mutator).");
    	return true;
    }
    
    
    
    
    public boolean assignsField(JCBlock body){
    	for(JCStatement s : body.getStatements()){
    		//System.out.println(s + " " + s.getClass());
    		JCTree lhs=null, rhs=null;
    		if (s instanceof JCExpressionStatement){
    			JCExpressionStatement e = (JCExpressionStatement)s;
    			if(e.expr instanceof JCAssign){
    				JCAssign a = ((JCAssign)e.expr);
    				lhs = TreeInfo.skipParens(a.lhs);
    				rhs = TreeInfo.skipParens(a.rhs);
    			}
    			else
    				continue;
    		}
    		else if(s instanceof JCVariableDecl){
    			JCVariableDecl e = (JCVariableDecl)s;
    			lhs = null;
    			rhs = TreeInfo.skipParens(e.init);
    		}
    		else
    			continue;
    		if(rhs!=null && lhs!= null);
    			//System.out.println("assigns: " + s + " "+ initType(lhs) + "  " + initType(rhs));
			String lhsFieldName = getThisFieldNameOrNull(lhs);
			String rhsFieldName = getThisFieldNameOrNull(TreeInfo.skipParens(rhs));
			//System.out.println(lhsFieldName + " " + rhsFieldName );
			if (lhsFieldName != null || rhsFieldName != null) {
				return true;
			}
    	}
    	return false;
    }
    
    
    public boolean mutator_requires_mutates_check(JCMethodDecl md ){
    	return !assignsField(md.body)//this.genSet(md).isEmpty() 
    			|| hasAnnotation(md.mods, "Mutates")
    			|| hasAnnotation(md.mods, "Free")
    			|| md.sym.isConstructor();	
    }
    
    public boolean mutator_calls_check(JCMethodInvocation mi, Env<AttrContext> env ){
    	if(env.enclMethod == null)
    	{
    		return true;
    	}
    	return !is_mutator(mi ) 
    			|| hasAnnotation(env.enclMethod.mods, "Free") 
    			|| hasAnnotation(env.enclMethod.mods, "Mutates")
    			|| env.enclMethod.sym.isConstructor();	
    }
    
	public boolean mutator_receiver_check(JCMethodInvocation mi ){
	    //System.out.println("MUT: " + mi.getMethodSelect() + " " + mi.getMethodSelect().getClass());
	    if(mi.getMethodSelect() instanceof JCIdent)
	    	return true;
	    else if(mi.getMethodSelect() instanceof JCFieldAccess){
	    	JCFieldAccess fa = (JCFieldAccess)mi.getMethodSelect();
	    	return ((!is_mutator(mi ))
	    			|| initType(fa.selected ) == FREE
	    			|| initType(fa.selected ) == UNDEFINED
	    			|| immType(fa.selected )  == MUTABLE);
	    }
	    else
	    {
	    	System.out.println("Not implemented yet (mutator_receiver_check): " + mi);
	    	return true;
	    }
	}


    /*-------------------*/
    /* Flowfacts Methods */
    /*-------------------*/

    public FlowFacts dup() {
	ImmutabilityFlowFacts ff = new ImmutabilityFlowFacts();
	ff.makeCloneOf(this); 
	return ff;
    }

    
    
    protected static String getThisFieldNameOrNull(JCTree lhs) {
        // check if we've got a field access of the form "this.f"                                                                                               
        if (lhs instanceof JCFieldAccess) {
                JCFieldAccess fa = (JCFieldAccess) lhs;
                if (fa.selected instanceof JCIdent &&
                		((JCIdent)fa.selected).name.toString().equals("this")) {
                	return fa.name.toString();
                }
                else if(fa.selected instanceof JCFieldAccess)
                	return getThisFieldNameOrNull(fa.selected);
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

    //No clue how to properly tell what type of method I'm in without env, so I'm just checking to see
    //  if either (1) super(..) is called, or (2) this(..) is called.
    //  If either is called, I'm in a constructor(?), and I indicate "this" is free in my flowfacts
    //Still have no idea how to tell I'm in a free method.
    //In other words, this is a hack.
    public FlowFacts genSet(JCTree tree){
    	if(TreeInfo.symbol(tree)!=null){
    		//System.out.println(tree + " " + TreeInfo.symbol(tree).owner + " " + TreeInfo.symbol(tree).owner.getClass());
    		Symbol s = TreeInfo.symbol(tree).owner;
    		if(s instanceof MethodSymbol){
    			MethodSymbol m = (MethodSymbol)s;
    			//System.out.println("METH: " +  m.getAnnotationMirrors() + " " + m.isConstructor() + tree);
    		}
    	}
    	ImmutabilityFlowFacts gen = this;//new ImmutabilityFlowFacts();
    	System.out.println("GENSET: " +gen + " "  + tree.getClass() +" " +  tree);
    	if(tree instanceof JCMethodInvocation){
    		JCMethodInvocation a = (JCMethodInvocation)tree;
    		//System.out.println(a.meth);
    		if(a.meth.toString().equals("super") || a.meth.toString().equals("this")){
    			gen.add(FREE +"this");
    		}
    	}
    	
    	if (tree instanceof JCAssign){
        	//System.out.println(this + " " + tree);
        	JCAssign a = ((JCAssign)tree);
        	JCTree lhs = TreeInfo.skipParens(a.lhs);
        	String fieldName = getThisFieldNameOrNull(lhs);
        	if (fieldName != null) {
        		//System.out.println("Adding an assigned field " + fieldName + " to the set."+ initType(lhs) + " " + initType(a.rhs)); 
        		if(initType(lhs) == UNDEFINED)
        			if(initType(a.rhs) == 4)
        				gen.add("4" + fieldName);
        			else if(initType(a.rhs)==5)
        				gen.add(5 + fieldName);
        		
        				
        		//gen.add(tree); //fieldName);
        	}
        }
        else if (tree instanceof JCVariableDecl){
                JCVariableDecl vd = ((JCVariableDecl) tree);
                //System.out.println(vd.name.toString());
                if(!(vd.sym.isLocal() || vd.sym.isStatic())){
                	//welp im done trying to do flowfacts the right way, number in front of the string is the inittype
                	gen.add(UNDEFINED + vd.name.toString());
                }
                //String fieldName = (! (vd.sym.isLocal() || vd.sym.isStatic())) ? vd.name.toString() : null;
                    // add a variable declaration if it includes                                                                                                
                    // an initializer                                                                                                                           
               // if (vd.init != null && fieldName != null)
                //	gen.add(tree);
        }
        return gen;
}


    //cant remember why this is needed (probably just example code), maybe remove
    public boolean fieldOfThis(JCTree t){
    	//explicit-this case
    	if(t instanceof JCFieldAccess){
    		JCFieldAccess fa = (JCFieldAccess)t;
    		if(fa.selected instanceof JCIdent){
    			JCIdent parent = (JCIdent)fa.selected; 
    			if(parent.name.toString().equals("this"))
    				return true;
    		}
    	}
    	else if(t instanceof JCIdent){
    		System.out.println("FIXME" + " " + t);
    	}
    	return false;
    }

    // nothing kills a definite assignment
    public FlowFacts killSet(JCTree tree){ return new ImmutabilityFlowFacts(); }

	
    
    
    // we don't need path sensitivity
    public boolean condDistinguished(JCTree tree){
	return false;
    }
	
    // these are never used by our analysis, since it is not path-sensitive
    public FlowFacts genSetTrue(JCTree tree) { return null; }
    public FlowFacts genSetFalse(JCTree tree) { return null; }
    public FlowFacts killSetTrue(JCTree tree) { return null; }
    public FlowFacts killSetFalse(JCTree tree) { return null; }
}

class InitPair{
	//need to change from String I think.
	JCTree tree;
	int init;
	String name = ""; 
	
	public InitPair(String t, int i){
		name = t;
		init = i;
	}
	
	public InitPair(JCTree t, int i, String s){
		tree = t;
		init = i;
		name = s;
	}
	
	public String toString(){
		if(init == ImmutabilityFlowFacts.FREE)
			return "@F " + name;
		else if(init == ImmutabilityFlowFacts.COMMITTED)
			return "@C " + name;
		else
			return "@U " + name;
	}
	
	public boolean equals(InitPair other){
		return name.equals(other.name);
	}
}
