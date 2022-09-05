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
puts 'brk
'
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
 poke ap 0
 gosub Match
 if rt == 1 then goto Command::Let else rem ''
 
 let ap = 2000 + 0
 poke ap 112
 let ap = ap + 1
 poke ap 114
 let ap = ap + 1
 poke ap 105
 let ap = ap + 1
 poke ap 110
 let ap = ap + 1
 poke ap 116
 let ap = ap + 1
 poke ap 0 
 gosub Match
 if rt == 1 then goto Command::Print else rem '' 
 
 let ap = 2000 + 0
 poke ap 105
 let ap = ap + 1
 poke ap 102
 let ap = ap + 1
 poke ap 0
 gosub Match
 if rt == 1 then goto Command::If else rem ''
 
 let ap = 2000 + 0
 poke ap 103
 let ap = ap + 1
 poke ap 111
 let ap = ap + 1
 poke ap 116
 let ap = ap + 1
 poke ap 111
 let ap = ap + 1
 poke ap 0
 gosub Match
 if rt == 1 then goto Command::Goto else rem ''
 
 let ap = 2000 + 0 
 poke ap 108
 let ap = ap + 1
 poke ap 97
 let ap = ap + 1
 poke ap 98
 let ap = ap + 1
 poke ap 101
 let ap = ap + 1
 poke ap 108
 let ap = ap + 1
 poke ap 0
 gosub Match
 if rt == 1 then goto Command::Label else rem ''
 
 let ap = 2000 + 0 
 poke ap 114
 let ap = ap + 1
 poke ap 101
 let ap = ap + 1
 poke ap 109
 let ap = ap + 1
 poke ap 0
 gosub Match
 if rt == 1 then goto Command::Rem else rem ''
 
 let ap = 2000 + 0
 poke ap 103
 let ap = ap + 1
 poke ap 111
 let ap = ap + 1
 poke ap 115
 let ap = ap + 1
 poke ap 117
 let ap = ap + 1
 poke ap 98
 let ap = ap + 1
 poke ap 0
 gosub Match
 if rt == 1 then goto Command::Gosub else rem '' 

 let ap = 2000 + 0
 poke ap 114
 let ap = ap + 1
 poke ap 101
 let ap = ap + 1
 poke ap 116
 let ap = ap + 1
 poke ap 117
 let ap = ap + 1
 poke ap 114
 let ap = ap + 1
 poke ap 110
 let ap = ap + 1
 poke ap 0
 gosub Match
 if rt == 1 then goto Command::Return else rem '' 
 
 let ap = 2000 + 0
 poke ap 101
 let ap = ap + 1
 poke ap 110
 let ap = ap + 1
 poke ap 100
 let ap = ap + 1
 poke ap 0 
 gosub Match
 if rt == 1 then goto Command::End else rem ''
 
 let ap = 2000 + 0
 poke ap 112
 let ap = ap + 1
 poke ap 117
 let ap = ap + 1
 poke ap 115
 let ap = ap + 1
 poke ap 104
 let ap = ap + 1
 poke ap 0
 gosub Match
 if rt == 1 then goto Command::Push else rem ''
 
 let ap = 2000 + 0
 poke ap 112
 let ap = ap + 1
 poke ap 117
 let ap = ap + 1
 poke ap 108
 let ap = ap + 1
 poke ap 108
 let ap = ap + 1
 poke ap 0
 gosub Match
 if rt == 1 then goto Command::Pull else rem ''

 let ap = 2000 + 0
 poke ap 112
 let ap = ap + 1
 poke ap 101
 let ap = ap + 1
 poke ap 101
 let ap = ap + 1
 poke ap 107
 let ap = ap + 1
 poke ap 0
 gosub Match
 if rt == 1 then goto Command::Peek else rem ''

 let ap = 2000 + 0
 poke ap 112
 let ap = ap + 1
 poke ap 111
 let ap = ap + 1
 poke ap 107
 let ap = ap + 1
 poke ap 101
 let ap = ap + 1
 poke ap 0
 gosub Match
 if rt == 1 then goto Command::Poke else rem ''

 let ap = 2000 + 0
 poke ap 112
 let ap = ap + 1
 poke ap 117
 let ap = ap + 1
 poke ap 116
 let ap = ap + 1
 poke ap 115
 let ap = ap + 1
 poke ap 0
 gosub Match
 if rt == 1 then goto Command::Puts else rem ''
 
 
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
  let lt = li + 1
  puts 'sAD 10
sRD 11
clr
sAD 12
lab temp'
  print li
  puts '
lDA 10
jm0 temp'
  print lt
  puts '
set 1
sub
sAD 10
lDA 12
lDR 11
add
sAD 12
got temp'
  print li
  puts '
lab temp'
  print lt
  puts '
lDA 12
'
  let li = li + 2
 return
 


label Command::Print
 let am = 32 + 0
 gosub Depo
 gosub EvalObj
 puts 'sRD 0
out 0
'
 return
 
 
 
