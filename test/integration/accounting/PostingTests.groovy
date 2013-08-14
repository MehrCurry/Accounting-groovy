package accounting

import de.gzockoll.types.money.Money

import static org.junit.Assert.*
import org.junit.*

class PostingTests {

    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testSomething() {
        def a1=new Account("Account 1",Account.EUR)
        a1.save()
        def a2=new Account("Account 2",Account.EUR)
        a2.save()
        def a3=new Account("Account 3",Account.EUR)
        a3.save()

        def posting = new Posting().credit(Money.euros(10),a1)
                .credit(Money.euros(20),a2)
                .debit(Money.euros(30),a3).post()
        [a1,a2,a3,posting].each { it.save() }

        assert Posting.findAll().size() == 1
    }
}
