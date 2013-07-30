package test;

import javacop.annotations.Free;
import javacop.annotations.Immutable;
import javacop.annotations.Mutable;
import javacop.annotations.Unclassified;

public class Subtypes {

	public void main(String[] args) {
		@Mutable Object m = new Object();
		@Immutable Object i = new Object();
		@Mutable Object m2 = i; //ERROR: assigning immutable to mutable
		@Immutable Object i2 = m;//ERROR: assigning mutable to immutable
		
		@Mutable Object m3 = meth();//ERROR: assigning immutable to mutable
		
		
	}
	
	public @Immutable Object meth(){
		@Mutable Object m = null;
		meth();//OK
		meth2();//ERROR: calling free-receiver-method with unclassified receiver
		meth3();//OK
		return m;//ERROR: returning mutable when expecting immutable
	}
	
	@Free public void meth2(){
		meth();//ERROR: calling committed-receiver-method with unclassified receiver
		meth2();//OK
		meth3();//OK
	}
	
	@Unclassified public void meth3(){
		meth();//ERROR: calling committed-receiver-method with unclassified receiver
		meth2();//ERROR: calling free-receiver-method with unclassified receiver
		meth3();//OK
	}

}
