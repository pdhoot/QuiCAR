import hmac
from hashlib import sha1
from ConfigParser import ConfigParser , SafeConfigParser
import time
import json
import requests

config = ConfigParser()
parser = SafeConfigParser()
config.read('../config/config.cfg')

appid = config.get('app' , 'appid')
email = config.get('app' , 'email')
key = config.get('app' , 'appsecret')
apiname = config.get('app' , 'endpoint')
yyyymmdd = time.strftime('%Y%m%d')
yyyymmdd = yyyymmdd = yyyymmdd[0:4]+'-'+yyyymmdd[4:6]+'-' + yyyymmdd[6:]
message = email + appid + yyyymmdd
hexdigest = hmac.new(key , message , sha1).hexdigest()
url = 'https://api.quikr.com/app/auth/access_token'
headers = {'Content-Type':'application/json'}
data = json.dumps({ "appId" : appid, "signature" : hexdigest})
r = requests.post(url , data=data , headers=headers)
result = json.loads(r.text)
tokenId = result['tokenId']
token = result['token']
message = appid + apiname + yyyymmdd
signature = hmac.new(str(token) , message, sha1).hexdigest()
parser.add_section('app')
parser.set('app' , 'appid' , appid)
parser.set('app' , 'appsecret' , key)
parser.set('app' , 'tokenid' , str(tokenId))
parser.set('app' , 'token' , token)
parser.set('app' , 'signature' , signature)
parser.set('app' , 'email' , email)
parser.set('app' , 'endpoint' , apiname)
f = open('../config/config.cfg' , 'w')
parser.write(f)