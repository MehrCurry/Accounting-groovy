package accounting.groovy

import accounting.Account
import accounting.DetailAccount
import accounting.Posting
import accounting.SummaryAccount
import com.ibm.icu.util.Currency as Currency

class Ledger {
    Map<String,Account> accounts
    List<Posting> postings

    static constraints = {
    }

    Account newAccount(String name, Currency currency) {
        def names=name.split(':')

        def lastAccount
        names.each {
            def account=accounts.get(names[i])
            if (!account) {
                account = it.is(names.last()) ? new DetailAccount(parent: lastAccount, name: it, currency: currency) : new SummaryAccount(parent: lastAccount, name: it, currency: currency)
                if (name.is(names.first()))
                    accounts.put(it,account)
            }
            lastAccount = account
        }
        lastAccount
    }
}
