package accounting

import de.gzockoll.types.money.Money
import time.StopWatch

class PostingService {
    def post(Posting postings) {
        postings.each { posting ->
            posting.save(failOnError: true)
            posting.doPost()
        }
    }

    def multipost(Ledger ledger, Account a1,Account a2) {
        def duration= StopWatch.time {100.times { ledger.posting(it.toString()).credit(Money.euros(it),a1).debit(Money.euros(it),a2).post()}}
        println duration
    }
}
