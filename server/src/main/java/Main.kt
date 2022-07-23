import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.FileInputStream
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException

var CLIENT_PATH = ""

fun main() {
    CLIENT_PATH = System.getProperty("user.dir") + "/Hanabi.jar"
    println("Located: $CLIENT_PATH")
    println("Initializing...")
    val port = 37254 //你的驗證伺服器端口
    val tcpServer = ServerSocket(port)

    DBHelper.init("101.43.166.241", "3306", "hanabi", "hanabi", "w2Zh7mKJL2YywwAw")

    println("Waiting for connection...")
    while (!tcpServer.isClosed) {
        val clientSocket: Socket = tcpServer.accept()
        val client = Client(clientSocket)
        client.start()
    }
}

class Client(private val clientSocket: Socket) : Thread() {

    override fun run() {
        val input = DataInputStream(clientSocket.getInputStream())
        val output = DataOutputStream(clientSocket.getOutputStream())
        while (true) {
            if (clientSocket.isClosed) break
            val received: String = try {
                input.readUTF()
            } catch (e: Exception) {
                return
            }
            clientSocket.inetAddress.hostAddress ?: return
            val ip = clientSocket.inetAddress.hostAddress.toString()

            if (verify(received)) {
                output.writeUTF("U2FsdGVkX19mdvTKKe9cnW3d881zwWCJea5qVu60d9zcbiQruJL1L46MFZoljN0r6i4UtYE84l+gegxkqhN/fOZLeov95hENaMBVEPbVyCo=")
                println("Sending file to $ip...")
                sendFile(clientSocket, output, System.nanoTime())
            }
        }
    }

    private fun verify(received: String): Boolean {
        var username = received.split("§")[0]
        var password = received.split("§")[1]
        var hwid = received.split("§")[2]
        println("User $username($password) try to login - $hwid")
        var message = DBHelper.login(username, MD5Utils.getMD5(password), hwid)
        println(message)
        return message.length < 14
    }


    private fun sendFile(socket: Socket, output: DataOutputStream, startTime: Long) {
        //每次都讀取一下端文件, 如果你不想每次都讀取可以搞個Field存到内存
        val fileStream = DataInputStream(BufferedInputStream(FileInputStream(CLIENT_PATH)))
        val bufferSize = 8192
        val buf = ByteArray(bufferSize)
        try {
            while (true) {
                val read: Int = fileStream.read(buf)
                if (read == -1) {
                    break
                }
                output.write(buf, 0, read)
            }
            output.flush()
            fileStream.close()
            socket.close()
            println(
                "Finish sending file in " + String.format(
                    "%.3f",
                    (System.nanoTime() - startTime) / 1E9
                ) + " seconds"
            )
        } catch (e: SocketException) {
            println("Connection reset, client disconnected")
        }
    }
}