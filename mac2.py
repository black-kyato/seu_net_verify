from __future__ import absolute_import
import re
import requests
import socket
import os
from io import open


# get ip
def get_host_ip():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.connect((u'8.8.8.8', 80))
    ip = s.getsockname()[0]
    s.close()
    return ip


my_ip = get_host_ip()
my_mac = get_mac_address(my_ip)

# header
headers = {
    u'User-Agent': u'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36',
    u'Host': u'10.9.10.100:801',
    u'Connection': u'keep-alive',
    u'DNT': u'1',
    u'Referer': u'http://10.9.10.100/a79.htm'
}

# get user info
if os.path.exists(u'.config') and os.path.getsize(u'.config'):
    print u"please wait......"
    f = open(u".config", u'r')
    user = f.readline()
    password = f.readline()
else:
    f = open(u".config", u'w')
    user = raw_input(u"input username(like 22020****):\n")
    password = raw_input(u"input password:\n")
    f.write(user)
    f.write(u'\n')
    f.write(password)
f.close()


# get url
url = u'http://10.9.10.100:801/eportal/?c=Portal&a=login&callback=dr1003&login_method=1&user_account=%2C0%2C' + \
      user + u'&user_password=' + password + u'&wlan_user_ip=' + my_ip + u'&wlan_user_ipv6=&wlan_user_mac=000000000000' + \
    + u'&wlan_ac_ip=&wlan_ac_name=&jsVersion=3.3.2&v=2481'

response = requests.get(url, headers=headers)
if response.status_code == 200 :
    a = response.text[-3:-2]
    if a == u'"':
        print u"congratulate!login successfully ^_^"
    elif a == u'2':
        print u"Already online!QAQ"
    else:
        print u"password error~"
        f = open(u".config", u'w')
        f.close()

