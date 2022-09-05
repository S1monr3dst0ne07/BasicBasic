# BasicBasic
BasicBasic is a minimalistic version of the Basic Programming Language,
intended for writing a compiler in itself. BasicBasic has only the bare minimum
of command needed to do pattern matching and other things needed for compilation,
but also makes life easier, by using labels instead of line numbers.
BasicBasic is not intended for writing actual programs, but just to serve
as a demonstration of a toy-language, powerful enough to self-host.
The compiler, BasicBasic implements, compiles to S1monsAssembly.

## Compiling

Compiling the compiler, with the external python compiler:
```
python compiler.py progs\bb.bb > build.s1
```

Compiling the compiler, with itself:
```
python "S1monsAssembly4 Virtual Machine v3.py" -f build.s1 -a progs\bb.bb@10000 -l > subbuild.s1
```

Compiling some other program, with the self-compiled compiler:
```
python "S1monsAssembly4 Virtual Machine v3.py" -f subbuild.s1 -a progs\Count.bb@10000 -l > subsubbuild.s1
```

And finally running the resulting assembly:
```
python "S1monsAssembly4 Virtual Machine v3.py" -f subsubbuild.s1
```