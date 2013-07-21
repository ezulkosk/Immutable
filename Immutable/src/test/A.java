package test;

import javacop.annotations.Free;
import javacop.annotations.Immutable;
import javacop.annotations.Unclassified;


public class A{
	
	@Immutable A i_A;
	@Unclassified public Object method(@Free Object o){
		@Unclassified Object o2 = null;
		int a = 5;
		int b = 6;
		a=  6; 
		o = this.method(null);
		
		A d = new A(o);
		return o2;
	}
	
	public A(Object o){
		
	}
	
	public void method2(@Free Object o3)
	{
		int c = 7;
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