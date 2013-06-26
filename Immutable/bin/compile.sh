#!/bin/bash
JCOPJAR=/home/ezulkosk/workspace/Immutable/lib/JavaCop.jar
export JCOPJAR

jcopc javacop/initrules/InitRules
jar cf InitRules.jar javacop
javacop -jcp InitRules.jar javacop.initrules.InitRules test/List.java