label Command::If
 let am = 32 + 0
 gosub Depo
 let lt = li + 1
 
 puts 'clr'
 gosub pNl
 
 gosub EvalObj
 puts 'add'
 gosub pNl
 let am = 32 + 0
 gosub Depo
 
 gosub Cons
 peek 1000 oo
 peek 1001 ot
 if oo == 61 then if ot == 61 then let op = 0 + 0 else rem '' else rem ''
 if oo == 60 then if ot == 62 then let op = 1 + 0 else rem '' else rem ''
 if oo == 62 then if ot == 0 then let op = 2 + 0 else rem '' else rem ''
 if oo == 60 then if ot == 0 then let op = 3 + 0 else rem '' else rem ''
 
 let am = 32 + 0
 gosub Depo
 gosub EvalObj
 
 let am = 32 + 0
 gosub Depo
 let am = 116 + 0
 gosub Depo
 let am = 104 + 0
 gosub Depo
 let am = 101 + 0
 gosub Depo
 let am = 110 + 0
 gosub Depo
 let am = 32 + 0
 gosub Depo

 if op == 0 then goto Command::If::Eqe else rem ''
 if op == 1 then goto Command::If::Not else rem ''
 if op == 2 then goto Command::If::Gra else rem ''
 if op == 3 then goto Command::If::Les else rem ''
 goto Error 
 
 label Command::If::Return

 push li
 let li = li + 3
 gosub Command
 
 let am = 32 + 0
 gosub Depo
 let am = 101 + 0
 gosub Depo
 let am = 108 + 0
 gosub Depo
 let am = 115 + 0
 gosub Depo
 let am = 101 + 0
 gosub Depo
 let am = 32 + 0
 gosub Depo

 pull tm
 let tm = tm + 2
 push tm
 puts 'got _'
 print tm
 gosub pNl
 let tm = tm - 1
 puts 'lab _'
 print tm
 gosub pNl

 gosub Command
 
 pull tm
 puts 'lab _'
 print tm
 gosub pNl
 return
 
 label Command::If::Eqe
  puts 'jmA _'
  print li
  gosub pNl
  puts 'got _'
  print lt
  gosub pNl
  puts 'lab _'
  print li
  gosub pNl
 goto Command::If::Return
 label Command::If::Not
  puts 'jmA _'
  print lt
  gosub pNl
 goto Command::If::Return
 label Command::If::Gra
  puts 'jmG _'
  print li
  gosub pNl
  puts 'got _'
  print lt
  gosub pNl
  puts 'lab _'
  print li
  gosub pNl
 goto Command::If::Return
 label Command::If::Les
  puts 'jmL _'
  print li
  gosub pNl
  puts 'got _'
  print lt
  gosub pNl
  puts 'lab _'
  print li
  gosub pNl
 goto Command::If::Return
 
 
label Command::Goto
 let am = 32 + 0
 gosub Depo
 gosub Cons
 puts 'got '
 let tp = 1000 + 0
 label Command::Goto::Loop
  peek tp tm
  if tm == 0 then goto Command::Goto::Exit else rem ''
  puts tm
  let tp = tp + 1
  goto Command::Goto::Loop
 label Command::Goto::Exit
  gosub pNl
  return
  
label Command::Label
 let am = 32 + 0
 gosub Depo
 gosub Cons
 puts 'lab '
 let tp = 1000 + 0
 label Command::Label::Loop
  peek tp tm
  if tm == 0 then goto Command::Label::Exit else rem ''
  puts tm
  let tp = tp + 1
  goto Command::Label::Loop
 label Command::Label::Exit
  gosub pNl
  return
 
label Command::Rem
 let am = 32 + 0
 gosub Depo
 gosub String
 return 
 
label Command::Gosub
 let am = 32 + 0
 gosub Depo
 gosub Cons
 puts 'jmS '
 let tp = 1000 + 0
 label Command::Label::Loop
  peek tp tm
  if tm == 0 then goto Command::Label::Exit else rem ''
  puts tm
  let tp = tp + 1
  goto Command::Label::Loop
 label Command::Label::Exit
  gosub pNl
  return
 
label Command::Return
 puts 'ret'
 gosub pNl
 return
 
label Command::End
 puts 'brk'
 gosub pNl
 return
 
label Command::Push
 let am = 32 + 0
 gosub Depo
 puts 'clr'
 gosub pNl
 gosub EvalObj
 puts 'add'
 gosub pNl
 puts 'pha'
 gosub pNl
 return
 
label Command::Pull
 let am = 32 + 0
 gosub Depo
 gosub Cons
 gosub Var2Addr
 puts 'pla'
 gosub pNl
 puts 'sAD '
 print rt
 gosub pNl
 return

label Command::Peek
 let am = 32 + 0
 gosub Depo
 gosub Cons
 peek 1000 tm
 if tm > 60 then puts 'lPR ' else puts 'lDR '
 if tm > 60 then gosub Var2Addr else gosub Const2Val
 print rt
 gosub pNl

 let am = 32 + 0
 gosub Depo
 gosub Cons
 gosub Var2Addr
 puts 'sRD '
 print rt
 gosub pNl
 return 
 
label Command::Poke
 let am = 32 + 0
 gosub Depo
 gosub Cons
 peek 1000 ty
 if ty > 60 then gosub Var2Addr else gosub Const2Val
 let rs = rt + 0
 
 let am = 32 + 0
 gosub Depo
 gosub EvalObj
 if ty > 60 then puts 'sRP ' else puts 'sRD '
 print rs
 gosub pNl
 return
 
label Command::Puts
 let am = 32 + 0
 gosub Depo
 
 peek sp tm
 if tm == 39 then goto Command::Puts::String else rem ''
 gosub Cons
 gosub Var2Addr
 puts 'lDA '
 print rt
 gosub pNl
 puts 'putstr'
 gosub pNl
 return
 
 label Command::Puts::String
  gosub String
  let tp = 1000 + 0
  label Command::Puts::StringLoop
   peek tp tm
   if tm == 0 then goto Command::Puts::Exit else rem ''
   let tp = tp + 1
   puts 'clr
set '
   print tm
   gosub pNl
   puts 'add
putstr'
   gosub pNl
   goto Command::Puts::StringLoop
  label Command::Puts::Exit
  
  return
 
 
 
 
 
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
  goto String::Loop
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
 if tm > 60 then goto EvalObj::Var else rem ''
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
 return

rem 'helper routines'
label Error
 puts 'Error: Invaild Syntax'
 end

label pNl
 put '
'
 return
