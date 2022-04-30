# 2022T2-CSCI3170-Project (Group 2)

### Member list
- Jason,   Chan Wing Kit,  1155142198
- Oscar,   Chau Tak Ho  ,  1155143501
- Keaton,  Kwok Chun Kiu,  1155141911

### How to run
1. Download the main branch of this repository
1. Send the ZIP file to CSE Linux1 server `scp CSCI3170-project-main.zip <username>@linux1.cse.cuhk.edu.hk`
1. Log in to the CSE Linux1 server `ssh <username>@linux1.cse.cuhk.edu.hk`
1. Unpack the ZIP file `unzip CSCI3170-project-main.zip`
1. Go to the unpacked directory `cd CSCI3170-project-main`
1. Execute the below commands to run the program (requires JavaSDK 11 or above, MySQL database server set up properly)
```
javac main.java admin/*.java query/*.java user/*.java manager/*.java menu/*.java

java -cp .:mysql-connector-java-5.1.47\(2\).jar main
```
