var utils = require('./utils');

exports.registerRoutes = function (app, contracts) {

    app.post('/api/loans', function (req, res) {
        var from = req.header("Public-Address");

        var identifier = req.body.identifier;
        if (!identifier) {
            res.status(400).send("'identifier' missing from body.");
            return;
        }
        contracts.loanRegistry().createLoan(identifier, from, function (error, data) {
            if (error) {
                res.status(500).send('Failed to create loan: ' + error.message);
            } else {
                var address = data[0];
                var result = data[1].toNumber();
                if (result != 0) {
                    res.status(500).send('Failed to create loan: Error code:' + result);
                    return;
                }
                res.status(200).send({loanAddress: address});
            }
        });

    });

    app.delete('/api/loans/:identifier', function (req, res) {
        var identifier = req.params.identifier;
        var from = req.header("Public-Address");
        contracts.loanRegistry().removeLoan(identifier, from, function (error, result) {
            utils.txResp(res, error, result, "Failed to remove loan");
        });
    });

    app.get('/api/loans', function (req, res) {
        console.log("getting loans from registry.");
        var from = req.header("Public-Address");
        contracts.loanRegistry().loans(from, function (error, loans) {
            if (error) {
                res.status(500).send('Failed to get loans: ' + error.message);
            } else {
                res.status(200).send({loans: loans});
            }
        });
    });

    app.get('/api/loans/:identifier', function (req, res) {
        console.log("getting loan from registry.");
        var identifier = req.params.identifier;
        var from = req.header("Public-Address");
        contracts.loanRegistry().loan(identifier, from, function (error, id, addr) {
            if (error) {
                res.status(500).send('Failed to get issuer: ' + error.message);
            } else {
                res.status(200).send({
                    loanId: id,
                    loanAddress: addr
                });
            }
        });
    });
};