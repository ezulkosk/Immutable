#!/bin/bash
JCOPJAR=../lib/JavaCop.jar 
export JCOPJAR

jcopc javacop/immutability/ImmutabilityRules
jar cf ImmutabilityRules.jar javacop
#javacop -jcp ImmutabilityRules.jar javacop.immutability.ImmutabilityRules test/List.java
#javacop -jcp ImmutabilityRules.jar javacop.immutability.ImmutabilityRules test/A.java
#javacop -jcp ImmutabilityRules.jar javacop.immutability.ImmutabilityRules java/lang/String.java
javacop -jcp ImmutabilityRules.jar javacop.immutability.ImmutabilityRules test/MutatesReceiver.java