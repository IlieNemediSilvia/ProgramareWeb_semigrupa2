package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockRepository repository;

    public List<String> getProducts() {
        return repository.getProducts();
    }

    @Transactional
    public void addStock(String product, int units) {
        repository.addStock(product, units, Date.valueOf(LocalDate.now()));
    }

    @Transactional
    public void replaceStock(long id, int units) {
        repository.replaceStock(id, units);
    }

    public int getStock(String product, Date date) {
       return repository.getStock(product, date);
    }

    public boolean containsEntry(long id) {
        return repository.countEntry(id) > 0;
    }

    @Transactional
    public void removeOldEntries(Date date) {
        repository.removeOldEntries(date);
    }

    List<StockEntry> getEntries(String product) {
        return repository.findByProduct(product);
    }

    @Transactional
    public void removeStock(String product, int units) {
        repository.removeStock(product, units, Date.valueOf(LocalDate.now()));
    }

    @Transactional
    public void removeProduct(String product) {
        repository.removeProduct(product);
    }
}
