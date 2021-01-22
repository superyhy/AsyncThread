package com.yhy.elasticsearch.respository;

import org.apache.http.HttpHost;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Java Low Level Client使用
 */
public class LowLevelTest {

    public static void main(String[] args) throws IOException {

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
        //同步
        Response response = restClient.performRequest(request);
        //5.解析response，获取响应结果
        BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String str = null;
        while((str = br.readLine())!= null){
            System.out.println(str);
        }

        br.close();

        restClient.close();


    }
}
