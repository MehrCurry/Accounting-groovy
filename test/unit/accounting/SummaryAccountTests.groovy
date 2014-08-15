package accounting

import de.gzockoll.types.money.Money
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(SummaryAccount)
@Mock([Posting,DetailAccount,Entry])
class SummaryAccountTests {

    void testSomething() {
        def summary=new SummaryAccount(null,"Summary",DetailAccount.EUR)

        def a1 = new DetailAccount("Hurz", DetailAccount.EUR)
        summary.addAccount(a1);
        def a2 = new DetailAccount("Hurz", DetailAccount.EUR)
        summary.addAccount(a2);
        def a3 = new DetailAccount("Hurz", DetailAccount.EUR)

        assert summary.balance() == Money.euros(0)

        def p1=new Posting(memo:"JUNIT").credit(Money.euros(10),a1).debit(Money.euros(10),a3).post()
        assert summary.balance() == Money.euros(10)
        def p2=p1.inverse(new Date()).post()
        [a1,a2,a3,summary].each { assert it.balance() == Money.euros(0)}

    }
}
