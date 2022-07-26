import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.ServerSocket
import java.util.*

fun main() {

    //New Thread SUS
    Thread {
        var serverSocket: ServerSocket? = null
        try {
            serverSocket = ServerSocket(8912)
            val socket = serverSocket.accept()
            var input: DataInputStream? = null
            var output: DataOutputStream? = null
            try {
                input = DataInputStream(socket.getInputStream())
                output = DataOutputStream(socket.getOutputStream())
            } catch (e: IOException) {
                e.printStackTrace()
            }
            while (!serverSocket.isClosed) {
                val received = Objects.requireNonNull(input)!!.readUTF()
                if (received == "FuckYou") Objects.requireNonNull(output)!!
                    .writeUTF("Super§super")
            }
        } catch (e: IOException) {
            try {
                serverSocket!!.close()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
    }.start()
}