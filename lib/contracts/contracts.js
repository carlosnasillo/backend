var fs = require('fs-extra');
var erisC = require('eris-contracts');
// All that needs doing atm.
var dapp = require('../../dapp.json');
var erisdbURL = dapp.server_url;
var pubAddress = dapp.pub_address;
var pubKey = dapp.pub_key;
var privKey = dapp.priv_key;
var path = require('path');

var lrModule = require('./loan_registry');
var loanModule = require('./loan');

var accountData = {address: pubAddress, pubKey: pubKey, privKey: privKey};

function Contracts() {
    this._contractManager = null;
    this._loanRegistry = null;
    this._loan = null;
}

Contracts.prototype.contractManager = function () {
    return this._contractManager;
};

Contracts.prototype.loanRegistry = function () {
    return this._loanRegistry;
};

Contracts.prototype.loan = function () {
    return this._loan;
};

function contractsLib(accountData, callback) {
    try {
        var appData = fs.readJsonSync(path.join(__dirname, '../../dapp.json'));
        erisC.newContractManagerDev(appData.serverURL, accountData, callback);
    } catch(err) {
        return callback(err);
    }
}

exports.newInstance = function(callback){
    erisC.newContractManagerDev(erisdbURL, accountData, function(error, cm){
        if (error) {
            callback(error);
            return;
        }

        if (!dapp.loan_registry_address) {
            callback(new Error("No loan-registry has been deployed."));
            return;
        }

        var lrAbi, loanAbi;
        try {
            lrAbi = fs.readJsonSync('./contracts/LoanRegistry.abi');
            loanAbi = fs.readJsonSync('./contracts/Loan.abi');
        } catch (error) {
            callback(error);
            return;
        }

        var contracts = new Contracts();

        contracts._contractManager = cm;

        var lrFactory = cm.newContractFactory(lrAbi);
        var loanFactory = cm.newContractFactory(loanAbi);

        var lrContract = lrFactory.at(dapp.loan_registry_address);
        contracts._loanRegistry = lrModule.newInstance(lrContract, loanFactory);
        contracts._loan = loanModule.newInstance(contracts._loanRegistry);

        callback(null, contracts);

    });
};

exports.contractsLib = contractsLib;