package microservicedemo.productservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtils {
    private static String signKey = "hitTest";
    private static Long expire = 3600000L;

    /**
     * 生成JWT令牌
     * @param claims JWT第二部分负载 payload 中存储的内容
     * @return
     */
    public static String generateJwt(Map<String, Object> claims){
        String jwt = Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, signKey)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
        return jwt;
    }

    /**
     * 解析JWT令牌
     * @param jwt JWT令牌
     * @return JWT第二部分负载 payload 中存储的内容
     */
    public static Claims parseJWT(String jwt){
        Claims claims = Jwts.parser()
                .setSigningKey(signKey)
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }

    /**
     * 获取id信息
     * @param jwt
     * @return
     */
    public static Integer GetId(String jwt){
        String id = Jwts.parser().setSigningKey(signKey).parseClaimsJws(jwt).getBody().get("id").toString();
        return Integer.parseInt(id);
    }

    //解释:虽然jwt可以直接解析出来,但是签名的存在目的是为了jwt的完整性和安全性,有了签名就能够保证了jwt的来源安全
}
