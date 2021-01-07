package com.yhy.elasticsearch.bean;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
/**
 * indexName:index对应索引名称，就像mysql的中的数据库名
 * indexStoreType: type类型，就像mysql中的一张表
 * document：文档，相当于MySQL中的一行数据
 * Field：列，相当于mysql中的一列
 *
 * @Field默认是可以不加的，默认所有属性都会添加到ES中。
 * 加上@Field之后，@document默认把所有字段加上索引失效，只有家@Field 才会被索引(同时也看设置索引的属性是否为no)
 *
 */
@Document(indexName = "es_test")
public class User {

    @Id
    private String id;

    /**
     * 玩家姓名
     */
    private String userName;


}
