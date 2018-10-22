#!/usr/bin/env bash
rm ./out/*
solc src/main/java/contracts/MyContract.sol --bin --abi -o out/
web3j solidity generate ./out/MyContract.bin ./out/MyContract.abi -o ./src/main/java/ -p org.valyaev