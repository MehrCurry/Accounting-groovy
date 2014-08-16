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
        entries.collect{ it.mode.signedValueOf(it.amount)}.sum(zero)
    }

    Money balance(Entry.Mode mode) {
        this.validate()
        final zero = Money.fromMinor(0, currency)
        entries.findAll { it.mode == mode}.collect {entry -> entry.mode.signedValueOf(entry.amount) }.sum(zero)
    }

    def printT() {
        println name
        println "Credit\t\t|\t\tDebit"
        println "--------------------------"
        entries.each {
            if (it.mode == Entry.Mode.DEBIT)
                print "\t\t\t|\t\t"
            print it.amount
            if (it.mode == Entry.Mode.CREDIT)
                print "\t\t|"
            println ""
        }
        println "=========================="
        println balance()
    }
}