/*
Copyright (c) 2013, Luis Quesada - https://github.com/lquesada
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * The name of its contributors may not be used to endorse or promote
      products derived from this software without specific prior written
      permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

// RunTimeSubclassFinder Example.
//
// Running...
//
// $ javac -d . RunTimeSubclassFinder.java  Example.java
// $ java com.elezeta.runtimesubclassfinder.example.Example 
//
// or
//
// $ javac -d . RunTimeSubclassFinder.java  Example.java
// $ jar cfm Example.jar examplemanifest com
// $ java -jar Example.jar
//
// ...yields...
//
// com.elezeta.runtimesubclassfinder.example.Example$InheritsFromExample
// com.elezeta.runtimesubclassfinder.example.AnotherInheritingClass;

package com.elezeta.runtimesubclassfinder.example;

import com.elezeta.runtimesubclassfinder.RunTimeSubclassFinder;
import java.util.Set;
import java.util.Iterator;

public class Example {

  class InheritsFromExample extends Example {

  }

  public static void main(String args[]) {
      try {
          Set<Class<?>> ret;
          ret = RunTimeSubclassFinder.findSubclasses("com.elezeta",Example.class);
          for (Iterator<Class<?>> ite = ret.iterator();ite.hasNext();) {
              System.out.println(ite.next().getName());
          }
      } catch (Exception e) {
          e.printStackTrace();
      }
  }

}

class AnotherInheritingClass extends Example {

}
