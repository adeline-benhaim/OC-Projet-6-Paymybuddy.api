package com.paymybuddy.api.service;

import com.paymybuddy.api.exception.TransactionException;
import com.paymybuddy.api.model.Commission;
import com.paymybuddy.api.model.Connection;
import com.paymybuddy.api.model.Transaction;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.model.dto.TransactionDto;
import com.paymybuddy.api.repository.CommissionRepository;
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
    @Autowired
    CommissionRepository commissionRepository;

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
     * Create a new transaction between the current user and a user connected
     *
     * @param transactionDto transaction to create
     * @return transaction created
     */
    @Override
    @Transactional
    public Transaction createTransaction(TransactionDto transactionDto) {
        logger.info("Create a new transaction");
        User currentUser = userRepository.findByUserId(idCurrentUser);
        User userBeneficiary = userRepository.findUserIdByEmail(transactionDto.getEmailBeneficiary());
        if (userBeneficiary == null) {
            logger.error("Unable to create transaction because user beneficiary doesn't exist in database");
            throw new TransactionException("Unable to create transaction because user beneficiary doesn't exist in database");
        }
        double commission = transactionDto.getAmount() * 0.05;
        double balanceCurrentUser = currentUser.getBalance();
        double balanceCurrentUserUpdated = balanceCurrentUser - (transactionDto.getAmount() + commission);
        if (balanceCurrentUserUpdated < 0) {
            logger.error("Unable to make the transaction because the balance is insufficient");
            throw new TransactionException("Balance not sufficient to make the transaction, maximum amount authorized : " + balanceCurrentUser);
        }
        Connection connection = connectionRepository.findByIdUserAndEmailOfUserLinked(idCurrentUser, transactionDto.getEmailBeneficiary());
        long currentDate = System.currentTimeMillis();
        Transaction newTransaction = Transaction.builder()
                .idConnection(connection.getConnectionId())
                .idTransmitter(idCurrentUser)
                .idBeneficiary(userBeneficiary.getUserId())
                .connectionName(connection.getName())
                .description(transactionDto.getDescription())
                .amount(transactionDto.getAmount())
                .date(new java.sql.Timestamp(currentDate))
                .success(true)
                .build();
        double balanceUserBeneficiary = userBeneficiary.getBalance();
        double balanceUserBeneficiaryUpdated = balanceUserBeneficiary + transactionDto.getAmount();
        currentUser.setBalance(balanceCurrentUserUpdated);
        userBeneficiary.setBalance(balanceUserBeneficiaryUpdated);
        Commission newCommission = Commission.builder()
                .idTransaction(newTransaction.getTransactionId())
                .amount(commission)
                .date(newTransaction.getDate())
                .build();
        commissionRepository.save(newCommission);
        logger.info("New transaction created");
        return transactionRepository.save(newTransaction);
    }


}
