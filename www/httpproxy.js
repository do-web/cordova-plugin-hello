/*global cordova, module*/

module.exports = {
    get: function (uri, host, port, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Httpproxy", "get", [uri, host, port]);
    }
};
