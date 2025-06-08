package cn.microservicedemo.productclient.Client;

import cn.microservicedemo.productclient.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品服务远程调用客户端
 * @FeignClient(name="product-service",fallback = ProductServiceFallback.class):
 * 这个注解标记了该接口是一个Feign客户端，它将远程调用名为product-service的服务。fallback属性指定了一个降级处理类ProductServiceFallback，用于在远程调用失败时提供备用的处理逻辑。
 * *
 * @GetMapping("/findByProductId/{productId}"): 这个注解标记了该方法将处理一个HTTP GET请求，并且指定了请求的路径为/findByProductId/{productId}。其中，
 * {productId}是一个路径参数。
 * *
 * Product findByProductId(@RequestParam(value = "productId") Long productId):
 * 这个方法用于远程调用商品服务，通过传入商品id作为请求参数，调用商品服务的findByProductId方法，并返回一个Product对象。
 * *
 * @GetMapping("queryAllProduct"): 这个注解标记了该方法将处理一个HTTP GET请求，并且指定了请求的路径为queryAllProduct。
 * *
 * List<Product> queryAllProduct(): 这个方法用于远程调用商品服务，调用商品服务的queryAllProduct方法，并返回一个List<Product>对象。
 * *
 * @GetMapping("queryAllt"): 这个注解标记了该方法将处理一个HTTP GET请求，并且指定了请求的路径为queryAllt。
 *
 * * List<Product> queryAl(): 这个方法用于远程调用商品服务，调用商品服务的queryAl方法，并返回一个List<Product>对象。
 */
@FeignClient(name="product-service",fallback = ProductServiceFallback.class)
public interface ProductServiceClient {

    /**
     * 根据商品id获取商品对象
     */
    @GetMapping("/findByProductId/{productId}")
    Product findByProductId(@RequestParam(value = "productId") Long productId);

    /**
     * 获取所有商品集合
     */
    @GetMapping("queryAllProduct")
    List<Product> queryAllProduct();

    @GetMapping("queryAllt")
    List<Product> queryAl();


    /** 新增商品 */
    @PostMapping("/")
    int addProduct(@RequestBody Product product);

    /* -------------------- U：修改 -------------------- */

    /** 更新商品（全量或局部均可，根据 Product 字段而定） */
    @PutMapping("/{productId}")
    public int updateProduct(@PathVariable Long productId,
                             @RequestBody Product product);

    /* -------------------- D：删除 -------------------- */

    /** 删除商品 */
    @DeleteMapping("/{productId}")
    public int deleteProduct(@PathVariable Long productId);

}
