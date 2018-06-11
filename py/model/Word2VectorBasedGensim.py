# -*- coding:utf-8 -*-
#import BuildDict
import CutItemWithKey
'''
加载预料、训练模型，写下模型，将向量和词写入Word2Vec.dat文件
'''
import sys
# 引入 word2vec
from gensim.models import word2vec
import gensim
# 引入日志配置
import logging
import numpy as np

import os
user_dir = os.path.dirname(os.path.realpath(__file__))

logging.basicConfig(format='%(asctime)s : %(levelname)s : %(message)s', level=logging.INFO)
#
# # 引入数据集
# raw_sentences = ["the quick brown fox jumps over the lazy dogs","yoyoyo you go home now to sleep"]
#
# # 切分词汇
# sentences= [s.split(' ') for s in raw_sentences]
# print(sentences)
#
# # 构建模型
# model = word2vec.Word2Vec(sentences, min_count=1)
#
# # 进行相关性比较
# print(model.similarity('dogs','you'))
#
# # model = gensim.models.Word2Vec(iter=1)  # an empty model, no training yet
# # model.build_vocab(some_sentences)  # can be a non-repeatable, 1-pass generator
# # model.train(other_sentences)  # can be a non-repeatable, 1-pass generator



def getSentences(filename):
	sentences = []
	i = 0
	with open(filename,'r',encoding='utf-8') as r:
		lines = r.readlines()
		for line in lines:
			if line.strip(' ').strip('\n') == "":
				continue
			i += 1
			# assert i < 10
			print("sentence: ",i)
			temp = [key for key in line.split(',') if key.strip(' ').split('\n') != "" and '0' not in key and '.' not in key]
			# print(temp)
			sentences.append(temp)
		print(len(sentences))
	return sentences

def buildModelAndTrainAndSave():
	# filename = '/home/zhzy/Downloads/xcy/tech_analysis/py/model/DealedData.dat'
	filename = user_dir+os.sep+"DealedData.dat"
	sentences = getSentences(filename)
	print("training model.....")
	model = word2vec.Word2Vec(sentences=sentences,size=150,iter=50,min_count=1,max_vocab_size=4000000,compute_loss=True)

	# model.save('/home/zhzy/Downloads/xcy/tech_analysis/py/model/word2vec.model')
	model.save(user_dir+os.sep+"word2vec_new.model")
	print("training end")

def loadModel():
	# model = word2vec.Word2Vec.load('/home/zhzy/Downloads/xcy/tech_analysis/py/model/word2vec.model')
	model = word2vec.Word2Vec.load(user_dir+os.sep+"word2vec_new.model")
	return model


def predict(model,word):
	res = model.most_similar([word],topn=5)
	ans = [key[0] for key in res]
	return ans


# def getVec(model,word):
# 	print(model[word])  # raw NumPy vector of a word
# # array([-0.00449447, -0.00310097,  0.02421786, ...], dtype=float32)

# def getUniqueWord(filename):
# 	keyWords = set()
# 	i = 0
# 	with open(filename, 'r', encoding='utf-8') as r:
# 		lines = r.readlines()
# 		for line in lines:
# 			if line.strip(' ').strip('\n') == "":
# 				continue
# 			temp = [key for key in line.split(',') if key.strip(' ').split('\n') != ""]
# 			for key in temp:
# 				if '.' in key or 'cm' in key or 'nm' in key:
# 					continue
# 				keyWords.add(key)
# 	print(len(keyWords))
# 	return keyWords
def writeWordAndVector(model, targetFilename):
	count = 0
	with open(targetFilename,'w',encoding='utf-8') as w:
		for key in model.wv.vocab:
			count += 1
			# print(count)
			w.write(key+",")
			for f in model[key].astype(dtype=np.float64):
				w.write(str(f)+',')
			w.write('\n')
	print("word count: ",count)
	print('Done')


def forJava():
	print("Starting train data........")
	buildModelAndTrainAndSave()
	model = loadModel()
	# targetFilename = '/home/zhzy/Downloads/xcy/tech_analysis/py/model/Word2Vec.dat'
	#targetFilename = user_dir+os.sep+"Word2Vec.dat"
	targetFilename = user_dir+os.sep+"Word2Vec_more.dat"
	writeWordAndVector(model, targetFilename)
	print("End....")
	print("the file model needed done")

def getData():
	#BuildDict.getDict()
	CutItemWithKey.getDealData()
	# getDealData()

if __name__ =='__main__':
	#getData()
	#forJava()
	model = loadModel()
	# print(len(model.wv.vocab))
	# print(predict(model,"并行计算"))
	print(type(model["并行计算"]))
	print(type(model["并行计算"][0]))



