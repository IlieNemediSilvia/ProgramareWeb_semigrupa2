package demo.service;

import demo.domain.Account;
import demo.domain.Currency;
import demo.domain.Payment;
import demo.domain.User;
import demo.repository.AccountRepository;
import demo.repository.CurrencyRepository;
import demo.repository.PaymentRepository;
import demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private AccountRepository accountRepository;

    public void sendPayment (String senderEmail, String senderPassword, String receiverEmail, String currencyCode, double amount) {
        User sender = getUser(senderEmail, senderPassword);
        User receiver = getUser(receiverEmail);
        Currency currency = getCurrency(currencyCode);
        Account senderAccount = getUserAccountForCurrency(sender, currency);
        Account receiverAccount = getUserAccountForCurrency(receiver, currency);
        if (amount > senderAccount.getAmount()) {
            throw BankException.userHasNotEnoughMoney();
        }
        Payment payment = new Payment();
        payment.setSender(senderAccount);
        payment.setReceiver(receiverAccount);
        payment.setCurrency(currency);
        payment.setAmount(amount);

        try {
            payment = paymentRepository.save(payment);
            if (payment == null) {
                throw BankException.paymentCouldNotBeSaved();
            }
        } catch (RuntimeException e) {
            if (e instanceof BankException) {
                throw e;
            } else {
                throw BankException.paymentCouldNotBeProcessed();
            }
        }
    }

    private User getUser(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw BankException.userNotFound();
        }
        return user.get();
    }

    private User getUser(String email, String password) {
        User user = getUser(email);
        if (!user.getPassword().equals(password)) {
            throw BankException.badCredentials();
        }
        return user;
    }

    private Currency getCurrency(String code) {
        Optional<Currency> currency = currencyRepository.findByCode(code);
        if (currency.isEmpty()) {
            throw BankException.currencyNotFound();
        }
        return currency.get();
    }

    private Account getUserAccountForCurrency (User user, Currency currency) {
        List<Account> accounts = accountRepository.findAllByUser(user);
        Optional<Account> account = accounts.stream().filter(a -> a.getCurrency().equals(currency)).findFirst();
        if (account.isEmpty()) {
            throw BankException.userHasNoAccountForCurrency();
        }
        return account.get();
    }

}
