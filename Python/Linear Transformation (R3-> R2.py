class R3_to_R2_LT():
    "Represents a transformation from R^3 to R^2. x is a 3 element list of real numbers and y is a 2 element list of real numbers" 
    def __init__(self,x,y):
        
        self.r3 = x
        self.r2 = y
    def __repr__(self):
        a= str(self.r3)+ " " + str(self.r2)
        return a
    def add(self, z):
        a = []
        b = []
        for l in range(len(self.r3)):
            a.append(self.r3[l]+z.r3[l])
        for m in range(len(self.r2)):
            b.append(self.r2[m]+z.r2[m])
        print(self.r3,"+",z.r3,"=",a)
        print(self.r2,"+",z.r2,"=",b)
        return R3_to_R2_LT(a,b)
    def scale(self,constant):
        a = list(self.r3)
        b = list(self.r2)
        for l in range(len(self.r3)):
            a[l] = self.r3[l]* constant
        for l in range(len(self.r2)):
            b[l] = self.r2[l] * constant
        print(self.r3,"*",constant,"=",a)
        print(self.r2,"*",constant,"=",b)
        return R3_to_R2_LT(a,b)
def test():
    a= R3_to_R2_LT([1,2,3],[2,1])
    b= R3_to_R2_LT([0,1,0],[1,1])
    c = R3_to_R2_LT([0,0,1],[1,2])
    print("a", a)
    print("b", b)
    print("c", c)
    print("b+c")
    d=b.add(c)
    print("d", d)
    print("c*-2")
    e=c.scale(-2)
    print("e",e)
    print("d+e")
    f=d.add(e)
    print("f",f)
    print("f * -1")
    g=f.scale(-1)
    print("g + c")
    h=g.add(c)
    print("h",h)
    
