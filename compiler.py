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

    #consume one character, check again given character and raise syntax error if not matching
    def Depo(self, xChar):
        if self.xSourceBuffer.pop(0) != xChar:
            print([chr(x) for x in self.xSourceBuffer])
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
        self.Depo(ord(' '))
        self.xTempBuffer.append(0)

        if self.Match([ord(x) for x in "let"] + [0]):
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
            self.EvalObj()
            self.Write("sRD 0")
            self.Write("out 0")
        
        
        
    def Compile(self, xRaw):
        self.xSourceBuffer = [ord(x) for x in list(xRaw)] + [0]



        while True:
            self.Command()


            #check eof
            if self.xSourceBuffer[0] == 0 or self.xSourceBuffer[1] == 0:
                break
            else:
                self.Depo(ord('\n'))

            
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