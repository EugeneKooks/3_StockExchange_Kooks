package by.kooks.stockExchange.action;

import by.kooks.stockExchange.entity.Broker;
import by.kooks.stockExchange.entity.Company;
import by.kooks.stockExchange.entity.StockExchange;
import by.kooks.stockExchange.exception.WrongDataException;
import by.kooks.stockExchange.parser.InputDataParser;
import by.kooks.stockExchange.reader.TradingDataReader;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


public class StockMarketAction {

    public List <Broker> compareBrokers (List <Broker> brokerList) {

        Collections.sort(brokerList,(Comparator.comparingDouble(this::calculateAssets)));

        return brokerList;
    }
    private double calculateAssets (Broker broker){
        double assets = broker.getMoney();
        for (Map.Entry<Company, Integer> entry : broker.getBrokerShares().entrySet()) {
            assets+=entry.getKey().getSharePrice()*entry.getValue();
        }
        return assets;
    }

    public static void fillStockExchange(StockExchange stockExchange, String fileName) throws WrongDataException {
        //read companies' info from file (name and share price)
        List <String> companiesList = TradingDataReader.readData(fileName);
        Map <String, Integer> companies = InputDataParser.parseList(companiesList);

        for (Map.Entry<String, Integer> entry : companies.entrySet()) {
            stockExchange.addCompany(new Company(entry.getKey(), entry.getValue()));
        }
    }

}