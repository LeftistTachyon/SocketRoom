# Client - Server Communication Protocol  
### About  
This is a document which keeps track of the communication protocol between the client and the server.  
The _command name_ or _header_ refers to the prefix of the command.  
The _body_ of the command refers to the information, if any, which immeadiately follow the header.  
  
## Command table  
|Command Name/Header|Direction       |Command Body        |Response                      |
|:-----------------:|:--------------:|:------------------:|:----------------------------:|
|`PING`             |Either          |(none)              |`PING`                        |
|`SUBMITNAME`       |Server to Client|(none)              |The name of the client is sent|
|`NAMEACCEPTED`     |Server to Client|(none)              |(none)                        |
|`NEWCLIENT`        |Server to Client|`[String name]`     |(none)                        |
|`IPREQUEST`        |Client to Server|`[String requested]`|The IP of the requested client|
