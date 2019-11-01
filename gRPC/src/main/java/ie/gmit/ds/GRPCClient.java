package ie.gmit.ds;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class GRPCClient 
{
	// Provides channel lifecycle management
	private final ManagedChannel channel;
	
	// Use generated grpc method for communication between client and server
	private final PasswordServiceGrpc.PasswordServiceBlockingStub syncPasswordStub;	// blocking/synchronous stub waits for the server to respond
	private final PasswordServiceGrpc.PasswordServiceStub asyncPasswordStub; // non-blocking/asynchronous stub, where the response is returned asynchronously. 
	
	private static final Logger logger = Logger.getLogger(GRPCClient.class.getName());
	private Scanner scanner = new Scanner(System.in);
	
	private String password;
	private ByteString hashedPassword;	//	Return type of getHashedPassword and getSalt is a ByteString
	private ByteString salt;
	private int userID;
	
	private static final String LOCAL_HOST = "localhost";
	private static final int PORT = 50550;
	private static final int WAIT_TIME = 5;
	
	UserRequest userRequest;
	UserResponse userResponse;
	

	public GRPCClient(String host, int port)
	{
        channel = ManagedChannelBuilder	//	The channel we want to connect to with specified address and port
                .forAddress(host, port) 
                .usePlaintext()
                .build();
        
        //	Use the channel to create our stubs using the generated methods from the PasswordServiceGrpc class
        syncPasswordStub = PasswordServiceGrpc.newBlockingStub(channel);
        asyncPasswordStub = PasswordServiceGrpc.newStub(channel);
    }
	
	// Terminate client after the allocated listening time is up
    public void shutdown() throws InterruptedException
    {
        channel.shutdown().awaitTermination(WAIT_TIME, TimeUnit.SECONDS);
    }
    
    //	synchronous stubs make blocking rpc calls where the RPC call waits for the server to respond
    public void userRequest() 
    {
    	System.out.println("Please enter a user ID: ");
		userID = scanner.nextInt();

		System.out.println("Please enter a password: ");
		password = scanner.next();
		
		userRequest = UserRequest.newBuilder()	// Pass in user input to generated methods 
								 .setUserID(userID)
								 .setPassword(password)
								 .build();
		
		userResponse = UserResponse.newBuilder()
								   .getDefaultInstanceForType();
		
		userResponse = syncPasswordStub.hash(userRequest);

		hashedPassword = userResponse.getHashedPassword();
		salt = userResponse.getSalt();
	
    }
    //	Asynchronous stubs make non-blocking rpc calls where the response is returned asynchronously via a StreamObserver
    public void confirmPasswordRequest() {
		StreamObserver<BoolValue> responseObserver = new StreamObserver<BoolValue>() {	//	Used for sending orreceiving stream messages
			
			//	StreamObserver methods
			public void onNext(BoolValue boolValue) {	//	Receives value from stream
				if (boolValue.getValue()) {
					logger.info("Entered password is correct");
				} else {
					logger.info("Entered password is incorrect");
				}
			}
			
			@Override
			public void onError(Throwable throwable) {
				logger.info(throwable.getLocalizedMessage());
			}

			@Override
			public void onCompleted() {
				logger.info("Completed");
			}
		};

			asyncPasswordStub.validate(ConfirmPasswordRequest.newBuilder()
															 .setPassword(password)
															 .setSalt(salt)
															 .setHashedPassword(hashedPassword)
															 .build(), responseObserver);	
	}
    
    public static void main(String[] args) throws InterruptedException
    {
    	GRPCClient grpcClient = new GRPCClient(LOCAL_HOST, PORT);

			grpcClient.userRequest();
			grpcClient.confirmPasswordRequest();

			Thread.currentThread().join();	//	Prevents main from exiting
    	
    }
}