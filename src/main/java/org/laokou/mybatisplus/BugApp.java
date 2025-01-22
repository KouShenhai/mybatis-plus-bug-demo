package org.laokou.mybatisplus;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.mybatisplus.entity.User;
import org.laokou.mybatisplus.mapper.UserMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class BugApp implements CommandLineRunner {

    private final UserMapper userMapper;

    public static void main(String[] args) {
        SpringApplication.run(BugApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(userMapper.selectList(Wrappers.lambdaQuery(User.class).eq(User::getName, "laokou")
                .eq(User::getId, 1).eq(User::getTenantId, 0)));
        System.out.println(userMapper.selectList(Wrappers.emptyWrapper()));
    }
}
