import accounting.DetailAccount
import accounting.Ledger
import com.ibm.icu.util.Currency as Currency
import com.ibm.icu.util.ULocale
import de.gzockoll.types.money.Money

class BootStrap {
    static final EUR = Currency.getInstance("EUR")

    def init = { servletContext ->
    }

    def destroy = {
    }
}
