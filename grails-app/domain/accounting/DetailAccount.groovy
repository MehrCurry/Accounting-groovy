package accounting

import de.gzockoll.types.money.Money
import groovy.transform.ToString

@ToString
class DetailAccount extends Account {
    static final EUR=Currency.getInstance("EUR")
    public static final String FORMAT = "| %-30s | %10s | %10s | %-40s |\n"
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
        printf FORMAT,"Posted At","DEBIT","CREDIT","MEMO"
        println '-' * 103
        entries.each { Entry entry ->
            String debit= entry.mode == Entry.Mode.DEBIT ? entry.amount : ""
            String credit= entry.mode == Entry.Mode.CREDIT ? entry.amount : ""
            printf FORMAT,entry.posting.whenPosted,debit,credit,entry.posting.memo
        }
        println '=' * 103
        println balance()
    }
}