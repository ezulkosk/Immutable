package javacop.initrules;

import constrainer.*;
import com.sun.tools.javac.code.*;
import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javac.code.Type.*;
import com.sun.tools.javac.code.Flags;
import static com.sun.tools.javac.code.Flags.*;
import com.sun.tools.javac.code.Kinds;
import static com.sun.tools.javac.code.Kinds.*;
import com.sun.tools.javac.comp.*;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.Name.*;
import com.sun.tools.javac.code.Attribute.*;
public class InitRules extends AbstractConstraints{

public InitRules(Log log, Table names, Symtab syms, Types types){
    super(log,names,syms,types);
}

//Generated Code

public List<? extends FlowFacts> getDataFlowAnalyses(){
	return List.of(new InitFlowFacts(this));
}



public boolean hasproperty_noInitAnnotation_1(final Env<AttrContext> env, final Object s_){
  if(!(s_ instanceof Symbol)){return false;}
    final Symbol s = (Symbol) s_;
    {
    if((((!hasproperty_free(env, s)) && (!hasproperty_explicitlyCommitted(env, s))) && (!hasproperty_unclassified(env, s)))){
    }
    else{
        return false;
    }
    }
    return true;
}

public boolean hasproperty_noInitAnnotation(final Env<AttrContext> env, final Object _a0){
    return hasproperty_noInitAnnotation_1(env, _a0);
}

/*----------------------------------------------*/


public boolean hasproperty_committed_1(final Env<AttrContext> env, final Object s_){
  if(!(s_ instanceof Symbol)){return false;}
    final Symbol s = (Symbol) s_;
    {
    if((hasproperty_explicitlyCommitted(env, s) || ((!hasproperty_free(env, s)) && (!hasproperty_unclassified(env, s))))){
    }
    else{
        return false;
    }
    }
    return true;
}

public boolean hasproperty_committed_2(final Env<AttrContext> env, final Object t_){
  if(!(t_ instanceof JCTree)){return false;}
    final JCTree t = (JCTree) t_;
    {
    if((holdsSymbol(t) && hasproperty_committed(env, getSymbol(t)))){
    }
    else{
        return false;
    }
    }
    return true;
}

public boolean hasproperty_committed(final Env<AttrContext> env, final Object _a0){
    return hasproperty_committed_1(env, _a0) || hasproperty_committed_2(env, _a0);
}

/*----------------------------------------------*/


public boolean hasproperty_mutable_1(final Env<AttrContext> env, final Object s_){
  if(!(s_ instanceof Symbol)){return false;}
    final Symbol s = (Symbol) s_;
    {
    if(hasAnnotation(s, "javacop.annotations.Mutable")){
    }
    else{
        return false;
    }
    }
    return true;
}

public boolean hasproperty_mutable(final Env<AttrContext> env, final Object _a0){
    return hasproperty_mutable_1(env, _a0);
}

/*----------------------------------------------*/


public boolean hasproperty_unclassified_1(final Env<AttrContext> env, final Object s_){
  if(!(s_ instanceof Symbol)){return false;}
    final Symbol s = (Symbol) s_;
    {
    if(hasAnnotation(s, "javacop.annotations.Unclassified")){
    }
    else{
        return false;
    }
    }
    return true;
}

public boolean hasproperty_unclassified(final Env<AttrContext> env, final Object _a0){
    return hasproperty_unclassified_1(env, _a0);
}

/*----------------------------------------------*/


public boolean hasproperty_free_1(final Env<AttrContext> env, final Object s_){
  if(!(s_ instanceof Symbol)){return false;}
    final Symbol s = (Symbol) s_;
    {
    if(hasAnnotation(s, "javacop.annotations.Free")){
    }
    else{
        return false;
    }
    }
    return true;
}

public boolean hasproperty_free(final Env<AttrContext> env, final Object _a0){
    return hasproperty_free_1(env, _a0);
}

/*----------------------------------------------*/


public boolean hasproperty_singleInitAnnotation_1(final Env<AttrContext> env, final Object s_){
  if(!(s_ instanceof Symbol)){return false;}
    final Symbol s = (Symbol) s_;
    {
    if((((!(hasproperty_free(env, s) && hasproperty_explicitlyCommitted(env, s))) && (!(hasproperty_free(env, s) && hasproperty_unclassified(env, s)))) && (!(hasproperty_explicitlyCommitted(env, s) && hasproperty_unclassified(env, s))))){
    }
    else{
        return false;
    }
    }
    return true;
}

public boolean hasproperty_singleInitAnnotation(final Env<AttrContext> env, final Object _a0){
    return hasproperty_singleInitAnnotation_1(env, _a0);
}

/*----------------------------------------------*/


public boolean hasproperty_explicitlyCommitted_1(final Env<AttrContext> env, final Object t_){
  if(!(t_ instanceof JCTree)){return false;}
    final JCTree t = (JCTree) t_;
    {
    if((holdsSymbol(t) && hasproperty_explicitlyCommitted(env, getSymbol(t)))){
    }
    else{
        return false;
    }
    }
    return true;
}

public boolean hasproperty_explicitlyCommitted_2(final Env<AttrContext> env, final Object s_){
  if(!(s_ instanceof Symbol)){return false;}
    final Symbol s = (Symbol) s_;
    {
    if(hasAnnotation(s, "javacop.annotations.Committed")){
    }
    else{
        return false;
    }
    }
    return true;
}

public boolean hasproperty_explicitlyCommitted(final Env<AttrContext> env, final Object _a0){
    return hasproperty_explicitlyCommitted_1(env, _a0) || hasproperty_explicitlyCommitted_2(env, _a0);
}

/*----------------------------------------------*/


public boolean hasproperty_singleMutationAnnotation_1(final Env<AttrContext> env, final Object s_){
  if(!(s_ instanceof Symbol)){return false;}
    final Symbol s = (Symbol) s_;
    {
    if((hasproperty_mutable(env, s) ^ hasproperty_immutable(env, s))){
    }
    else{
        return false;
    }
    }
    return true;
}

public boolean hasproperty_singleMutationAnnotation(final Env<AttrContext> env, final Object _a0){
    return hasproperty_singleMutationAnnotation_1(env, _a0);
}

/*----------------------------------------------*/


public boolean hasproperty_definitelyAssignedVariable_1(final Env<AttrContext> env, final Object a_){
  if(!(a_ instanceof JCAssign)){return false;}
    final JCAssign a = (JCAssign) a_;
    {
    final BindingVars _f1_vars = new BindingVars(new BindingVar[]{new BindingVar("ff", InitFlowFacts.class)});
    if(Caster.cast(a.getFlowFacts(InitFlowFacts.class), "ff", _f1_vars)){
        final InitFlowFacts ff = (InitFlowFacts) _f1_vars.get("ff");
        {
        if(ff.hasBeenInitialized(lhs(a))){
        }
        else{
            return false;
        }
        }
    }
    else{
        return false;
    }
    }
    return true;
}

public boolean hasproperty_definitelyAssignedVariable(final Env<AttrContext> env, final Object _a0){
    return hasproperty_definitelyAssignedVariable_1(env, _a0);
}

/*----------------------------------------------*/


public boolean hasproperty_immutable_1(final Env<AttrContext> env, final Object s_){
  if(!(s_ instanceof Symbol)){return false;}
    final Symbol s = (Symbol) s_;
    {
    if((hasAnnotation(s, "javacop.annotations.Immutable") || (!hasproperty_mutable(env, s)))){
    }
    else{
        return false;
    }
    }
    return true;
}

public boolean hasproperty_immutable_2(final Env<AttrContext> env, final Object t_){
  if(!(t_ instanceof JCTree)){return false;}
    final JCTree t = (JCTree) t_;
    {
    if((holdsSymbol(t) && hasproperty_immutable(env, getSymbol(t)))){
    }
    else{
        return false;
    }
    }
    return true;
}

public boolean hasproperty_immutable(final Env<AttrContext> env, final Object _a0){
    return hasproperty_immutable_1(env, _a0) || hasproperty_immutable_2(env, _a0);
}

/*----------------------------------------------*/

public void rule_singleInitAnnotation(final JCMethodDecl md, final Env<AttrContext> env){
    {
    if(holdsSymbol(md)){
        {
        if(hasproperty_singleInitAnnotation(env, getSymbol(md))){
        }
        else{
            wrapWarning(md, "MethodDecl can have only one of: [@Free,@Classified,@Unclassified].");
        }
        }
    }
    }

}
public void rule_singleMutationAnnotation(final JCMethodDecl md, final Env<AttrContext> env){
    {
    if(holdsSymbol(md)){
        {
        if(hasproperty_singleMutationAnnotation(env, getSymbol(md))){
        }
        else{
            wrapWarning(md, "MethodDecl can have only one of: [@Mutable,@Immutable].");
        }
        }
    }
    }

}
public void rule_noInitAnnotation(final JCMethodDecl md, final Env<AttrContext> env){
    {
    if((holdsSymbol(md) && isConstructor(getSymbol(md)))){
        {
        if(hasproperty_noInitAnnotation(env, getSymbol(md))){
        }
        else{
            wrapWarning(md, "Constructors should not have initialization modifiers.");
        }
        }
    }
    }

}
@Override public void validateMethodDef(final JCMethodDecl tree, final Env<AttrContext> env){
    rule_singleInitAnnotation(tree,env);
    rule_singleMutationAnnotation(tree,env);
    rule_noInitAnnotation(tree,env);
}

/*----------------------------------------------*/

public void rule_singleInitAnnotation(final JCVariableDecl vd, final Env<AttrContext> env){
    {
    if(holdsSymbol(vd)){
        {
        if(hasproperty_singleInitAnnotation(env, getSymbol(vd))){
        }
        else{
            wrapWarning(vd, "VarDecl can have only one of: [@Free,@Classified,@Unclassified].");
        }
        }
    }
    }

}
public void rule_singleMutationAnnotation(final JCVariableDecl vd, final Env<AttrContext> env){
    {
    if(holdsSymbol(vd)){
        {
        if(hasproperty_singleMutationAnnotation(env, getSymbol(vd))){
        }
        else{
            wrapWarning(vd, "VarDecl can have only one of: [@Mutable,@Immutable].");
        }
        }
    }
    }

}
public void rule_noInitAnnotation(final JCVariableDecl vd, final Env<AttrContext> env){
    {
    if(((holdsSymbol(vd) && (!isStatic(getSymbol(vd)))) && (!isLocal(getSymbol(vd))))){
        {
        if(hasproperty_noInitAnnotation(env, getSymbol(vd))){
        }
        else{
            wrapWarning(vd, "Fields should not have initialization modifiers.");
        }
        }
    }
    }

}
@Override public void validateVarDef(final JCVariableDecl tree, final Env<AttrContext> env){
    rule_singleInitAnnotation(tree,env);
    rule_singleMutationAnnotation(tree,env);
    rule_noInitAnnotation(tree,env);
}

/*----------------------------------------------*/

public void rule_assign(final JCAssign a, final Env<AttrContext> env){
    {
    if((holdsSymbol(lhs(a)) && hasproperty_committed(env, getSymbol(lhs(a))))){
        {
        if(hasproperty_committed(env, rhs(a))){
        }
        else{
            wrapWarning(a, "Left committed but defAssVar failed.");
        }
        }
    }
    }

}
public void rule_tvarass(final JCAssign a, final Env<AttrContext> env){
    {
    if((1 < 2)){
    }
    else{
        wrapError(a, "Rule tvarass failed with no provided reason.\n");
    }
    }

}
@Override public void validateAssign(final JCAssign tree, final Env<AttrContext> env){
    rule_assign(tree,env);
    rule_tvarass(tree,env);
}

/*----------------------------------------------*/

public void rule_test(final JCTree a, final Env<AttrContext> env){
    {
    if(holdsSymbol(a)){
    }
    else{
        wrapWarning(a, "Never hit.");
    }
    }

}
@Override public void validateTree(final JCTree tree, final Env<AttrContext> env){
    rule_test(tree,env);
}

/*----------------------------------------------*/

}