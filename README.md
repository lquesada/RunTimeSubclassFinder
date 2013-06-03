RunTimeSubclassFinder
============

RunTimeSubclassFinder - Find all the classes implementing or inheriting from a given interface or class.  
Copyright (c) 2010, Luis Quesada - https://github.com/lquesada


Original code from JavaWorld:

- http://www.javaworld.com/javaworld/javatips/jw-javatip113.html 

Also inspired by Daniel Le Berre RTSI class.

Improvements:

- Avoids considering "<error>"-classes.
- Also works if package is in a JAR file.
- Recursive search in directories.
- Does not instantiate classes, just checks them using reflection.

Usage:

    import org.modelcc.runtimesubclassfinder.RunTimeSubclassFinder;

    RunTimeSubclassFinder.findSubclasses(String packageName,Class classObject).

Check Example.java for a complete example.

