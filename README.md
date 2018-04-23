Multi Chatting Program
====================
This program is a multi chatting program. You can sign in, sign up, send and receive message in chat room while you are online. Also you can receive message that you can't reveive beacause you are offline or busy. You can join in chat room with other users.
This program is written by Java.

# Documentation
Documentation can be found at [here](https://github.com/peter9378/chat_message/blob/master/ProjectDesignDocument.docx) The documentation covers details of program usage and design.

# Getting Started

## Requirement
- [jdk10](http://www.oracle.com/technetwork/java/javase/downloads/jdk10-downloads-4416644.html)
- [mysql-connector](https://dev.mysql.com/downloads/connector/j/5.1.html) (to debug)

## Test Enviornment
- OS : Windows7
- IDE : Intellij
- External JAR : MySQL Connector(mysql-connector-java-5.1.46-bin.jar)
> You should add external jars in your IDE to use JDBC driver.
- Port : 7777
- DB : MySQL(CentOS5 apm, http://210.89.188.165)

## Setting
This program is using TCP. So you need to open the port. Default port is 7777 now.
In local enviornment, you don't need to open the port. (But you need to change serverIP to 127.0.0.1)
> Please make sure install jdk10

## Server
` java -jar server.jar `

## Client
` java -jar client.jar `

## How to use
### Server
- after running server, you don't need to do anything.
- anyway, you can check chat room status in real-time at console UI.
- server will automatically send [NOTICE] message to users. [NOTICE] messages are not stored in database.

### Client
- Sign in
	- if you have an account, you can sign in immediately in console UI.
	- enter your id and password.
	- if password is incorrect, you have to enter the password again.
- Sign up
	- if you don't have an account, you can sign up in console UI.
	- all you need to enter is name, id, password. Your password will be encrypted(SHA-256) and stored in database.
	- if same id or name is already exist, you should use another id or name(notice which one is overlapped).
- in chat room
	- you can send message by pressing enter key or clicking the "send" button.
	- you can receive messages from other users.
	- you can check your status at the top of window(online/offline/busy).
	- you can exit the program by clicking the "exit" button.
	- there are special commands(/online, /offline, /busy, /userlist, /exit).
	- /online : your status will be changed to "online". Also you will receive messages that you can't reveive before when your status was "busy" or "offline".
	- /offline : your status will be changed to "offline". Now you can't send and receive messages, but you're still in the chat room.
	- /busy : your status will be changed to "busy". You can send message but can't receive messages.
	- /userlist : you can see the whole user's name and status.
	- /retrieve : you can retrieve all message history in database(MessageBox)
	- /exit : the program will be closed. You can exit the program without clicking the "exit" button.
	- all messages are stored in database with timestamp. So you can find out when other user has sent messages.

## Author
peter9378@naver.com

## License
MIT LICENSE