package demo.service;

public class BankException extends RuntimeException {

    public enum ErrorCode {
        USER_NOT_FOUND,
        BAD_CREDENTIALS,
        CURRENCY_NOT_FOUND,
        USER_HAS_NO_ACCOUNT_FOR_CURRENCY,
        ACCOUNT_HAS_NOT_ENOUGH_MONEY_FOR_PAYMENT,
        PAYMENT_COULD_NOT_BE_PROCESSED,
        PAYMENT_COULD_NOT_BE_SAVED,
        USER_WITH_SAME_EMAIL_ALREADY_EXISTS,
        USER_COULD_NOT_BE_SAVED,
        USER_COULD_NOT_BE_REMOVED,
        ACCOUNT_ALREADY_EXISTS,
        ACCOUNT_COULD_NOT_BE_OPENED,
        ACCOUNT_COULD_NOT_BE_SAVED,
        ACCOUNT_COULD_NOT_BE_CLOSED,
        ACCOUNT_IS_NOT_EMPTY
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
    public static BankException userWithSameEmailAlreadyExists() { return new BankException(ErrorCode.USER_WITH_SAME_EMAIL_ALREADY_EXISTS);}
    public static BankException userCouldNotBeSaved() { return new BankException(ErrorCode.USER_COULD_NOT_BE_SAVED);}
    public static BankException userCouldNotBeRemoved() { return new BankException(ErrorCode.USER_COULD_NOT_BE_REMOVED);}
    public static BankException accountAlreadyExists() { return new BankException(ErrorCode.ACCOUNT_ALREADY_EXISTS);}
    public static BankException accountCouldNotBeOpened() { return new BankException(ErrorCode.ACCOUNT_COULD_NOT_BE_OPENED);}
    public static BankException accountCouldNotBeSaved() { return new BankException(ErrorCode.ACCOUNT_COULD_NOT_BE_SAVED);}
    public static BankException accountCouldNotBeClosed() { return new BankException(ErrorCode.ACCOUNT_COULD_NOT_BE_CLOSED);}
    public static BankException accountIsNotEmpty() { return new BankException(ErrorCode.ACCOUNT_IS_NOT_EMPTY);}
}
