package stocktrader;

import java.time.LocalTime;

public class StockInformation {


    public StockInformation(int commandType, Stock stock, LocalTime transactionDate) {
        this.commandType = commandType;
        this.stock = stock;
        this.transactionDate = transactionDate;
    }

    public int getCommandType() {
        return commandType;
    }

    public void setCommandType(int commandType) {
        this.commandType = commandType;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public LocalTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    private int commandType;
    private Stock stock;
    private LocalTime transactionDate;
}

