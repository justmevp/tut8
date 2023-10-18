package stocktrader.model.entity;

import stocktrader.model.entity.Stock;

import java.time.LocalTime;

public class StockInformation {


    public StockInformation(int commandType, Stock stock, LocalTime transactionDate) {
        this.commandType = commandType;
        this.stock = new Stock(stock.getName(),stock.getQuanity(),stock.getPrice());
        this.transactionDate = transactionDate;
    }

    public int getCommandType() {
        return commandType;
    }

    public void setCommandType(int commandType) {
        this.commandType = commandType;
    }

    public LocalTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    private int commandType;
    private Stock stock;
    private LocalTime transactionDate;
}

