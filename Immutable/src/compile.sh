#!/bin/bash
JCOPJAR=../lib/JavaCop.jar 
export JCOPJAR

# We assume jcopc and javacop are on path.

jcopc javacop/immutability/ImmutabilityRules
jar cf ImmutabilityRules.jar javacop

########################################
#
# You should be able to uncomment any  
# of these to run the tests. The first 
# 3 tests are probably the most        
# informative.
#
# Note: java.lang.String.javaback should
# be changed to java.lang.String.java
# for that test, but make sure to
# change it back (or remove it) before 
# running other tests
#
########################################

#javacop -jcp ImmutabilityRules.jar javacop.immutability.ImmutabilityRules test/DeepImmutability.java 
#javacop -jcp ImmutabilityRules.jar javacop.immutability.ImmutabilityRules test/Subtypes.java
#javacop -jcp ImmutabilityRules.jar javacop.immutability.ImmutabilityRules test/MutatesReceiver.java

#javacop -jcp ImmutabilityRules.jar javacop.immutability.ImmutabilityRules java/lang/Boolean.java
javacop -jcp ImmutabilityRules.jar javacop.immutability.ImmutabilityRules java/lang/String.java
#javacop -jcp ImmutabilityRules.jar javacop.immutability.ImmutabilityRules java/util/HashMap.java

#javacop -jcp ImmutabilityRules.jar javacop.immutability.ImmutabilityRules kodkod/ast/*.java