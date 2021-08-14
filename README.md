# Pay my buddy

A web application that allows customers to transfer money to manage their finances or pay their friends. This app uses
Java to run and stores the data in Mysql DB.

## Get started

These instructions will help you get a copy of the web application prototype on your local computer for version 1
development.

### Prerequisites

What things you need to install the software and how to install them 

- Java 11
- Maven 3.7.1
- Spring Boot 2.5.1
- Mysql 8.0.23

### Installing

A step by step series of examples that tell you how to get a development env running:

1.Install Java:

https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html

2.Install Maven:

https://maven.apache.org/install.html

3.Install MySql:

https://dev.mysql.com/downloads/mysql/

After downloading the mysql 8 installer and installing it, you will be asked to configure the password for the default `root` account.
This code uses the default root account to connect and the password can be set as `rootroot`. If you add another user/credentials make sure to change the same in the code base.

# Running App

Post installation of MySQL, Java and Maven, you will have to set up the tables and data in the database.
For this, please run the sql commands present in the `Data.sql` file located at src/main/resources/Data.sql.
For more security login credentials must be externalized, a folder containing the .jar and .properties files is accessible at src/main/resources/PayMyBuddyRun.
Move this folder out of the environment, using command prompt navigate to this folder then run the following command to start the application :
`java -jar paymybuddy.api-0.0.1-SNAPSHOT.jar`

## Physical data model

Here is the physical data model explaining the structure of the database tables.
![Alt text](https://i.postimg.cc/g03cwP0w/Mod-le-physique-de-donn-es.jpg "Physical data model")

