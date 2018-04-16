package nyomio.data

import java.nio.ByteBuffer

/**
 * Cassandra create statement:
 */

data class TrafficLog(val sourceIp: String, val receiveTimestamp: Long, val data: ByteBuffer,
                      var deviceId: String?, var nativeId: String?, var timestampInData: Long?)