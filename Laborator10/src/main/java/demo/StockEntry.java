package demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "stock")
public class StockEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String product;

    @Column
    private int units;

    @Column
    private Date date;

    public long getId() {
        return id;
    }

    public String getProduct() {
        return product;
    }

    public int getUnits() {
        return units;
    }

    public Date getDate() {
        return date;
    }
}
