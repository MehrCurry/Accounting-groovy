package accounting

import org.joda.time.DateTime

class Event {
    DateTime occured=DateTime.now()
    Type type

    static constraints = {
    }

    static enum Type {
        RESERVATION,SALE,REFUND,NOTIFY
    }
}
