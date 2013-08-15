package accounting.groovy

import accounting.DetailAccount
import accounting.SummaryAccount
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Ledger)
@Mock([SummaryAccount,DetailAccount])
class LedgerTests {

    void testSomething() {
        def ledger=new Ledger()

        def account=ledger.newAccount("a:b:c:d",DetailAccount.EUR)
        assert account != null
    }
}
