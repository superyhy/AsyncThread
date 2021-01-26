package com.yhy.elasticsearch.config;


import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
public class ESIndexConfig {

    @Autowired
    RestHighLevelClient  esClient;

    /**
     * ik 分词模式
     */
    public static final String IK_SMART = "ik_smart";
    public static final String IK_MAX_WORD="ik_max_word";


    /**
     * books索引的常量
     */
    public static final String  BOOKS_TABLE_INDEX = "book_table2";
    public static final String  BOOKS_TABLE_TYPE = "_doc";


    @Bean
    public void createIndex(){
        createBookTableIndex();
    }
    /**
     * 创建book_table索引
     */
    private void createBookTableIndex() {
        //判断索引是否存在
        try {
            GetIndexRequest blog = new GetIndexRequest(BOOKS_TABLE_INDEX);
            boolean exist = esClient.indices().exists(blog, RequestOptions.DEFAULT);
            if(exist){
                return;
            }
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            builder.startObject("properties");
            builder.startObject("author").field("type","keyword").endObject();
            builder.startObject("price").field("type","double").endObject();
            builder.startObject("publish").field("type","text").field("analyzer",IK_SMART).endObject();
            builder.startObject("name").field("type","text").field("analyzer",IK_SMART).endObject();
            builder.startObject("type").field("type","keyword").endObject();
            builder.startObject("info").field("type","text").field("analyzer",IK_MAX_WORD).endObject();
            builder.endObject();
            builder.endObject();

            //创建一个索引
            CreateIndexRequest booksTableIndex = new  CreateIndexRequest(BOOKS_TABLE_INDEX);
            //配置settings,分片、副本信息
            booksTableIndex.settings(Settings.builder().put("index.number_of_shards",3).put("index.number_of_replicas", 2));
            //设置mapping字段信息
            booksTableIndex.mapping(builder);
            //执行请求，创建索引
            esClient.indices().create(booksTableIndex,RequestOptions.DEFAULT);

        } catch (Exception e) {
            log.error("创建索引失败",e);
            throw new RuntimeException("创建索引失败");
        }
    }







}
