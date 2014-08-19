package accounting

class DualCurrencyAccount extends DetailAccount {
    DetailAccount foreignAccount
    DetailAccount currencyCorrectionAccount

    static constraints = {
    }

    DualCurrencyAccount(Ledger ledger, String name, Currency currency, Account foreignAccont, Account correctionAccount, Account parent=null) {
        super(ledger, name, currency, parent)
        this.foreignAccount=foreignAccont
        this.currencyCorrectionAccount=correctionAccount
    }

}
