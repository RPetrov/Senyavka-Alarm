package rpetrov.senyavkaspeakingalarmclock.providers

/**
 * Created by Roman Petrov
 */


object Utils {
    fun getCorrectWordForDigit(value: Int, one: String, few: String, many: String, other: String): String {

        return if (value % 10 == 1 && value % 100 != 11) {
            one
        } else if (value % 10 in 2..4 && !(value % 100 in 12..14)) {
            few
        } else if (value % 10 == 0 || value % 10 in 5..9 || value % 100 in 11..14) {
            many
        } else {
            other
        }


//         val lastDigit = value % 10;
//
//         return if (lastDigit == 0 || lastDigit in 5..20) {
//             form1
//         } else
//             if (lastDigit == 1 || lastDigit == 21) {
//                 form2
//             } else
//                 if (lastDigit == 2 || lastDigit == 3 || lastDigit == 4 || lastDigit == 22 || lastDigit == 23) {
//                     form3
//                 } else {
//                     throw RuntimeException()
//                 }

    }

}