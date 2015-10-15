var utils = require('./utils');

function Loan(lr) {
    this._lr = lr;
}

Loan.prototype.identifier = function (aIdentifier, from, callback) {
    console.log("issue called");
    var identifier = utils.fromAscii(aIdentifier);
    this.loanContract(identifier, from, function (error, lc) {
        if (error) {
            callback(error);
            return;
        }
        lc.identifier({from: from}, function (error, result) {
            if (error) {
                callback(error);
                return;
            }
            callback(null, utils.toAscii(result));
        });
    });
};

Loan.prototype.loanContract = function (bondId, from, callback) {
    return this._lr.loanContract(bondId, from, callback);
};

exports.newInstance = function(lr){
    return new Loan(lr);
};