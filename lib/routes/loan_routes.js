var utils = require('./utils');

exports.registerRoutes = function (app, contracts) {

    app.get('/api/loans/:identifier/identifier', function (req, res) {
        console.log("getting loan from registry.");
        var identifier = req.params.identifier;
        var from = req.header("Public-Address");
        contracts.loan().identifier(identifier, from, function (error, id) {
            if (error) {
                res.status(500).send('Failed to get issuer: ' + error.message);
            } else {
                res.status(200).send({
                    identifier: id
                });
            }
        });
    });
};