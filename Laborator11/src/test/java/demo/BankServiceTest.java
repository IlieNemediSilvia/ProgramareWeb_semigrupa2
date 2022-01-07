package demo;

import demo.domain.Account;
import demo.domain.Currency;
import demo.domain.Payment;
import demo.domain.User;
import demo.repository.AccountRepository;
import demo.repository.CurrencyRepository;
import demo.repository.PaymentRepository;
import demo.repository.UserRepository;
import demo.service.BankException;
import demo.service.BankService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BankServiceTest {

    @InjectMocks
    private BankService bankService;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CurrencyRepository currencyRepository;
    @Mock
    private AccountRepository accountRepository;

    private static Optional<User> sender;
    private static Optional<User> receiver;
    private static Optional<Currency> currency;
    private static double amount;
    private static List<Account> senderAccounts;
    private static List<Account> receiverAccounts;
    private static List<Account> senderAccountsWrongCurrency;

    @BeforeAll
    public static void setup() {
        sender = Optional.of(new User(1L, "John", "Doe", "jdoe@gmail.com", "p@ssword"));
        receiver = Optional.of(new User(2L, "Ion", "Popescu", "popescu@gmail.com", "passw0rd"));
        currency = Optional.of(new Currency(1L, "Leu nou romanesc", "RON", "Romania"));
        amount = 10000;
        senderAccounts = new ArrayList<>();
        senderAccounts.add(new Account(1L, sender.get(), currency.get(), amount));
        senderAccountsWrongCurrency = new ArrayList<>();
        senderAccountsWrongCurrency.add(new Account(2L, sender.get(), new Currency(2L, "Dolar american", "USD", "SUA"), amount));
        senderAccountsWrongCurrency.add(new Account(3L, sender.get(), new Currency(3L, "Euro", "EUR", "Uniunea Europeana"), amount));
        receiverAccounts = new ArrayList<>();
        receiverAccounts.add(new Account(4L, sender.get(), currency.get(), amount));
    }

    @Test
    @DisplayName("Test invalid sender")
    public void testInvalidSender() {
        when(userRepository.findByEmail(sender.get().getEmail())).thenReturn(Optional.empty());
        BankException exception = assertThrows(BankException.class,
                () -> bankService.sendPayment(sender.get().getEmail(), sender.get().getPassword(),
                        receiver.get().getEmail(), currency.get().getCode(), amount/10));
        assertEquals(BankException.ErrorCode.USER_NOT_FOUND, exception.getError());
    }


    @Test
    @DisplayName("Test invalid password")
    public void testInvalidPassword() {
        when(userRepository.findByEmail(sender.get().getEmail())).thenReturn(sender);
        BankException exception = assertThrows(BankException.class,
                () -> bankService.sendPayment(sender.get().getEmail(), "test",
                        receiver.get().getEmail(), currency.get().getCode(), amount/10));
        assertEquals(BankException.ErrorCode.BAD_CREDENTIALS, exception.getError());
    }

    @Test
    @DisplayName("Test invalid receiver")
    public void testInvalidReceiver() {
        when(userRepository.findByEmail(sender.get().getEmail())).thenReturn(sender);
        when(userRepository.findByEmail(receiver.get().getEmail())).thenReturn(Optional.empty());
        BankException exception = assertThrows(BankException.class,
                () -> bankService.sendPayment(sender.get().getEmail(), sender.get().getPassword(),
                        receiver.get().getEmail(), currency.get().getCode(), amount/10));
        assertEquals(BankException.ErrorCode.USER_NOT_FOUND, exception.getError());
    }

    @Test
    @DisplayName("Test invalid currency")
    public void testInvalidCurrency() {
        when(userRepository.findByEmail(sender.get().getEmail())).thenReturn(sender);
        when(userRepository.findByEmail(receiver.get().getEmail())).thenReturn(receiver);
        when(currencyRepository.findByCode(currency.get().getCode())).thenReturn(Optional.empty());
        BankException exception = assertThrows(BankException.class,
                () -> bankService.sendPayment(sender.get().getEmail(), sender.get().getPassword(),
                        receiver.get().getEmail(), currency.get().getCode(), amount/10));
        assertEquals(BankException.ErrorCode.CURRENCY_NOT_FOUND, exception.getError());
    }

    @Test
    @DisplayName("Test sender has no account for currency")
    public void testSenderNoAccountForCurrency() {
        when(userRepository.findByEmail(sender.get().getEmail())).thenReturn(sender);
        when(userRepository.findByEmail(receiver.get().getEmail())).thenReturn(receiver);
        when(currencyRepository.findByCode(currency.get().getCode())).thenReturn(currency);
        when(accountRepository.findAllByUser(sender.get())).thenReturn(Collections.emptyList());
        BankException exception = assertThrows(BankException.class,
                () -> bankService.sendPayment(sender.get().getEmail(), sender.get().getPassword(),
                        receiver.get().getEmail(), currency.get().getCode(), amount/10));
        assertEquals(BankException.ErrorCode.USER_HAS_NO_ACCOUNT_FOR_CURRENCY, exception.getError());
    }

    @Test
    @DisplayName("Test receiver has no account for currency")
    public void testReceiverNoAccountForCurrency() {
        when(userRepository.findByEmail(sender.get().getEmail())).thenReturn(sender);
        when(userRepository.findByEmail(receiver.get().getEmail())).thenReturn(receiver);
        when(currencyRepository.findByCode(currency.get().getCode())).thenReturn(currency);
        when(accountRepository.findAllByUser(sender.get())).thenReturn(senderAccounts);
        when(accountRepository.findAllByUser(receiver.get())).thenReturn(Collections.emptyList());
        BankException exception = assertThrows(BankException.class,
                () -> bankService.sendPayment(sender.get().getEmail(), sender.get().getPassword(),
                        receiver.get().getEmail(), currency.get().getCode(), amount/10));
        assertEquals(BankException.ErrorCode.USER_HAS_NO_ACCOUNT_FOR_CURRENCY, exception.getError());
    }

    @Test
    @DisplayName("Test sender has not enough money")
    public void testSenderNotEnoughMoney() {
        when(userRepository.findByEmail(sender.get().getEmail())).thenReturn(sender);
        when(userRepository.findByEmail(receiver.get().getEmail())).thenReturn(receiver);
        when(currencyRepository.findByCode(currency.get().getCode())).thenReturn(currency);
        when(accountRepository.findAllByUser(sender.get())).thenReturn(senderAccounts);
        when(accountRepository.findAllByUser(receiver.get())).thenReturn(receiverAccounts);
        BankException exception = assertThrows(BankException.class,
                () -> bankService.sendPayment(sender.get().getEmail(), sender.get().getPassword(),
                        receiver.get().getEmail(), currency.get().getCode(), amount * 2));
        assertEquals(BankException.ErrorCode.ACCOUNT_HAS_NOT_ENOUGH_MONEY_FOR_PAYMENT, exception.getError());
    }

    @Test
    @DisplayName("Test payment was not saved")
    public void testPaymentWasNotSaved() {
        when(userRepository.findByEmail(sender.get().getEmail())).thenReturn(sender);
        when(userRepository.findByEmail(receiver.get().getEmail())).thenReturn(receiver);
        when(currencyRepository.findByCode(currency.get().getCode())).thenReturn(currency);
        when(accountRepository.findAllByUser(sender.get())).thenReturn(senderAccounts);
        when(accountRepository.findAllByUser(receiver.get())).thenReturn(receiverAccounts);
        when(paymentRepository.save(any(Payment.class))).thenReturn(null);
        BankException exception = assertThrows(BankException.class,
                () -> bankService.sendPayment(sender.get().getEmail(), sender.get().getPassword(),
                        receiver.get().getEmail(), currency.get().getCode(), amount/5));
        assertEquals(BankException.ErrorCode.PAYMENT_COULD_NOT_BE_SAVED, exception.getError());
    }
}
