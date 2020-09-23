/***
 * 以下是仿照python代码写的可用于安卓手机的脚本软件，基于auto.js平台打包，相关github项目地址https://github.com/hyb1996/Auto.js
 * 注意：以下仅判断了请求成功与否，get成功默认登录成功，所以务必填写正确的账号密码才能运行，另外开热点也会影响软件运行，整体勉强可用的状态，仍有很大优化空间。
 * 打包好的软件下载：https://wwe.lanzous.com/iyH6Jglnt3c
 */
"ui";
"auto";
//console.show();

var config=storages.create("config")
showLoginUI();

//显示登录界面
function showLoginUI(){
    ui.layout(
        
        <vertical >
             <text margin="5 100" gravity="center" textStyle="bold" size="20">东大校园网一键登录</text>
        
           <button id="使用说明" w="123" text="使用说明"/>
    
          <linear margin="15 10">
             <text w="56"  color="#111111" size="16">账号:</text>
             <input id="账号" w="110" h="40"/>
             <text w="56"  color="#111111" size="16" >密码:</text>
             <input id="密码" w="110" h="40"/>
         </linear>

       <ScrollView margin="10 10">

            <vertical >

             <button id="保存配置"    text="记住密码"/>

             <button id="登录"    text="登录"/>
 
           </vertical >
       </ScrollView>

       </vertical >
    );
 
    configread();
    ui.使用说明.click(() => showshuomingUI());

    ui.登录.click(() => start());       //启动登录请求

    ui.保存配置.click(function(){  configsave(),toast("保存成功"),sleep(300); });
}


function start(){

    var thread=threads.start(function(){                   
        //在新线程执行的代码
     
        var username = ui.账号.getText();
        var password = ui.密码.getText();
    if(username!=null && password!=null){

    //console.show();
    importClass('java.net.Inet4Address');
    importClass('java.net.InetAddress');
    importClass('java.net.NetworkInterface');
    importClass('java.util.Enumeration');
    importClass('java.net.Inet6Address');
    //获取内网IP地址
    var hostIp = null;
    try {
        var nis = NetworkInterface.getNetworkInterfaces();
        var ia = null;
        while (nis.hasMoreElements()) {
            var ni = nis.nextElement();
            var ias = ni.getInetAddresses();
            while (ias.hasMoreElements()) {
                ia = ias.nextElement();
                if (ia instanceof Inet6Address) {
                    continue;
                }
                var ip = ia.getHostAddress();
                if (!"127.0.0.1".equals(ip)) {
                    hostIp = ia.getHostAddress();
                    break;
                }
            }
        }
    } catch (e) {
        log(e);
    }
  //  log(hostIp);
    
    var mac_address=device.getMacAddress()
    mac_address =mac_address.replace(/:/g,"")
  //  log(mac_address);
    
    var url = 'http://10.9.10.100:801/eportal/?c=Portal&a=login&callback=dr1003&login_method=1&user_account=%2C0%2C' 
              + username + '&user_password=' + password + '&wlan_user_ip=' + hostIp + '&wlan_user_ipv6=&wlan_user_mac=' 
              + mac_address + '&wlan_ac_ip=&wlan_ac_name=&jsVersion=3.3.2&v=2481'
    
    var r = http.get(url, {
        headers : {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36',
            'Host': '10.9.10.100:801',
            'Connection': 'keep-alive',
            'DNT': '1',
            'Referer': 'http://10.9.10.100/a79.htm'
        }
    });
   
    log("code = " + r.statusCode);
    log("html = " + r.body.string());
  
    if (r.statusCode == 200) {
        log("请求成功")
    
        toast("登陆成功")

        exit();
    } 

    else{

        log("登陆失败")
        toast("登陆失败")
    }
  }
    
else{
       toast("请输入上网账号和密码");
     }
});
}
 

//生成配置文件  
function configsave(){
   var x=ui.账号.getText()                  
   config.put("账号",x.toString())  

   x=ui.密码.getText()                  
   config.put("密码",x.toString())

}

//读取配置文件  
function configread(){
    
   if(config.get("密码")!=null){         
    
      var m=(config.get("密码"))
      ui.密码.setText(m.toString());
      
     }
     
   if(config.get("账号")!=null){
      var x
      x=(config.get("账号"))
      ui.账号.setText(x.toString());
     }

}

//使用说明界面
function showshuomingUI(){
    ui.layout(
     <vertical >
     <text  gravity="center_horizontal" layout_gravity="bottom" color="#111111"  size="16"> 第一次使用需输入账号密码后点击记住密码，必须先连接校园网输入正确的账号密码再点击登录，成功后会自动退出，不保证一定成功，软件不需要各种隐私权限，若弹出直接拒绝即可。</text>
  
     <ScrollView >
      <vertical>
      <button id="cancel"  layout_gravity="bottom" text="返回"/>
      </vertical>
     </ScrollView>     
    
     </vertical>
    
    );
    ui.cancel.click(() => showLoginUI());
}

