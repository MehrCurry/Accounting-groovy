package accounting

import de.gzockoll.types.money.Money
import groovy.transform.ToString

@ToString
class SummaryAccount extends Account {
    Set accounts = []

    static hasMany = [accounts:Account]
    static constraints = {
    }

    SummaryAccount(Ledger ledger,String name, Account parent = null) {
        super(ledger,name,parent)
    }

    def addAccount(Account a) {
        accounts << a;
    }

    Money balance() {
        accounts.collect {it.balance()}.sum()
    }


    void post(Entry entry) {
        throw new UnsupportedOperationException("Write operations on summary accounts are unsupported!");
    }
}
