package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.BalanceDTO;
import com.techelevator.tenmo.model.BalanceUpdateDTO;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private AccountDao accountDao;
    @Autowired
    private JdbcUserDao jdbcUserDao;
    @Autowired
    private JdbcAccountDao jdbcAccountDao;

    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @RequestMapping(path = "/display-users", method = RequestMethod.GET)
    public List<String> displayUsers() {
        List<String> usernames = new ArrayList<>();
        List<User> users = jdbcUserDao.findAll();

        for (int i = 0; i < users.size(); i++) {
            usernames.add("username: " + users.get(i).getUsername());
        }
        return usernames;
    }

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BalanceDTO getBalance(Principal principal) {
        return accountDao.getBalance(principal.getName());
    }

    @RequestMapping(path = "/transfer", method = RequestMethod.PUT)
    public TransferDTO transferBalance(Principal principal, @RequestBody BalanceUpdateDTO balanceUpdateDTO) {
        return accountDao.transferBalance(principal.getName(), balanceUpdateDTO.getUsername(), balanceUpdateDTO.getAmount());
    }

    @RequestMapping(path = "/display-user-transfers", method = RequestMethod.GET)
    public List<String> displayUserTransfers(Principal principal) {
        List<String> transfers = new ArrayList<>();
        List<TransferDTO> transfersOriginal = jdbcAccountDao.displayTransfersByAccountId(jdbcUserDao.findAccountIdByUsername(principal.getName()));

        for (int i = 0; i < transfersOriginal.size(); i++) {
            transfers.add("transferId: " + transfersOriginal.get(i).getTransferId());
            transfers.add("transferAmount: " + transfersOriginal.get(i).getTransferAmount());
            transfers.add("fromAccountId: " + transfersOriginal.get(i).getFromAccountId());
            transfers.add("toAccountId: " + transfersOriginal.get(i).getToAccountId());
            transfers.add("");
        }

        return transfers;
    }

    @RequestMapping(path = "/display-transfer/{transferId}", method = RequestMethod.GET)
    public TransferDTO displayTransfer(Principal principal, @PathVariable int transferId) {
        return jdbcAccountDao.displayTransferByTransferId(transferId);
    }
}
