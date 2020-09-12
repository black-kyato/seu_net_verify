import re
import requests
import socket
import os


# 通过udp数据包获取本机的外网ip
def get_host_ip():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.connect(('8.8.8.8', 80))
    ip = s.getsockname()[0]
    s.close()
    return ip


# 解析cmd获取指定ip对应的mac
def get_mac_address(ip):
    mac = None
    a = {}
    for line in os.popen("ipconfig /all"):
        if line.lstrip().startswith("物理地址"):
            mac = line.split(":")[1].replace("-", "").lstrip()

        if line.lstrip().startswith("IPv4"):
            ip = line.split(":")[1]
            s = re.compile(r'(\d+).(\d+).(\d+).(\d+)')
            t = s.search(ip)
            if t:
                ip = t.group()
            a[ip] = mac

    if ip in a:
        return a[ip]
    else:
        return mac


my_ip = get_host_ip()
my_mac = get_mac_address(my_ip)

# 请求报头
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36',
    'Host': '10.9.10.100:801',
    'Connection': 'keep-alive',
    'DNT': '1',
    'Referer': 'http://10.9.10.100/a79.htm'
}

# 获取用户名密码
if os.path.exists('登录配置勿删') and os.path.getsize('登录配置勿删'):
    print("登录中......")
    f = open("登录配置勿删", 'r')
    user = f.readline()
    password = f.readline()
else:
    f = open("登录配置勿删", 'w')
    user = input("首次登录请输入一卡通号：\n")
    password = input("请输入网络认证密码:\n")
    f.write(user)
    f.write('\n')
    f.write(password)
f.close()

# 生成登录链接
url = 'http://10.9.10.100:801/eportal/?c=Portal&a=login&callback=dr1003&login_method=1&user_account=%2C0%2C' + \
      user + '&user_password=' + password + '&wlan_user_ip=' + my_ip + '&wlan_user_ipv6=&wlan_user_mac=' + \
      my_mac + '&wlan_ac_ip=&wlan_ac_name=&jsVersion=3.3.2&v=2481'

response = requests.get(url, headers=headers)
if response.status_code == 200 :
    a = response.text[-3:-2]
    if a == '"':
        print("恭喜，登录成功 ^_^")
    elif a == '2':
        print("已经在线QAQ")
    else:
        print("密码错误~")

