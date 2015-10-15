var erisC = require('eris-contracts');
var BigNumber = require('bignumber.js');

var SCALE = new BigNumber(2).pow(new BigNumber(64));

// var SECONDS_PER_DAY = 86400;

// ******************* internal utilities ********************

function fromAscii(str) {
    return erisC.utils.asciiToHex(str, 32);
}

function toAscii(str) {
    if (str.length < 2) {
        return "";
    }
    if (str.slice(0, 2) == "0x") {
        str = str.slice(2);
    }
    if (str.length % 2 != 0) {
        str = str + "0";
    }
    for (var i = 0; i < str.length / 2; i++) {
        var index = 2 * i;
        if (str[index] == "0" && str[index + 1] == "0") {
            str = str.slice(0, index);
            break;
        }
    }
    return erisC.utils.hexToAscii(str);
}

function bnToReal(bn) {
    return bn.mul(SCALE);
}

function numToReal(num) {
    return new BigNumber(num).mul(SCALE);
}

function realToBn(rl) {
    return rl.div(SCALE);
}

function tsToDate(ts) {
    var tsNum = ts.toNumber();
    if (tsNum === 0) {
        return "";
    } else {
        return moment.unix(tsNum).format("YYYY-MM-DD");
    }
}

function tsToDateTime(ts) {
    var tsNum = ts.toNumber();
    if (tsNum === 0) {
        return "";
    } else {
        return moment.unix(tsNum);
    }
}

function timeToTimeString(time) {
    var tsNum = Math.floor(time.toNumber() / 60);
    var hr = Math.floor(tsNum / 60);
    var min = tsNum % 60;
    var hour = hr.toString();
    var minute = min.toString();
    if(hour.length === 1){
        hour = "0" + hour;
    }
    if(minute.length === 1){
        minute = "0" + minute;
    }
    return hour + ":" + minute;
}

function dateStringToTs(dateString) {
    var mm = moment(dateString, "YYYY-MM-DD");
    if (!mm.isValid()) {
        console.log("Bad date: " + dateString);
        throw new Error("Not a valid date: " + dateString);
    }

    return mm.unix();
}

function dateStringAdd(dateString, days) {
    return moment(dateString, "YYYY-MM-DD").add(days, 'days').format("YYYY-MM-DD");
}

function dateTimeStringToTs(dateString) {
    var mm = moment(dateString);
    if (!mm.isValid()) {
        console.log("Bad date: " + dateString);
        throw new Error("Not a valid date: " + dateString);
    }
    return mm.unix();
}

function timeStringToTime(timeString) {
    var hr_min = timeString.trim().split(':');
    if (hr_min.length != 2) {
        console.log("Bad time: " + timeString);
        throw new Error("Not a valid time: " + timeString);
    }
    var hr = parseInt(hr_min[0]), min = parseInt(hr_min[1]);

    if (hr !== Math.floor(hr) || 0 > hr || 24 < hr) {
        console.log("Bad hour: " + hr_min[0]);
        throw new Error("Not a valid hour: " + hr_min[1]);
    }
    if (min !== Math.floor(min) || 0 > min || 60 <= min) {
        console.log("Bad minute: " + hr_min[1]);
        throw new Error("Not a valid minute: " + hr_min[1]);
    }

    return (hr * 60 + min) * 60;
}

function isBytes32(str) {
    return typeof(str) === "string" && str != "" && byteLength(str) < 32;
}

function byteLength(str) {
    var s = str.length;
    for (var i = str.length - 1; i >= 0; i--) {
        var code = str.charCodeAt(i);
        if (code > 0x7f && code <= 0x7ff) s++;
        else if (code > 0x7ff && code <= 0xffff) s += 2;
        if (code >= 0xDC00 && code <= 0xDFFF) i--;
    }
    return s;
}

exports.fromAscii = fromAscii;
exports.toAscii = toAscii;
exports.bnToReal = bnToReal;
exports.numToReal = numToReal;
exports.realToBn = realToBn;
exports.tsToDate = tsToDate;
exports.tsToDateTime = tsToDateTime;
exports.dateStringToTs = dateStringToTs;
exports.timeStringToTime = timeStringToTime;
exports.timeToTimeString = timeToTimeString;
exports.dateStringAdd = dateStringAdd;