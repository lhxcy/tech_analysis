from sklearn import metrics
import numpy as np
from sklearn.linear_model import LinearRegression
import matplotlib.pyplot as plt
from sklearn.cross_validation import train_test_split
import pandas as pd
import os
user_dir = os.path.dirname(os.path.realpath(__file__))

def getData(filename):
	data = pd.read_csv(filename)
	# X_feature_cols = ['2005', '2008', '2010','2012','2014']
	X_feature_cols = ['2005',  '2010','2012','2014']
	Y_feature_cols = ['2016']
	# Xtest_feature_cols = [ '2008', '2010','2012','2014','2016']
	Xtest_feature_cols = [ '2010','2012','2014','2016']
	keywords = ['keyword']
	return  data[X_feature_cols],data[Y_feature_cols],data[Xtest_feature_cols],data[keywords]


def train(filename):
	x,y,x_test,keywords = getData(filename)
	# x_train, x_test, y_train, y_test = train_test_split(x, y, random_state=1)
	linreg = LinearRegression()
	print("start train model")
	model = linreg.fit(x,y)
	print("train model end")
	# filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/linearModel.dat"
	# prefilePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/linearModelPrediction.dat"
	filePath = user_dir+os.sep+"linearModel.dat"
	prefilePath = user_dir+os.sep+"linearModelPrediction.dat"
	with open(filePath,'w') as w:
		w.write(str(linreg.intercept_[0])+'\n')
		for f in linreg.coef_[0]:
			w.write(str(f)+',')
		w.write('\n')
	pred = prediction(linreg,x_test)
	with open(prefilePath,'w') as w:
		for i in range(len(keywords)):
			w.write(str(keywords.values[i][0])+','+str(int(pred[i][0]))+'\n')
	print("prediction over!")

def prediction(linreg,x_test):
	y_pred = linreg.predict(x_test)
	return y_pred

if __name__ == '__main__':
	# filename = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/preDataSet.dat"
	filename = user_dir+os.sep+"preDataSet.dat"
	train(filename)
