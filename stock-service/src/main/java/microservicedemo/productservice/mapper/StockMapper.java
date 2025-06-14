package microservicedemo.productservice.mapper;

import microservicedemo.productservice.po.Stock;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StockMapper {

    // 查询单个库存记录
    @Select("SELECT stock, stock_name AS stockName, quantity FROM stock WHERE stock = #{id}")
    Stock findStockById(@Param("id") Long id);

    // 查询所有库存记录
    @Select("SELECT stock, stock_name AS stockName, quantity FROM stock")
    List<Stock> queryAllStock();

    // 插入库存记录
    @Insert("INSERT INTO stock (stock, stock_name, quantity) VALUES (#{stock}, #{stockName}, #{quantity})")
    int insertStock(Stock stock);

    // 更新库存记录
    @Update("UPDATE stock SET stock_name = #{stockName}, quantity = #{quantity} WHERE stock = #{stock}")
    int updateStock(Stock stock);

    // 删除库存记录
    @Delete("DELETE FROM stock WHERE stock = #{id}")
    int deleteStockById(@Param("id") Long id);
}
