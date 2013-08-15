package accounting

import de.gzockoll.types.money.Money
import org.joda.time.DateTime

class Posting {
    DateTime whenCreated=DateTime.now()
    DateTime whenPosted
    String memo

    Set entries = []

    static hasMany = [entries:Entry]
    static constraints = {
        whenPosted nullable: true
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
        assert !isPosted()
        assert canPost()
        whenPosted=DateTime.now()
        entries.each { it.post() }
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

    def credit(Money amount, DetailAccount account) {
        add(amount,account,Entry.Mode.CREDIT)
        this
    }

    def debit(Money amount, DetailAccount account) {
        add(amount.negate(),account,Entry.Mode.DEBIT)
        this
    }
}
