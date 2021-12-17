package demo.service;

public class BankException extends RuntimeException {

    public enum ErrorCode {
        USER_NOT_FOUND,
        BAD_CREDENTIALS,
        CURRENCY_NOT_FOUND,
        USER_HAS_NO_ACCOUNT_FOR_CURRENCY,
        ACCOUNT_HAS_NOT_ENOUGH_MONEY_FOR_PAYMENT,
        PAYMENT_COULD_NOT_BE_PROCESSED,
        PAYMENT_COULD_NOT_BE_SAVED
    }

    private ErrorCode error;

    private BankException(ErrorCode error) {
        this.error = error;
    }

    public ErrorCode getError() {
        return error;
    }

    public static BankException userNotFound() {
        return new BankException(ErrorCode.USER_NOT_FOUND);
    }
    public static BankException badCredentials() {
        return new BankException(ErrorCode.BAD_CREDENTIALS);
    }
    public static BankException currencyNotFound() {
        return new BankException(ErrorCode.CURRENCY_NOT_FOUND);
    }
    public static BankException userHasNoAccountForCurrency() {
        return new BankException(ErrorCode.USER_HAS_NO_ACCOUNT_FOR_CURRENCY);
    }
    public static BankException userHasNotEnoughMoney() {
        return new BankException(ErrorCode.ACCOUNT_HAS_NOT_ENOUGH_MONEY_FOR_PAYMENT);
    }
    public static BankException paymentCouldNotBeProcessed() {
        return new BankException(ErrorCode.PAYMENT_COULD_NOT_BE_PROCESSED);
    }
    public static BankException paymentCouldNotBeSaved() {
        return new BankException(ErrorCode.PAYMENT_COULD_NOT_BE_SAVED);
    }
}
