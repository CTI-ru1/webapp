
function message(value2add){
    var myspan=document.getElementById('area');
    var oldtext = myspan.value;
    myspan.innerHTML=value2add+"<br>"+oldtext;
}


  var Envelope = {
    1: {name: 'type',  type: 'string', rule: 'required'},
    2: {name: 'control',    type: 'int',  rule: 'optional'},
    3: {name: 'nodeReadings', type: 'string', rule: 'optional'},
    4: {name: 'linkReadings', type: 'string', rule: 'optional'},
  };
  function inspect(obj) {
    var ret = '';
    for (var key in obj) {
      ret += key + ':' + obj[key] + "\n";
    }
    return ret;
  }
  function testDecoder(file) {
    pbuf = new Protobuf.Decoder(Envelope).decode(file);
    message("done "+pbuf)
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
    encodedProtocol=protocol;

    try{

    if(!("WebSocket" in window)){
         message("You have a browser that does not support Websockets!");
                 if(!("MozWebSocket" in window)){
                         message("You have a browser that does not support MozWebsockets!");
                         return -1;
                }
        else {
            socket = new MozWebSocket(host,protocol);

                message('You have a browser that supports MozWebSockets');
        }
    }
    else {
        message(encodedProtocol);
        socket = new WebSocket(host,encodedProtocol);
        message('You have a browser that supports WebSockets');
    }
        socket.onopen = function(){
            message('socket.onopen ');
//            message('Socket Status: '+socket.readyState+' (open)'+"");
        }

        socket.onmessage = function(msg){
            message('Received: '+msg.data);
            testDecoder(msg.data);

        }

        socket.onclose = function(){
            message("socket.onclose")
//            message('Socket Status: '+socket.readyState+' (Closed)'+"");
        }

    } catch(exception){
            message('Error'+exception+"");
    }


    function send(){
        var text = $('#text').val();

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
        $('#text').val("");
    }


}//End connect

function disconnect(){
    socket.close();
    message("Connection Closed!");
}
