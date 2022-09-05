let ix = 10000 + 0

label loop

peek ix vx
if vx == 0 then end else rem 'exit if value is 0, aka terminator'
puts vx

let ix = ix + 1
goto loop