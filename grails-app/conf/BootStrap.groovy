import accounting.Account
import accounting.Entry
import accounting.Posting
import de.gzockoll.types.money.Money

class BootStrap {

    def init = { servletContext ->
        def a1=new Account("0000",Account.EUR)
        def a2=new Account("0001",Account.EUR)
        def a3=new Account("0002",Account.EUR)
        [a1,a2,a3].each { it.save() }

        def posting = new Posting(memo:"Hurz");
        posting.credit(Money.euros(10),a1).credit(Money.euros(20),a2).debit(Money.euros(30),a3).post()
        [posting,a1,a2,a3].each { it.save() }
    }

    def destroy = {
    }
}
