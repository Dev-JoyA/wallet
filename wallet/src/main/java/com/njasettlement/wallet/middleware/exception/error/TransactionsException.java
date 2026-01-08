package com.njasettlement.wallet.middleware.exception.error;

public class TransactionsException extends WalletException{
    public TransactionsException() {super(); }

    public TransactionsException(String message) {super(message);}

    public TransactionsException(String message, Throwable cause) {super(message, cause);}

    public TransactionsException(Throwable cause) {super(cause);}
}
