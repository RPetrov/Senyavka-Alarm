package rpetrov.senyavkaspeakingalarmclock.providers

/**
 * Created by Roman Petrov
 */


/**
 *
 */
interface IProvider {
    fun prepare(): Boolean
    fun getText(): String
    fun getPermissions(): Array<String>
}