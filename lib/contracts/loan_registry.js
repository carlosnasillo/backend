var utils = require('./utils');

function LoanRegistry(lrContract, loanFactory){
    this._lr = lrContract;
    this._lf = loanFactory;
    this._loanCache = {};
}

LoanRegistry.prototype.createLoan = function(aIdentifier, from, callback){
    var identifier = utils.fromAscii(aIdentifier);
    this._lr.createLoan(identifier, {from: from}, callback);
};

LoanRegistry.prototype.removeLoan = function(aIdentifier, from, callback){
    var identifier = utils.fromAscii(aIdentifier);
    this._lr.removeLoan(identifier, {from: from}, callback);
};

LoanRegistry.prototype.loan = function(aIdentifier, from, callback){
    var identifier = utils.fromAscii(aIdentifier);
    this._lr.loan(identifier, {from: from}, function(error, data){
        if (error) {
            callback(error);
            return;
        }
        callback(null, utils.toAscii(data[0]), data[1]);
    });
};

LoanRegistry.prototype.loans = function(from, callback){
    var loans = [];
    var that = this;
    this._lr.numLoans({from: from}, function (error, data) {
        if (error) {
            callback(error);
            return;
        }

        var numLoans = data.toNumber();
        console.log("Number of loans: " + numLoans);
        if (numLoans === 0) {
            callback(null, loans);
            return;
        }

        var ctr = 0;

        for (var i = 0; i < numLoans; i++) {
            that._lr.loanFromIndex(i, {from: from}, function (error, data) {
                if (error) {
                    callback(error);
                    return;
                }
                // id, address
                add(utils.toAscii(data[0]), data[1]);
            });
        }

        function add(id, addr) {
            loans.push({loanId: id, loanAddress: addr});
            if (++ctr === numLoans) {
                callback(null, loans);
            }
        }

    });
};

LoanRegistry.prototype.loanContract = function (identifier, from, callback) {
    var that = this;
    this._lr.loanAddress(identifier, {from: from}, function (error, address) {
        if (error) {
            callback(error);
            return;
        }
        var loanC = that._loanCache[address];
        if (!loanC) {
            loanC = that._lf.at(address);
            that._loanCache[address] = loanC;
        }
        callback(null, loanC);
    });
};

exports.newInstance = function(lrContract, loanFact){
    return new LoanRegistry(lrContract, loanFact);
};