var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);

        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);

        });
        sendName();
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'password': $("#password").val(),'userID': $("#userID").val()}));

}

function showGreeting(message) {
    var messageCut =message.split(",");
    if (messageCut[1]=="m"){
        $("#greetings").append("<tr><td>Welcome, manager. You will be redirect in 2 sec.</td></tr>");
        setCookie(messageCut[0]);
        setTimeout(function(){ $(location).attr('href', "/manager")}, 2000);
    }else if (messageCut[1]=="e"){
        $("#greetings").append("<tr><td>Welcome, employee. You will be redirect in 2 sec.</td></tr>");
        setCookie(messageCut[0]);
        setTimeout(function(){ $(location).attr('href', "/employee")}, 2000);
    }else if (messageCut[1]=="w"){
        $("#greetings").append("<tr><td>Welcome, You will be redirect in 2 sec.</td></tr>");
        setTimeout(function(){ $(location).attr('href', "/index.html")}, 2000);
    }else{
        $("#greetings").append("<tr><td>" + messageCut[1] + " Please retry."+"</td></tr>");
    }

}

function setCookie(message) {
    var token ="token";
    var d = new Date();
    //set the expeire day to 1
    d.setTime(d.getTime() + (1*24*60*60*1000));
    var expires = "expires="+ d.toUTCString();
    document.cookie = token + "=" + message + ";" + expires + ";path=/";

}
$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});
