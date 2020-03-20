
var jrpc = new simple_jsonrpc();
var socket = new WebSocket("ws://localhost:24879/chat");

//wait of call


socket.onmessage = function (event) {
    console.info("onmessage: " + event.data);
    jrpc.messageHandler(event.data);
};

jrpc.toStream = function (_msg) {
    socket.send(_msg);
};

socket.onerror = function (error) {
    console.error("Error: " + error.message);
};

socket.onclose = function (event) {
    if (event.wasClean) {
        console.info('Connection close was clean');
    } else {
        console.error('Connection suddenly close');
    }
    console.info('close code : ' + event.code + ' reason: ' + event.reason);
};

socket.onopen = function () {
    setInterval(function () {
        refresh_content();
    }, 3000);
};

function refresh_content() {

    jrpc.call('player.currentlyPlaying').then(function (result) {
        if(typeof result.show === "undefined") {
            document.getElementById("spotify-image").src = "https://i.scdn.co/image/" + result.album.coverGroup.image[0].fileId.toLowerCase();
            document.getElementById("spotify-artist").innerText = result.artist[0].name;
            document.getElementById("spotify-title").innerText = result.name;
            //console.log(result.album.coverGroup.image[0].fileId.toLowerCase());
            console.log(result);
        } else {
            document.getElementById("head-artist").innerText = "Show:";
            document.getElementById("head-song").innerText = "Title:";
            document.getElementById("spotify-image").src = "https://i.scdn.co/image/" + result.coverImage.image[1].fileId.toLowerCase();
            document.getElementById("spotify-artist").innerText =  result.show.name;
            document.getElementById("spotify-title").innerText = result.name;
            //console.log(result.album.coverGroup.image[0].fileId.toLowerCase());
            console.log(result);
        }
    });

}

function song_play() {
    $("#play_pause_button").attr('onclick', 'song_pause()');
    $("#play_pause_image").removeClass("fa-play").addClass("fa-pause");
    jrpc.call('player.play');
}

function song_pause() {
    $("#play_pause_button").attr('onclick', 'song_play()');
    $("#play_pause_image").removeClass("fa-pause").addClass("fa-play");
    jrpc.call('player.pause');
}

function song_forward() {
    jrpc.call('player.next');
}

function song_backward() {
    jrpc.call('player.prev');
}