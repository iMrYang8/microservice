package microservicedemo.productservice.controller;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import microservicedemo.productservice.mapper.ProductMapper;
import microservicedemo.productservice.mapper.StockMapper;
import microservicedemo.productservice.po.Product;
import microservicedemo.productservice.po.Stock;
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
        // 1. 检查库存是否存在
        Stock stock = stockMapper.findStockById(product.getStock());
        if (stock == null) {
            // 抛出 400 错误或自定义异常
            throw new IllegalArgumentException("对应库存不存在，无法添加商品，ID=" + product.getId());
        }

        // 2. 新增商品
        int rows = productMapper.insertProduct(product);
        log.info("POST /product → 新增 {} 条记录，商品ID={}", rows, product.getId());

        // 3. 增加库存数量
        stock.setQuantity(stock.getQuantity() + 1);
        stockMapper.updateStock(stock);
        log.info("同步增加库存数量 → 库存ID={}, 数量={}", stock.getStock(), stock.getQuantity());

        return rows;
    }

    /** 更新商品 */
    @PutMapping("/{productId}")
    public int updateProduct(@PathVariable Long productId, @RequestBody Product product) {
        // 1. 查询旧商品，获取旧 stockId
        Product oldProduct = productMapper.findByProductId(productId);
        if (oldProduct == null) {
            throw new IllegalArgumentException("待更新商品不存在，ID=" + productId);
        }

        Long oldStockId = oldProduct.getStock();
        Long newStockId = product.getStock();

        // 2. 如果 stock 有变化，处理库存调整
        if (newStockId != null && !newStockId.equals(oldStockId)) {
            // 2.1 检查新库存是否存在
            Stock newStock = stockMapper.findStockById(newStockId);
            if (newStock == null) {
                throw new IllegalArgumentException("目标库存不存在，ID=" + newStockId);
            }

            // 2.2 旧库存 -1
            if (oldStockId != null) {
                Stock oldStock = stockMapper.findStockById(oldStockId);
                if (oldStock != null && oldStock.getQuantity() > 0) {
                    oldStock.setQuantity(oldStock.getQuantity() - 1);
                    stockMapper.updateStock(oldStock);
                    log.info("旧库存 {} 数量减一，新数量={}", oldStockId, oldStock.getQuantity());
                }
            }

            // 2.3 新库存 +1
            newStock.setQuantity(newStock.getQuantity() + 1);
            stockMapper.updateStock(newStock);
            log.info("新库存 {} 数量加一，新数量={}", newStockId, newStock.getQuantity());
        }

        // 3. 执行商品更新
        product.setId(productId);
        int rows = productMapper.updateProduct(product);
        log.info("PUT /product/{} → 更新 {} 条记录", productId, rows);

        return rows;
    }

    /** 删除商品 */
    @DeleteMapping("/{productId}")
    public int deleteProduct(@PathVariable Long productId) {
        // 1. 查询商品，拿到对应 stockId
        Product product = productMapper.findByProductId(productId);
        if (product == null) {
            throw new IllegalArgumentException("商品不存在，ID=" + productId);
        }
        Long stockId = product.getStock();

        // 2. 执行删除
        int rows = productMapper.deleteProductById(productId);
        log.info("DELETE /product/{} → 删除 {} 条记录", productId, rows);

        // 3. 扣减库存
        if (rows > 0 && stockId != null) {
            Stock stock = stockMapper.findStockById(stockId);
            if (stock != null && stock.getQuantity() > 0) {
                stock.setQuantity(stock.getQuantity() - 1);
                stockMapper.updateStock(stock);
                log.info("同步减少库存数量 → 库存ID={}, 数量={}", stock.getStock(), stock.getQuantity());
            } else {
                log.warn("对应库存不存在或数量已为 0");
            }
        }

        return rows;
    }


    // -------------------- 库存管理 --------------------

    /** 查询所有库存记录 */
    @GetMapping("/stock")
    public List<Stock> queryAllStock() {
        List<Stock> list = stockMapper.queryAllStock();
        log.info("GET /stock → {} 条库存记录", list.size());
        return list;
    }

    /** 根据 stockId 查询库存 */
    @GetMapping("/stock/{stockId}")
    public Stock findStockById(@PathVariable Long stockId) {
        Stock stock = stockMapper.findStockById(stockId);
        log.info("GET /stock/{} → {}", stockId, stock);
        return stock;
    }

    /** 新增库存记录 */
    @PostMapping("/stock")
    public int addStock(@RequestBody Stock stock) {
        int rows = stockMapper.insertStock(stock);
        log.info("POST /stock → 新增 {} 条库存记录", rows);
        return rows;
    }

    /** 更新库存记录 */
    @PutMapping("/stock/{stockId}")
    public int updateStock(@PathVariable Long stockId, @RequestBody Stock stock) {
        stock.setStock(stockId);
        int rows = stockMapper.updateStock(stock);
        log.info("PUT /stock/{} → 更新 {} 条库存记录", stockId, rows);
        return rows;
    }

    /** 删除库存记录 */
    @DeleteMapping("/stock/{stockId}")
    public int deleteStock(@PathVariable Long stockId) {
        int rows = stockMapper.deleteStockById(stockId);
        log.info("DELETE /stock/{} → 删除 {} 条库存记录", stockId, rows);
        return rows;
    }

}
