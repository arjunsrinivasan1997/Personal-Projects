#prime number checker
print("Prime number checker")
def prime_number_checker(number):
 
      if number<1:
            print (number, "is not a prime number because prime numbers must be greater than 1")
      elif number==2:
            print (2,"is a prime number")
      else:
            for a in range(2,number):
      
                  if (number%a) == 0:
                        print(number,"is not a prime number",'because',a,"times",number//a,"equals",number)
                        break
            else:
                  print(number, "is a prime number")
print("This program checks to see if a given number is prime")
a=int(input('enter a number:'))
print(prime_number_checker(a))
a=input("Would you like to check another number? Type y to check another number and no to end the program")
while a=="y":
      a=int(input('enter a number:'))
      print(prime_number_checker(a))
      a=input("Would you like to check another number? Type y to check another number and n to end the program")      
while a=='n':
      break


