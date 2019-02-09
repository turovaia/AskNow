package grpcservice

import java.net.DatagramSocket
import java.net.InetAddress

fun main(args: Array<String>) {

    var ip = args.getOrElse(1) {""}

    if (ip.isBlank())
        DatagramSocket().use { socket ->
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002)
            ip = socket.localAddress.hostAddress
        }
    println("current ip $ip")

    val port = args.getOrNull(0)?.toIntOrNull() ?: 5004
    println("current port $port")

    Server.start(ip, port)

}