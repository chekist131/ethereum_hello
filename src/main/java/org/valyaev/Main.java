package org.valyaev;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import rx.Observable;

import java.math.BigInteger;

public class Main {


    public static void main(String[] args) throws Exception {
        Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:8545"));
        TransactionManager transactionManager = new ClientTransactionManager(web3j, "0xb9ad54780aeb2bd6c654d11d1f63f5b27cd7657a");

        MyContract myContract = MyContract.deploy(web3j, transactionManager, new DefaultGasProvider()).send();
        subscribe(myContract);

        for(int i = 0 ; i < 1000; i++) {
            myContract.emitEvent(Integer.toString(i)).sendAsync();
        }
        System.out.println("Закончил отправку транзакций");
        Runtime.getRuntime().exec("pkill -9 geth");
        System.out.println("Блокчейн остановлен");

        web3j.blockObservable(true).subscribe(next -> {
            System.out.println(next.getRawResponse());
        });
    }

    private static void subscribe(MyContract myContract) {
        final int[] max = {0};

        Observable<MyContract.TxEventResponse> observable = myContract.txEventObservable(DefaultBlockParameter.valueOf(BigInteger.ONE), DefaultBlockParameterName.LATEST);
        observable.subscribe(next -> {
            Integer counter = Integer.valueOf(next.message);
            if(counter > max[0]) {
                max[0] = counter;
                System.out.println("Max: " + max[0]);
            }
        }, error -> {
            System.out.println(new Exception("Ошибка сабскрайбера", error));
        });

    }
}