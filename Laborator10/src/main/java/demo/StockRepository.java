package demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<StockEntry, Long> {

    @Query(value="select distinct product from stock order by product", nativeQuery = true)
    List<String> getProducts();

    @Modifying
    @Query(value = "insert into stock (product, units, date) values (:product, :units, :date)", nativeQuery = true)
    void addStock(String product, int units, Date date);

    List<StockEntry> findByProduct(String product);

    @Modifying
    @Query(value="delete from stock where product = :product", nativeQuery = true)
    void removeProduct(String product);

    @Modifying
    @Query(value = "insert into stock (product, units, date) values (:product, -:units, :date)", nativeQuery = true)
    void removeStock(String product, int units, Date date);

    @Modifying
    @Query(value = "update stock set units = :units where id = :id", nativeQuery = true)
    void replaceStock(long id, int units);

    @Query("select sum(units) from StockEntry where product = :product and date <= :date")
    int getStock(String product, Date date);

    @Modifying
    @Query(value = "delete from stock where date <= :date", nativeQuery = true)
    void removeOldEntries(Date date);

    @Query(value = "select count(id) from stock where id = :id", nativeQuery = true)
    int countEntry (long id);

}
