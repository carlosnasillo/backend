var fs = require('fs-extra');
var dapp = require('./dapp.json');
var pubAddress = dapp.pub_address;
var pubKey = dapp.pub_key;
var privKey = dapp.priv_key;

var accountData = {address: pubAddress, pubKey: pubKey, privKey: privKey};
var contractsModule = require('./lib/contracts/contracts');

function deploy(callback) {
    var abi, compiled;
    try {
        abi = fs.readJsonSync('contracts/LoanRegistry.abi');
        compiled = fs.readFileSync('contracts/LoanRegistry.bin').toString();
    } catch (error) {
        callback(error);
        return;
    }
    contractsModule.contractsLib(accountData, function (error, contracts) {
        if (error) {
            throw error;
        }

        var lrFactory = contracts.newContractFactory(abi);

        lrFactory.new({data: compiled}, function (error, contract) {
            if (error) {
                callback(error);
                return;
            }
            // Update dapp with the loan registry contract address and write to dapp.json
            dapp.loan_registry_address = contract.address;
            var err;

            try {
                fs.writeJsonSync('dapp.json', dapp);
            } catch (error) {
                err = error;
            }
            contracts.erisDb().shutDown(function () {
                callback(err);
            });
        })

    });

}

deploy(function (error) {
    if (error) {
        throw error;
    }
    console.log("**** Deployed Loan Registry ****");
});