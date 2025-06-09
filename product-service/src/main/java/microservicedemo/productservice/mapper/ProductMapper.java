package microservicedemo.productservice.mapper;

import microservicedemo.productservice.po.Product;
import org.apache.ibatis.annotations.*;


import java.util.List;

@Mapper
public interface ProductMapper {
    @Select("select p.id ,p.product_name as productName,p.price as price, p.stock as stock from product p where id = #{productId}")
    Product findByProductId(@Param("productId") Long productId);

    @Select("select p.id,p.product_name as productName,p.price as price, p.stock as stock from product p")
    List<Product> queryAllProduct();

    // 插入商品记录
    @Insert("INSERT INTO product (product_name, price, stock) VALUES (#{productName}, #{price}, #{stock})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertProduct(Product product);

    // 根据 ID 更新商品记录
    @Update("UPDATE product SET product_name = #{productName}, price = #{price}, stock=#{stock} WHERE id = #{id}")
    int updateProduct(Product product);

    // 根据 ID 删除商品记录
    @Delete("DELETE FROM product WHERE id = #{id}")
    int deleteProductById(@Param("id") Long id);
}
