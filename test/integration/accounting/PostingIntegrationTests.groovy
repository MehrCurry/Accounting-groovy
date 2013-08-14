package accounting
import de.gzockoll.types.money.Money
import org.junit.After
import org.junit.Before
import org.junit.Test

class PostingIntegrationTests {

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
        def a1=new DetailAccount("DetailAccount 1",DetailAccount.EUR)
        a1.save()
        def a2=new DetailAccount("DetailAccount 2",DetailAccount.EUR)
        a2.save()
        def a3=new DetailAccount("DetailAccount 3",DetailAccount.EUR)
        a3.save()

        def posting = new Posting().credit(Money.euros(10),a1)
                .credit(Money.euros(20),a2)
                .debit(Money.euros(30),a3).post()
        [a1,a2,a3,posting].each { it.save() }

        assert Posting.findAll().size() == 1
    }
}
