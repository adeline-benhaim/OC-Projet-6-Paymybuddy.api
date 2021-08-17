package com.paymybuddy.api.service;

import com.paymybuddy.api.exception.TransferException;
import com.paymybuddy.api.model.Transfer;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.repository.TransferRepository;
import com.paymybuddy.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.paymybuddy.api.service.SecurityUtils.getIdCurrentUser;

@Service
public class TransferServiceImpl implements TransferService {
    private static final Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get a pair with a list of transfer and a long number
     *
     * @param pageable the result list
     * @return a pair with a list of transfer belong to current user and the total of elements of this list
     */
    @Override
    public Pair<Page<Transfer>, Long> getTransfers(Pageable pageable) {
        logger.info("Get a list of transfers");
       Page<Transfer> transferPage = transferRepository.findByIdUser(getIdCurrentUser(), pageable);
       return Pair.of(transferPage, transferPage.getTotalElements());
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
            case DEBIT -> {
                balanceCurrentUserUpdated = (balanceCurrentUser - transfer.getAmount());
                logger.info("New debit created : -" + transfer.getAmount());
                if (balanceCurrentUserUpdated < 0) {
                    logger.error("Unable to make the debit because the balance is insufficient ");
                    throw new TransferException("Balance not sufficient to make the transfer, maximum amount authorized : " + balanceCurrentUser);
                }
            }
            case CREDIT -> {
                balanceCurrentUserUpdated = (balanceCurrentUser + transfer.getAmount());
                logger.info("New credit created : +" + transfer.getAmount());
            }
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
