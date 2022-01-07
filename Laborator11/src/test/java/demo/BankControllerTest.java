package demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.controller.BankController;
import demo.domain.Account;
import demo.domain.Currency;
import demo.domain.Payment;
import demo.domain.User;
import demo.repository.AccountRepository;
import demo.repository.CurrencyRepository;
import demo.repository.PaymentRepository;
import demo.repository.UserRepository;
import demo.service.BankException;
import demo.service.Credentials;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static demo.service.BankException.userWithSameEmailAlreadyExists;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BankController.class)
@EnableTransactionManagement
@ComponentScan
public class BankControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentRepository paymentRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CurrencyRepository currencyRepository;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private PlatformTransactionManager transactionManager;

    private static User sender;
    private static User receiver;
    private static Currency euro;
    private static Currency pound;
    private static Currency dollar;
    private static List<Account> senderAccounts;
    private static List<Account> receiverAccounts;

    @BeforeAll
    public static void setup() {
        euro = new Currency(1L, "Euro", "EUR", "European Union");
        dollar = new Currency(2L, "American Dollar", "USD", "USA");
        pound = new Currency(3L, "Pound", "GBP", "United Kingdom");
        sender = new User(1L, "John", "Doe", "john.doe@gmail.com", "password");
        receiver = new User(2L, "Ion", "Popescu", "ion.popescu@gmail.com", "parola");
        senderAccounts = new ArrayList<>();
        senderAccounts.add(new Account(1L, sender, euro, 10000));
        senderAccounts.add(new Account(2L, sender, dollar, 20000));
        receiverAccounts = new ArrayList<>();
        receiverAccounts.add(new Account(3L, receiver, euro, 10000));
        receiverAccounts.add(new Account(4L, receiver, dollar, 10000));
    }

    @Value("${administrator.email}")
    private String adminEmail;
    @Value("${administrator.password}")
    private String adminPassword;

    @Test
    public void testGetAccounts() throws Exception {
        when(userRepository.findByEmail("dummy@domain.com")).thenReturn(Optional.empty());
        String endpoint = "/api/accounts";
        mockMvc.perform(get(endpoint).header(Credentials.AUTHORIZATION, credentials("dummy@domain.com", "dummy")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").value(BankException.userNotFound().getError().toString()));
        when(userRepository.findByEmail(sender.getEmail())).thenReturn(Optional.of(sender));
        when(accountRepository.findAllByUser(sender)).thenReturn(Collections.emptyList());
        mockMvc.perform(get(endpoint).header(Credentials.AUTHORIZATION, credentials(sender)))
                .andExpect(status().isNoContent());
        when(accountRepository.findAllByUser(sender)).thenReturn(senderAccounts);
        mockMvc.perform(get(endpoint).header(Credentials.AUTHORIZATION, credentials(sender)))
                .andExpect(status().isOk());
    }

    @Test
    public void testSendPayment() throws Exception {
        when(currencyRepository.findByCode(senderAccounts.get(0).getCurrency().getCode())).thenReturn(Optional.of(senderAccounts.get(0).getCurrency()));
        when(currencyRepository.findByCode(pound.getCode())).thenReturn(Optional.of(pound));
        when(userRepository.findByEmail(sender.getEmail())).thenReturn(Optional.of(sender));
        when(accountRepository.findAllByUser(sender)).thenReturn(senderAccounts);
        when(userRepository.findByEmail(receiver.getEmail())).thenReturn(Optional.of(receiver));
        when(accountRepository.findAllByUser(receiver)).thenReturn(receiverAccounts);
        when(paymentRepository.save(any())).thenReturn(new Payment());
        String endpoint = "/api/payments?receiver=%s&currency=%s&amount=%f";
        mockMvc.perform(post(String.format(endpoint, receiver.getEmail(), pound.getCode(), senderAccounts.get(0).getAmount()))
                .header(Credentials.AUTHORIZATION, credentials(sender)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value(BankException.userHasNoAccountForCurrency().getError().toString()));
        mockMvc.perform(post(String.format(endpoint, receiver.getEmail(), senderAccounts.get(0).getCurrency().getCode(), senderAccounts.get(0).getAmount()/2))
                        .header(Credentials.AUTHORIZATION, credentials(sender)))
                .andExpect(status().isAccepted());
        mockMvc.perform(post(String.format(endpoint, receiver.getEmail(), senderAccounts.get(0).getCurrency().getCode(), senderAccounts.get(0).getAmount()*2))
                        .header(Credentials.AUTHORIZATION, credentials(sender)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value(BankException.userHasNotEnoughMoney().getError().toString()));
    }

    @Test
    public void testSaveUser() throws Exception {
        String endpoint = "/api/users";
        mockMvc.perform(post(endpoint)
                        .header(Credentials.AUTHORIZATION, credentials(sender))
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(sender)))
                .andExpect(status().isForbidden());
        when(userRepository.findByEmail(sender.getEmail())).thenReturn(Optional.of(sender));
        mockMvc.perform(post(endpoint)
                        .header(Credentials.AUTHORIZATION, credentials(adminEmail, adminPassword))
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(sender)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value(userWithSameEmailAlreadyExists().getError().toString()));
        when(userRepository.findByEmail(sender.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(new User());
        mockMvc.perform(post(endpoint)
                        .header(Credentials.AUTHORIZATION, credentials(adminEmail, adminPassword))
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(sender)))
                .andExpect(status().isAccepted());
    }

    private static String credentials (String email, String password) {
        return String.format("Basic %s", Base64.getEncoder().encodeToString(String.format("%s:%s", email, password).getBytes()));
    }
    private static String credentials (User user) {
        return credentials(user.getEmail(), user.getPassword());
    }
}
