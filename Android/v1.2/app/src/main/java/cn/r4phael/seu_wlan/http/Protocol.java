package cn.r4phael.seu_wlan.http;

import retrofit2.Call;
import retrofit2.http.Query;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Protocol {
    @Headers({
            "Accept: */*",
            "Accept-Encoding: gzip, deflate",
            "Accept-Language: zh-CN,zh;q=0.9,en;q=0.8",
            "Connection: keep-alive",
            "DNT: 1",
            "Host: 10.9.10.100:801",
            "Referer: http://10.9.10.100/a79.htm",
            "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.83 Safari/537.36"
    })
    @GET("/")
    Call<String> check_verify_status();

    @Headers({
            "Accept: */*",
            "Accept-Encoding: gzip, deflate",
            "Accept-Language: zh-CN,zh;q=0.9,en;q=0.8",
            "Connection: keep-alive",
            "DNT: 1",
            "Host: 10.9.10.100:801",
            "Referer: http://10.9.10.100/a79.htm",
            "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.83 Safari/537.36",
            "Content-Type: text/html; charset=gb2312",
            "Content-Type: text/html; charset=UTF-8"
    })
    @GET("/eportal/")
    Call<String> do_login(@Query("c") String c, @Query("a") String a, @Query("callback") String callback, @Query("login_method") int login_method,
                               @Query("user_account") String user_account, @Query("user_password") String user_password, @Query("wlan_user_ip") String wlan_user_ip,
                               @Query("wlan_user_ipv6") String wlan_user_ipv6, @Query("wlan_user_mac") String wlan_user_mac, @Query("wlan_ac_ip") String wlan_ac_ip,
                               @Query("wlan_ac_name") String wlan_ac_name, @Query("jsVersion") String jsVersion, @Query("v") int v);

}
