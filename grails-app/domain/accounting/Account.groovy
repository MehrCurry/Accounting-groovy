package accounting

import com.ibm.icu.util.Currency as Currency
import de.gzockoll.types.money.Money
/**
 * Created with IntelliJ IDEA.
 * User: Guido Zockoll
 * Date: 14.08.13
 * Time: 14:46
 * To change this template use File | Settings | File Templates.
 */
abstract class Account {

    String name
    Currency currency

    Account(String name,Currency currency) {
        this.name=name
        this.currency=currency
    }

    static belongsTo = [parent:SummaryAccount]

    static constraints = {
        parent nullable: true
        currency inList: Currency.availableCurrencies.sort()
    }

    abstract void post(Entry entry)
    abstract Money balance()
    String canonicalName() {
        def cn=parent?.canonicalName()
        parent!=null ? "$cn:$name" : name
    }
}