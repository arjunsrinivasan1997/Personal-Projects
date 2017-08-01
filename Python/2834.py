def test():
	list1 = ['a','b','c']
	list2 = ['t','u','v']
	list3 = ['d','e','f']
	list4 = ['g','h','i']
	for i in list1:
		for x in list2:
			for y in list3:
				for z in list4:
					print(i+x+y+z)
					
test()
