package stocktrader.model.repository;

import stocktrader.model.FileHandle;
import stocktrader.model.entity.Stock;
import stocktrader.model.entity.StockInformation;
import stocktrader.model.entity.User;

import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class UserRepository {

    double userMoney;
    private FileHandle fileHandle;
    private ArrayList<StockInformation> userStocks = new ArrayList<>();

    public UserRepository(FileHandle fileHandle) {
        this.fileHandle = fileHandle;
    }

    public ArrayList<StockInformation> getUserInfo() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileHandle.getFilename("userbalance")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length >= 3) {
                    String stockName = tokens[0];
                    int quantity = Integer.parseInt(tokens[1]);
                    double price = Double.parseDouble(tokens[2]);
                    StockInformation stockInformation = new StockInformation(1, new Stock(stockName, quantity, price), null);
                    userStocks.add(stockInformation);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userStocks;
    }

    //
    public boolean storeUserInfo(ArrayList<StockInformation> userStock2, double userMoney) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileHandle.getFilename("userbalance.txt")))) {
            // Đầu tiên, ghi số tiền
            writer.write(String.valueOf(userMoney));
            writer.newLine();

            // Sau đó, ghi thông tin của các cổ phiếu
            for (StockInformation stockInformation : userStock2) {
                String userStockLine = stockInformation.getStock().getName()
                        + "," + stockInformation.getStock().getQuanity()
                        + "," + stockInformation.getStock().getPrice();
                writer.write(userStockLine);
                writer.newLine();
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public double getUserMoney() {
        // Thêm code để đọc giá trị tiền từ tệp và trả về nó
        double money = 0.0; // Đọc giá trị tiền từ tệp
        try (BufferedReader reader = new BufferedReader(new FileReader(fileHandle.getFilename("userbalance.txt")))) {
            String line;
            if ((line = reader.readLine()) != null) {
                money = Double.parseDouble(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return money;
    }

}
