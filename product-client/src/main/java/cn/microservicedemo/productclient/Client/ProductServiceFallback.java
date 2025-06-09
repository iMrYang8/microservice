package cn.microservicedemo.productclient.Client;

import cn.microservicedemo.productclient.model.Product;
import cn.microservicedemo.productclient.model.Stock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component  // 加上 Component 让 Spring 能识别为 Bean
public class ProductServiceFallback implements ProductServiceClient {

    // -------------------- 商品相关 --------------------

    @Override
    public Product findByProductId(Long productId) {
        log.warn("Fallback → findByProductId({}) 调用失败", productId);
        Product pro = new Product();
        pro.setProductName("Error - fallback");
        return pro;
    }

    @Override
    public List<Product> queryAllProduct() {
        log.warn("Fallback → queryAllProduct 调用失败");
        return Collections.emptyList();
    }

    @Override
    public List<Product> queryAl() {
        log.warn("Fallback → queryAl 调用失败");
        return Collections.emptyList();
    }

    @Override
    public int addProduct(Product product) {
        log.warn("Fallback → addProduct 调用失败，参数：{}", product);
        return 0;
    }

    @Override
    public int updateProduct(Long productId, Product product) {
        log.warn("Fallback → updateProduct({}, ...) 调用失败", productId);
        return 0;
    }

    @Override
    public int deleteProduct(Long productId) {
        log.warn("Fallback → deleteProduct({}) 调用失败", productId);
        return 0;
    }

    // -------------------- 库存相关 --------------------

    @Override
    public List<Stock> queryAllStock() {
        log.warn("Fallback → queryAllStock 调用失败");
        return Collections.emptyList();
    }

    @Override
    public Stock findStockById(Long stockId) {
        log.warn("Fallback → findStockById({}) 调用失败", stockId);
        Stock fallbackStock = new Stock();
        fallbackStock.setStock(stockId);
        fallbackStock.setStockName("Error - fallback");
        fallbackStock.setQuantity(0);
        return fallbackStock;
    }

    @Override
    public int addStock(Stock stock) {
        log.warn("Fallback → addStock 调用失败，参数：{}", stock);
        return 0;
    }

    @Override
    public int updateStock(Long stockId, Stock stock) {
        log.warn("Fallback → updateStock({}, ...) 调用失败", stockId);
        return 0;
    }

    @Override
    public int deleteStock(Long stockId) {
        log.warn("Fallback → deleteStock({}) 调用失败", stockId);
        return 0;
    }
}
