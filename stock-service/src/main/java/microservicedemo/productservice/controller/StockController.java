package microservicedemo.productservice.controller;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import microservicedemo.productservice.mapper.StockMapper;
import microservicedemo.productservice.po.Stock;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品的服务控制层
 */
@AllArgsConstructor
@RestController
@Slf4j
public class StockController {

    private final StockMapper stockMapper;

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
