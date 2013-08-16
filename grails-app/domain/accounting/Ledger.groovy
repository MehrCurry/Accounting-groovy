package accounting
import com.ibm.icu.util.Currency as Currency

class Ledger {
    Map<String,Account> accounts = [:]
    List<Posting> postings = []
    String name

    def postingService

    static hasMany = [accounts:Account,postings:Posting]
    static constraints = {
    }

    Ledger(String name) {
        this.name=name
    }

    Account newAccount(String name, Currency currency) {
        assert !accounts.containsKey(name)
        def names=name.split(':')

        def lastAccount
        (0..<names.size()).each { i ->
            String n = names[i]
            def canonicalName = names[0..i].join(':')
            def account=accounts.get(canonicalName)
            if (!account) {
                account = n.is(names.last()) ? new DetailAccount(this, n ,currency, lastAccount) : new SummaryAccount(this, n, lastAccount)
                lastAccount?.addAccount(account)
                accounts.put(account.canonicalName(),account)
                account.save(failOnError: true)
            }
            lastAccount = account
        }
        lastAccount
    }

    Posting posting(String memo) {
        new Posting(memo: memo,ledger: this)
    }

    def post(Posting posting) {
        assert posting.canPost()
        postingService.post(posting)
        postings << posting
        posting
    }
}
