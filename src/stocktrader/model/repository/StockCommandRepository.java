package stocktrader.model.repository;

import stocktrader.model.FileHandle;
import stocktrader.model.entity.Stock;
import stocktrader.model.entity.StockInformation;

import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;

public class StockCommandRepository {
    private ArrayList<StockInformation> userHistory = new ArrayList<>();
    private FileHandle fileHandle;

    public StockCommandRepository(FileHandle fileHandle) {
        this.fileHandle = fileHandle;
    }
//    private Stock createStockFromInfo(String stockInfo) {
//        String[] stockData = stockInfo.split(",");
//        if (stockData.length >= 3) {
//            String name = stockData[0];
//            int quantity = Integer.parseInt(stockData[1]);
//            double price = Double.parseDouble(stockData[2]);
//            return new Stock(name, quantity, price);
//        }
//        return null; // Xử lý trường hợp không thể tạo Stock từ chuỗi thông tin
//    }


    public ArrayList<StockInformation> getStockCommands() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileHandle.getFilename("userhistory.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length >= 5) {
                    int commandType = Integer.parseInt(tokens[0]);
                    String stockName = tokens[1];
                    int quantity = Integer.parseInt(tokens[2]);
                    double price = Double.parseDouble(tokens[3]);
                    String dateString = tokens[4];
                    LocalTime transactionDate = LocalTime.parse(dateString);
                    Stock stock = new Stock(stockName, quantity, price);
                    StockInformation stockInformation = new StockInformation(commandType, stock, transactionDate);
                    userHistory.add(stockInformation);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userHistory;
    }


    public boolean storeStockCommands(ArrayList<StockInformation> userHistory) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileHandle.getFilename("userhistory.txt")));
            for (StockInformation stockInformation : userHistory) {
                int commandType = stockInformation.getCommandType();
                String stockName = stockInformation.getStock().getName();
                int stockQuantity = stockInformation.getStock().getQuanity();
                double stockPrice = stockInformation.getStock().getPrice();
                LocalTime transactionTime = stockInformation.getTransactionDate();

                String userHistoryLine = commandType + "," + stockName + "," + stockQuantity + "," + stockPrice + "," + transactionTime;
                writer.write(userHistoryLine);
                writer.newLine();
            }
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean appendLastStockCommand() {
        // Thực hiện logic để thêm lệnh cổ phiếu cuối cùng vào tệp
        return false;
    }
}
