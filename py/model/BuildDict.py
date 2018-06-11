'''
构建数据集的短语字典
'''
import os
user_dir = os.path.dirname(os.path.realpath(__file__))

def getSetKey(filename):
	keySet = set()
	count = 0
	with open(filename, 'r', encoding='utf-8') as r:
		lines = r.readlines()
		for line in lines:
			line = line.split('!')
			for key in line[2:]:
				if key.strip(' ').strip('\n') == "":
					count += 1
					continue
				keySet.add(key.strip(' ').strip('\n'))
	print("bad words: ",count)
	print(len(keySet))
	return keySet

def writeDict(sourceFile,targetFile):
	keySet = getSetKey(sourceFile)
	with open(targetFile,'w',encoding='utf-8') as w:
		for key in keySet:
			w.write(key + " " + str(200) + '\n')

def getDict():
	# sourceFile = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/keywordsForDict.dat"
	# targetFile = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/keyDict.dat"
	sourceFile = user_dir+os.sep+"keywordsForDict.dat"
	targetFile = user_dir+os.sep+"keyDict.dat"
	writeDict(sourceFile=sourceFile, targetFile=targetFile)
	print("successfully build keyword dictionary")
if __name__ == "__main__":
	print("start building keyword dictionary")
	getDict()



















