package com.yhy.elasticsearch.respository;


import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * High Level Client操作索引
 */
public class HighLevelTest {

    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
               new HttpHost("localhost",9200,"http"),
                new HttpHost("localhost",9201,"http"),
                new HttpHost("localhost",9202,"http")
        ));
//        //删除已存在的索引
//        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("blog");
//        client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);

        //创建一个索引
        CreateIndexRequest blog1 = new  CreateIndexRequest("blog—one");
        //配置settings,分片、副本信息
        blog1.settings(Settings.builder().put("index.number_of_shards",3).put("index.number_of_replicas", 2));
        //配置字段类型，字段类型通过json字符串，Map以及XContentBuilder 三种方式来构建
//        //json字符串方式
//        blog1.mapping("{\"properties\": {\"title\": {\"type\": \"text\"}}}", XContentType.JSON);

        //map的方式
        Map<String,String>  title = new HashMap<>();
        title.put("type","text");
        title.put("analyzer","ik_smart");
        Map<String,Object> properties = new HashMap<>();
        properties.put("title",title);
        Map<String,Object> mappings = new HashMap<>();
        mappings.put("properties",properties);
        blog1.mapping(mappings);

        //XContentBuilder方式，创建字段的Mapping
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
        xContentBuilder.startObject();
        xContentBuilder.startObject("properties");
        xContentBuilder.startObject("content");
        xContentBuilder.field("type","keyword");
        xContentBuilder.field("analyzer","ik_smart");
        xContentBuilder.endObject();
        xContentBuilder.endObject();
        xContentBuilder.endObject();


        //配置别名
        blog1.alias(new Alias("blog_alias"));

        //执行请求，创建索引
        client.indices().create(blog1,RequestOptions.DEFAULT);


        //关闭client
        client.close();


    }
}
