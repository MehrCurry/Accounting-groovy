package accounting

import de.gzockoll.types.money.Money
/**
 * Created by guido on 17.08.14.
 */
class CurrencyCorrectionRule {
    String otherAccount
    String correctionAccountName

    def currencyConverterService

    def fire(Account account) {
        def ledger=account.ledger

        def foreignCurrencyBalance=account.balance()
        def defaultCurrencyBalance=ledger.accountByname(otherAccount).balance()

        def difference= currencyConverterService.convert(foreignCurrencyBalance,defaultCurrencyBalance.currency)-defaultCurrencyBalance
        if (difference.value != 0) {
            def zero= Money.fromMajor(0,foreignCurrencyBalance.currency)
            ledger.posting("Currency correction")
                    .credit(zero,account)
                    .debit(zero,account)
                    .credit(difference,ledger.accountByname(otherAccount))
                    .debit(difference,ledger.accountByname(correctionAccountName))
                    .post()
        }

    }
}
