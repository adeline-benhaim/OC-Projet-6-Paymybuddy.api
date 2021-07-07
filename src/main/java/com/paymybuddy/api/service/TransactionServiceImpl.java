package com.paymybuddy.api.service;

import com.paymybuddy.api.exception.TransactionException;
import com.paymybuddy.api.model.Connection;
import com.paymybuddy.api.model.Transaction;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.repository.ConnectionRepository;
import com.paymybuddy.api.repository.TransactionRepository;
import com.paymybuddy.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ConnectionRepository connectionRepository;

    int idCurrentUser = User.getCurrentUser();

    /**
     * Get a list of transactions issued and received belong to current user
     *
     * @return a list of transactions issued and received belong to current user
     */
    @Override
    public List<Transaction> getAllTransactions() {
        logger.info("Get a list of all transactions");
        return transactionRepository.findByIdTransmitterOrIdBeneficiary(idCurrentUser, idCurrentUser);
    }

    /**
     * Create a new transfer between the current user and a user connected
     *
     * @param transaction to create
     * @return transaction created
     */
    @Override
    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        logger.info("Create a new transaction");
        User currentUser = userRepository.findByUserId(idCurrentUser);
        User userBeneficiary = userRepository.findByUserId(transaction.getIdBeneficiary());
        if(userBeneficiary == null) {
            logger.error("Unable to create transaction because user beneficiary doesn't exist in database");
            throw new TransactionException("Unable to create transaction because user beneficiary doesn't exist in database");
        }
        int balanceCurrentUser = currentUser.getBalance();
        int balanceCurrentUserUpdated = balanceCurrentUser - transaction.getAmount();
        if (balanceCurrentUserUpdated < 0) {
            logger.error("Unable to make the transaction because the balance is insufficient");
            throw new TransactionException("Balance not sufficient to make the transaction, maximum amount authorized : " + balanceCurrentUser);
        }
        Connection connection = connectionRepository.findByIdUserAndEmailOfUserLinked(idCurrentUser, userBeneficiary.getEmail());
        long currentDate = System.currentTimeMillis();
        Transaction newTransaction = Transaction.builder()
                .idConnection(connection.getConnectionId())
                .idTransmitter(idCurrentUser)
                .idBeneficiary(transaction.getIdBeneficiary())
                .connectionName(connection.getName())
                .description(transaction.getDescription())
                .amount(transaction.getAmount())
                .date(new java.sql.Timestamp(currentDate))
                .success(true)
                .build();
        int balanceUserBeneficiary = userBeneficiary.getBalance();
        int balanceUserBeneficiaryUpdated = balanceUserBeneficiary + transaction.getAmount();
        currentUser.setBalance(balanceCurrentUserUpdated);
        userBeneficiary.setBalance(balanceUserBeneficiaryUpdated);
        logger.info("New transaction created");
        return transactionRepository.save(newTransaction);
    }


}
