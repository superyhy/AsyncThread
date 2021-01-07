package com.yhy.elasticsearch.dto;


import lombok.Data;

@Data
public class UserDTO {

    private String name;

    private Integer pageSize;

    private Integer pageNum;

}
