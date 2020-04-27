window.onload = function () {
    refresh_content();
    setInterval(function () {
        refresh_content();
    }, 5000);
};

document.onkeydown = checkKey;

function checkKey(e) {

    e = e || window.event;

    if (e.keyCode == '38') {
        // up arrow
        song_volumeup()
    }
    else if (e.keyCode == '40') {
        // down arrow
        song_volumedown()
    }
    else if (e.keyCode == '37') {
        // left arrow
        song_backward()
    }
    else if (e.keyCode == '39') {
        // right arrow
        song_forward()
    }

}

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
    console.log("RESPONSE: " + response.status);
    if (response.status === 200) {
        return await response.json(); // parses JSON response into native JavaScript objects
    } else {
        return null;
    }
}

async function getData(url = '', data = {}) {
    // Default options are marked with *
    const response = await fetch(url, {
        method: 'GET', // *GET, POST, PUT, DELETE, etc.
        cache: 'default', // *default, no-cache, reload, force-cache, only-if-cached
        mode: 'cors',
        headers: {
            'Content-Type': 'application/json',
        }
    }).catch(error => {
        console.error(error)
    });

    console.log("RESPONSE: " + response);
    if(response === undefined) return response;
    if (response.status === 200) {
        return await response.json(); // parses JSON response into native JavaScript objects
    } else {
        return undefined;
    }
}

function refresh_content() {
    getData("player/current", "").then(result => {
        console.log("TEST"+result);
        if (result === undefined) {
            document.getElementById("spotify-image").src = "images/placeholder_1.png";
            document.getElementById("spotify-artist").innerText = "Not Connected";
            document.getElementById("spotify-title").innerText = "Not Connected";
            document.getElementById("spotify-not-connected").hidden = false
        } else {
            document.getElementById("spotify-image").src = "https://i.scdn.co/image/" + result.image.key;
            document.getElementById("spotify-artist").innerText = result.track.artist[0].name;
            document.getElementById("spotify-title").innerText = result.track.name;
            document.getElementById("spotify-not-connected").hidden = true
        }
    }).catch(error => {
        console.error(error)
    });
}

function song_play() {
    $("#play_pause_button").attr('onclick', 'song_pause()');
    $("#play_pause_image").removeClass("fa-play").addClass("fa-pause");
    getData("player/resume", "");
}

function song_pause() {
    $("#play_pause_button").attr('onclick', 'song_play()');
    $("#play_pause_image").removeClass("fa-pause").addClass("fa-play");
    getData("player/pause", "");
}

function song_forward() {
    getData("player/next", "");
}

function song_backward() {
    getData("player/previous", "");
}

function song_volumeup() {
    getData("player/volume/up", "");
}

function song_volumedown() {
    getData("player/volume/down", "");
}