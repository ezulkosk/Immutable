package test;

import javacop.annotations.FreeM;
import javacop.annotations.Immutable;
import javacop.annotations.Mutable;
import javacop.annotations.UnclassifiedM;

//Various examples of how the init-type and imm-type of objects may cause warnings.
public class Subtypes {

	public void main(String[] args) {
		@Mutable Object m = new Object();
		@Immutable Object i = new Object();
		@Mutable Object m2 = i; //Error, assigning immutable to mutable
		@Immutable Object i2 = m;//Error, assigning mutable to immutable		
		@Mutable Object m3 = meth();//Error, assigning immutable to mutable		
	}
	
	public @Immutable Object meth(){
		@Mutable Object m = null;
		meth();//OK
		this.meth();//OK
		meth2();//Error, calling free-receiver-method with committed receiver
		this.meth2();//Error, calling free-receiver-method with committed receiver
		meth3();//OK
		return m;//Error, returning mutable when expecting immutable
	}
	
	@FreeM public void meth2(){
		meth();//Error, calling committed-receiver-method with free receiver
		meth2();//OK
		meth3();//OK
	}
	
	@UnclassifiedM public void meth3(){
		meth();//Error, calling committed-receiver-method with unclassified receiver
		meth2();//Error, calling free-receiver-method with unclassified receiver
		meth3();//OK
	}

}
