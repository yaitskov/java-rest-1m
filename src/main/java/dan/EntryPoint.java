package dan;

import static java.nio.ByteBuffer.allocateDirect;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketOptions;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.StandardOpenOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(EntryPoint.class);
    public static void main(String[] args) throws Exception {
        logger.info("Listen port 8801.");
        ByteBuffer input = allocateDirect(8192);
        ByteBuffer response = allocateDirect(8192);
        response.put("HTTP/1.1 204 OK\r\n".getBytes())
                .put("\r\n".getBytes());
        response.flip();
        response.mark();
        try (ServerSocketChannel ssc = ServerSocketChannel.open()) {
            ssc.bind(new InetSocketAddress(8801));
            while (true) {
                try (SocketChannel sc = ssc.accept()) {
                    while (sc.read(input) == input.capacity()) {
                        input.clear();
                    }
                    crunchNumbers();
                    while (response.hasRemaining()) {
                        sc.write(response);
                    }
                } catch (IOException e) {
                    logger.error("Connection", e);
                }
                input.clear();
                response.reset();
            }
        }
    }

    private static void crunchNumbers() {
        for (int i = 1; i < 1000000; ++i);
    }
}
