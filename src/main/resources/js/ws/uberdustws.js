function message(value2add){
    var myspan=document.getElementById('area');
    var oldtext = myspan.innerHTML;
    myspan.innerHTML=value2add+"<br>"+oldtext;
}

function clearMessages(){
    document.getElementById('area').value="";
}

var t=setTimeout("ping()",30000);
var sockets=new Array();
var sock_count=0;

function connect(hostname,node,capability){
	var mysock_id=sock_count;
	sock_count++;

    clearMessages();

    var host = "ws://"+hostname+"/readings.ws";

    var encodedProtocol=protocol.replace(/@/g,".").replace(/:/g,"--");

    try{

    if(!("WebSocket" in window)){
         message("You have a browser that does not support Websockets!");
                 if(!("MozWebSocket" in window)){
                         message("You have a browser that does not support MozWebsockets!");
                         return -1;
                }


        else {
            sockets[mysock_id] = new MozWebSocket(host,encodedProtocol);

                message('You have a browser that supports MozWebSockets');
        }
    }
    else {
//        message("encodedProtocol="+encodedProtocol);
//        message("Protocol="+protocol);
        sockets[mysock_id] = new WebSocket(host,encodedProtocol);
        message('You have a browser that supports WebSockets');
    }
        sockets[mysock_id].onopen = function(){
            message('socket.onopen ');
//            message('Socket Status: '+socket.readyState+' (open)'+"");
        }

        sockets[mysock_id].onmessage = function(msg){
            if (!(msg.data instanceof Blob)) {
                if (msg.data!="pong"){
                    message(msg.data);
                }
            }
        }
       sockets[mysock_id].onclose = function(){
            message("socket.onclose")
//            message('Socket Status: '+socket.readyState+' (Closed)'+"");
        }


    } catch(exception){
            message('Error'+exception+"");
    }




}//End connect

function ping()
{
	var i=0;
	for (i=0;i<sock_count;i++){
		send("ping",i);
	}
	var t=setTimeout("ping()",30000);
}

function send(text,sock_id){
    if(text==""){
        message('Please enter a message'+"");
        return ;
    }
    try{
        sockets[sock_id].send(text);
//        message('Sent: '+text+"")
    } catch(exception){
        message('Exception '+"");
    }
}

function disconnect(sock_id){
    sockets[sock_id].close();
    message("Connection Closed!");
}
