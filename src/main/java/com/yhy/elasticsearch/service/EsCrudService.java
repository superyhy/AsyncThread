package com.yhy.elasticsearch.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.yhy.elasticsearch.bean.BookBean;
import com.yhy.elasticsearch.config.ESIndexConfig;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EsCrudService {

    @Autowired
    RestHighLevelClient highLevelClient;

    ObjectMapper objectMapper = new ObjectMapper();


    /**
     * 根据索引，类型，ID查询文档
     */
    public void get(String id) {
        GetRequest getRequest = new GetRequest(ESIndexConfig.BOOKS_TABLE_INDEX, id);
        GetResponse getResponse = null;
        try {
            getResponse = highLevelClient.get(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BookBean queryBook = null;
        try {
            queryBook = objectMapper.readValue(getResponse.getSourceAsString(), BookBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(queryBook);
    }

    /**
     * 插入数据，批量操作
     */
    public void add() {
        BulkRequest bulkRequest = new BulkRequest();

        List<IndexRequest> addRequests = new ArrayList<>();

        List<BookBean> books = new ArrayList<>();
        BookBean book1 = BookBean.builder().author("难进").price(12d).info("前").build();
        BookBean book2 = BookBean.builder().author("难进").price(12d).info("前").build();
        books.add(book1);
        books.add(book2);

        books.forEach(bookBean -> {
            IndexRequest indexRequest = new IndexRequest(ESIndexConfig.BOOKS_TABLE_INDEX);
            try {
                indexRequest.source(objectMapper.writeValueAsString(bookBean), XContentType.JSON);
            } catch (Exception e) {
                e.printStackTrace();
            }
            addRequests.add(indexRequest);
        });

        addRequests.forEach(addRequest -> {
            bulkRequest.add(addRequest);
        });

        try {
            highLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 复合查询 bool query
     * 查询书名中有“计算机”，简介中有“大学”，价格在0-20之间的书籍，设置分页（0-20）
     */
    public void query() {
        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();
        searchBuilder.from(0);
        searchBuilder.size(20);

        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.must(QueryBuilders.termQuery("name", "计算机"));
        boolBuilder.must(QueryBuilders.termQuery("info", "大学"));
        boolBuilder.must(QueryBuilders.rangeQuery("price").gte(0).lt(20));

        searchBuilder.query(boolBuilder);
        SearchRequest searchRequest = new SearchRequest(ESIndexConfig.BOOKS_TABLE_INDEX);
        searchRequest.source(searchBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SearchHit[] searchHits = searchResponse.getHits().getHits();

        for (SearchHit hit : searchHits) {
            System.out.println(hit.getSourceAsString());
        }
    }


    /**
     * 指标聚合，计算书价的平均值
     */
    public void queryTarget() {
        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();
        AggregationBuilder avgAggBuilder = AggregationBuilders.avg("avg").field("price");

        searchBuilder.aggregation(avgAggBuilder);

        SearchRequest searchRequest = new SearchRequest(ESIndexConfig.BOOKS_TABLE_INDEX);

        searchRequest.source(searchBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(searchResponse);


        //取到平均值的value
        Avg avg = searchResponse.getAggregations().get("avg");
        System.out.println(avg.getValue());

    }

    /**
     * 聚合查询：指标、分桶、管道
     * 统计每个作者的图书数量和书价平均值(桶聚合)，分桶的字段一定要是keyWord类型
     */
    public void queryTest() {
        Map<String, Long> groupMap = new HashMap<>();
        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();


        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("agg").field("author");
        AggregationBuilder avgAggBuilder = AggregationBuilders.avg("avg").field("price");
        aggregationBuilder.subAggregation(avgAggBuilder);
        searchBuilder.aggregation(aggregationBuilder);

        SearchRequest searchRequest = new SearchRequest(ESIndexConfig.BOOKS_TABLE_INDEX);

        searchRequest.source(searchBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Terms terms = searchResponse.getAggregations().get("agg");
        for (Terms.Bucket entry : terms.getBuckets()) {
            groupMap.put(entry.getKeyAsString(), entry.getDocCount());
        }

        System.out.println(searchResponse);

        System.out.println(groupMap);


    }


}
