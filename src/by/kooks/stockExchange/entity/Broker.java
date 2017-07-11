package by.kooks.stockExchange.entity;

import java.util.*;
import java.util.concurrent.Callable;

public class Broker implements Callable<Broker>{

    private String name;
    private double money;
    private static StockExchange stockExchange;
    private Map<Company, Integer> brokerShares = new HashMap<>();
    private static Random random = new Random();
    public Broker(String name, double money, StockExchange stockExchange) {
        this.name = name;
        this.money = money;
        this.stockExchange = stockExchange;
    }
    public String getName() {
        return name;
    }
    public double getMoney() {
        return money;
    }
    public Map<Company, Integer> getBrokerShares() {
        return brokerShares;
    }
    public void setMoney(double money) {
        this.money = money;
    }

    @Override
    public Broker call()  {
        System.out.println(name+" begins.");
        //how many times to buy
        int buyNum = random.nextInt(3)+1;
        //choosing random company and shares quantity to buy
        for (int i = 0; i < buyNum; i++) {
            Company company = stockExchange.getCompanies().get(random.nextInt(stockExchange.getCompanies().size()));
            company.buyShares(random.nextInt(100)+1,this);
        }
        //how many times to sell
        int saleNum = random.nextInt(brokerShares.size()+1);
        //choosing random company from broker companies and shares quantity to sell
        for (int i = 0; i < saleNum; i++) {
            List<Company> brokerCompanies = new ArrayList<>();
            for (Map.Entry<Company, Integer> entry : brokerShares.entrySet()) {
                brokerCompanies.add(entry.getKey());
            }
            Company company = brokerCompanies.get(random.nextInt(brokerShares.size()));
            company.sellShares(random.nextInt(brokerShares.get(company)),this);
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Broker)) return false;

        Broker broker = (Broker) o;

        if (Double.compare(broker.getMoney(), getMoney()) != 0) return false;
        if (getName() != null ? !getName().equals(broker.getName()) : broker.getName() != null) return false;
        return getBrokerShares() != null ? getBrokerShares().equals(broker.getBrokerShares()) : broker.getBrokerShares() == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getName() != null ? getName().hashCode() : 0;
        temp = Double.doubleToLongBits(getMoney());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (getBrokerShares() != null ? getBrokerShares().hashCode() : 0);
        return result;
    }

    @Override
    public String toString (){
        String s = "";
        double assets=money;
        for (Map.Entry<Company, Integer> entry : brokerShares.entrySet()) {
            s+=entry.getValue()+" shares of "+entry.getKey().getName() + ", ";
            assets+=entry.getKey().getSharePrice()*entry.getValue();
        }
        return name+" has "+ (brokerShares.isEmpty()?"0 shares":s) +String.format("%.2f",money)+"$ of money, total assets: " +
                String.format("%.2f",assets) + "$";
    }
}
