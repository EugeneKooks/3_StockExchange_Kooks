package by.kooks.stockExchange.runner;

import by.kooks.stockExchange.action.StockMarketAction;
import by.kooks.stockExchange.entity.Broker;
import by.kooks.stockExchange.entity.StockExchange;
import by.kooks.stockExchange.exception.WrongDataException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class StockRunner {

    private static final Logger LOGGER = LogManager.getLogger(StockRunner.class.getSimpleName());
    private static final String FILE_NAME = "file/companies.txt";
    private static final double DEFAULT_MONEY_SUM = 50000;

    public static void main(String[] args) {

        List <Future<Broker>> futureList = new ArrayList<>();
        StockExchange stockMarket = StockExchange.getInstance();
        //fill the stockexchange with companies
        try {
            StockMarketAction.fillStockExchange(stockMarket, FILE_NAME);
        } catch (WrongDataException e) {
            LOGGER.log(Level.ERROR, e);
        }
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            futureList.add(exec.submit(new Broker("Broker " + (i + 1), DEFAULT_MONEY_SUM, stockMarket)));
        }
        exec.shutdown();
        //extract brokers from <Broker> future
        StockMarketAction action = new StockMarketAction();
        List <Broker> brokerList = new ArrayList<>();
        try {
            for (Future<Broker> future : futureList) {
                brokerList.add(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.log(Level.ERROR, e, e.getCause());
        }
        //sort brokers by their profits
        brokerList = action.compareBrokers(brokerList);
        System.out.println();
        LOGGER.log(Level.INFO, "Brokers after the trade in order of increasing the amount of assets: ".toUpperCase());
        System.out.println();
        brokerList.forEach(a-> LOGGER.log(Level.INFO,a));

    }
}