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
    fun getName(): String
    fun getDescription(): String
    fun isConfigurable(): Boolean
}