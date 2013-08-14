import accounting.Account
import accounting.Entry
import de.gzockoll.types.money.Money

class BootStrap {

    def init = { servletContext ->
        def a1=new Account("0000",Account.EUR).save()
        new Account("0001",Account.EUR).save()
        new Account("0002",Account.EUR).save()

        new Entry(a1,"Bla",Money.fromMajor(10,Account.EUR)).post()
    }

    def destroy = {
    }
}
