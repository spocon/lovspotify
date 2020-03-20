window.onload = function () {
    setInterval(function () {
        refresh_content();
    }, 5000);
};

async function postData(url = '', data = {}) {
    // Default options are marked with *
    const response = await fetch(url, {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
        mode: 'cors',
        headers: {
            'Content-Type': 'application/json',
        }
    });
    console.log("RESPONSE: "+ response.status);
    if(response.status === 200) {
        return await response.json(); // parses JSON response into native JavaScript objects
    } else {
        return null;
    }
}

async function getData(url = '', data = {}) {
    // Default options are marked with *
    const response = await fetch(url, {
        method: 'GET', // *GET, POST, PUT, DELETE, etc.
        cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
        mode: 'cors',
        headers: {
            'Content-Type': 'application/json',
        }
    });
    console.log("RESPONSE: "+ response.status);
    if(response.status === 200) {
        return await response.json(); // parses JSON response into native JavaScript objects
    } else {
        return null;
    }
}

function refresh_content() {
    getData("http://localhost:8080/player/current", "").then(result => {
        console.log(result);
        // if (typeof result.show === "undefined") {
        document.getElementById("spotify-image").src = "https://i.scdn.co/image/" + result.track.album.coverGroup.image[0].fileId.toLowerCase();
        document.getElementById("spotify-artist").innerText = result.track.artist[0].name;
        document.getElementById("spotify-title").innerText = result.track.name;
    })



        /*} else {
            document.getElementById("head-artist").innerText = "Show:";
            document.getElementById("head-song").innerText = "Title:";
            document.getElementById("spotify-image").src = "https://i.scdn.co/image/" + result.track.coverImage.image[1].fileId.toLowerCase();
            document.getElementById("spotify-artist").innerText = result.track.show.name;
            document.getElementById("spotify-title").innerText = result.track.name;
            //console.log(result.album.coverGroup.image[0].fileId.toLowerCase());
            console.log(result);
        }*/



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