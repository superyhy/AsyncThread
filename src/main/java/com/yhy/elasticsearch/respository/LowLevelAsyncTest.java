package com.yhy.elasticsearch.respository;

import org.apache.http.HttpHost;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.elasticsearch.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * low level异步操作
 */
public class LowLevelAsyncTest {

    public static void main(String[] args) {
        //1. 构建一个RestClient对象
        RestClientBuilder  builder = RestClient.builder(
                new HttpHost("localhost",9200,"http"),
                new HttpHost("localhost",9201,"http"),
                new HttpHost("localhost",9202,"http")

        );

        //2. 如果需要在请求头中设置认证信息，通过 builder 来设置
        RestClient restClient = builder.build();

        //3.构建请求
        Request request = new Request("GET","/books/_search");
        //添加请求参数
        request.addParameter("pretty","true");

        //发起请求，请求方式：同步|异步
        //异步操作
        restClient.performRequestAsync(request, new ResponseListener() {

            //解析response，返回响应结果
            @Override
            public void onSuccess(Response response) {
                 try(BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));){
                     String str=null;
                     while((str = br.readLine()) !=null){
                         System.out.println(str);
                     }
                 } catch (Exception e) {
                     e.printStackTrace();
                 }finally {
                     IOUtils.closeQuietly(restClient);
                 }
            }

            //请求失败的原因
            @Override
            public void onFailure(Exception exception) {

            }
        });
    }
}
