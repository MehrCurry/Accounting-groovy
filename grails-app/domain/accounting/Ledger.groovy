package accounting

import groovy.transform.ToString
import org.joda.time.DateTime

@ToString
class Ledger {
    DateTime whenCreated=DateTime.now()
    Map<String,Account> accounts = [:]
    List<Posting> postings = []
    String name

    def postingService

    static hasMany = [accounts:Account,postings:Posting]
    static constraints = {
    }

    static Ledger withName(String name) {
        findOrCreateByName(name)
    }

    Account newAccount(String name, String isoCode) {
        newAccount(name,Currency.getInstance(isoCode))
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
    Account accountByname(String name) {
        accounts.get(name)
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

    def boolean isBalanced() {
        def detailAccounts = accounts.values().findAll { it instanceof DetailAccount }
        def balances=detailAccounts.collect {it.balance()}.groupBy {it.currency}
        balances.values().every { it.sum().amount() == 0}
    }
}
