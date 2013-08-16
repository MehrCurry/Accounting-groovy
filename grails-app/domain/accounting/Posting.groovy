package accounting

import de.gzockoll.types.money.Money
import groovy.transform.PackageScope
import groovy.transform.ToString
import org.joda.time.DateTime

@ToString
class Posting {
    DateTime whenCreated=DateTime.now()
    DateTime whenPosted
    String memo

    Set entries = []

    static belongsTo = [ledger:Ledger]
    static hasMany = [entries:Entry]
    static constraints = {
        whenPosted nullable: true
        ledger nullable: true
    }

    private add(Money amount, DetailAccount account, mode) {
        assert !posted
        addToEntries (new Entry(account, amount, this,mode))
    }


    boolean canPost() {
        return hasEntries() && isBalanced()
    }

    boolean hasEntries() {
        entries.size() > 0
    }

    boolean isBalanced() {
        balance().value == 0G
    }

    Money balance() {
        assert entries != null
        entries.collect{it.amount}.sum()
    }

    def post() {
        ledger.post(this)
    }

    @PackageScope doPost() {
        assert !isPosted()
        assert canPost()
        entries.each { it.post() }
        whenPosted=DateTime.now()
        this
    }

    boolean isPosted() {
        whenPosted != null
    }

    def inverse(aDate) {
        def trans = new Posting(date:aDate,memo: "Storno: $memo")
        entries.each { trans.add(it.amount.negate(),it.account,it.mode.negate()) }
        trans;
    }

    def credit(Money amount, Account account) {
        add(amount,account,Entry.Mode.CREDIT)
        this
    }

    def debit(Money amount, Account account) {
        add(amount.negate(),account,Entry.Mode.DEBIT)
        this
    }
}
