# Distributed-Systems-Project

The purpose of this project is to gain practical experience in interprocess communication
protocols used in modern distributed systems. You’ll build a distributed user account
management system in two parts. Each part of the project will involve developing a
service which will be able to connect to other services via APIs. Each service will be
submitted separately. The two interconnected services you’ll develop are as follows:

• Part 1: gRPC Password service
• Part 2: RESTful User Account Service

Part1:
Develop a Password Service which will provide password hashing and
verification services. Your service will expose a gRPC API with two
methods:

• hash: Used to generate a hash of a user’s password. Takes a password as input,
returns the hash of the password, along with the salt used to generate the hash.
Includes userId on input and output because we will be calling the method asynchronously in Part 2 and will need the userId on the asynchronous response.
• validate: Used to validate a user-entered password by comparing it to the stored
hash. Takes a password, a hashed password and a salt as input. Uses the salt to
hash the input password and compares the resulting hash to the hashed password.
The userId and password input parameters to the hash and validate methods should
be of type integer and string respectively. You are free to determine the other types as
you see fit. Note that in Part 2 you’ll be storing the hashed password and salt returned
from the hash method. The hash method includes userId on input and output because
we will be calling the method asynchronously in Part 2 and will need the userId on the
asynchronous response.
