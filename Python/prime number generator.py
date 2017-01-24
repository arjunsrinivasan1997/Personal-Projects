#prime number generator#
print('This program generates all prime numbers up to a certain number')
b=int(input("What number do you want primes generated up to?"))
prime_number_list=[]
#True is Prime Numbers, False is composite numbers#
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
for a in range(2,b):
      if prime_number_checker(a)==True:
            prime_number_list.append(a)
print('primes up to %s'%(b))
for a in prime_number_list:
      print (a) 



        
        

