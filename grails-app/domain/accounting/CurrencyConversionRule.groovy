package accounting

import accounting.groovy.CurrencyConverterService
import de.gzockoll.types.money.Money

class CurrencyConversionRule extends PostingRule {
    String targetAccountName
    String otherAccountName


    CurrencyConverterService ccs;

    static constraints = {
    }

    @Override
    void fireRule(Account account,Entry entry) {
        def target=account.ledger.accountByname(targetAccountName)
        assert target != null

        def other=account.ledger.accountByname(otherAccountName)
        assert other != null

        Currency to=target.getCurrency()

        Money converted=ccs.convert(entry.amount,to)
        account.ledger.posting("Converted")
                .entry(converted,target,entry.mode)
                .entry(converted,other,entry.mode.negate())
                .post()
    }
}
