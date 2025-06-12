package microservicedemo.productservice.po;
import lombok.Data;


/**
 * 商品的实体
 *
 * @author me
 */
@Data
public class Stock {
    private Long stock;

    private String stockName;

    private Integer quantity;
}
