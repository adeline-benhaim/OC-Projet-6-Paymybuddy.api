package com.paymybuddy.api.config;

import com.paymybuddy.api.model.BankAccount;
import com.paymybuddy.api.model.Connection;
import com.paymybuddy.api.model.Transfer;
import com.paymybuddy.api.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class DataSourceTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * Mock User
     */
    List<User> userListMocked = new ArrayList<>();

    public void clearUserListMocked() {
        userListMocked.clear();
    }

    public void createUserListMocked() {
        User user1 = User.builder().userId(1).email("email1").password("password1").firstName("firstname1").lastName("lastname1").address("address1").zip(1000).city("city1").phone("01000").role("USER").connectionsUser(connectionListMocked).balance(100).build();
        User user2 = User.builder().userId(2).email("email2").password("password2").connectionsUser(null).balance(50).role("USER").build();
        User user3 = User.builder().userId(3).email("email3").password("password3").connectionsUser(null).balance(500).role("USER").build();
        userListMocked.addAll(Arrays.asList(user1, user2, user3));
    }

    /**
     * Mock Connection
     */
    List<Connection> connectionListMocked = new ArrayList<>();

    public void clearConnectionListMocked() {
        connectionListMocked.clear();
    }

    public void createConnectionListMocked() {
        Connection connection1 = Connection.builder().connectionId(1).emailOfUserLinked("email2").name("connection1").idUser(1).build();
        connectionListMocked.add(connection1);
    }

    public Connection addConnectionMocked(Connection connection) {
        connectionListMocked.add(connection);
        return connection;
    }

    /**
     * Mock Transfer
     */
    List<Transfer> transferListMocked = new ArrayList<>();

    public void clearTransferListMocked() {
        transferListMocked.clear();
    }

    public void createTransferListMocked() {
        long currentDate = System.currentTimeMillis();
        Transfer transfer1 = Transfer.builder().idBankAccount(1).idUser(1).transferType(Transfer.TransferType.DEBIT).amount(25).date(new java.sql.Timestamp(currentDate)).success(true).build();
        Transfer transfer2 = Transfer.builder().idBankAccount(1).idUser(1).transferType(Transfer.TransferType.CREDIT).amount(50).date(new java.sql.Timestamp(currentDate)).success(true).build();
        transferListMocked.addAll(Arrays.asList(transfer1, transfer2));
    }

    /**
     * Mock BankAccount
     */
    List<BankAccount> bankAccountListMocked = new ArrayList<>();

    public void clearBankAccountListMocked() {
        bankAccountListMocked.clear();
    }

    public void createBankAccountListMocked() {
        BankAccount bankAccount1 = BankAccount.builder().accountId(1).idUser(1).bic("11111").iban("11111").name("bank1").build();
        BankAccount bankAccount2 = BankAccount.builder().accountId(2).idUser(1).bic("22222").iban("22222").name("bank2").build();
        BankAccount bankAccount3 = BankAccount.builder().accountId(3).idUser(2).bic("33333").iban("33333").name("bank1").build();
        bankAccountListMocked.addAll(Arrays.asList(bankAccount1, bankAccount2, bankAccount3));
    }

}
