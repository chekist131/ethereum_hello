pragma solidity ^0.4.0;

contract MyContract {

    event Tx(string message);

    string message;

    function MyContract(){

    }

    function setMessage(string _message) {
        message = _message;
    }

    function getMessage() public returns (string) {
        return message;
    }

    function emitEvent(string message) {
        emit Tx(message);
    }
}