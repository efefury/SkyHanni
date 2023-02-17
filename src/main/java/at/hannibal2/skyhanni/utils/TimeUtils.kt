package at.hannibal2.skyhanni.utils

object TimeUtils {
    fun formatDuration(
        millis: Long,
        biggestUnit: TimeUnit = TimeUnit.YEAR,
        showMilliSeconds: Boolean = false,
        longName: Boolean = false
    ): String {
        var milliseconds = millis
        val map = mutableMapOf<TimeUnit, Int>()
        for (unit in TimeUnit.values()) {
            if (unit.ordinal >= biggestUnit.ordinal) {
                val factor = unit.factor
                map[unit] = (milliseconds / factor).toInt()
                milliseconds %= factor
            }
        }

        val builder = StringBuilder()
        for ((unit, value) in map.entries) {
            if (value > 0 || builder.isNotEmpty() || unit == TimeUnit.SECOND) {
                builder.append(value)
                val name = if (longName) {
                    " " + unit.longName + if (value > 1) "s" else ""
                } else {
                    unit.shortName
                }

                if (unit == TimeUnit.SECOND) {
                    if (showMilliSeconds) {
                        val formatMillis = milliseconds / 100
                        builder.append(".")
                        builder.append(formatMillis)
                    }
                    builder.append(name)
                } else {
                    builder.append("$name ")
                }
            }
        }
        return builder.toString()
    }
}

private const val FACTOR_SECONDS = 1000L
private const val FACTOR_MINUTES = FACTOR_SECONDS * 60
private const val FACTOR_HOURS = FACTOR_MINUTES * 60
private const val FACTOR_DAYS = FACTOR_HOURS * 24
private const val FACTOR_YEARS = (FACTOR_DAYS * 365.25).toLong()

enum class TimeUnit(val factor: Long, val shortName: String, val longName: String) {
    YEAR(FACTOR_YEARS, "y", "Year"),
    DAY(FACTOR_DAYS, "d", "Day"),
    HOUR(FACTOR_HOURS, "h", "Hour"),
    MINUTE(FACTOR_MINUTES, "m", "Minute"),
    SECOND(FACTOR_SECONDS, "s", "Second"),
    ;
}