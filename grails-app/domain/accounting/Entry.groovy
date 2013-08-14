package accounting
import de.gzockoll.types.money.Money

class Entry {
    // DateTime whenCharged
    // DateTime whenBooked=DateTime.now()
    Money amount

    static belongsTo = [account:Account, posting:Posting]

    static embedded = ['amount']

    Entry(Account account, Money amount, Posting posting) {
        this.account = account
        this.amount = amount
        this.posting = posting
    }

    def post() {
        assert validate()
        account.add(this)
    }

    static constraints = {
        account isBlank: false
        amount isBlank: false
        posting isBlank: false
    }
}
