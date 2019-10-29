package ie.gmit.ds;

import com.google.protobuf.ByteString;

import io.grpc.BindableService;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.StreamObserver;

public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase {

	public PasswordServiceImpl() {

	}

	// Must override bothe the hash method and the validate method
	// from the grpc class

	@Override
	public void hash(UserRequest userReq, StreamObserver<UserReply> streamObserver) {
		// using the auto generated UserRequest class,
		// use the getPassword method get a password then store it as an array
		char[] password = userReq.getPassword().toCharArray();

		// Use the getNextSalt method in Passwords to get and store a random password
		// salt
		byte[] salt = Passwords.getNextSalt();

		// Using the salt we can now hash the password.
		// Using salt ensures that no two hashed passwords are never the same,
		// Even if two passwords are the exact same
		byte[] hashedPassword = Passwords.hash(password, salt);

		// Get a response
		UserReply userRes = UserReply.newBuilder() // new build
				.setUserId(userReq.getUserId()) // set ID
				.setHashedPassword(ByteString.copyFrom(hashedPassword)) // set hashed password
				.setSalt(ByteString.copyFrom(salt)) // set random salt
				.build(); // build

		// Now we have to send the response to the client
		streamObserver.onNext(userRes);

		// And finish the request
		streamObserver.onCompleted();
	}

	@Override
	public void validate(ConfirmPasswordRequest userReq, StreamObserver<ConfirmPasswordReply> streamObserver) {
		// Pretty much the same as hash but will need to validate the password
		// Same variables
		char[] password = userReq.getPassword().toCharArray();
		byte[] salt = Passwords.getNextSalt();
		byte[] hashedPassword = Passwords.hash(password, salt);

		// Generate the response,
		ConfirmPasswordReply userRes = ConfirmPasswordReply.newBuilder()
				.setValidPassword(Passwords.isExpectedPassword(password, salt, hashedPassword)) // validate password
				.build();

		// send the res to the client
		streamObserver.onNext(userRes);
		
		// signal the request is finished
		streamObserver.onCompleted();
	}

}
