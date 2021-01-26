package com.yhy.elasticsearch.bean;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookBean {

    private String author;


    private Double price;

    private String publish;

    private String name;

    private String type;

    private String info;


}
