package rpetrov.senyavkaspeakingalarmclock.providers

/**
 * Created by Roman Petrov
 */


/**
 *
 * Время
 * Дата
 * Погода
 * Курс доллара
 * Событий на сегодня
 * Новые письма
 *
 *
 *
 */
interface IProvider {
    fun prepare(): Boolean
    fun getText(): String
}