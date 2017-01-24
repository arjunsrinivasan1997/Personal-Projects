def prime_number_checker(number):

      if number<1:
            return False
      elif number==2:
            return True
      else:
            for a in range(2,number):
                  if (number%a) == 0:
                        return False
                        break
            else:
                  return True
def prime_factorization(a):
      """prints the prime factorization of a number"""
      b=a
      list_of_prime_factors=[]
      if prime_number_checker(a):
            print("The prime factorization of",b,"is",b,"*",1)
            return
      while prime_number_checker(a)==False:
        for c in range(2,a):
              if a%c==0 and prime_number_checker(c)==True:
                a=int(a/c)
                list_of_prime_factors.append(str(c))

        if prime_number_checker(a)==True and a!=1:
            list_of_prime_factors.append(str(a))
      list_of_prime_factors.sort()

      print("The prime factorization of",b,"is","*".join(list_of_prime_factors))


a=int(input("What number do you want the prime factorization generated for?"))
prime_factorization(a)

b=int(input("What number do you want the prime factorization generated for?( Type anything but a number to exit the program"))
while type(b)==int:
      prime_factorization(b)
      b=int(input("What number do you want the prime factorization generated for?( Type anything but a number to exit the program"))
