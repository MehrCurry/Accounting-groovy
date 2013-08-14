package accounting
import de.gzockoll.types.money.Money
import groovy.transform.ToString
import persistance.MoneyUserType

@ToString
class Entry {
    Money amount
    Mode mode

    static belongsTo = [account:DetailAccount, posting:Posting]

    static mapping = {
        amount type: MoneyUserType, {
            column name: "amount"
            column name: "currency", sqlType: "char", length: 3
        }
    }

    static constraints = {
        account blank: false
        amount blank: false
        posting blank: false
    }

    Entry(DetailAccount account, Money amount, Posting posting, Mode mode) {
        this.account = account
        this.amount = amount
        this.posting = posting
        this.mode = mode
    }

    def post() {
        assert validate()
        account.post(this)
    }

    static enum Mode {
        CREDIT,DEBIT

        def negate() {
            Mode.values()[1-this.ordinal()]
        }
    }
}
