
function transformResponseToJson(response) {
    if(!response.ok) {
      throw Error(response.statusText);
    }
    if (typeof response !== null && typeof response !== undefined) {
        return response.json();
    }
    return null;
}
function handleErrors(err) {
     $('#results').empty();
     $('#numTrains').text('');
     $('#eta').text('');
     $('#currTime').text('');
     console.log(err);
}

function updatePage(station) {
    fetch('/trainSchedule?stn=' + station).then((response) => transformResponseToJson(response))
    .then((scheduleJSON) => {
        if (scheduleJSON !== null) {

            $('#results').empty();
            var platform = "";
            for (var key in scheduleJSON) {
                var train = scheduleJSON[key].abbreviation;
                var color = scheduleJSON[key].color;
                var hexColor = scheduleJSON[key].hexColor;
                var minutes = scheduleJSON[key].minutes;
                var plat = scheduleJSON[key].platform;
                if(platform!=plat) {
                  platform=plat;
                  $('#results').append("<h3>Platform "+plat+"</h3>");
                }
                $('#results').append("<ul class='platform-list'><li><span class='train'>" + train + "</span><span style='background-color:"+ hexColor +
                 "' class='"+ color + " route-spacer'></span><span class='minutes'>"+minutes+ " min</span></li></ul>");
            }
        } else {
            handleErrors('No Trains!');
        }
    }).then(() => {
        return fetch('/trainCount').then((response) => transformResponseToJson(response));
    }).then((trainCountReponseJSON) => {
        $('#numTrains').text(trainCountReponseJSON + " trains currently in service.");
    }).then(() => {
        return fetch('/westOaklandEta?stn=' + station);
    }).then((response) => transformResponseToJson(response)).then((etaReponseJSON) => {
        if (etaReponseJSON !== null) {
            $('#eta').text("Estimated time to West Oakland: " + etaReponseJSON.etaDate + ' ' + etaReponseJSON.etaTime);
            $('#currTime').text("Real Time Departures " + etaReponseJSON.currentTime);
        } else {
            handleErrors('No Trains to West Oakland!');
        }
    })
    .catch((err) => {
        handleErrors(err)
    });
}

$(document).ready(function() {

    var runButtonInterval = false;

    $('#submit').click(function() {
    	var station = $("#stations").val();
    	updatePage(station);
        if (!runButtonInterval) {
            setInterval(function() {
                updatePage(station);
            }, 60000);
            runButtonInterval = true;
        }

    });


});
