package grpcservice

import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress

object Server {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun start(ip: String, port: Int) {

        val server = NettyServerBuilder.forAddress(InetSocketAddress(ip, port)).addService(Parser).build().start()
        Runtime.getRuntime().addShutdownHook(Thread {
            server.shutdown()
        })
        logger.info("Parser Server running on ip {} port {}", ip, port)
        server.awaitTermination()
    }
}