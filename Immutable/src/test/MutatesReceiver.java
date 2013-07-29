package test;

import javacop.annotations.Immutable;
import javacop.annotations.Mutable;
import javacop.annotations.Mutates;
import javacop.annotations.Unclassified;

//Illustrates the problem with mutators.
//setField2 must have the @Mutates annotation,
//  followed by setField (since it calls setField2)
//Finally the call to setField in myMain should fail,
//  since m is immutable.
//Lines commented with "Error" should still produce 
//  warnings after these changes.
public class MutatesReceiver{
	int x;
	MutatesReceiver field;
	
	@Mutates public void setField(int f){
		setField2(f);
	}
	
	@Mutates public void setField2(int f){
		x = f;
	}
	
	//I guess main will require the @Mutates annotation.
	//Slightly awkward, but oh well.
	@Mutates public void myMain(){
		@Immutable MutatesReceiver i = new MutatesReceiver();
		@Mutable MutatesReceiver m = new MutatesReceiver();
		i.setField(1); //Error, trying to mutate an immutable
		m.setField(2); //OK, m is mutable, mutators are allowed
	}
	
	public MutatesReceiver(){
		field = new MutatesReceiver(this); //field is still free at this point, since this is not committed
		field.setField(3); //OK, we can mutate objects under construction (i.e. free objects)
	}
	
	public MutatesReceiver(@Unclassified MutatesReceiver other){
		other.setField(4); //since other _may_ be committed (even though it never is in this code), we cannot mutate it.
	}
	
}