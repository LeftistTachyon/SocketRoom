# Client - Server Communication Protocol  
### About  
This is a document which keeps track of the communication protocol between the client and the server.  
The _command name_ or _header_ refers to the prefix of the command.  
The _body_ of the command refers to the information, if any, which immeadiately follow the header.  
  
## Command table  
|Command Name/Header|Direction       |Command Body         |Response                                |
|:-----------------:|:--------------:|:-------------------:|:--------------------------------------:|
|`PING`             |Either          |(none)               |`PING`                                  |
|`SUBMITNAME`       |Server to Client|(none)               |The name of the client is sent          |
|`NAMEACCEPTED`     |Server to Client|(none)               |(none)                                  |
|`NEWCLIENT`        |Server to Client|`[String name]`      |(none)                                  |
|`CHALLENGE`        |Client to Server|`[String requested]` |None, but sends a challenge request     |
|`CHALLENGE`        |Server to Client|`[String challenger]`|Whether the client accepts the challenge|
|`CONNECT`          |Server to Client|`[String IP]`        |The IP that the client should connect to|