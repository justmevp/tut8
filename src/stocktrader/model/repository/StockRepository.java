package stocktrader.model.repository;

import stocktrader.model.FileHandle;
import stocktrader.model.entity.Stock;
import stocktrader.model.entity.User;

import java.io.*;
import java.util.ArrayList;

public class StockRepository {
    private FileHandle fileHandle;
    private ArrayList<Stock> stocks = new ArrayList<>();


    public StockRepository(FileHandle fileHandle) {
        this.fileHandle = fileHandle;
    }

    public ArrayList<Stock> getStocks() {
        // Thực hiện logic để lấy thông tin cổ phiếu từ tệp
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileHandle.getFilename("stocklist.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length >= 3) {
                    String name = tokens[0];
                    int quantity = Integer.parseInt(tokens[1]);
                    double price = Double.parseDouble(tokens[2]);
                    Stock stock = new Stock(name, quantity, price);
                    stocks.add(stock);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stocks;
    }

    public boolean storeStocks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileHandle.getFilename("userlist.txt")))) {
            for (Stock stock : stocks) {
                String stockLine = stock.getName() + "," + stock.getQuanity() + "," + stock.getPrice() ;
                writer.write(stockLine);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
