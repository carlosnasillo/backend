var express = require('express');
var bodyParser = require('body-parser');
var contractsModule = require('./lib/contracts/contracts');
var routes = require('./lib/routes/routes');

exports.start = function(callback) {
    contractsModule.newInstance(function(error, contracts){
        var app = express();
        app.use(bodyParser.json());
        routes.addRoutes(app, contracts);
        callback(null, app);
    });
};