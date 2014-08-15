package accounting

import de.gzockoll.types.money.Money
import groovy.transform.ToString

@ToString
class DetailAccount extends Account {
    static final EUR=Currency.getInstance("EUR")
    Currency currency
    Set entries = []

    static hasMany = [entries:Entry]

    static constraints = {
    }

    DetailAccount(Ledger ledger, String name, Currency currency, Account parent=null) {
        super(ledger, name, parent)
        this.currency=currency
    }

    void post(Entry entry) {
        assert entry.amount.currency == currency
        this.validate()
        entries.add entry
        fireEagerRules(entry)
    }

    Money balance() {
        this.validate()
        final zero = Money.fromMinor(0, currency)
        entries.find {it.mode == Entry.Mode.CREDIT}.collect { it.amount }.sum(zero) +
            entries.find {it.mode == Entry.Mode.DEBIT}.collect { it.amount }.sum(zero).negate()
    }

    Money balance(Entry.Mode mode) {
        this.validate()
        final zero = Money.fromMinor(0, currency)
        def balance=entries.findAll { it.mode == mode}.collect {entry -> entry.amount }.sum(zero)

        mode == Entry.Mode.CREDIT ? balance : balance.negate()
    }
}