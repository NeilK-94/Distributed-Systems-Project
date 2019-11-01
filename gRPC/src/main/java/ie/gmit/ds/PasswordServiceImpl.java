package ie.gmit.ds;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;

public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase
{
	
	public PasswordServiceImpl() {
		
	}
	@Override
	public void hash(UserRequest req, StreamObserver<UserResponse> resObserver)
	{
		// Creating the variables that will be passed with the gRPC request
		// Generating a random password
		char[] password = req.getPassword().toCharArray();	
		// using method from 'Passwords' class to get a salt for the hash method
		byte[] salt = Passwords.getNextSalt();
		// Creating a hashed version of the password using the salt for security
		byte[] hashedPassword = Passwords.hash(password, salt);
		
		// Generate the response
		UserResponse res =UserResponse.newBuilder() 
				.setUserID(req.getUserID())
				.setHashedPassword(ByteString.copyFrom(hashedPassword))
                .setSalt(ByteString.copyFrom(salt))
                .build();
		// send the res to the client
		resObserver.onNext(res);
		// signal the request is  finished
		resObserver.onCompleted();
	}// hash
	
	@Override
	public void validate(ConfirmPasswordRequest confirmReq, StreamObserver<BoolValue> resObserver)
	{
		try
		{
		// Variables are the same as above methods
		char[] password = confirmReq.getPassword().toCharArray();
		byte[] salt = Passwords.getNextSalt();
		byte[] hashedPassword = Passwords.hash(password, salt);

		if (Passwords.isExpectedPassword(password, salt, hashedPassword))
			resObserver.onNext(BoolValue.newBuilder().setValue(true).build());
		else
    	  	resObserver.onNext(BoolValue.newBuilder().setValue(false).build()); 
		}
		catch (RuntimeException ex)
		{
			resObserver.onNext(BoolValue.newBuilder().setValue(false).build());
		}
		
		// signal the request is  finished
		//	resObserver.onCompleted();
	}
}