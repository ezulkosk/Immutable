package test;

import javacop.annotations.FreeM;
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
	@Mutable int x;
	MutatesReceiver field, field2;
	
	@Unclassified @Mutates public void setField(){
		setField2();
	}
	
	@Mutates public void setField2(){
		x = 1;
	}
	
	
	//I guess main will require the @Mutates annotation.
	//Slightly awkward, but oh well.
	@Mutates public void myMain(){
		@Immutable MutatesReceiver i = new MutatesReceiver();
		@Mutable MutatesReceiver m = new MutatesReceiver();
		i.setField(); //Error, trying to mutate an immutable
		m.setField(); //OK, m is mutable, mutators are allowed
	}
	
	public MutatesReceiver(){
		
		field = new MutatesReceiver(this);//field is still free at this point, since this is not committed
		field.setField();//OK, we can mutate objects under construction (i.e. free objects)
		field2 = new MutatesReceiver();//field2 is now committed 
		field2.setField();//Error, field2 is committed at this point
	}
	
	public MutatesReceiver(@Unclassified MutatesReceiver other){
		this.field.setField();//OK
		field.setField();//OK
		other.setField(); //Error, since other _may_ be committed (even though it never is in this code), we cannot mutate it.
	}
	
	@FreeM public void test2(){
		this.x = 0;
		field.setField();
		x = 0;
	}
	
	
	public @Mutates void test(){
		@Mutable int z = this.x;
		@Mutable int y = x;
	}
	
}