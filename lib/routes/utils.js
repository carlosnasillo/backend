// Standard response function.
function txResp(res, error, code, msg){
    if(error){
        res.status(500).send(msg + ": " + error.message);
    } else {
        if(code != 0){
            res.status(500).send(msg + ", error code: " + code);
        } else {
            res.status(200).send({});
        }
    }
}

exports.txResp = txResp;