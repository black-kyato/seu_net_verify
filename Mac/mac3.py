import re
import requests
import socket
import os


# get ip
def get_host_ip():
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect(('8.8.8.8', 80))
    except BaseException:
        pass
    ip = s.getsockname()[0]
    s.close()
    return ip


my_ip = get_host_ip()

# header
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36',
    'Host': '10.9.10.100:801',
    'Connection': 'keep-alive',
    'DNT': '1',
    'Referer': 'http://10.9.10.100/a79.htm'
}

# get user info
if os.path.exists('.config') and os.path.getsize('.config'):
    print(u"please wait......")
    f = open(".config", 'r')
    user = f.readline()
    password = f.readline()
else:
    f = open(".config", 'w')
    user = raw_input("input username(like 22020****):\n")
    password = raw_input("input password:\n")
    f.write(user)
    f.write('\n')
    f.write(password)
f.close()


# get url
url = 'http://10.9.10.100:801/eportal/?c=Portal&a=login&callback=dr1003&login_method=1&user_account=%2C0%2C' + \
      user + '&user_password=' + password + '&wlan_user_ip=' + my_ip + '&wlan_user_ipv6=&wlan_user_mac=000000000000&wlan_ac_ip=&wlan_ac_name=&jsVersion=3.3.2&v=2481'

response = requests.get(url, headers=headers)
if response.status_code == 200 :
    a = response.text[-3:-2]
    if a == '"':
        print(u"congratulate!login successfully ^_^")
    elif a == '2':
        print(u"Already online!QAQ")
    else:
        print(u"password error~")
        f = open(".config", 'w')
        f.close()

