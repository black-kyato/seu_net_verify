# seu_net_verify
seu网安自研校园网认证客户端    

## Windows

**windows直接到release页面下载**  
https://github.com/black-kyato/seu_net_verify/releases

## Android

测试于以下设备：

- Nexus 5 原生 Android 6.0.1

- 华为麦芒 5 EMUI 5.0.4 Android 7.0

- 华为 Nova 7 EMUI 10.1.1 Androiod 10

使用中遇到问题请在 issues 提出

[点击下载](https://github.com/black-kyato/seu_net_verify/releases/download/v1.2/Android-v1.2.apk)

- 输入用户名密码后点击保存，再认证

- 无感认证服务

    - 启用：开机后自启动无感认证服务，部分系统需手动授予自启动权限
    
    - 禁用：关闭自启动
    
    - 开启：手动开启无感认证服务
    
    - 关闭：手动关闭无感认证服务
    
- 自启动权限配置（仅供参考）

    - 华为 EMUI 5.0.4：设置 -> 应用管理 -> SEU-WLAN -> 权限 -> 设置单项权限 -> 应用自动启动 （打开）
    
    - 华为 EMUI 10.1.1：设置 -> 应用 -> 应用启动管理 （搜索 SEU-WLAN，关闭自动管理，开启手动管理中三项权限）

## MAC OS X  

**mac平台请根据python版本下载code页面的脚本文件**  

没有pip的先安装pip，再导入requests库  
`pip install requests`    

自带python2.7，请下载**mac2.py**, 运行   
`python mac2.py`  

python3.x请下载**mac3.py**, 运行  
`python3 mac3.py`  

## Linux

测试于 Ubuntu20.04 & Python3.8 + Python 2.7,使用中遇到问题请在 issues 提出

- 安装依赖：

```
# python2.x
pip install requests lxml bs4

# python3.x
pip3 install requests bs4
```

- 下载**linux.py**，运行，会在代码同目录下生成 `.seu_wlan_config` 文件，用于记录登录信息

```
# python2.x
python2 linux.py

# python3.x
python3 linux.py
```

- Ubuntu 20.04 无感登录方案

```
# 备份原认证脚本
sudo mv /usr/libexec/gnome-shell-portal-helper /usr/libexec/gnome-shell-portal-helper-bak

# 创建自动认证脚本
echo '#!/bin/bash' > gnome-shell-portal-helper
echo 'if iwconfig | grep -q "seu-wlan"; then' >> gnome-shell-portal-helper
echo '	/usr/bin/python3 /usr/libexec/login.py' >> gnome-shell-portal-helper
echo 'else' >> gnome-shell-portal-helper
echo '	/usr/libexec/gnome-shell-portal-helper-bak' >> gnome-shell-portal-helper
echo 'fi' >> gnome-shell-portal-helper
chmod +x gnome-shell-portal-helper
sudo mv gnome-shell-portal-helper /usr/libexec/gnome-shell-portal-helper

# 创建配置文件
echo '{"username": "请输入用户名", "password": "请输入密码"}' > ~/.seu_wlan_config
vi ~/.seu_wlan_config

# 部署相关文件
sudo mv linux.py /usr/libexec/login.py
```
