import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder

/**
 * Created with IntelliJ IDEA.
 * User: Guido Zockoll
 * Date: 16.08.13
 * Time: 10:58
 * To change this template use File | Settings | File Templates.
 */
class PhoneNumberTests extends GroovyTestCase {

    void testPhoneNumber() {
        def util=PhoneNumberUtil.getInstance()

        def result=util.parse("+1-800-GOT-MILK","DE")
        println result
        assert result != null

        PhoneNumberOfflineGeocoder geocoder = PhoneNumberOfflineGeocoder.getInstance()
        println geocoder.getDescriptionForNumber(result, Locale.ENGLISH)
        println geocoder.getDescriptionForNumber(result, Locale.GERMAN)
        println geocoder.getDescriptionForNumber(result, Locale.ITALIAN)

    }
}
