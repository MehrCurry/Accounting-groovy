package accounting

import de.gzockoll.types.money.Money
import com.ibm.icu.util.Currency as Currency

class Account {
    static final EUR=Currency.getInstance("EUR")
    String name
    Currency currency
    Set entries = []

    static hasMany = [entries:Entry]

    static constraints = {
        name blank: false
            currency(inList: [Currency.getInstance("EUR"),Currency.getInstance("USD")])
        }

    Account(name, currency) {
        this.name = name
        this.currency = currency
    }

    def add(Entry entry) {
        this.validate()
        entries.add entry
    }

    def balance() {
        this.validate()
        final zero = Money.fromMinor(0, currency)
        entries.collect { it.amount }.sum(zero)
    }
}
