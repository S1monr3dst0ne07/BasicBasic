rem 'oh boy here we go

compile and run with:
    

memory maps:
    $10000          -> start of input file
    $01000 - $02000 -> temp buffer
    $02000 - $02200 -> argument ptr (for Match routine mostly)
'

let sp = 10000 + 0
let tp = 1000 + 0
let li = 0 + 0

rem 'return value'
let rt = 0 + 0

label Compile

 label Compile::Skips
  peek sp tm
  if tm <> 10 then if tm <> 32 then goto Compile::Com else rem '' else rem ''
  let sp = sp + 1
  goto Compile::Skips
 label Compile::Com
 peek sp tm
 if tm == 0 then goto Done else rem ''
 let tp = sp + 1
 peek tp tm
 if tm == 0 then goto Done else rem ''

 gosub Command
 goto Compile
label Done
puts 'brk'
end
rem 'subroutines'


label Command
 gosub Cons
 
 let ap = 2000 + 0
 poke ap 108
 let ap = ap + 1
 poke ap 101
 let ap = ap + 1
 poke ap 116
 let ap = ap + 1
 gosub Match
 if rt == 1 then goto Command::Let else rem ''
 
 return

label Command::Let
 let am = 32 + 0
 gosub Depo
 gosub Cons
 gosub Var2Addr
 let xv = rt + 0
 
 
 let am = 32 + 0
 gosub Depo
 let am = 61 + 0
 gosub Depo
 let am = 32 + 0
 gosub Depo
 
 puts 'clr'
 gosub pNl
 
 gosub EvalObj
 puts 'add'
 gosub pNl
 let am = 32 + 0
 gosub Depo
 
 gosub Cons
 peek 1000 op
 
 let am = 32 + 0
 gosub Depo 
 gosub EvalObj

 
 if op == 43 then puts 'add
' else rem ''
 if op == 45 then puts 'sub
' else rem ''
 if op == 42 then gosub Command::Let::Mul else rem ''
 
 puts 'sAD '
 print xv
 gosub pNl
 return
 
 label Command::Let::Mul
 
 
 
 
 
 
 
 
 
 
 
 
label String
 let am = 39 + 0
 let tp = 1000 + 0
 gosub Depo
 label String::Loop
  peek sp tm
  if tm == 39 then goto String::Exit else rem ''
  poke tp tm
  let sp = sp + 1
  let tp = tp + 1
  goto Skips::Loop
 label String::Exit
  poke tp 0
 return

label Cons
 let tp = 1000 + 0
 label Cons::Loop
  peek sp tm
  if tm == 32 then goto Cons::Exit else rem ''
  if tm == 10 then goto Cons::Exit else rem ''
  if tm == 0 then goto Cons::Exit else rem ''
  poke tp tm
  let sp = sp + 1
  let tp = tp + 1
  goto Cons::Loop
 label Cons::Exit
  poke tp 0
 return

label Match
 let tp = 1000 + 0
 let ap = 2000 + 0
 label Match::Loop
  peek tp tm
  peek ap am
  if am == 0 then goto Match::True else rem ''
  if tm == 0 then goto Match::True else rem ''
  if am <> tm then goto Match::False else rem ''
  let tp = tp + 1
  let ap = ap + 1
  goto Match::Loop
 label Match::True
  let rt = 1 + 0
  return
 label Match::False
  let rt = 0 + 0
  return

label Depo
 peek sp tm
 let sp = sp + 1
 if tm == am then return else goto Error

label EvalObj
 gosub Cons
 let tp = 1000 + 0
 peek tp tm
 if tm > 60 then EvalObj::Var else rem ''
 puts 'set '
 gosub Const2Val
 print rt
 gosub pNl
 return
 label EvalObj::Var
  puts 'lDR '
  gosub Var2Addr
  print rt
  gosub pNl
  return

label Const2Val
 let rt = 0 + 0
 let tp = 1000 + 0
 label Const2Val::Loop
  peek tp tm
  if tm == 0 then return else rem ''
  let rt = rt * 10
  let tm = tm - 48
  let rt = rt + tm
  let tp = tp + 1
  goto Const2Val::Loop
  
label Var2Addr
 peek 1000 co
 peek 1001 ct
 let co = co - 97
 let co = co * 27
 let ct = ct - 97
 let rt = co + ct
 let rt = rt + 100
 print rt
 return

rem 'helper routines'
label Error
 puts 'Error: Invaild Syntax'
 end

label pNl
 put '
'
 return
