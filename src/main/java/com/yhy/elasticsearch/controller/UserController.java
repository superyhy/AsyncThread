package com.yhy.elasticsearch.controller;


import com.yhy.elasticsearch.bean.User;
import com.yhy.elasticsearch.dto.UserDTO;
import com.yhy.elasticsearch.respository.UserRespository;
import com.yhy.elasticsearch.service.AsyncService;
import org.apache.lucene.queries.SearchAfterSortedDocQuery;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserRespository userRespository;

    @Autowired
    AsyncService asyncService;

    @GetMapping("/create")
    public User create(@RequestParam("userName") String userName) {
        User user1 = new User();
        user1.setUserName(userName);

        return userRespository.save(user1);

    }

    /**
     * 模糊匹配
     *
     * @param userName
     * @return
     */
    @ResponseBody
    @GetMapping("/queryUser")
    public List<User> getUserByName(@RequestParam("userName") String userName) {
        List<User> users = userRespository.findByUserNameLike(userName);

        return users;
    }


    @ResponseBody
    @GetMapping("/deleteUser/{id}")
    public void deleteUserById(@PathVariable String id) {
        userRespository.deleteById(id);
    }

    @ResponseBody
    @PostMapping("/queryUserPage")
    public Page<User> queryUserPage(@RequestBody UserDTO userDTO) {
         Pageable pageable = PageRequest.of(userDTO.getPageNum(),userDTO.getPageSize(),Sort.by("id"));
         User queryUser=new User();
         queryUser.setUserName(userDTO.getName());
         Page<User> users = userRespository.findAll(pageable);

        return users;
    }


    @ResponseBody
    @GetMapping("/test")
    public String thread(){
        return asyncService.myThreads().toString();
    }

    @ResponseBody
    @GetMapping("/test2")
    public void thread2(){
        asyncService.myThread3();
    }


}