package test;

import javacop.annotations.Free;
import javacop.annotations.Mutates;


public class A{
	int x;
	
	public void meth(){
		setField(1);
	}
	
	public void setField(int f){
		setField2(f);
	}
	
	@Mutates public void setField2(int f){
		x = f;
	}
	
}
	/*
	public void meth(){
		B b = new B();
		A a = meth2(new Object());
		A a2 = new A().meth2(null);
		int i = b.b.meth(null);
		
		
	}
	
	public A meth2(Object o){
		meth3();
		return null;
	}
	
	
	@Mutates public A meth3(){
		return null;
	}
	*/
	//public A(){}
	/*
	public A(@Free A other){
		@Free A a = new A(this);
		int y = other.x;
		
	}
	
	@Mutates public boolean setX(int i){
		test();
		B b = new B();
		b.meth(null);
		return true;
	}
	
	@Free public void test(){
		
	}
	
}

class B{
	public B b;
	public int meth(java.lang.Object o2){
		return 1;
	}
}



/*
public class A {
	
	@Mutable A m_AInA;
	@Immutable A i_AInA; 
	public A()
	{
	}
	
	public A(A a){}
	
	
	public void test(){
		@Mutable B m_BInMain = new B(new A());
		@Immutable A i_AInMain = m_BInMain.i_AInB.m_AInA;
		mutA1(m_BInMain.i_AInB.m_AInA);
		//@Immutable B b = new B(m_BInMain);
	}
	
	public void mutA1(@Immutable A m_A){
		
	}
	
}

class B{
	public @Immutable A i_AInB; 
	public @Mutable A m_AInB;
	
	public B(A a){
		this.m_AInB = a;
		this.i_AInB = a;
	}
	
	public B(@Mutable B b){
		
	}
	
}*/