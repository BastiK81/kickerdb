const getResponseTextFromApi = async(url) => {
    const response = await fetch(url);
    const data = await response.text();
    return data;
} 

const getJsonFromApi = async(url) => {
    const response = await fetch(url);
    const data = await response.json();
    return data;
}

function loadPage(){
    setAllUsers()
    buildTable()
    setPlayer('/kickerPlayer/getAllActive')
}

function buildTable(){
    var table = document.getElementById('myTable')
    table.innerHTML = '';
    getJsonFromApi('/kickerGame/ranking')
        .then(data => {
            for (var i = 0; i < data.length; i++){
                var row = `<tr>
                    <td>${i + 1}</td>
                    <td>${data[i].userName}</td>
                    <td>${data[i].games}</td>
                    <td>${data[i].wins}</td>
                    <td>${data[i].scorePlus}</td>
                    <td>${data[i].scoreMinus}</td>
                        </tr>`
                table.innerHTML += row
            }
        });
}

function getForecast(url){
    setPlayer(url)
}

function setAllUsers(){
    const playerSelection = document.getElementById('playerInPlayerManagement')
    getResponseTextFromApi('/kickerPlayer/getAll')
        .then(data => {
            var dataToJson = JSON.parse(data)

                while (playerSelection.children.length > 0) {
                    playerSelection.removeChild(playerSelection.firstChild);
                } 
                for (var i = 0; i < dataToJson.length; i++) {
                    var opt = new Option(dataToJson[i].userName, i);
                    playerSelection.add(opt,null);
                }
                elements.selectedIndex = 0;
        });
}

function setPlayer(url) {
    const elements = document.getElementsByClassName('playerselect');
    getResponseTextFromApi(url)
        .then(data => {
            var dataToJson = JSON.parse(data)
            for (var k = 0;k < elements.length; k++) {
                while (elements[k].children.length > 0) {
                    elements[k].removeChild(elements[k].firstChild);
                } 
                for (var i = 0; i < dataToJson.length; i++) {
                    var opt = new Option(dataToJson[i].userName, i);
                    elements[k].add(opt,null);
                }
                elements[k].selectedIndex = k;
            }  
        });
}

function sendGame(){
    let scorewhite = parseInt(document.getElementById('scorewhite').value);
    let scoreblack = parseInt(document.getElementById('scoreblack').value);
    if (isNaN(scorewhite) || isNaN(scoreblack)){
        window.alert("Score is missing");
        return;
    }
    if (scorewhite > 6 || scoreblack > 6){
        window.alert("Score bigger 6")
    }
    var jsonString = '{"playerOne" : "' + getSelectionValue(document.getElementById('whitedefense')) + '","playerTwo" : "' + getSelectionValue(document.getElementById('whiteoffense')) + '","playerThree" : "' + getSelectionValue(document.getElementById('blackdefense')) + '","playerFour" : "' + getSelectionValue(document.getElementById('blackoffense')) + '","scoreOne" : ' + scorewhite + ',"scoreTwo" : ' + scoreblack + '}';
    fetch('/kickerGame/addGame', {
        method: "POST",
        body: jsonString,
        headers: {"Content-type": "text/plain; charset=UTF-8"}
        })
        .then(response => response.text()) 
        .then(restext => {
            document.getElementById('scorewhite').value = null
            document.getElementById('scoreblack').value = null
            loadPage()
        });
}

function getSelectionValue(selection){
    let selectedOption = parseInt(selection.value);
    return selection.options[selectedOption].text;
}

function addNewPlayer(){
    var jsonString = '{"userName":"' + document.getElementById("userNameAddUser").value + '"}'
    var url = '/kickerPlayer/addPlayer'
    sendJsonToApi(url, jsonString)
    document.getElementById("userNameAddUser").value = null
}

function deletePlayer(){
    var jsonString = '{"userName":"' + getSelectionValue(document.getElementById('playerInPlayerManagement')) + '"}'
    var url = '/kickerPlayer/delete'
    sendJsonToApi(url, jsonString)
}

function changePlayerActivity(){
    var jsonString = '{"userName":"' + getSelectionValue(document.getElementById('playerInPlayerManagement')) + '"}'
    var url = '/kickerPlayer/changeActivity'
    sendJsonToApi(url, jsonString)
}

function sendJsonToApi(url, jsonString){
    fetch(url, {
        method: "POST",
        body: jsonString,
        headers: {'Content-Type': 'application/json'}
        })
        .then(response => response.text()) 
        .then(restext => {
            window.alert(restext)
            loadPage()
        });
}