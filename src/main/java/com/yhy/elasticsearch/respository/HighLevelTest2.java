package com.yhy.elasticsearch.respository;

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * High Level操作2
 */
public class HighLevelTest2 {

    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("localhost", 9200, "http"),
                new HttpHost("localhost", 9201, "http"),
                new HttpHost("localhost", 9202, "http")
        ));

        //判断索引是否存在
        GetIndexRequest blog = new GetIndexRequest("blog");
        boolean exist = client.indices().exists(blog, RequestOptions.DEFAULT);
        System.out.println("exist="+exist);


//        //修改索引信息
//        UpdateSettingsRequest  updateRequest = new UpdateSettingsRequest("blog");
//        updateRequest.settings(Settings.builder().put("index.blocks.write",true).build());
//        client.indices().putSettings(updateRequest,RequestOptions.DEFAULT);


        //添加文档
        IndexRequest indexRequest = new IndexRequest("blog");
        //map的方式
        Map<String,String>  map   = new HashMap<>();
        map.put("name","基努.里维斯");
        map.put("movie","黑客帝国");

        indexRequest.source(map);

        IndexResponse indexResponse = client.index(indexRequest,RequestOptions.DEFAULT);

        //关闭client
        client.close();





    }
}
