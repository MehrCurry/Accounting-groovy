package accounting

import com.ibm.icu.util.Currency as Currency
import de.gzockoll.types.money.Money

class SummaryAccount extends Account {
    Set accounts = []
    Currency currency
    String name

    static hasMany = [accounts:Account]
    static constraints = {
    }

    SummaryAccount(String name,Currency currency) {
        this.name=name
        this.currency=currency
    }

    def addAccount(Account a) {
        assert currency == a.currency
        addToAccounts(a);
    }

    Money balance() {
        accounts.collect {it.balance()}.sum()
    }


    void post(Entry entry) {
        throw new UnsupportedOperationException("Write operations on summary accounts are unsupported!");
    }
}
