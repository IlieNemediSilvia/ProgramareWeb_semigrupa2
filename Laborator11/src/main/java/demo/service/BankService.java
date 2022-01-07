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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static demo.service.BankException.accountAlreadyExists;
import static demo.service.BankException.badCredentials;
import static demo.service.BankException.userHasNotEnoughMoney;

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

    @Value("${administrator.email}")
    private String adminEmail;
    @Value("${administrator.password}")
    private String adminPassword;

    public void sendPayment (String senderEmail, String senderPassword, String receiverEmail, String currencyCode, double amount) {
        User sender = getUser(senderEmail, senderPassword);
        User receiver = getUser(receiverEmail);
        Currency currency = getCurrency(currencyCode);
        Account senderAccount = getUserAccountForCurrency(sender, currency, true);
        Account receiverAccount = getUserAccountForCurrency(receiver, currency, true);

        if (amount > senderAccount.getAmount()) {
            throw userHasNotEnoughMoney();
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

    public List<Account> getUserAccounts(String email, String password) {
        return accountRepository.findAllByUser(getUser(email, password));
    }

    @Transactional
    public void saveUser(Credentials credentials, User user) {
        checkCredentials(credentials);
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw BankException.userWithSameEmailAlreadyExists();
        }
        try {
            userRepository.save(user);
        } catch (RuntimeException e) {
            if (e instanceof BankException) {
                throw e;
            } else {
                throw BankException.userCouldNotBeSaved();
            }
        }
    }

    @Transactional
    public void removeUser(Credentials credentials, String email) {
        checkCredentials(credentials);
        try {
            userRepository.delete(userRepository.findByEmail(email).get());
        } catch (RuntimeException e) {
            if (e instanceof BankException) {
                throw e;
            } else {
                throw BankException.userCouldNotBeRemoved();
            }
        }
    }

    @Transactional
    public void openAccount(String email, String password, String currencyCode) {
        User user = getUser(email, password);
        Currency currency = getCurrency(currencyCode);
        Account account = getUserAccountForCurrency(user, currency, false);
        if (account != null) {
            throw accountAlreadyExists();
        }
        account = new Account();
        account.setUser(user);
        account.setCurrency(currency);
        try {
            accountRepository.save(account);
        } catch (RuntimeException e) {
            if (e instanceof BankException) {
                throw e;
            } else {
                throw BankException.accountCouldNotBeOpened();
            }
        }
    }

    @Transactional
    public void deposit (String email, String password, String currencyCode, double amount) {
        User user = getUser(email, password);
        Currency currency = getCurrency(currencyCode);
        Account account = getUserAccountForCurrency(user, currency, true);
        if (account.getAmount() + amount < 0) {
            throw userHasNotEnoughMoney();
        }
        account.setAmount(account.getAmount() + amount);
        try {
            accountRepository.save(account);
        } catch (RuntimeException e) {
            if (e instanceof BankException) {
                throw e;
            } else {
                throw BankException.accountCouldNotBeSaved();
            }
        }
    }

    public void closeAccount (String email, String password, String currencyCode) {
        User user = getUser(email, password);
        Currency currency = getCurrency(currencyCode);
        Account account = getUserAccountForCurrency(user, currency, true);
        if (account.getAmount() == 0) {
            try {
                accountRepository.delete(account);
            } catch (RuntimeException e) {
                if (e instanceof BankException) {
                    throw e;
                } else {
                    throw BankException.accountCouldNotBeClosed();
                }
            }
        } else {
            throw BankException.accountIsNotEmpty();
        }
    }

    private void checkCredentials(Credentials credentials) {
        if(!adminEmail.equals(credentials.getEmail()) || !adminPassword.equals(credentials.getPassword())) {
            throw badCredentials();
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

    private Account getUserAccountForCurrency (User user, Currency currency, boolean mustExist) {
        List<Account> accounts = accountRepository.findAllByUser(user);
        Optional<Account> account = accounts.stream().filter(a -> a.getCurrency().equals(currency)).findFirst();
        if (account.isPresent()) {
            return account.get();
        } else {
            if(mustExist) {
                throw BankException.userHasNoAccountForCurrency();
            } else {
                return null;
            }
        }
    }

}
