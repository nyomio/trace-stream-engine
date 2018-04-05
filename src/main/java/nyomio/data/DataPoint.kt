package nyomio.data

/**
 * The base domain class for storing and querying data points associated with 4D Message (time, lat, lng, altitude)
 */

data class DataPoint(
        val timestamp: Long,
        val where: Where?,
        val gnssData: GnssData?,
        val networkData: NetworkData?,
        val deviceData: DeviceData?,
        val canData: CanData?,
        val keyValueList: List<KeyValue>
        )

data class Where(
        val lat: Double,
        val lng: Double,
        val altitudeMeters: Double)

data class GnssData(
        val satNumInFix: Int,
        val accuracy_m: Float)

data class NetworkData(
        val mcc: Short,
        val mnc: Short,
        val lac: Int,
        val cellId: Int,
        val signalStrengthPercent: Float)

data class DeviceData(
        val externalPower_mV: Float,
        val battery_mV: Float
)

data class CanData(
        val fuelLevelPercent: Double,
        val odometer: Long
)

data class KeyValue(
        val key: String,
        val value: String
)