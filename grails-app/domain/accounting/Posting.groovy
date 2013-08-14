package accounting

import de.gzockoll.types.money.Money
import org.joda.time.DateTime

class Posting {
    DateTime whenCreated=DateTime.now()
    DateTime whenPosted
    String memo
    boolean posted=false

    Set entries = []

    static hasMany = [entries:Entry]
    static constraints = {
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

    private boolean isBalanced() {
        balance().value == 0G
    }

    def balance() {
        assert entries != null
        entries.collect{it.amount}.sum()
    }


    def post() {
        assert !posted
        assert canPost()
        whenPosted=DateTime.now()
        entries.each { it.post() }
        posted = true
        this
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
