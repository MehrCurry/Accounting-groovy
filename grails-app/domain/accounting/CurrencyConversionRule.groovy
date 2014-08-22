package accounting

import de.gzockoll.types.money.Money

class CurrencyConversionRule extends PostingRule {
    String targetAccountName
    String otherAccountName


    def currencyConverterService;

    static constraints = {
    }

    @Override
    void fireRule(Account account,Entry entry) {
        def target=account.ledger.accountByname(targetAccountName)
        assert target != null

        def other=account.ledger.accountByname(otherAccountName)
        assert other != null

        Currency to=target.getCurrency()

        Money converted=currencyConverterService.convert(entry.amount,to)
        account.ledger.posting("$entry.amount.currency>$to: $entry.posting.memo")
                .entry(converted,target,entry.mode)
                .entry(converted,other,entry.mode.negate())
                .post()
    }
}
