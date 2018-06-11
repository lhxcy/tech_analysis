import sys
import numpy as np
def hello():
	print ("hello hanshhu")

def canshu(word):
	print("ccccccccccccc",word)

def getKey(filename):
	with open(filename,'r',encoding= "utf-8") as r:
		lines = r.readlines()
		i = 0
		for line in lines:
			i += 1
			assert i < 10
			print(line)
# print("hello code")
if __name__ == '__main__':
	# print(args)
	print(np.add(np.ones([1]),np.ones([1])))
	d = np.add(np.ones([1]),np.ones([1]))
	print("hhhhhhhhhhhhhh",d)
	hello()
	getKey("paper.dat")
	# getKey(sys.argv[2])
	# canshu(sys.argv[1])