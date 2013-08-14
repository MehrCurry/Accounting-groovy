package accounting

import de.gzockoll.types.money.Money

class Transaction {
    Date date
    boolean posted=false

    Set entries = []

    static hasMany = [entries:Entry]
    static constraints = {
    }

    def add(Money amount, Account account, String text) {
        assertNotPosted();
        addToEntries (new Entry(account, text, amount))
    }

    private void assertCanPost() {
        if (!canPost())
            throw new IllegalStateException("balance must be zero");
    }

    public boolean canPost() {
        return entries.size()>0 && balance().value == 0G
    }

    def balance() {
        assert entries != null
        entries.collect{it.amount}.sum()
    }

    def assertNotPosted() {
        if (posted)
            throw new IllegalStateException("Transaction has been posted already!");
    }

    def post() {
        assertNotPosted()
        assertCanPost()
        // AuditLog.add("transaction.post",this);
        entries.each { it.post() }
        posted = true
    }

    def inverse(aDate) {
        def trans = new Transaction(date:d)
        entries.each { trans.add(new Entry(it.account, "Storno: $it.text",it.amount.negate())) }
        trans;
    }
}
