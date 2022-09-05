let ix = 0 + 0


gosub inc
gosub inc
gosub inc

print ix

gosub inc
gosub inc

print ix

push 1000
push 5
gosub add

print ix

gosub add

print ix

end


label inc
let ix = ix + 1
return

label add
pull ra
pull tm
push ra
let ix = ix + tm
return
