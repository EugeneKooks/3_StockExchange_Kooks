package by.kooks.stockExchange.entity;

import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class Company {

    private String name;
    private double sharePrice;
    private AtomicBoolean areStocksBought = new AtomicBoolean(true);
    private ArrayDeque <Broker> queue = new ArrayDeque<>();
    private ReentrantLock lock = new ReentrantLock();

    public Company(String name, double sharePrice) {
        this.name = name;
        this.sharePrice = sharePrice;
    }
    public double getSharePrice() {
        return sharePrice;
    }
    public void setSharePrice(double sharePrice) {
        this.sharePrice = sharePrice;
    }
    public String getName() {
        return name;
    }

    public void buyShares(int boughtSharesQuantity, Broker broker){
        queue.add(broker);
        lock.lock();
        try {
            while (queue.peek()!=null) {
                Broker currentBroker = queue.poll();
                double AllSharesPrice = sharePrice * boughtSharesQuantity;
                //check whether the broker has enough money to buyShares this amount of shares
                if (currentBroker.getMoney() > AllSharesPrice) {
                    currentBroker.setMoney(currentBroker.getMoney() - AllSharesPrice);
                    //check whether the broker has already bought this company's shares
                    if (currentBroker.getBrokerShares().containsKey(this)) {
                        int updatedSharesQuantity = currentBroker.getBrokerShares().get(this) + boughtSharesQuantity;
                        currentBroker.getBrokerShares().put(this, updatedSharesQuantity);
                    } else {
                        currentBroker.getBrokerShares().put(this, boughtSharesQuantity);
                    }
                    System.out.println(currentBroker.getName() + " bought " + boughtSharesQuantity + " shares of " + name + ", spent " + String.format("%.2f", AllSharesPrice) +"$");
                    areStocksBought.set(true);
                    double newPrice = setNewMarketSharePrice(areStocksBought, boughtSharesQuantity);
                    this.setSharePrice(newPrice);
                } else {
                    System.out.println(currentBroker.getName() + " doesn't have enough money to purchase " + boughtSharesQuantity + " shares of " + name);
                }
            }
        } finally {
            lock.unlock();
        }
    }
    public void sellShares(int soldSharesQuantity, Broker broker){
        if (lock.tryLock()) {
            try {
                double allSharesPrice = sharePrice * soldSharesQuantity;
                broker.setMoney(broker.getMoney() + allSharesPrice);
                int sharesInStock = broker.getBrokerShares().get(this) - soldSharesQuantity;
                broker.getBrokerShares().put(this,sharesInStock);
                System.out.println(broker.getName() + " sold " + soldSharesQuantity + " shares of " + name + ", got " + String.format("%.2f", allSharesPrice) + "$");
                areStocksBought.set(false);
                this.setSharePrice(setNewMarketSharePrice(areStocksBought, soldSharesQuantity));
            } finally {
                lock.unlock();
            }
        } else {
            System.out.println(broker.getName()+" didn't sell shares in time, decided to sell shares next time.");
        }
    }
    private double setNewMarketSharePrice(AtomicBoolean areStocksBought, int quantity){
        double priceChange = (double)quantity/ sharePrice;
        if (areStocksBought.get()){
            return sharePrice + priceChange;
        } else {
            return sharePrice - priceChange;
        }
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Company other = (Company) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (sharePrice != other.sharePrice)
            return false;
        return true;
    }
    @Override
    public String toString() {
        return this.name+" "+this.sharePrice;
    }
}
