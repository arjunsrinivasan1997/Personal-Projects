def hailstone(n):
    while n!=1:           
        if n%2==0:
            n=n//2
            print(str(bin(n))[2:len(str(bin(n)))])
        else:
            n=n*3+1
            print(str(bin(n))[2:len(str(bin(n)))])

hailstone(1000000000000000000000000000000000000000000000000000000000000)
