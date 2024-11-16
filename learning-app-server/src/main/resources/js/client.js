

function doRequest(endpoint, type, handler, content) {
    const BASE_URL = 'http://localhost:8080/entries/';
    const request = new XMLHttpRequest();
    const url = BASE_URL + endpoint;
    request.open(type, url, true);
    request.setRequestHeader('Content-Type', 'application/json');
    request.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            handler(request.responseText);
        }
    }
    request.send(JSON.stringify(content));
}

function getRequest(endpoint, handler) {
    doRequest(endpoint, 'GET', handler, '');
}

function postRequest(endpoint, handler, content) {
    doRequest(endpoint, 'POST', handler, content);
}

function log(content) {
    console.log("LOG: ", content);
}

function testGetAll() {
    getRequest('', log);
}

function testGetOne() {
    getRequest('1', log);
}

function testPost() {
    const entry = {
        'subject': 'test-subject',
        'date': '2024-11-16',
        'time': '08:00',
        'learned': 'Today I learned about ABC. ',
        'todo': 'Next step is to learn DEF. '
    };
    postRequest('', log, entry);
}
