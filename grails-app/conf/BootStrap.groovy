import accounting.DetailAccount
import accounting.DetailAccount
import accounting.Entry
import accounting.Posting
import accounting.SummaryAccount
import de.gzockoll.types.money.Money

class BootStrap {

    def init = { servletContext ->
        def a1=new DetailAccount("0000",DetailAccount.EUR)
        def a2=new DetailAccount("0001",DetailAccount.EUR)
        def a3=new DetailAccount("0002",DetailAccount.EUR)
        [a1,a2,a3].each { it.save() }

        def posting = new Posting(memo:"Hurz");
        posting.credit(Money.euros(10),a1).credit(Money.euros(20),a2).debit(Money.euros(30),a3).post()
        [posting,a1,a2,a3].each { it.save() }

        def s=new SummaryAccount("4000",DetailAccount.EUR)
        [a1,a2,a3].each { s.addAccount(it)}
        s.save()
    }

    def destroy = {
    }
}
