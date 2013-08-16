package accounting
import com.ibm.icu.util.Currency as Currency
import de.gzockoll.types.money.Money

class DualCurrencyAccount extends Account {
    Account primaryAccount
    Currency secondaryCurrency
    Account secondaryAccount

    def currencyConverterService

    static constraints = {
    }

    DualCurrencyAccount(String name, Currency primaryCurrency, Account primaryAccount, Currency secondaryCurrency, Account secondaryAccount) {
        super(name, primaryCurrency)
        this.primaryAccount = primaryAccount
        this.secondaryCurrency = secondaryCurrency
        this.secondaryAccount = secondaryAccount
    }

    @Override
    void post(Entry entry) {
        assert [currency,secondaryCurrency].contains(entry.amount.currency)
        if (entry.amount.currency == currency)
            postPrimary(entry)
        if (entry.amount.currency == secondaryCurrency)
            postSecondary(entry)
    }

    private postPrimary(Entry entry) {
        primaryAccount.post(entry)
        def otherAccount=ledger.newAccount("ForeingCurrencies:$secondaryCurrency")
        def Money otherAmount = currencyConverterService.convert(entry.amount,secondaryCurrency)
        switch (entry.mode) {
            case Entry.Mode.CREDIT:
                ledger.posting("entry.posting.name in $secondaryCurrency").credit(otherAmount,secondaryAccount).debit(otherAmount,otherAccount).post()
                break
            case Entry.Mode.DEBIT:
                ledger.posting("entry.posting.name in $secondaryCurrency").debit(otherAmount,secondaryAccount).credit(otherAmount,otherAccount).post()
                break
            default:
                throw IllegalArgumentException("Unsupported mode: $entry.mode")
        }
    }

    private postSecondary(Entry entry) {
        secondaryAccount.post(entry)
        def otherAccount=ledger.newAccount("ForeingCurrencies:$primaryCurrency")
        def Money otherAmount = currencyConverterService.convert(entry.amount,primaryCurrency)
        switch (entry.mode) {
            case Entry.Mode.CREDIT:
                ledger.posting("entry.posting.name in $primaryCurrency").credit(otherAmount,primaryAccount).debit(otherAmount,otherAccount).post()
                break
            case Entry.Mode.DEBIT:
                ledger.posting("entry.posting.name in $primaryCurrency").debit(otherAmount,primaryAccount).credit(otherAmount,otherAccount).post()
                break
            default:
                throw IllegalArgumentException("Unsupported mode: $entry.mode")
        }
    }

    @Override
    Money balance() {
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }
}
