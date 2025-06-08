package cn.microservicedemo.productclient.Client;

import cn.microservicedemo.productclient.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
public class ProductServiceFallback implements ProductServiceClient {
    @Override
    public Product findByProductId(Long productId) {
        log.info("findByProductId callback");
        Product pro=new Product();
        pro.setProductName("Error");
        return pro;
    }

    @Override
    public List<Product> queryAllProduct() {
        log.info("queryAllProduct callback");
        return null;
    }

    @Override
    public List<Product> queryAl() {
        return null;
    }
}
