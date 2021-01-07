package com.yhy.elasticsearch.dto;

import lombok.Data;

import java.util.List;


@Data
public class DirectoryTreeDTO {

    /**
     * 目录id
     */

    private Long id;

    /**
     * 目录名
     */

    private String directoryName;

    /**
     * 父目录Id
     */

    private Long parentId;

    /**
     * 当前目录下的二维码数
     */

    private Integer qrcodeNum;

    private Integer qrcodeNumTotal;

    private Integer orderNum;

    /**
     * 子节点
     */

    private List<DirectoryTreeDTO> children;

}
