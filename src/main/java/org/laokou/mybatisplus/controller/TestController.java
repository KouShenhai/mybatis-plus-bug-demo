package org.laokou.mybatisplus.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.mybatisplus.entity.User;
import org.laokou.mybatisplus.mapper.UserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final UserMapper userMapper;

    @GetMapping
    public void test() {
        System.out.println(userMapper.selectList(Wrappers.lambdaQuery(User.class).eq(User::getName, "laokou")
                .eq(User::getId, 1)));
    }

}
