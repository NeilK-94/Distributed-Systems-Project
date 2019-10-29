package ie.gmit.ds;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.logging.Logger;

public class GRPCServer {

    private Server server;
    private static final Logger logger = Logger.getLogger(GRPCServer.class.getName());


    private void start() throws IOException {
    	int port = 50051;
    	server = ServerBuilder.forPort(port).addService(new PasswordServiceImpl()).build().start();
		logger.info("Server started on port: " + server.getPort());
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }


    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
		final GRPCServer server = new GRPCServer();
		
		server.start();
		server.blockUntilShutdown();
		
	}
}