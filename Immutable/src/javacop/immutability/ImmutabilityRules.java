package javacop.immutability;

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
public class ImmutabilityRules extends AbstractConstraints{

public ImmutabilityRules(Log log, Table names, Symtab syms, Types types){
    super(log,names,syms,types);
}

//Generated Code

public List<? extends FlowFacts> getDataFlowAnalyses(){
	return List.of(new ImmutabilityFlowFacts(this));
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


public boolean hasproperty_mutability_helper_1(final Env<AttrContext> env, final Object l_, final Object r_){
  if(!(l_ instanceof Symbol)){return false;}
    final Symbol l = (Symbol) l_;
  if(!(r_ instanceof JCTree)){return false;}
    final JCTree r = (JCTree) r_;
    {
    final BindingVars _f1_vars = new BindingVars(new BindingVar[]{new BindingVar("ff", ImmutabilityFlowFacts.class)});
    if(Caster.cast(r.getFlowFacts(ImmutabilityFlowFacts.class), "ff", _f1_vars)){
        final ImmutabilityFlowFacts ff = (ImmutabilityFlowFacts) _f1_vars.get("ff");
        {
        if(ff.mutabilityCheck(ff.immType(l), ff.immType(r))){
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

public boolean hasproperty_mutability_helper_2(final Env<AttrContext> env, final Object l_, final Object r_){
  if(!(l_ instanceof JCTree)){return false;}
    final JCTree l = (JCTree) l_;
  if(!(r_ instanceof JCTree)){return false;}
    final JCTree r = (JCTree) r_;
    {
    final BindingVars _f1_vars = new BindingVars(new BindingVar[]{new BindingVar("ff", ImmutabilityFlowFacts.class)});
    if(Caster.cast(r.getFlowFacts(ImmutabilityFlowFacts.class), "ff", _f1_vars)){
        final ImmutabilityFlowFacts ff = (ImmutabilityFlowFacts) _f1_vars.get("ff");
        {
        if(ff.mutabilityCheck(ff.immType(l), ff.immType(r))){
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

public boolean hasproperty_mutability_helper(final Env<AttrContext> env, final Object _a0, final Object _a1){
    return hasproperty_mutability_helper_1(env, _a0, _a1) || hasproperty_mutability_helper_2(env, _a0, _a1);
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


public boolean hasproperty_init_helper_1(final Env<AttrContext> env, final Object l_, final Object r_){
  if(!(l_ instanceof Symbol)){return false;}
    final Symbol l = (Symbol) l_;
  if(!(r_ instanceof JCTree)){return false;}
    final JCTree r = (JCTree) r_;
    {
    final BindingVars _f1_vars = new BindingVars(new BindingVar[]{new BindingVar("ff", ImmutabilityFlowFacts.class)});
    if(Caster.cast(r.getFlowFacts(ImmutabilityFlowFacts.class), "ff", _f1_vars)){
        final ImmutabilityFlowFacts ff = (ImmutabilityFlowFacts) _f1_vars.get("ff");
        {
        if(ff.initCheck(ff.initType(l), ff.initType(r))){
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

public boolean hasproperty_init_helper_2(final Env<AttrContext> env, final Object l_, final Object r_){
  if(!(l_ instanceof JCTree)){return false;}
    final JCTree l = (JCTree) l_;
  if(!(r_ instanceof JCTree)){return false;}
    final JCTree r = (JCTree) r_;
    {
    final BindingVars _f1_vars = new BindingVars(new BindingVar[]{new BindingVar("ff", ImmutabilityFlowFacts.class)});
    if(Caster.cast(r.getFlowFacts(ImmutabilityFlowFacts.class), "ff", _f1_vars)){
        final ImmutabilityFlowFacts ff = (ImmutabilityFlowFacts) _f1_vars.get("ff");
        {
        if(ff.initCheck(ff.initType(l), ff.initType(r))){
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

public boolean hasproperty_init_helper(final Env<AttrContext> env, final Object _a0, final Object _a1){
    return hasproperty_init_helper_1(env, _a0, _a1) || hasproperty_init_helper_2(env, _a0, _a1);
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
    final BindingVars _f1_vars = new BindingVars(new BindingVar[]{new BindingVar("ff", ImmutabilityFlowFacts.class)});
    if(Caster.cast(a.getFlowFacts(ImmutabilityFlowFacts.class), "ff", _f1_vars)){
        final ImmutabilityFlowFacts ff = (ImmutabilityFlowFacts) _f1_vars.get("ff");
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

public void rule_can_only_call_mutator_from_mutator(final JCMethodInvocation mi, final Env<AttrContext> env){
    {
    final BindingVars _f1_vars = new BindingVars(new BindingVar[]{new BindingVar("ff", ImmutabilityFlowFacts.class)});
    if(Caster.cast(mi.getFlowFacts(ImmutabilityFlowFacts.class), "ff", _f1_vars)){
        final ImmutabilityFlowFacts ff = (ImmutabilityFlowFacts) _f1_vars.get("ff");
        {
        if(ff.mutator_calls_check(mi, env)){
        }
        else{
            wrapWarning(mi, "Mutator called from a method without @Free or @Mutates.");
        }
        }
    }
    else{
        wrapError(mi, "Rule can_only_call_mutator_from_mutator failed with no provided reason.\n");
    }
    }

}
public void rule_can_only_call_mutator_with_mutable(final JCMethodInvocation mi, final Env<AttrContext> env){
    {
    final BindingVars _f1_vars = new BindingVars(new BindingVar[]{new BindingVar("ff", ImmutabilityFlowFacts.class)});
    if(Caster.cast(mi.getFlowFacts(ImmutabilityFlowFacts.class), "ff", _f1_vars)){
        final ImmutabilityFlowFacts ff = (ImmutabilityFlowFacts) _f1_vars.get("ff");
        {
        if(ff.mutator_receiver_check(mi)){
        }
        else{
            wrapWarning(mi, "Mutator called with a committed/unclassified immutable receiver.");
        }
        }
    }
    else{
        wrapError(mi, "Rule can_only_call_mutator_with_mutable failed with no provided reason.\n");
    }
    }

}
public void rule_receiver_init_check(final JCMethodInvocation mi, final Env<AttrContext> env){
    {
    final BindingVars _f1_vars = new BindingVars(new BindingVar[]{new BindingVar("ff", ImmutabilityFlowFacts.class)});
    if(Caster.cast(mi.getFlowFacts(ImmutabilityFlowFacts.class), "ff", _f1_vars)){
        final ImmutabilityFlowFacts ff = (ImmutabilityFlowFacts) _f1_vars.get("ff");
        {
        if(ff.receiver_init_check(mi)){
        }
        else{
            wrapWarning(mi, "Init-type of receiver and method incompatible.");
        }
        }
    }
    else{
        wrapError(mi, "Rule receiver_init_check failed with no provided reason.\n");
    }
    }

}
@Override public void validateApply(final JCMethodInvocation tree, final Env<AttrContext> env){
    rule_can_only_call_mutator_from_mutator(tree,env);
    rule_can_only_call_mutator_with_mutable(tree,env);
    rule_receiver_init_check(tree,env);
}

/*----------------------------------------------*/

public void rule_mutator_requires_mutates(final JCMethodDecl md, final Env<AttrContext> env){
    {
    final BindingVars _f1_vars = new BindingVars(new BindingVar[]{new BindingVar("ff", ImmutabilityFlowFacts.class)});
    if(Caster.cast(md.getFlowFacts(ImmutabilityFlowFacts.class), "ff", _f1_vars)){
        final ImmutabilityFlowFacts ff = (ImmutabilityFlowFacts) _f1_vars.get("ff");
        {
        if(ff.mutator_requires_mutates_check(md)){
        }
        else{
            wrapWarning(md, "Field mutations occuring in method without @Mutates or @Free annotation.");
        }
        }
    }
    else{
        wrapError(md, "Rule mutator_requires_mutates failed with no provided reason.\n");
    }
    }

}
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
            wrapWarning(md, "Constructors should not have initialization annotations.");
        }
        }
    }
    }

}
@Override public void validateMethodDef(final JCMethodDecl tree, final Env<AttrContext> env){
    rule_mutator_requires_mutates(tree,env);
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
            wrapWarning(vd, "Fields should not have initialization annotations.");
        }
        }
    }
    }

}
public void rule_noStaticFields(final JCVariableDecl vd, final Env<AttrContext> env){
    {
    if((!isStatic(sym(vd)))){
    }
    else{
        wrapWarning(vd, "Static fields are currently not allowed.");
    }
    }

}
@Override public void validateVarDef(final JCVariableDecl tree, final Env<AttrContext> env){
    rule_singleInitAnnotation(tree,env);
    rule_singleMutationAnnotation(tree,env);
    rule_noInitAnnotation(tree,env);
    rule_noStaticFields(tree,env);
}

/*----------------------------------------------*/

public void rule_check_conditionals(final JCConditional c, final Env<AttrContext> env){
    {
    final BindingVars _f1_vars = new BindingVars(new BindingVar[]{new BindingVar("ff", ImmutabilityFlowFacts.class)});
    if(Caster.cast(c.getFlowFacts(ImmutabilityFlowFacts.class), "ff", _f1_vars)){
        final ImmutabilityFlowFacts ff = (ImmutabilityFlowFacts) _f1_vars.get("ff");
        {
        if(ff.check_conditional(c)){
        }
        else{
            wrapWarning(c, "Init-type or mutability type of conditional branches differ.");
        }
        }
    }
    else{
        wrapError(c, "Rule check_conditionals failed with no provided reason.\n");
    }
    }

}
@Override public void validateConditional(final JCConditional tree, final Env<AttrContext> env){
    rule_check_conditionals(tree,env);
}

/*----------------------------------------------*/

public void rule_assignment(final JCAssign a, final Env<AttrContext> env){
    {
    final BindingVars _f1_vars = new BindingVars(new BindingVar[]{new BindingVar("ff", ImmutabilityFlowFacts.class)});
    if(Caster.cast(a.getFlowFacts(ImmutabilityFlowFacts.class), "ff", _f1_vars)){
        final ImmutabilityFlowFacts ff = (ImmutabilityFlowFacts) _f1_vars.get("ff");
        {
        if(ff.assign_to_committed_immutable_check(ff.initType(lhs(a)), ff.immType(lhs(a)))){
        }
        else{
            wrapWarning(a, "Assigning to committed immutable object.");
        }
        }
    }
    else{
        wrapError(a, "Rule assignment failed with no provided reason.\n");
    }
    }

}
@Override public void validateAssign(final JCAssign tree, final Env<AttrContext> env){
    rule_assignment(tree,env);
}

/*----------------------------------------------*/

public void subsymbolrule_subtypeCheck(final JCTree r, final Symbol l, final Env<AttrContext> env){
    {
    if(hasproperty_mutability_helper(env, l, r)){
    }
    else{
        wrapWarning(r, ((("Cannot assign mutable to immutable or vice versa: " + l) + " <- ") + r));
    }
    }
    {
    if(hasproperty_init_helper(env, l, r)){
    }
    else{
        wrapWarning(r, ((("Incompatible init types: " + l) + " <- ") + r));
    }
    }

}
public void subsymbolrule_no_mutables_in_immutable_constructor(final JCTree r, final Symbol l, final Env<AttrContext> env){
    {
    if((r instanceof JCNewClass)){
        {
        final BindingVars _f1_vars = new BindingVars(new BindingVar[]{new BindingVar("ff", ImmutabilityFlowFacts.class)});
        if(Caster.cast(r.getFlowFacts(ImmutabilityFlowFacts.class), "ff", _f1_vars)){
            final ImmutabilityFlowFacts ff = (ImmutabilityFlowFacts) _f1_vars.get("ff");
            {
            if(ff.constructorCheck(ff.immType(l), ff.checkConstructorArgs(r))){
            }
            else{
                wrapWarning(r, "Passing a mutable into an immutable object constructor.");
            }
            }
        }
        else{
            wrapError(r, "Rule no_mutables_in_immutable_constructor failed with no provided reason.\n");
        }
        }
    }
    }

}
@Override public void validateSubsymboling(final JCTree expr, final Symbol supr, final Env<AttrContext> env){
    subsymbolrule_subtypeCheck(expr,supr,env);
    subsymbolrule_no_mutables_in_immutable_constructor(expr,supr,env);
}

/*----------------------------------------------*/

}