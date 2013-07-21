
package javacop.immutability;


import java.util.Iterator;
import java.util.List;

import com.sun.source.tree.LiteralTree;
import com.sun.tools.javac.code.Attribute.Compound;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.comp.AbstractFlowFacts;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.FlowFacts;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;

import constrainer.AbstractConstraints;

// Probably could do a lot of these analyses in the jcop file,
// but Eclipse makes working Java-side so much easier.
public class ImmutabilityFlowFacts extends AbstractFlowFacts<InitPair>  {
	
	// EITHER -- literals / return of constructor
	public static final int MUTABLE = 0, IMMUTABLE = 1, EITHER = 2;
	public static final int FREE = 3, COMMITTED = 4, UNCLASSIFIED = 5; 
	
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
    public boolean checkConstructorArgs(JCTree t, Env<AttrContext> env){
    	JCNewClass c = (JCNewClass)t;
    	for(JCExpression e : c.args)
    		if(immType(e, env) == 0)
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
    
    //ensure we're not putting an immutable in a mutable or vice versa, 
    //Note: EITHER only occurs on return from a constructor
    public boolean mutabilityCheck(int l, int r){
    	return(l == r || r == EITHER);
    }
    
    
    // A field is mutable until committed, at which point it is
    // immutable if it, or its enclosing object, is declared immutable.
    // So we need to check both the field itself and its parents for immutability.
    public int immType(JCFieldAccess fa, Env<AttrContext> env){
    	if(!hasAnnotation(fa.sym.getAnnotationMirrors(),"Mutable"))
    		return IMMUTABLE;
    	return immType(fa.selected, env);
    }
    
    public int immType(JCIdent id, Env<AttrContext> env){
    	Symbol s = id.sym;
    	if(!hasAnnotation(s.getAnnotationMirrors(),"Mutable"))
    		return IMMUTABLE;
    	else
    		return MUTABLE;
    } 
    
    //A method returns an immutable object if it is not declared mutable
    public int immType(JCMethodInvocation mi, Env<AttrContext> env){
    	for(JCTree t : env.enclClass.getMembers()){
    		if(t instanceof JCMethodDecl){
    			JCMethodDecl md = (JCMethodDecl)t;
    			if(md.name.toString().equals(mi.getMethodSelect().toString())){
    				if(!hasAnnotation(md.getModifiers(),("Mutable")))
    					return IMMUTABLE;
    				else
    					return MUTABLE;
    			}
    		}
    	}
    	return IMMUTABLE;
    }
    
    //For now return EITHER, and let the variable its assigning into choose for us
    public int immType(JCNewClass nc, Env<AttrContext> env){
    	return EITHER;//!hasAnnotation(nc.constructor.getAnnotationMirrors(),("Mutable"));
    }
    
    public int immType(Symbol s, Env<AttrContext>env){
    	//System.out.println("IMMTYPE SYM: " + s + " isMutable: " + hasAnnotation(s.getAnnotationMirrors(), "Mutable"));
    	if(hasAnnotation(s.getAnnotationMirrors(), "Mutable"))
    		return MUTABLE;
    	return IMMUTABLE;
    }
    
    // Probably should use the visitor pattern, but seems like overkill for now.
    public int immType(JCTree t, Env<AttrContext> env){
    	int ret = IMMUTABLE;
    	if(t instanceof JCFieldAccess)
    		ret = immType((JCFieldAccess)t, env);
    	else if(t instanceof JCIdent)
    		ret = immType((JCIdent)t, env);
    	else if(t instanceof JCMethodInvocation)
    		ret = immType((JCMethodInvocation)t, env);
    	else if(t instanceof JCNewClass)
    		ret = immType((JCNewClass)t, env);
    	else if(t instanceof LiteralTree)
    		ret = EITHER;
    	else
    		System.out.println("Unhandled case in immType: " + t + " " + t.getClass());
    	return ret;
    }
    
    
    
    //cant remember why this is needed (probably just example code) TODO maybe remove
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
    
    
    
    /*------------------------*/
    /* Initialization Methods */
    /*------------------------*/
    
    // determines if the enclosing method/constructor's receiver is Free 
    // (always true for constructors)
    public boolean freeReceiver(Env<AttrContext> env){
    	return hasAnnotation(env.enclMethod.mods, "Free") || env.enclMethod.sym.isConstructor();
    }
    
    public boolean initCheck(int l, int r){
    	return l == UNCLASSIFIED || l == r;
    }
    
    public int initType(JCFieldAccess fa, Env<AttrContext> env){
    	return initType(fa.selected, env);
    }
    
    public int initType(JCIdent id, Env<AttrContext> env){
    	return initType(id.sym, env);
    } 
    
    //A method returns an immutable object if it is not declared mutable
    //TODO don't think method overrides will work. 
    public int initType(JCMethodInvocation mi, Env<AttrContext> env){
    	return initType(mi.getMethodSelect(), env);
    }
    
    //If all actual parameters are non-committed, return committed
    //Else free
    public int initType(JCNewClass nc, Env<AttrContext> env){
    	for(JCExpression e : nc.args){
    		int init = initType(e, env);
    		if(init != COMMITTED)
    			return FREE;
    	}
    	return COMMITTED;
    }
    
    public int initType(Symbol s, Env<AttrContext> env){
    	if(hasAnnotation(s.getAnnotationMirrors(),"Free"))
    		return FREE;
    	else if(hasAnnotation(s.getAnnotationMirrors(),"UNCLASSIFIED"))
    		return UNCLASSIFIED;
    	else
    		return COMMITTED;
    }
    
    public int initType(JCTree t, Env<AttrContext> env){
    	//System.out.println("INITTYPE: " + t + " " + t.getClass());
    	int ret = COMMITTED;
    	if(t instanceof JCFieldAccess)
    		ret = initType((JCFieldAccess)t, env);
    	else if(t instanceof JCIdent)
    		ret = initType((JCIdent)t, env);
    	else if(t instanceof JCMethodInvocation)
    		ret = initType((JCMethodInvocation)t, env);
    	else if(t instanceof JCNewClass)
    		ret = initType((JCNewClass)t, env);
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
   
    /*-------------------*/
    /* Flowfacts Methods */
    /*-------------------*/

    public FlowFacts dup() {
	ImmutabilityFlowFacts ff = new ImmutabilityFlowFacts();
	ff.makeCloneOf(this);
	return ff;
    }

    //Note: Unfortunately I can't view the method annotations here,
    // will have to deal with elsewhere using the environment.
    public FlowFacts genSet(JCTree tree){
    	ImmutabilityFlowFacts gen = this;//new ImmutabilityFlowFacts();
    	//System.out.println("GENSET: " + gen + " " + tree + " " + tree.getClass()+ " " );
    	if (tree instanceof JCAssign){
    		JCAssign a = ((JCAssign)tree);
    		//System.out.println("ASSIGN: " + tree + " " + a.lhs.getClass());
    	}
    	else if (tree instanceof JCVariableDecl){
    		JCVariableDecl vd = ((JCVariableDecl) tree);	
    		//System.out.println("VarDecl: " + vd.name + " " + vd.init + " " + vd.mods);
    		String fieldName = vd.name.toString();
    		// add a variable declaration if it includes
    		// an initializer
    		//if (vd.init != null){
    			//System.out.println("Adding: " + vd.name.toString() );
    			gen.add(new InitPair(fieldName, COMMITTED));
    		//}
    	}
    	//System.out.println(gen);
    	return gen;
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
	String name;
	int init;
	public InitPair(String s, int i){
		name = s;
		init = i;
	}
	
	public String toString(){
		if(init == ImmutabilityFlowFacts.FREE)
			return "@F " + name;
		else if(init == ImmutabilityFlowFacts.COMMITTED)
			return "@C " + name;
		else
			return "@U " + name;
	}
}
