package cn.microservicedemo.stockClient.Client;

import cn.microservicedemo.stockClient.model.Stock;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "stock-service", fallback = StockServiceFallback.class)
@Service
public interface StockServiceClient {

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
