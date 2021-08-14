package com.paymybuddy.api.service;

import com.paymybuddy.api.constants.CommissionRate;
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
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.paymybuddy.api.service.SecurityUtils.getIdCurrentUser;

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

    @Override
    public long getTotalElementPageable(Pageable pageable) {
        return transactionRepository.findByIdTransmitterOrIdBeneficiaryOrderByDateDesc(getIdCurrentUser(), getIdCurrentUser(), pageable).getTotalElements();
    }

    /**
     * Get a list of transactions issued and received belong to current user
     *
     * @return a list of transactions issued and received belong to current user
     */
    @Override
    public List<Transaction> getAllTransactions(Pageable pageable) {
        logger.info("Get a list of all transactions");
        return transactionRepository.findByIdTransmitterOrIdBeneficiaryOrderByDateDesc(getIdCurrentUser(), getIdCurrentUser(), pageable)
                .stream()
                .peek(transaction -> {
                    if (transaction.getIdTransmitter() == getIdCurrentUser()) {
                        transaction.setAmount(-transaction.getAmount());
                    }
                })
                .collect(Collectors.toList());
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
        User currentUser = userRepository.findByUserId(getIdCurrentUser());
        User userBeneficiary = userRepository.findUserByEmail(transactionDto.getEmailBeneficiary());
        if (userBeneficiary == null) {
            logger.error("Unable to create transaction because user beneficiary doesn't exist in database");
            throw new TransactionException("Unable to create transaction because user beneficiary doesn't exist in database");
        }
        double commission = transactionDto.getAmount() * CommissionRate.COMMISSION_RATE;
        double balanceCurrentUser = currentUser.getBalance();
        double balanceCurrentUserUpdated = balanceCurrentUser - (transactionDto.getAmount() + commission);
        double maximumAuthorizedAmount = balanceCurrentUser / CommissionRate.RATE_CALCULATION_MAXIMUM_AUTHORIZED;
        if (balanceCurrentUserUpdated < 0) {
            logger.error("Unable to make the transaction because the balance is insufficient");
            throw new TransactionException("Balance not sufficient to make the transaction, maximum authorized amount : " + Math.round(maximumAuthorizedAmount*100.0)/100.0);
        }
        Connection connection = connectionRepository.findByIdUserAndEmailOfUserLinked(getIdCurrentUser(), transactionDto.getEmailBeneficiary());
        long currentDate = System.currentTimeMillis();
        Transaction newTransaction = Transaction.builder()
                .idConnection(connection.getConnectionId())
                .idTransmitter(getIdCurrentUser())
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
        logger.info("New transaction created");
        Transaction transaction = transactionRepository.save(newTransaction);
        Commission newCommission = Commission.builder()
                .idTransaction(newTransaction.getTransactionId())
                .amount(commission)
                .date(newTransaction.getDate())
                .build();
        commissionRepository.save(newCommission);
        return transaction;
    }
}
