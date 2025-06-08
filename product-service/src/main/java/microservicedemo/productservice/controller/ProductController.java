package microservicedemo.productservice.controller;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import microservicedemo.productservice.mapper.ProductMapper;
import microservicedemo.productservice.mapper.StockMapper;
import microservicedemo.productservice.po.Product;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品的服务控制层
 */
@AllArgsConstructor
@RestController
@Slf4j
public class ProductController {

    private final ProductMapper productMapper;
    private final StockMapper stockMapper;
    /**
     * 根据商品id查询商品
     */
    @GetMapping("/findByProductId/{productId}")
    public Product findByProductId(@PathVariable Long productId) throws InterruptedException {
      //  Thread.sleep(2000);
        Product product = productMapper.findByProductId(productId);
        log.info("-------------OK   /findByProductId/{productId}--------------------");

        return product;
    }
    /**
     * 查询所有商品
     */
    @GetMapping("/queryAllProduct")
    public List<Product> findByProductId() {
        List<Product> productList = productMapper.queryAllProduct();
        log.info("-------------OK   queryAllProduct--------------------");
        return productList;
    }

    /* -------------------- C：新增 -------------------- */

    /** 新增商品 */
    @PostMapping("/")
    public int addProduct(@RequestBody Product product) {
        int rows = productMapper.insertProduct(product);
        log.info("POST /product → 新增 {} 条记录，商品ID={}", rows, product.getId());
        return rows;              // 返回插入行数，或可改为返回新建 Product
    }

    /* -------------------- U：修改 -------------------- */

    /** 更新商品（全量或局部均可，根据 Product 字段而定） */
    @PutMapping("/{productId}")
    public int updateProduct(@PathVariable Long productId,
                             @RequestBody Product product) {
        product.setId(productId);  // 确保 ID 一致
        int rows = productMapper.updateProduct(product);
        log.info("PUT /product/{} → 更新 {} 条记录", productId, rows);
        return rows;
    }

    /* -------------------- D：删除 -------------------- */

    /** 删除商品 */
    @DeleteMapping("/{productId}")
    public int deleteProduct(@PathVariable Long productId) {
        int rows = productMapper.deleteProductById(productId);
        log.info("DELETE /product/{} → 删除 {} 条记录", productId, rows);
        return rows;
    }


}
