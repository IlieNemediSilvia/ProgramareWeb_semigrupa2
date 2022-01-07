package demo.controller;

import demo.domain.Account;
import demo.domain.User;
import demo.service.BankService;
import demo.service.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BankController {

    @Autowired
    private BankService service;

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccounts(@RequestHeader(Credentials.AUTHORIZATION) String header) {
        Credentials credentials = new Credentials(header);
        List<Account> accounts = service.getUserAccounts(credentials.getEmail(), credentials.getPassword());
        return accounts.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(accounts);
    }

    @PostMapping("/payments")
    public ResponseEntity<Void> sendPayment (@RequestHeader(Credentials.AUTHORIZATION) String header,
                                             @RequestParam("receiver") String receiver,
                                             @RequestParam("currency") String currencyCode,
                                             @RequestParam double amount) {
        Credentials credentials = new Credentials(header);
        service.sendPayment(credentials.getEmail(), credentials.getPassword(), receiver, currencyCode, amount);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/users")
    public ResponseEntity<Void> saveUser(@RequestHeader(Credentials.AUTHORIZATION) String header, @RequestBody User user) {
        Credentials credentials = new Credentials(header);
        service.saveUser(credentials, user);
        return ResponseEntity.accepted().build();
    }
    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteUser(@RequestHeader(Credentials.AUTHORIZATION) String header, @RequestParam String email) {
        Credentials credentials = new Credentials(header);
        service.removeUser(credentials, email);
        return ResponseEntity.accepted().build();
    }
}
