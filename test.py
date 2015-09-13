try:
	import simplejson as json
except:
	import json

import requests
from ConfigParser import ConfigParser
config = ConfigParser()

config.read('./config/config.cfg')
appid = config.get('app' , 'appId')
tokenid = config.get('app' , 'tokenId')
signature = config.get('app' , 'signature')

headers = {'X-Quikr-App-Id': appid , 'X-Quikr-Token-Id' : tokenid , 'X-Quikr-Signature':signature \
			 , 'Content-Type': 'application/json' , 'Host': 'api.quikr.com' , 'Cache-Control':'no-cache'}
j = 0
ads = []
count  = 0
while True:
	url = 'http://api.quikr.com/public/adsByCategory?categoryId=71&from='+`j`+'&size=100'

	r = requests.post(url , headers=headers)
	result = json.loads(r.text)

	for i in result['AdsByCategoryResponse']['AdsByCategoryData']['docs']:
		if i not in ads:
			count += 1
			ads.append(i)
	j+=100

	if count%100 == 0:
		f = open('./ads.json' , 'w')
		f.write(json.dumps(ads))
		f.close();
		f = open('./count.txt', 'w')
		f.write(json.dumps(count));
		f.close();
