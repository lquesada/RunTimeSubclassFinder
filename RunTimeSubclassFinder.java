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
    * Neither the name of the <organization> nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

// RunTimeSubclassFinder - Find all the classes implementing or inheriting
//                         from a given interface or class.

// Original code from JavaWorld:
//
// - http://www.javaworld.com/javaworld/javatips/jw-javatip113.html 
//
// Also inspired by Daniel Le Berre RTSI class

// Improvements:
//
// - Avoids considering "<error>"-classes.
// - Also works if package is in a JAR file.
// - Recursive search in directories.
// - Does not instantiate classes, just checks them using reflection.

// Usage:
//
//    import com.elezeta.runtimesubclassfinder.RunTimeSubclassFinder;
//
//    RunTimeSubclassFinder.findSubclasses(package,class).

// Check Example.java for a complete example.

package com.elezeta.runtimesubclassfinder;

import java.util.Set;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;


public final class RunTimeSubclassFinder {

   private RunTimeSubclassFinder() { }

   /**
     * Find all the classes implementing or inheriting from a given interface or
     *   class.
     * @param pckgname the package name
     * @param parentClass the class
     * @return a set of extending classes
     * @throws ClassNotFoundException
     */
    public static Set<Class<?>> findSubclasses(String pckgname,
                                                Class<?> parentClass)
                                                throws ClassNotFoundException {

        Set<Class<?>> ret = new HashSet<Class<?>>();

        // Converts the package name into a directory name

        String name = pckgname;
        if (!name.startsWith("/")) {
            name = "/" + name;
        }
        name = name.replace('.','/');
    
        // If the package name yields no valid resource, return empty list

        URL url = RunTimeSubclassFinder.class.getResource(name);
        if (url == null) {
            return ret;
        }
    
        // Check whether package is in a directory or in a jar file.

        File directory = new File(url.getFile());

        if (directory.exists()) { // package is in a directory

            // Read directory contents.

            String [] f = directory.list();
            for (int i=0;i<f.length;i++) {
                if (f[i].endsWith(".class") &&  // If current file is class
                   !f[i].contains("<error>")) { // and not <error>-class...
                    String classname = f[i].substring(0,f[i].length()-6);
                    Class<?> newClass = getClass(pckgname+"."+classname);
                    if (newClass != null) {
                        if (parentClass.isAssignableFrom(newClass) &&
                           !parentClass.equals(newClass)) {

                           // Add to return list.

                            ret.add(newClass);

                        }
                    }
                }
                else { // If current file is a directory...

                    // Recursive call.

                    String newpckg = pckgname+"."+f[i];
                    ret.addAll(findSubclasses(newpckg,parentClass));

                }
            }
        } else { // package is in a jar file
            JarInputStream jarFile;
            try {

                // Get location from URI.

                String URI = parentClass.getProtectionDomain().getCodeSource()
                                        .getLocation().toString();
                String location = URI.substring(5); // remove leading "file:"
         
                // Read jar contents.

                jarFile = new JarInputStream(new FileInputStream(location));
                JarEntry e;
                e = jarFile.getNextJarEntry();
                while (e != null) {
                    try {
                        String ename = e.getName();
                        if (ename.endsWith(".class") && //If current file
                           !ename.contains("<error>")) { // is class...
                            String classname = ename.substring(0,
                                 ename.length()-6);

                            // Remove leading "/".

                            if (classname.startsWith("/")) {
                                classname = classname.substring(1);
                            }

                            // Convert directory name into package name.

                            classname = classname.replace('/','.');
                            Class<?> newClass = getClass(classname);
                            if (newClass != null) {
                                if (parentClass.isAssignableFrom(newClass) &&
                                   !parentClass.equals(newClass)) {
                    
                                    // Add to return list.

                                    ret.add(newClass);

                                }
                            }
                        }
                        e = jarFile.getNextJarEntry();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }                    
                }
                jarFile.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }                        
        }
        return ret;
    }


    /**
     * Returns the Class object of a primitive or non-primitive class name.
     * @param className the class name
     * @return the Class object
     * @throws ClassNotFoundException
     */
    private static Class getClass(String className)
                                                throws ClassNotFoundException {
        if (!className.contains(".")) {
            if("int" .equals(className)) return int.class;
            if("long".equals(className)) return long.class;
            if("byte".equals(className)) return byte.class;
            if("short".equals(className)) return short.class;
            if("float".equals(className)) return float.class;
            if("double".equals(className)) return double.class;
            if("boolean".equals(className)) return boolean.class;
            if("char".equals(className)) return char.class;
        }
        return Class.forName(className);
    }
}
