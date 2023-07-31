package io.github.nscuro;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusIntegrationTest
public class HelloGrpcServiceIT {

    private static ManagedChannel channel;

    @BeforeAll
    static void beforeAll() {
        int port = ConfigProvider.getConfig().getValue("quarkus.http.test-port", Integer.class);
        channel = ManagedChannelBuilder.forAddress("localhost", port)
                .usePlaintext()
                .build();
    }

    @AfterAll
    static void afterAll() {
        if (channel != null) {
            channel.shutdownNow();
            channel = null;
        }
    }

    Channel channel() {
        return channel;
    }

    @Test
    public void testHello() {
        HelloGrpcGrpc.HelloGrpcBlockingStub stub = HelloGrpcGrpc.newBlockingStub(channel());
        HelloReply reply = stub
                .sayHello(HelloRequest.newBuilder().setName("Neo").build());
        assertEquals("Hello Neo!", reply.getMessage());
    }

}
