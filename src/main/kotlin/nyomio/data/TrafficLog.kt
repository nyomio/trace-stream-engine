package nyomio.data

/**
 * Cassandra create statement:
 */

data class TrafficLog(val sourceIp: String, val receiveTimestamp: Long, val data: ByteArray,
                      var deviceId: String?, var nativeId: String?, var timestampInData: Long?)