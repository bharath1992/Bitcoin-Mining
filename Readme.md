

COP 5612

Project – 1


Ankit Sharma (UFID: 24868901)

Bharath Kurumaddali (UFID: 6513-0561)
How to Run:
We have 2 different files for client and server. server file is named project1_server.scala and client is project1_client.scala. Now, the tar we have submitted has 2 folders, Final corresponds to Server and Final1 to Client. Make sure akka and scala libraries are the same version. To run the program change directory into bin of scala and copy configuration file from respective folders i.e to run Server copy application.conf from Final and to run server copy application file from Final1 into bin folder. Make sure bin has the application.conf file.  Then in the bin folder of Scala-2.11.2 do the following commands for server:
To Compile server:
./scalac -cp "<path of scala Libraries> /lib/*: <path of akka Libraries> /*:."<path of project>/Final/src/project1_server.scala
To run server:
./scala -cp "<path of scala Libraries> /lib/*: <path of akka Libraries> /*:." project1_server 4

4 is the no of zeroes any number can be given in its place.

In another machine compile and run the Client the application.conf here must be copied from Final1 folder. The application.conf files for server and client are slightly different.
To compile client:
./scalac -cp "<path of scala Libraries> /lib/*: <path of akka Libraries> /*:."<path of project>/Final1/src/project1_client.scala

To run the code:
./scala -cp "<path of scala Libraries> /lib/*: <path of akka Libraries> /*:." project1_client 192.168.1.3

PS: Enter the IP adress if running on the same machine also donot enter 127.0.0.1. 


1.The work size of 100,000 results in the great performance. Giving the smaller amount reduces the performance, though not considerably. Also the number of cores affects performance. We did some initial reading in different websites and found that a work size close to 150000 is not be crossed. Keeping that in mind, we had 2 systems connected through remote kept all other parameters constant and kept varying this work size to finally figure out that 100,000 had good performance, values around also had a decent performance, we used a stop watch to calculate an approximate running time.

2.Running the program to mine bitcoin for input value 4. Generated strings were appended to gatorlink id bharath92. It could find 8456 bitcoins in 5 mins. The results have been added as a textfile with this submission: op2.txt is the file containing results.

3.Running time for the above: Running time for k=5 : user time: 301 secs; system  time :37 secs;  CPU time = 2038.75 secs.  
CPUtime/realtime = 2038+37/301 = 6.89 are the cores used.

4.	For the range used in the program and allowing the program to run for a duration of 5 minutes on the test setup described earlier - the bitcoin with a maximum of 7 0s was found.                                                                                        bharath923fa5k9 : 0000000d997227caac4f0008a09ccea28d4a55fa0796151a4152729cf578f202 bharath92481t8z : 0000000bf15bfaff4a755db1a79eabe1b46d6d4195e243b3cf88d879f9fe0c43 bharath925bmygt : 00000006e5570281b45db2f8eebc87f4d3afdae4b0093180270b5728033c4585 bharath925rswyy : 00000009b2fa0747100462291c607dcb3af054bb0a7d31cd79c2f2dcb848860f

5.	Largest machine we ran code on was 3. Having 4, 4 and 8cores respectively.