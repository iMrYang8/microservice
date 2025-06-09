package cn.microservicedemo.productclient.Client;

import cn.microservicedemo.productclient.model.Product;
import cn.microservicedemo.productclient.model.Stock;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "product-service", fallback = ProductServiceFallback.class)
@Service
public interface ProductServiceClient {

    // -------------------- 商品管理 --------------------

    /** 根据商品id获取商品对象 */
    @GetMapping("/findByProductId/{productId}")
    Product findByProductId(@PathVariable("productId") Long productId);

    /** 获取所有商品集合 */
    @GetMapping("/queryAllProduct")
    List<Product> queryAllProduct();

    /** 示例接口：可以删除，或改名明确用途 */
    @GetMapping("/queryAllt")
    List<Product> queryAl();

    /** 新增商品 */
    @PostMapping("/")
    int addProduct(@RequestBody Product product);

    /** 更新商品 */
    @PutMapping("/{productId}")
    int updateProduct(@PathVariable("productId") Long productId,
                      @RequestBody Product product);

    /** 删除商品 */
    @DeleteMapping("/{productId}")
    int deleteProduct(@PathVariable("productId") Long productId);

    // -------------------- 库存管理 --------------------

    /** 查询所有库存记录 */
    @GetMapping("/stock")
    List<Stock> queryAllStock();

    /** 查询指定库存记录 */
    @GetMapping("/stock/{stockId}")
    Stock findStockById(@PathVariable("stockId") Long stockId);

    /** 新增库存记录 */
    @PostMapping("/stock")
    int addStock(@RequestBody Stock stock);

    /** 更新库存记录 */
    @PutMapping("/stock/{stockId}")
    int updateStock(@PathVariable("stockId") Long stockId,
                    @RequestBody Stock stock);

    /** 删除库存记录 */
    @DeleteMapping("/stock/{stockId}")
    int deleteStock(@PathVariable("stockId") Long stockId);
}
