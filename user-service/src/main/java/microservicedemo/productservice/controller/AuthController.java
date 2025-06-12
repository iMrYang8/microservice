package microservicedemo.productservice.controller;

import cn.microservice.common.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservicedemo.productservice.mapper.UserMapper;
import microservicedemo.productservice.po.User;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthController {

    private final UserMapper userMapper;

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> res = new HashMap<>();
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

        try {
            int row = userMapper.insert(user);  // 你确保 UserMapper.xml 里有 <insert> 方法
            if (row > 0) {
                res.put("code", 200);
                res.put("msg", "注册成功");
            } else {
                res.put("code", 500);
                res.put("msg", "注册失败");
            }
        } catch (Exception e) {
            res.put("code", 500);
            res.put("msg", "注册异常：" + e.getMessage());
        }

        return res;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User user) {
        Map<String, Object> res = new HashMap<>();
        String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());

        User u = userMapper.selectByUsername(user.getUsername()); // 你需要在 UserMapper.xml 里实现该方法

        if (u == null) {
            res.put("code", 404);
            res.put("msg", "用户不存在");
        } else if (!u.getPassword().equals(password)) {
            res.put("code", 401);
            res.put("msg", "密码错误");
        } else {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", u.getId());
            claims.put("username", u.getUsername());
            String jwt = JwtUtils.generateJwt(claims);

            res.put("code", 200);
            res.put("msg", "登录成功");
            res.put("token", jwt);
        }

        return res;
    }
}
