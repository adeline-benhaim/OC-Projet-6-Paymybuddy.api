package com.paymybuddy.api.service;

import com.paymybuddy.api.exception.TransferException;
import com.paymybuddy.api.model.Transfer;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.repository.TransferRepository;
import com.paymybuddy.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.paymybuddy.api.service.SecurityUtils.getIdCurrentUser;

@Service
public class TransferServiceImpl implements TransferService {
    private static final Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get the list of transfers for the current user
     *
     * @return the list of transfers for the current user
     */
    @Override
    public List<Transfer> getTransfers() {
        logger.info("Get a list of transfers");
        return transferRepository.findByIdUser(getIdCurrentUser());
    }

    /**
     * Create a new transfer between the current user and their bank account
     *
     * @param transfer to create
     * @return transfer created
     */
    @Override
    @Transactional
    public Transfer createTransfer(Transfer transfer) {
        logger.info("Create a new transfer");
        User currentUser = userRepository.findByUserId(getIdCurrentUser());
        double balanceCurrentUser = currentUser.getBalance();
        double balanceCurrentUserUpdated = 0;
        switch (transfer.getTransferType()) {
            case DEBIT:
                balanceCurrentUserUpdated = (balanceCurrentUser - transfer.getAmount());
                logger.info("New debit created : -" + transfer.getAmount());
                if (balanceCurrentUserUpdated < 0) {
                    logger.error("Unable to make the debit because the balance is insufficient ");
                    throw new TransferException("Balance not sufficient to make the transfer, maximum amount authorized : " + balanceCurrentUser);
                }
                break;
            case CREDIT:
                balanceCurrentUserUpdated = (balanceCurrentUser + transfer.getAmount());
                logger.info("New credit created : +" + transfer.getAmount());
                break;
        }
        long currentDate = System.currentTimeMillis();
        Transfer newTransfer = Transfer.builder()
                .idUser(getIdCurrentUser())
                .idBankAccount(transfer.getIdBankAccount())
                .amount(transfer.getAmount())
                .transferType(transfer.getTransferType())
                .date(new java.sql.Timestamp(currentDate))
                .success(true)
                .build();
        currentUser.setBalance(balanceCurrentUserUpdated);
        logger.info("New transfer created");
        return transferRepository.save(newTransfer);
    }
}
