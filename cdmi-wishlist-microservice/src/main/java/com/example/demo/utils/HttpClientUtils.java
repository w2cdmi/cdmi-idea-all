package com.example.demo.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/************************************************************
 * @version 3.0.1
 * @Description: <pre>对HTTP访问进行简单的封装</pre>
 ************************************************************/
public class HttpClientUtils {
    public static String httpPostWithNameValuePair(String url, Map<String, String> map) {
        try {
            PostMethod post = new PostMethod(url);

            if(map != null && !map.isEmpty()) {
                NameValuePair[] pairs = new NameValuePair[map.size()];

                int i = 0;
                for(String key : map.keySet()) {
                    pairs[i++] = new NameValuePair(key, map.get(key));
                }

                post.setRequestBody(pairs);
            }

            HttpClient client = new HttpClient();
            client.executeMethod(post);

            return post.getResponseBodyAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String httpPostWithJsonBody(String url, Object o) {
        try {
            PostMethod post = new PostMethod(url);

            if(o != null) {
                String body = JSON.toJSONString(o);

                post.setRequestEntity(new StringRequestEntity(body, null, "utf-8"));
            }

            HttpClient client = new HttpClient();
            client.executeMethod(post);

            return post.getResponseBodyAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String httpGet(String url) {
        try {
            HttpMethod get = new GetMethod(url);

            org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
            client.executeMethod(get);

            return get.getResponseBodyAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //下载byte[]数据
    public static byte[] downloadAsStream(String urlStr) {
        InputStream in = null;
        ByteArrayOutputStream outStream = null;
        try {
            URL url = new URL(urlStr);
            HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
            urlConnection.setHostnameVerifier(new HostnameVerifier(){
                                public boolean verify(String string,SSLSession ssls) {
                    return true;
                }
            });
            in = urlConnection.getInputStream();

            outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while((len = in.read(buffer)) != -1){
                outStream.write(buffer, 0, len);
            }

            return outStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(outStream);
        }

        return null;
    }
}
