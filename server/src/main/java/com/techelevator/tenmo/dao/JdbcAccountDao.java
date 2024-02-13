package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.BalanceDTO;
import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BalanceDTO getBalance(String username) {

        String sql = "SELECT username, balance\n" +
                "FROM account\n" +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id\n" +
                "WHERE username = ?;";

        BalanceDTO balanceDTO = null;

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
            if (results.next()) {
                String user = results.getString("username");
                BigDecimal balance = results.getBigDecimal("balance");

                balanceDTO = new BalanceDTO();
                balanceDTO.setUsername(user);
                balanceDTO.setBalance(balance);
            }
        } catch (DataAccessException e) {

        }
        return balanceDTO;
    }

    @Override
    public TransferDTO transferBalance(String fromUsername, String toUsername, BigDecimal transferAmount) {

        if (fromUsername.equals(toUsername)) {
            return null;
        }
        String balanceSql = "SELECT balance\n" +
                "FROM account\n" +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id\n" +
                "WHERE username = ?;";

        Integer fromUsernameBalance = jdbcTemplate.queryForObject(balanceSql, Integer.class, fromUsername);

        BigDecimal balance = BigDecimal.valueOf(fromUsernameBalance);

        if (transferAmount.compareTo(balance) > 0) {
            return null;
        }
        if (transferAmount.compareTo(new BigDecimal("0")) <= 0) {
            return null;
        }

        String sql = "UPDATE account SET balance = balance + ? WHERE account.account_id = (SELECT account.account_id " +
                "FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "WHERE username = ?);";

        jdbcTemplate.update(sql, transferAmount.multiply(new BigDecimal("-1")), fromUsername);
        jdbcTemplate.update(sql, transferAmount, toUsername);

        String sql2 = "INSERT INTO transactions (transfer_amount, from_account_id, to_account_id) VALUES (?," +
                "(SELECT account.account_id FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id WHERE username = ?), " +
                "(SELECT account.account_id FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id WHERE username = ?))" +
                "RETURNING transfer_id;";

        Integer newTransferId = jdbcTemplate.queryForObject(sql2, Integer.class, transferAmount, fromUsername, toUsername);

        return displayTransferByTransferId(newTransferId);
    }

    @Override
    public List<TransferDTO> displayTransfersByAccountId(int accountId) {
        List<TransferDTO> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_amount, from_account_id, to_account_id FROM transactions WHERE from_account_id = ? OR to_account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
        while(results.next()) {
            TransferDTO transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public TransferDTO displayTransferByTransferId(int transferId) {
        String sql = "SELECT transfer_id, transfer_amount, from_account_id, to_account_id FROM transactions WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        TransferDTO transfer = null;
        if(results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }

    private TransferDTO mapRowToTransfer(SqlRowSet rs) {
        TransferDTO transfer = new TransferDTO();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferAmount(rs.getBigDecimal("transfer_amount"));
        transfer.setFromAccountId(rs.getInt("from_account_id"));
        transfer.setToAccountId(rs.getInt("to_account_id"));
        return transfer;
    }
}
