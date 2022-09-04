import sys
import traceback


class cUtils:
    @staticmethod
    def Error(xMsg):
        print(xMsg)
        traceback.print_stack()
        sys.exit(0)

class cCompiler:
    def __init__(self):
        self.xSourceBuffer = []
        self.xTempBuffer   = []
        self.xOutputBuffer = ""

        self.xLabelIndex = 0

    #consumes source buffer, writing into temp buffer, 'till space or eof is hit
    def Cons(self):
        self.xTempBuffer = []
        while True:
            xChar = self.xSourceBuffer[0]
            
            if xChar == ord(' ') or xChar == 0 or xChar == ord('\n'): 
                break
                        
            else: 
                self.xTempBuffer.append(xChar)
                self.xSourceBuffer.pop(0)

    #match string in temp buffer with argument
    def Match(self, xCharArray):
        
        xIndex = 0
        while True:
            if xCharArray[xIndex] == 0 or self.xTempBuffer[xIndex] == 0:  return True
            elif xCharArray[xIndex] != self.xTempBuffer[xIndex]:          return False

            xIndex += 1
        return True

    #consume string
    def String(self):
        self.Depo(ord("'"))
        self.xTempBuffer = []        
        
        while True:
            xChar = self.xSourceBuffer.pop(0)

            if xChar == ord("'"):
                self.xTempBuffer.append(0)                
                break
            
            self.xTempBuffer.append(xChar)

    #consume one character, check again given character and raise syntax error if not matching
    def Depo(self, xChar):
        if self.xSourceBuffer.pop(0) != xChar:
            cUtils.Error("Error: Invaild syntax")            

    #evaluate object (const or var) and set reg
    def EvalObj(self):
        self.Cons()
        if self.xTempBuffer[0] > 60:
            self.Write(f'lDR {self.Var2Addr()}')      
        else:
            self.Write(f'set {self.Const2Val()}')
            
    #convert char array to value
    #from temp
    def Const2Val(self):
        self.xTempBuffer.append(0)
        xTemp = 0            

        while True:
            xChar = self.xTempBuffer.pop(0)            
            if xChar == 0:
                break
            else:
                xTemp = xTemp * 10 + (xChar - 48)
        
        return xTemp

    #(statically) map variable to memory address
    #from temp
    def Var2Addr(self):
        xChar0 = self.xTempBuffer[0]
        xChar1 = self.xTempBuffer[1]
        
        if xChar0 > 90: xChar0 -= 32
        if xChar1 > 90: xChar1 -= 32
                
        return (xChar1 - 65) + ((xChar0 - 65) * 27) + 100

    def Write(self, x):
        self.xOutputBuffer += x + "\n"

    def Command(self):
        self.Cons()
        self.xTempBuffer.append(0)
        #print([chr(x) for x in self.xTempBuffer])

        if self.Match([ord(x) for x in "let"] + [0]):
            self.Depo(ord(' '))
            self.Cons()
            xVar = self.Var2Addr()
            
            self.Depo(ord(' '))
            self.Depo(ord('='))
            self.Depo(ord(' '))
                
            self.Write("clr")
            
            self.EvalObj()
            self.Write("add")
            self.Depo(ord(' '))

            self.Cons()
            xOp = self.xTempBuffer[0]

            self.Depo(ord(' '))
            self.EvalObj()

            if   xOp == 43: self.Write("add")
            elif xOp == 45: self.Write("sub")
            elif xOp == 42:
                #10 - A
                #11 - B
                #12 - Result
                self.Write("clr")
                self.Write("sAD 12")
                self.Write("sAD 10")
                self.Write("sRD 11")
                self.Write(f"lab temp{self.xLabelIndex}")
 
                self.Write("lDA 10")
                self.Write(f"jm0 temp{self.xLabelIndex + 1}")
                self.Write("set 1")
                self.Write("add")
                self.Write("sAD 10")
            
                self.Write("lDA 12")
                self.Write("lDR 11")
                self.Write("add")
                self.Write("sAD 12")
            
                self.Write(f"got temp{self.xLabelIndex}")
                self.Write(f"lab temp{self.xLabelIndex + 1}")

                self.Write("lDA 12")
                self.xLabelIndex += 2

            self.Write(f'sAD {xVar}')

        elif self.Match([ord(x) for x in "print"] + [0]):
            self.Depo(ord(' '))
            self.EvalObj()
            self.Write("sRD 0")
            self.Write("out 0")
        
        elif self.Match([ord(x) for x in "if"] + [0]):
            self.Depo(ord(' '))
            #keep reference to label index
            xSkipLabel = self.xLabelIndex
            xElseLabel = self.xLabelIndex + 1
            xEndLabel  = self.xLabelIndex + 2 
            self.xLabelIndex += 3

            self.Write("clr")
            
            self.EvalObj()
            self.Write("add")
            self.Depo(ord(' '))

            self.Cons()
            self.xOp = self.xTempBuffer[:] + [0, 0]

            self.Depo(ord(' '))
            self.EvalObj()

            self.Depo(ord(' '))
            self.Depo(ord('t'))
            self.Depo(ord('h'))
            self.Depo(ord('e'))
            self.Depo(ord('n'))
            self.Depo(ord(' '))
                        
            #skip by condition
            if   self.xOp[0] == 61 and self.xOp[1] == 61: 
                self.Write(f'jmA _{xSkipLabel}')
                self.Write(f'got _{xElseLabel}')
                self.Write(f'lab _{xSkipLabel}')
            elif self.xOp[0] == 60 and self.xOp[1] == 62: 
                self.Write(f'jmA _{xElseLabel}')
            elif self.xOp[0] == 62:
                self.Write(f'jmG _{xSkipLabel}')
                self.Write(f'got _{xElseLabel}')
                self.Write(f'lab _{xSkipLabel}')                
            elif self.xOp[0] == 60:
                self.Write(f'jmL _{xSkipLabel}')
                self.Write(f'got _{xElseLabel}')
                self.Write(f'lab _{xSkipLabel}')
                
            else:
                cUtils.Error("Error: Invaild syntax")
            

            #evaluate first sub-command
            self.Command()
            
            #consume else
            self.Depo(ord(' '))
            self.Depo(ord('e'))
            self.Depo(ord('l'))
            self.Depo(ord('s'))
            self.Depo(ord('e'))
            self.Depo(ord(' '))            
            
            #second flow change
            self.Write(f'got _{xEndLabel}')
            self.Write(f'lab _{xElseLabel}')
            
            #second
            self.Command()
            
            #end label
            self.Write(f'lab _{xEndLabel}')     
            
        elif self.Match([ord(x) for x in "goto"] + [0]):            
            self.Depo(ord(' '))
            self.Cons()
            self.Write(f'got {"".join([chr(x) for x in self.xTempBuffer])}')     
            
        elif self.Match([ord(x) for x in "label"] + [0]):
            self.Depo(ord(' '))
            self.Cons()            
            self.Write(f'lab {"".join([chr(x) for x in self.xTempBuffer])}')    
            
        elif self.Match([ord(x) for x in "rem"] + [0]):
            self.Depo(ord(' '))
            self.String()

        elif self.Match([ord(x) for x in "gosub"] + [0]):
            self.Depo(ord(' '))
            self.Cons()            
            self.Write(f'jmS {"".join([chr(x) for x in self.xTempBuffer])}')    

        elif self.Match([ord(x) for x in "return"] + [0]):
            self.Write('ret')
            
        elif self.Match([ord(x) for x in "end"] + [0]):
            self.Write('brk')

        elif self.Match([ord(x) for x in "push"] + [0]):
            self.Depo(ord(' '))
            self.Write("clr")
            self.EvalObj()
            self.Write("add")
            self.Write(f'pha')
        
        elif self.Match([ord(x) for x in "pull"] + [0]):
            self.Depo(ord(' '))
            self.Cons()
            xVar = self.Var2Addr()            
            self.Write(f'pla')
            self.Write(f'sAD {xVar}')
        
        elif self.Match([ord(x) for x in "peek"] + [0]):
            self.Depo(ord(' '))
            self.Cons()
            if self.xTempBuffer[0] > 60:    xAddr = self.Var2Addr()      
            else:                           xAddr = self.Const2Val()
            
            self.Depo(ord(' '))
            self.Cons()
            xDest = self.Var2Addr()
            self.Write(f"lPA {xAddr}")
            self.Write(f"sAD {xDest}")

        elif self.Match([ord(x) for x in "poke"] + [0]):
            self.Depo(ord(' '))
            self.Cons()
            if self.xTempBuffer[0] > 60:    xAddr = self.Var2Addr()      
            else:                           xAddr = self.Const2Val()
            
            self.Depo(ord(' '))
            self.EvalObj()
            self.Write(f"sRP {xAddr}")
            
        elif self.Match([ord(x) for x in "puts"] + [0]):
            self.Depo(ord(' '))
            
            if self.xSourceBuffer[0] == "'":                     
                self.String()
                while self.xTempBuffer[0] != 0:
                    self.Write("clr")
                    self.Write(f"set {self.xTempBuffer.pop(0)}")
                    self.Write("add")
                    self.Write("putstr")
            
            else:
                self.Cons()
                xVar = self.Var2Addr()
                self.Write(f"lDA {xVar}")
                self.Write("putstr")
                
            
        
    def Compile(self, xRaw):
        self.xSourceBuffer = [ord(x) for x in list(xRaw)] + [0]
        while True:
            
            #check empty lines
            while self.xSourceBuffer[0] == ord('\n'):
                self.Depo(ord('\n'))                

            #check eof
            if self.xSourceBuffer[0] == 0 or self.xSourceBuffer[1] == 0:
                break

            self.Command()


            
        self.Write("brk")
            
        return self.xOutputBuffer

def Main():
    if len(sys.argv) < 2:
        cUtils.Error("Error: No input file specified")
        
    else:
        with open(sys.argv[1], "r") as xFile:
            xFileContent = xFile.read()
            xComp = cCompiler()
            xAsm = xComp.Compile(xFileContent)
        
            print(xAsm)


if __name__ == '__main__':
    Main()