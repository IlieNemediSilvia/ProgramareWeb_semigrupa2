package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StockController {

    @Autowired
    private StockService service;

    @GetMapping(value="/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getProducts() {
        List<String> products = service.getProducts();
        if (!products.isEmpty()) {
            return ResponseEntity.ok(products);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value="/products/{product}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StockEntry>> getEntries(@PathVariable("product") String product) {
        List<StockEntry> entries = service.getEntries(product);
        if (!entries.isEmpty()) {
            return ResponseEntity.ok(entries);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value="/stock/{product}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getStock(@PathVariable("product") String product, @RequestParam(name="date", required = false) String date) {
        if(!service.getEntries(product).isEmpty()) {
            int stock = service.getStock(product, Date.valueOf(date != null ? LocalDate.parse(date) : LocalDate.now()));
            return ResponseEntity.ok(stock);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value="/stock", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> addStock(@RequestParam("product") String product, @RequestParam("units") int units) {
        service.addStock(product, units);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value="/stock", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> removeStock(@RequestParam("product") String product, @RequestParam("units") int units) {
        if(service.getStock(product, Date.valueOf(LocalDate.now())) > units) {
            service.removeStock(product, units);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value="/stock", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> replaceStock(@RequestParam("id") long id, @RequestParam("units") int units) {
        if(service.containsEntry(id)) {
            service.replaceStock(id, units);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/stocks")
    public ResponseEntity<Void> removeOldEntries (@RequestParam(name="date", required = false) String date) {
        service.removeOldEntries(Date.valueOf(date != null ? LocalDate.parse(date) : LocalDate.now()));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/product/{product}")
    public ResponseEntity<Void> removeProduct (@PathVariable("product") String product) {
        service.removeProduct(product);
        return ResponseEntity.noContent().build();
    }
}
