poke 100 1
poke 101 10
poke 102 100
poke 103 1000
let tp = 102 + 0



label test
 peek tp tm
 if tm == 0 then end else rem ''
 print tm
 let tp = tp + 1
 goto test
