package accounting
import de.gzockoll.types.money.Money

class Entry {
    // DateTime whenCharged
    // DateTime whenBooked=DateTime.now()
    Money amount
    String text

    static belongsTo = [account:Account]

    static embedded = ['amount']

    Entry(account, String text, Money amount) {
        this.account = account
        this.text = text
        this.amount = amount
    }

    def post() {
        // assert whenCharged == null
        assert amount != null

        // whenCharged = DateTime.now()
        account.add(this)
    }

    static constraints = {
    }
}
