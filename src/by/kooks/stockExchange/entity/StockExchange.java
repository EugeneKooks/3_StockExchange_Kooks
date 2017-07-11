package by.kooks.stockExchange.entity;

import by.kooks.stockExchange.exception.WrongDataException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;


public final class StockExchange {

    private static StockExchange instance;
    private static ReentrantLock lock = new ReentrantLock();
    private List<Company> companies = new ArrayList<>();
    private static AtomicBoolean flag = new AtomicBoolean(true);

    private StockExchange() {}
    public static StockExchange getInstance (){
        if (flag.get()) {
            lock.lock();
            try{
                if (instance==null){
                    instance = new StockExchange();
                    flag.set(false);
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }
    public List<Company> getCompanies() {
        return companies;
    }
    public void addCompany (Company newCompany) throws WrongDataException {
        String newName = newCompany.getName();
        //check whether this company already exists
        for (Company company : companies) {
            if (company.getName().equalsIgnoreCase(newName)){
                throw new WrongDataException("Company with name "+newName+" already operates on the StockMarket.");
            }
        }
        companies.add(newCompany);
    }
}