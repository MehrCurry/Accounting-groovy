package accounting

import de.gzockoll.types.money.Money
import org.joda.time.DateTime

class Posting {
    final DateTime whenCreated=DateTime.now()
    String memo
    boolean posted=false

    Set entries = []

    static hasMany = [entries:Entry]
    static constraints = {
    }

    private add(Money amount, DetailAccount account, mode) {
        assertNotPosted();
        addToEntries (new Entry(account, amount, this,mode))
    }

    private void assertCanPost() {
        if (!canPost())
            throw new IllegalStateException("balance must be zero");
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

    def assertNotPosted() {
        if (posted)
            throw new IllegalStateException("Posting has been posted already!");
    }

    def post() {
        assertNotPosted()
        assertCanPost()
        // AuditLog.post("transaction.post",this);
        entries.each { it.post() }
        posted = true
        this
    }

    def inverse(aDate) {
        def trans = new Posting(date:d,memo: "Storno: $memo")
        entries.each { trans.add(new Entry(it.account, it.amount.negate())) }
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
