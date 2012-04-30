function message(value2add){
    var myspan=document.getElementById('area');
    var oldtext = myspan.innerHTML;
    myspan.innerHTML=value2add+"<br>"+oldtext;
}

function clearMessages(){
    document.getElementById('area').value="";
}

var socket;

function connect(hostname,node,capability){

    clearMessages();

    var host = "ws://"+hostname+"/readings.ws";

    var protocol = "SUB@"+node+"@"+capability;
    var encodedProtocol="";
    for (var i=0;i<protocol.length;i++){
        if (protocol[i]=="@"){
            encodedProtocol+=".";
        }
        else if(protocol[i]==":"){
            encodedProtocol+="-";
        }
        else{
            encodedProtocol+=protocol[i];
        }
    }
//    encodedProtocol=protocol;

    try{

    if(!("WebSocket" in window)){
         message("You have a browser that does not support Websockets!");
                 if(!("MozWebSocket" in window)){
                         message("You have a browser that does not support MozWebsockets!");
                         return -1;
                }


        else {
            socket = new MozWebSocket(host,encodedProtocol);

                message('You have a browser that supports MozWebSockets');
        }
    }
    else {
//        message("encodedProtocol="+encodedProtocol);
//        message("Protocol="+protocol);
        socket = new WebSocket(host,encodedProtocol);
        message('You have a browser that supports WebSockets');
    }
        socket.onopen = function(){
            message('socket.onopen ');
//            message('Socket Status: '+socket.readyState+' (open)'+"");
        }

        socket.onmessage = function(msg){
            if (!(msg.data instanceof Blob)) {
                message(msg.data);
            }
        }
       socket.onclose = function(){
            message("socket.onclose")
//            message('Socket Status: '+socket.readyState+' (Closed)'+"");
        }

    } catch(exception){
            message('Error'+exception+"");
    }




}//End connect

function send(text){
    if(text==""){
        message('Please enter a message'+"");
        return ;
    }
    try{
        socket.send(text);
        message('Sent: '+text+"")
    } catch(exception){
        message('Exception '+"");
    }
}

function disconnect(){
    socket.close();
    message("Connection Closed!");
}
