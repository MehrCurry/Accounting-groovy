package time
/**
 * Created with IntelliJ IDEA.
 * User: Guido Zockoll
 * Date: 30.08.13
 * Time: 08:48
 * To change this template use File | Settings | File Templates.
 */
class StopWatch {
    static time(closure) {
        def start=System.nanoTime()
        closure.call()
        def stop=System.nanoTime()
        (stop-start)/1000.0
    }
}
