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

        def a1=ledger.newAccount("a:b:c:d",DetailAccount.EUR)
        assert a1 != null

        def a2=ledger.newAccount("a:b:c:e",DetailAccount.EUR)
        assert a2 != null

        assert a1.parent == a2.parent
        assert a1.parent.accounts.size() == 2
        assert a1.parent.accounts.contains(a2)
        assert a2.parent.accounts.contains(a1)
        assert ledger.accounts.size() == 5
    }
}
