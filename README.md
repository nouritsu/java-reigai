# About

A dynamically typed, object oriented interpreted language implementation in Java.  
Inspired by [Crafting Interpreters by Robert Nystrom](https://craftinginterpreters.com/)

This version of the interpreter is implemented with a recursive descent parser and a tree walk style interpreter.  
In simpler terms - it is orders of magnitude than python.  
Calculating the 20th fibonacci number in python takes about `250ms`, it took Reigai about `1500ms`. Check `test/speed` for code.

# Usage

- Clone the repository
- Use command `make [file]`
- Running command without file argument will run interpreter in REPL mode
- If the file argument is provided, the file will be interpreted.

You need to have a Java and Make installed.  
Tested on Windows and Linux.

# Documentation

Check [this](https://craftinginterpreters.com/the-lox-language.html) for documentation of the language.  
There are a few features added in Reigai that are missing in Lox, with more coming.

## Mod Operator

```
reiPL :> print 5 % 5;
0
reiPL :> print 10 % 8;
2
```

Returns the remainder after performing division.

## Native Functions

### Len

```
reiPL :> print len("Hello");
5
```

Returns the length of string provided.

### Round, Floor, Ceil

```
reiPL :> print round(3.1415);
3
reiPL :> print floor(3.1415);
3
reiPL :> print ceil(3.1415);
4
```

Work as expected.

### Abs

```
reiPL :> print abs(-6.28);
6.28
```

Works as expected.

### Pow

```
reiPL :> print pow(2, 10);
1024
```

Raises first argument to second argument.
