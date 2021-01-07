package com.yhy.elasticsearch.respository;

import com.yhy.elasticsearch.bean.User;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * ElasticsearchRepository<user,String>,<>分别对应实体类和主键类型
 */
public interface UserRespository extends ElasticsearchRepository<User, String> {
    /**
     * 在ElasticsearchRepository中我们可以使用Not Add Like Or Between
     * 等关键词自动创建查询语句。
     */
    /**
     * @param userName
     * @return
     */
    List<User> queryUserByUserName(String userName);

    /**
     * 实现模糊查询，id倒序
     *
     * @param userName
     * @return
     */

    List<User> findByUserNameLike(String userName);





}
