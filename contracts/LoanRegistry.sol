// Categories offset by 1000; each with a generic message at the start. Sub-categories offset by 100.
contract Errors {

    // ********************** Normal execution **********************

    uint16 constant NO_ERROR = 0;

    // ********************** Resources **********************

    uint16 constant RESOURCE_ERROR = 1000;
    uint16 constant RESOURCE_NOT_FOUND = 1001;
    uint16 constant RESOURCE_ALREADY_EXISTS = 1002;

    // ********************** Access **********************

    uint16 constant ACCESS_DENIED = 2000;

    // ********************** Input **********************

    uint16 constant PARAMETER_ERROR = 3000;
    uint16 constant INVALID_PARAM_VALUE = 3001;
    uint16 constant NULL_PARAM_NOT_ALLOWED = 3002;
    uint16 constant INTEGER_OUT_OF_BOUNDS = 3003;
    
    // Arrays
    uint16 constant ARRAY_INDEX_OUT_OF_BOUNDS = 3100;

    // ********************** Contract states *******************

    // Catch all for when the state of the contract does not allow the operation.
    uint16 constant INVALID_STATE = 4000;

    // ********************** Transfers *******************
    // Transferring some form of value from one account to another is very common,
    // so it should have default error codes.

    uint16 constant TRANSFER_FAILED = 8000;
    uint16 constant NO_SENDER_ACCOUNT = 8001;
    uint16 constant NO_TARGET_ACCOUNT = 8002;
    uint16 constant TARGET_IS_SENDER = 8003;
    uint16 constant TRANSFER_NOT_ALLOWED = 8004;
    // Balance-related.
    uint16 constant INSUFFICIENT_SENDER_BALANCE = 8100;

}

contract Bytes32ToAddressMapper {

    struct BAMElement {
        uint keyIndex;
        address value;
    }

    struct BAMap
    {
        mapping(bytes32 => BAMElement) data;
        bytes32[] keys;
        uint size;
    }

    // Will overwrite existing data by default. Control this from extending contract.
    function _insert(BAMap storage map, bytes32 key, address value) internal returns (bool newEntry)
    {
        var exists = map.data[key].value != 0;
        if (!exists){
            var keyIndex = map.keys.length++;
            map.keys[keyIndex] = key;
            map.data[key] = BAMElement(keyIndex, value);
            map.size++;
        }
        return !exists;
    }

    function _remove(BAMap storage map, bytes32 key) internal returns (bool removed)
    {
        var elem = map.data[key];
        var exists = elem.value != 0;
        if (!exists){
            return false;
        }
        var keyIndex = elem.keyIndex;
        delete map.data[key];
        var len = map.keys.length;
        if(keyIndex != len - 1){
            var swap = map.keys[len - 1];
            map.keys[keyIndex] = swap;
            map.data[swap].keyIndex = keyIndex;
        }
        map.keys.length--;
        map.size--;
        return true;
    }

    function _removeAll(BAMap storage map) internal returns (uint numRemoved){
        var l = map.keys.length;
        for(uint i = 0; i < l; i++){
            delete map.data[map.keys[i]];
        }
        delete map.keys;
        map.size = 0;
        return l;
    }

    function _hasKey(BAMap storage map, bytes32 key) internal constant returns (bool has){
        return map.data[key].value != 0;
    }

    function _keyFromIndex(BAMap storage map, uint index) internal constant returns (bytes32 key){
        if(index >= map.keys.length){
            return 0;
        }
        return map.keys[index];
    }

    function _keyIndex(BAMap storage map, bytes32 key) internal constant returns (int index){
        var elem = map.data[key];
        if(elem.value == 0){
            return -1;
        }
        return int(elem.keyIndex);
    }

    function _size(BAMap storage map) internal constant returns (uint size){
        return map.size;
    }

}

contract Loan {
    
    bytes32 _identifier;
    address _latticeAddress;
    
    function Loan(bytes32 identifier, address latticeAddress){
        _identifier = identifier;
        _latticeAddress = latticeAddress;
    }
    
    function identifier() constant returns (bytes32 identifier){
        return _identifier;
    }
}

contract LoanRegistry is Bytes32ToAddressMapper, Errors {

    address _root;

    BAMap _map;
    BAMap _default_map;

    function LoanRegistry() {
        _root = msg.sender;
    }

    function createLoan(bytes32 identifier) external returns (address addr, uint16 error) {
        if(!_isRoot()){
            error = ACCESS_DENIED;
            return;
        }
        if(identifier == 0){
            error = NULL_PARAM_NOT_ALLOWED;
            return;
        }
        // Id taken.
        if(_map.data[identifier].value != 0){
            error = RESOURCE_ALREADY_EXISTS;
            return;
        }
        
        var loan = new Loan(identifier, this);
        _insert(_map, identifier, address(loan));
        addr = address(loan);
    }

    function removeLoan(bytes32 identifier) external returns (uint16 error) {
        if(!_isRoot()){
            return ACCESS_DENIED;
        }
        if(identifier == 0){
            return NULL_PARAM_NOT_ALLOWED;
        }
        var removed = _remove(_map, identifier);
        if(!removed){
            return RESOURCE_NOT_FOUND;
        }
    }

    function loan(bytes32 identifier) constant returns (bytes32 loanId, address loanAddress){
        loanId = identifier;
        loanAddress = _map.data[identifier].value;
    }

    function loanAddress(bytes32 identifier) constant returns (address loanAddress){
        return _map.data[identifier].value;
    }

    function loanIdFromIndex(uint index) constant returns (bytes32 identifier){
        if(index >= _map.keys.length){
            return;
        }
        identifier = _map.keys[index];
    }

    function loanFromIndex(uint index) constant returns (bytes32 identifier, address loanAddress){
        if(index >= _map.keys.length){
            return;
        }
        identifier = _map.keys[index];
        loanAddress = _map.data[identifier].value;
    }

    function numLoans() constant returns (uint numLoans){
        return _map.size;
    }

    function _isRoot() constant internal returns (bool isRoot) {
        return msg.sender == _root;
    }

}