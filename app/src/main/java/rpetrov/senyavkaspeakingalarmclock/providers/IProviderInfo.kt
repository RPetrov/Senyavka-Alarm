package rpetrov.senyavkaspeakingalarmclock.providers

/**
 * Created by Roman Petrov
 */
interface IProviderInfo {
    fun getPermissions(): Array<String>
    fun getName(): String
    fun getDescription(): String
    fun isConfigurable(): Boolean
    fun getConfigLayout(): Int

    fun isEnable(): Boolean
    fun enable(boolean: Boolean)
}