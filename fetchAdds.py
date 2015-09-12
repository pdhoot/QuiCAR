try:
	import simplejson as json
except:
	import json

import requests

headers = {'X-Quikr-App-Id': '518' , 'X-Quikr-Token-Id' : '2918550' , 'X-Quikr-Signature':'e6095c0876f288b86c3cbaee403c0a8ed67416da' \
			 , 'Content-Type': 'application/json' , 'Host': 'api.quikr.com' , 'Cache-Control':'no-cache'}
j = 0
condition = True
ads = []
while condition:
	url = 'http://api.quikr.com/public/adsByLocation?lat=28.573651&lon=77.257297&radius=3000&from='+`j`+'&size=500'

	r = requests.post(url , headers=headers)
	result = json.loads(r.text)

	for i in result['AdsByLocationResponse']['AdsByLocationData']['docs']:
		if i['id'] not in ads:
			ads.append(i['id'])
			print i['id']
		elif i['id']==ads[-1]:
			continue
		else:
			print i['id']
			condition = False
			break

	j+=100