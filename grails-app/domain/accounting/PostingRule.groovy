package accounting

/**
 * Created by Guido.Zockoll on 11.08.2014.
 */
abstract class PostingRule {
    abstract void fireRule(Account account,Entry entry);
}
