import jieba
'''
使用结巴对数据进行处理，构建训练语料
'''
import os
user_dir = os.path.dirname(os.path.realpath(__file__))

def writeCutKeyword(sourceFile,targetFile,stopwordsFile):
	stopwords = []
	badStopCount = 0
	with open(stopwordsFile,'r',encoding='utf-8') as r:
		lines = r.readlines()
		for line in lines:
			if line.strip(' ').split('\n') == "":
				badStopCount += 1
				continue
			stopwords.append(line.strip('\n'))
	#print(len(stopwords))
	# print(stopwords)
	#print("badStopCount:",badStopCount)
	i= 0
	anslist = []
	with open(sourceFile,'r',encoding='utf-8') as r:
		lines = r.readlines()
		with open(targetFile,'w',encoding='utf-8') as w:
			for line in lines:
				# i += 1
				# assert i < 10
				str_load = jieba.cut(line)
				cutList = [key.strip(' ').strip('\n') for key in str_load
						   if key.strip(' ').strip('\n') not in stopwords and len(key.strip(' ').strip('\n')) > 2]
				#print(cutList)
				i += 1
				print("deal: ",i)
			# 	anslist.append(cutList)
			# i = 0
			# for templist in anslist:
			# 	i += 1
				# print("write: ",i)
				for key in cutList:
					w.write(key+',')
				w.write('\n')

def getDealData():
	# jieba.load_userdict("/home/zhzy/Downloads/xcy/tech_analysis/py/model/keyDict.dat")
	# sourceFile = "/home/zh zy/Downloads/xcy/tech_analysis/py/model/OriginalData.dat"
	# targetFile = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/DealedData.dat"
	# stopwordsFile = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/stopwords.txt"
	jieba.load_userdict(user_dir+os.sep+"keyDict.dat")
	sourceFile = user_dir+os.sep+"OriginalData.dat"
	targetFile = user_dir+os.sep+"DealedData.dat"
	stopwordsFile = user_dir+os.sep+"stopwords.txt"
	# print(sourceFile)
	# print(targetFile)
	# print(stopwordsFile)
	writeCutKeyword(sourceFile, targetFile, stopwordsFile)
	print("successfully get DealedData")
if __name__ =="__main__":
	print("start cutting paper abstract data")
	getDealData()

