package test;

import javacop.annotations.Immutable;
import javacop.annotations.Mutable;

//Demonstrates var
public class DeepImmutability {

	@Immutable DeepImmutability imm;
	@Mutable DeepImmutability mut;
	
	public void main(){
		@Immutable DeepImmutability i = new DeepImmutability();
		@Mutable DeepImmutability m = new DeepImmutability();
		
		immMeth(i.imm);//OK
		mutMeth(i.imm);//Error, imm is immutable
		
		mutMeth(m.mut.imm);//Error, imm is immutable 
		mutMeth(m.mut.imm.mut);//Error, any subfield of an immutable must be immutable
		
		immMeth(m.mut.mut.imm.mut);//OK
		immMeth(m.mut.mut.mut.mut);//Error, all mutable subfields, so it is mutable
	}
	
	//only takes immutable objects
	public void immMeth(@Immutable DeepImmutability o){ }
	//only takes mutable objects
	public void mutMeth(@Mutable DeepImmutability o){ }

}
