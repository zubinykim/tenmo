package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.BalanceDTO;
import com.techelevator.tenmo.model.TransferDTO;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    public BalanceDTO getBalance(String username);

    public TransferDTO transferBalance(String fromUsername, String toUsername, BigDecimal amount);

    public List<TransferDTO> displayTransfersByAccountId(int accountId);

    public TransferDTO displayTransferByTransferId(int transferId);

}
