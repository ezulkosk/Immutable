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
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.comp.AbstractFlowFacts;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.FlowFacts;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCBlock;
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
    //TODO Ensure that the return type is actually mutable/immutable (jcop side).
    //TODO handle casts somewhere 
    //TODO handle call with free receiver from committed receiver
    //TODO handle the other rules for @Mutates
    //TODO change so that when accessing the fields of a free object the fields are unclassified, not free
    public int immType(JCMethodInvocation mi, Env<AttrContext> env){
    	//System.out.println("IMMMETH: " + mi + " " + mi.getMethodSelect() + " : " + mi.getMethodSelect());
    	JCTree sel = mi.getMethodSelect();
    	Symbol s = null;
    	Name name = null;
    	//System.out.println("METH: " + mi.meth);
    	//System.out.println("SEL: " + sel + " " + sel.getClass());
    	if(sel instanceof JCFieldAccess){
    		s = ((JCFieldAccess)sel).sym.owner;
    		name = ((JCFieldAccess)sel).name;
    	}
    	else if(sel instanceof JCIdent){
    		s = ((JCIdent)sel).sym.owner;
    		name = ((JCIdent)sel).name;
    	}
    	//System.out.println(s + " " + s.enclClass().getEnclosedElements() + " " );
    	//System.out.println(fa.sym.owner + " " + fa.sym.type.tsym + " " + fa.getIdentifier());
    	//System.out.println(((JCIdent)fa.selected).sym.type.tsym.getEnclosedElements());
    	
    	//XXX use mi.meth.type to get the return type and args
    	//not worrying about overloaded methods yet.
    	for(Symbol t : s.enclClass().getEnclosedElements()){
    		if(t instanceof MethodSymbol){
    			MethodSymbol ms = (MethodSymbol)t;
    			//System.out.println("IN " + ms.name.toString() + ":" + name);
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
    	//System.out.println(t +  " " + t.getClass());
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
    
    /*------------------------*/
    /* Initialization Methods */
    /*------------------------*/
    
    // determines if the enclosing method/constructor's receiver is Free 
    // (always true for constructors)
    public int receiverType(Env<AttrContext> env){
    	if(hasAnnotation(env.enclMethod.mods, "Free") || env.enclMethod.sym.isConstructor())
    		return FREE;
    	else if(hasAnnotation(env.enclMethod.mods, "Unclassified") || env.enclMethod.sym.isConstructor())
    		return UNCLASSIFIED;
    	else
    		return COMMITTED;
    }
    
    public boolean initCheck(int l, int r){
    	//System.out.println(l + " " + r);
    	return l == UNCLASSIFIED || l == r;
    }
    
    public int initType(JCFieldAccess fa, Env<AttrContext> env){
    	return initType(fa.selected, env);
    }
    
    public int initType(JCIdent id, Env<AttrContext> env){
    	System.out.println("owner: " + id.sym.owner + " " + id.sym.owner.getClass());
    	//System.out.println(id.sym.owner)
    	if(id.name.toString().equals("this"))
    		return receiverType(env);
    	return initType(id.sym, env);
    } 
    
    //A method returns an immutable object if it is not declared mutable
    //TODO fix me like immtype
    public int initType(JCMethodInvocation mi, Env<AttrContext> env){
    	//System.out.println("INITMETH: " + mi + " " + mi.getMethodSelect());
    	return initType(mi.getMethodSelect(), env);
    }
    
    //If all actual parameters are non-committed, return committed
    //Else free
    public int initType(JCNewClass nc, Env<AttrContext> env){
    	for(JCExpression e : nc.args){
    		int init = initType(e, env);
    		if(init != COMMITTED){
    			return FREE;
    		}
    	}
    	return COMMITTED;
    }
    
    public int initType(Symbol s, Env<AttrContext> env){
    	if(hasAnnotation(s.getAnnotationMirrors(),"Free"))
    		return FREE;
    	else if(hasAnnotation(s.getAnnotationMirrors(),"Unclassified"))
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
   
    
    /*-----------------*/
    /* Mutator Checker */
    /*-----------------*/
    
    public boolean is_mutator(JCMethodInvocation mi, Env<AttrContext> env){
    	//System.out.println("IMMMETH: " + mi + " " + mi.getMethodSelect() + " : " + mi.getMethodSelect());
    	//System.out.println("IN MUTATOR");
    	
    	//not dealing with super(), but shouldn't matter since it will be called in a constructor,
    	//where mutation is OK.
    	if(mi.meth.toString().equals("super"))
    		return false;
    	
    	JCTree sel = mi.getMethodSelect();
    	Symbol s = null;
    	Name name = null;
    	//System.out.println("METH: " + mi.meth);
    	//System.out.println("SEL: " + sel + " " + sel.getClass());
    	if(sel instanceof JCFieldAccess){
    		s = ((JCFieldAccess)sel).sym.owner;
    		name = ((JCFieldAccess)sel).name;
    	}
    	else if(sel instanceof JCIdent){
    		s = ((JCIdent)sel).sym.owner;
    		name = ((JCIdent)sel).name;
    	}
    	//System.out.println(s + " " + s.enclClass().getEnclosedElements());
    	//System.out.println(fa.sym.owner + " " + fa.sym.type.tsym + " " + fa.getIdentifier());
    	//System.out.println(((JCIdent)fa.selected).sym.type.tsym.getEnclosedElements());
    	
    	// use mi.meth.type to get the return type and args
    	//not worrying about overloaded methods yet.
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
    	for(JCStatement s : body.getStatements())
    		//System.out.println(s + " " + s.getClass());
    		if (s instanceof JCExpressionStatement){
    			JCExpressionStatement e = (JCExpressionStatement)s;
    			if(e.expr instanceof JCAssign){
    				JCAssign a = ((JCAssign)e.expr);
    				JCTree lhs = TreeInfo.skipParens(a.lhs);
    				String fieldName = getThisFieldNameOrNull(lhs);
    				if (fieldName != null) {
    					return true;
    				}
    			}
    		}

    	return false;
    }
    
    
    public boolean mutator_requires_mutates_check(JCMethodDecl md, Env<AttrContext> env){
    	return !assignsField(md.body)//this.genSet(md).isEmpty() 
    			|| hasAnnotation(md.mods, "Mutates")
    			|| hasAnnotation(md.mods, "Free")
    			|| md.sym.isConstructor();	
    }
    
    public boolean mutator_calls_check(JCMethodInvocation mi, Env<AttrContext> env){
    	
    	return !is_mutator(mi, env) 
    			|| hasAnnotation(env.enclMethod.mods, "Mutates") 
    			|| receiverType(env) == FREE;//hasAnnotation(env.enclMethod.mods, "Free");		
    }

    //public String getBadAssignment(JCMethodDecl md){
    //	return this.genSet(md).toString();
    //}
    
	public boolean mutator_receiver_check(JCMethodInvocation mi, Env<AttrContext> env){
	    //System.out.println(mi.getMethodSelect() + " " + mi.getMethodSelect().getClass());
	    if(mi.getMethodSelect() instanceof JCIdent)
	    	return true;
	    else if(mi.getMethodSelect() instanceof JCFieldAccess){
	    	JCFieldAccess fa = (JCFieldAccess)mi.getMethodSelect();
	    	//System.out.println(fa.selected);
	    	return !is_mutator(mi,env)
	    			|| initType(fa.selected, env) == FREE
	    			|| immType(fa.selected, env)  == MUTABLE;
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

    
    //I basically need the same flow-facts as the non-null type system included with JavaCop,
    //This code was taken from there.
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

    //only use flowfacts in methods with Free receivers (including constructors)
    public FlowFacts genSet(JCTree tree){
    	if(TreeInfo.symbol(tree)!=null){
    		//System.out.println(tree + " " + TreeInfo.symbol(tree).owner + " " + TreeInfo.symbol(tree).owner.getClass());
    		Symbol s = TreeInfo.symbol(tree).owner;
    		if(s instanceof MethodSymbol){
    			MethodSymbol m = (MethodSymbol)s;
    			//System.out.println(m.getAnnotationMirrors() + " " + m.isConstructor());
    		}
    	}
    	ImmutabilityFlowFacts gen = this;//new ImmutabilityFlowFacts();
    	//System.out.println("GENSET: " + tree + " " +gen);
    	
        if (tree instanceof JCAssign){
                JCAssign a = ((JCAssign)tree);
                JCTree lhs = TreeInfo.skipParens(a.lhs);
                String fieldName = getThisFieldNameOrNull(lhs);
                if (fieldName != null) {
                        //System.out.println("Adding an assigned field " + fieldName + " to the set.");                                                         
                        //gen.add(tree); //fieldName);
                }
        }
        else if (tree instanceof JCVariableDecl){
                JCVariableDecl vd = ((JCVariableDecl) tree);
                //System.out.println(vd.name.toString());
                if(!(vd.sym.isLocal() || vd.sym.isStatic()))
                	gen.add(new InitPair(vd.name.toString(), FREE));
                //String fieldName = (! (vd.sym.isLocal() || vd.sym.isStatic())) ? vd.name.toString() : null;
                    // add a variable declaration if it includes                                                                                                
                    // an initializer                                                                                                                           
               // if (vd.init != null && fieldName != null)
                //	gen.add(tree);
        }
        return gen;
}

    
    /* bagging this approach for now
    //Note: Unfortunately I can't view the method annotations here,
    // will have to deal with elsewhere using the environment.
    public FlowFacts genSet(JCTree tree){
    	ImmutabilityFlowFacts gen = this;//new ImmutabilityFlowFacts();
    	System.out.println("GENSET: \n" + gen + " " + tree  + "\nEND GENSET " );
    	if (tree instanceof JCAssign){
    		JCAssign a = ((JCAssign)tree);
    		System.out.println("ASSIGN: " + tree + " " + a.lhs.getClass() + "NEED TO GET THE DECL");
    	}
    	else if (tree instanceof JCVariableDecl){
    		JCVariableDecl vd = ((JCVariableDecl) tree);
    		//if we're not in a method/constructor where the receiver is free, all fields should be committed 
    		if(!vd.sym.isLocal() && !vd.sym.isStatic())
    			gen.add(new InitPair(tree, COMMITTED, vd.name.toString()));
    		//System.out.println("VarDecl: " + vd.mods.getAnnotations());
    		else if(hasAnnotation(vd.mods,"Free"))
    			gen.add(new InitPair(vd, FREE, vd.name.toString()));
    		else if(hasAnnotation(vd.mods,"Unclassified"))
    			gen.add(new InitPair(vd, UNCLASSIFIED, vd.name.toString()));
    		else
    			gen.add(new InitPair(vd, COMMITTED, vd.name.toString()));
    		//if(vd.mods.contains(""))//pick up here 
    		String fieldName = vd.name.toString();
    	}
    	else if(tree instanceof JCMethodDecl){
    		System.out.println("in");
    	}
    	return gen;
    }
    */
    
    

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
