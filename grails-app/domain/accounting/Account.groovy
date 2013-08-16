package accounting

import de.gzockoll.types.money.Money
/**
 * Created with IntelliJ IDEA.
 * User: Guido Zockoll
 * Date: 14.08.13
 * Time: 14:46
 * To change this template use File | Settings | File Templates.
 */
abstract class Account {
    String name

    static belongsTo = [ledger: Ledger,parent:Account]

    static constraints = {
        parent nullable: true
        ledger nullable: true
    }

    Account(ledger, String name, Account parent=null) {
        this.ledger = ledger
        this.name = name
        this.parent = parent
    }

    abstract void post(Entry entry)
    abstract Money balance()
    String canonicalName() {
        def cn=parent?.canonicalName()
        parent ? "$cn:$name" : name
    }
}