var lrRoutes = require('./loan_registry_routes');
var loanRoutes = require('./loan_routes');

exports.addRoutes = function(app, contracts){
    lrRoutes.registerRoutes(app, contracts);
    loanRoutes.registerRoutes(app, contracts);
};