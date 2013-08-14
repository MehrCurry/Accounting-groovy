package accounting

import com.ibm.icu.util.Currency as Currency
import de.gzockoll.types.money.Money

class DetailAccount extends Account {
    static final EUR=Currency.getInstance("EUR")
    String name
    Currency currency
    Set entries = []

    static hasMany = [entries:Entry]

    static constraints = {
        name blank: false
            currency(inList: [Currency.getInstance("EUR"),Currency.getInstance("USD")])
        }

    DetailAccount(name, currency) {
        this.name = name
        this.currency = currency
    }


    void post(Entry entry) {
        assert entry.amount.currency == currency
        this.validate()
        entries.add entry
    }

    Money balance() {
        this.validate()
        final zero = Money.fromMinor(0, currency)
        entries.collect { it.amount }.sum(zero)
    }

    Money balance(Entry.Mode mode) {
        this.validate()
        final zero = Money.fromMinor(0, currency)
        def balance=entries.findAll { it.mode == mode}.collect {entry -> entry.amount }.sum(zero)

        mode == Entry.Mode.CREDIT ? balance : balance.negate()
    }
}