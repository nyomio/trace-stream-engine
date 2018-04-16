package nyomio.devicemock


import java.io.File
import java.io.InputStream
import java.net.Socket


fun main(args: Array<String>) {
    val inputStream: InputStream = File("simpleclinet_recv_data.dump").inputStream()

    val socket = Socket("127.0.0.1", 8500)
    val sos = socket.getOutputStream();

    inputStream.bufferedReader().useLines {
        lines -> lines.forEach {
            Thread.sleep(100)
            sos.write(hexToByteArray(it))
        }
    }

    socket.close()
}

fun hexToByteArray(hexStr : String) : ByteArray {
    val array = ByteArray((hexStr.length / 2) + 1)
    array[0] = 48;
    for (i in 0 until hexStr.length step 2) {
        val hex = hexStr.substring(i , i+2)
        val subInt = Integer.valueOf(hex, 16)
        array[i / 2 + 1] = subInt.toByte()

    }
    return array;
}


class DeviceMock {

}