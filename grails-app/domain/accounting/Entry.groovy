package accounting
import de.gzockoll.types.money.Money
import persistance.MoneyUserType

class Entry {
    Money amount
    Mode mode

    static belongsTo = [account:Account, posting:Posting]

    static mapping = {
        amount type: MoneyUserType, {
            column name: "amount"
            column name: "currency", sqlType: "char", length: 3
        }
    }

    Entry(Account account, Money amount, Posting posting, Mode mode) {
        this.account = account
        this.amount = amount
        this.posting = posting
        this.mode = mode
    }

    def post() {
        assert validate()
        account.add(this)
    }

    static constraints = {
        account blank: false
        amount blank: false
        posting blank: false
    }
    static enum Mode {
        CREDIT,DEBIT
    }
}
