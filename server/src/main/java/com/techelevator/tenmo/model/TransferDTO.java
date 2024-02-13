package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferDTO {

    private int transferId;
    private BigDecimal transferAmount;
    private int fromAccountId;
    private int toAccountId;

    public TransferDTO() {
        this.transferId = transferId;
        this.transferAmount = transferAmount;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public int getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(int fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public int getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(int toAccountId) {
        this.toAccountId = toAccountId;
    }
}
