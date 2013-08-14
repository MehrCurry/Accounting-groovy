package accounting
import de.gzockoll.types.money.Money
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@TestFor(DetailAccount)
@Mock([Posting,DetailAccount,Entry])
class DetailAccountTests {
    def account

    void setUp() {
        account=new DetailAccount(name:"JUNIT",currency: DetailAccount.EUR)
    }

    void tearDown() {
        // Tear down logic here
    }

    void testWrongCurrency() {
        shouldFail { account.post(new Entry(account,Money.fromMajor(java.util.Currency.getInstance("USD"))),new Posting(),Entry.Mode.CREDIT)}
    }

    void testBalance() {
        account.post(new Entry(account,Money.euros(10),new Posting(),Entry.Mode.CREDIT))
        assert account.balance() == Money.euros(10)
        assert account.balance(Entry.Mode.CREDIT) == Money.euros(10)
        assert account.balance(Entry.Mode.DEBIT) == Money.euros(0)

        account.post(new Entry(account,Money.euros(-30),new Posting(),Entry.Mode.DEBIT))
        assert account.balance() == Money.euros(-20)
        assert account.balance(Entry.Mode.CREDIT) == Money.euros(10)
        assert account.balance(Entry.Mode.DEBIT) == Money.euros(30)
    }
}
