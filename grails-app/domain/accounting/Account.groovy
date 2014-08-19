package accounting

import de.gzockoll.types.money.Money
import org.joda.time.DateTime

/**
 * Created with IntelliJ IDEA.
 * User: Guido Zockoll
 * Date: 14.08.13
 * Time: 14:46
 * To change this template use File | Settings | File Templates.
 */
abstract class Account {
    String name
    DateTime whenCreated=DateTime.now()
    def rules=[]

    static belongsTo = [ledger: Ledger,parent:Account]
    static hasMany = [rules:PostingRule]

    static constraints = {
        name unique: true
        parent nullable: true
        ledger nullable: true
    }

    static mapp

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

    def fireEagerRules(entry) {
        rules.each { it.fireRule(this,entry) }
    }

    def rule(PostingRule rule) {
        rules.add(rule)
    }

    def fireRule(rule) {
        rule.fire(this)
    }
}