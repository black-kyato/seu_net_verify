# -*- coding: utf-8 -*-
import json
import re
import socket
import os

import requests
from bs4 import BeautifulSoup


headers = {
    'Accept': '*/*',
    'Accept-Encoding': 'gzip, deflate',
    'Accept-Language': 'zh-CN,zh;q=0.9,en;q=0.8',
    'Connection': 'keep-alive',
    'DNT': '1',
    'Host': '10.9.10.100:801',
    'Referer': 'http://10.9.10.100/a79.htm',
    'User-Agent': 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.83 Safari/537.36'
}


def js2dict(scripts):
    data = {}
    for script in scripts:
        s1 = re.sub('//.*', '', script.string)
        s2 = re.sub('/\*.*\*/', '', s1)
        l = s2.split(';')
        for i in l:
            if '{' in i:
                k, v = i.strip().split('=', 1)
                data[k] = v
            else:
                for j in i.strip().split(','):
                    if j == '':
                        continue
                    try:
                        k, v = j.strip().split('=', 1)
                        if '\'' in v:
                            v = v.replace('\'', '')
                        elif '"' in v:
                            v = v.replace('"', '')
                        elif '%' in v or '-' in v or '*' in v:
                            pass
                        else:
                            v = int(v)
                        data[k] = v
                    except:
                        pass
    return data


def load_config():
    if not os.path.exists('.seu_wlan_config'):
        with open('.seu_wlan_config', 'w') as wfile:
            wfile.write('{"username": "请填写用户名", "password": "请填写密码"}')
        os.popen("notify-send 'SEU-WLAN认证失败' '配置文件缺失，请检查 {}/.seu_wlan_config 中是否正确填写'".format(os.getcwd()))
        return False, None
    with open('.seu_wlan_config', 'r') as rfile:
        _config = json.loads(rfile.read().strip())
    if "username" in _config.keys() and "password" in _config.keys():
        return True, _config
    else:
        return False, None


if __name__ == '__main__':
    session = requests.session()
    session.headers = headers

    res = session.get("http://10.9.10.100/")
    html = res.content.decode('gb2312')
    soup = BeautifulSoup(html, 'lxml')
    scripts = soup.findAll('script')
    data = js2dict(scripts)
    success, config = load_config()

    if success:
        keys = ["v4ip", "v46ip", "ss5"]
        for key in keys:
            if key in data.keys():
                ip = data.get(key)
                break
        else:
            s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
            s.connect(('8.8.8.8', 80))
            ip = s.getsockname()[0]
            s.close()

        params = {
            'c': 'Portal',
            'a': 'login',
            'callback': 'dr1003',
            'login_method': 1,
            'user_account': ',0,' + config["username"],
            'user_password': config["password"],
            'wlan_user_ip': ip,
            'wlan_user_ipv6': '',
            'wlan_user_mac': '000000000000',
            'wlan_ac_ip': '',
            'wlan_ac_name': '',
            'jsVersion': '3.3.2',
            'v': 4822
        }

        res = session.get('http://10.9.10.100:{}/eportal/'.format(data["authloginport"]), params=params)
        ret = json.loads(res.content.decode('utf8')[7:-1])
        if ret['result'] == '1':
            os.popen("notify-send 'SEU-WLAN认证成功'")
        else:
            os.popen("notify-send 'SEU-WLAN认证失败' '错误码: {}，错误信息: {}'".format(ret['ret_code'], ret['msg']))

