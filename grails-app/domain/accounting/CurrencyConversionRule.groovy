package accounting

import accounting.groovy.CurrencyConverterService
import de.gzockoll.types.money.Money

class CurrencyConversionRule extends PostingRule {
    Account target;

    CurrencyConverterService ccs;

    static constraints = {
    }

    @Override
    void fireRule(Account account,Entry entry) {
        Currency to=target.getCurrency()

        Money converted=ccs.convert(entry.amount,target)
        println(converted)
    }
}
