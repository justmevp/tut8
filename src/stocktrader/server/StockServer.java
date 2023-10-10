package stocktrader.server;

import stocktrader.Stock;
import stocktrader.StockInformation;
import stocktrader.User;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockServer {

    private List<User> users = new ArrayList<>();
    private List<Stock> stocks = new ArrayList<>();
    private List<StockInformation> userStocks = new ArrayList<>();
    private Map<Integer, Integer> remainingStockQuantities = new HashMap<>();


    public StockServer() {
        users.add(new User("user1", "12345"));
        users.add(new User("user2", "123456"));
        users.add(new User("user3", "1234567"));

        stocks.add(new Stock(1, 10, "vingroup", 1000));
        stocks.add(new Stock(2, 12, "barcelona", 2000));
        stocks.add(new Stock(3, 5, "intermiami", 3000));

        for (Stock stock : stocks) {
            remainingStockQuantities.put(stock.getStockNo(), stock.getQuanity());
        }
    }

    public double getStockPrice(int stockNo) {
        for (Stock stock : stocks) {
            if (stock.getStockNo() == stockNo) {
                return stock.getPrice();
            }
        }
        return 0; // Trả về 0 nếu không tìm thấy cổ phiếu với số thứ tự tương ứng
    }


    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                user.setLoggedIn(true);
                System.out.println("Success");
                return true;
            }
        }
        System.out.println("Invalid username or password. Please try again.");
        return false;
    }


    public String listAllStocks() {

        StringBuilder builder = new StringBuilder();
        for (Stock stock : stocks) {
//            int stockQuantity = stock.getQuanity();
            int stockNo = stock.getStockNo();
            int remainingQuantity = remainingStockQuantities.get(stockNo); // Get the remaining quantity
            String stockName = stock.getName();
            double stockPrice = stock.getPrice();
            builder.append(stockNo).append(", ")
                    .append(remainingQuantity).append(", ") // Display the remaining quantity
                    .append(stockName).append(", ")
                    .append(stockPrice).append("\n");

        }
        return builder.toString();
    }

    public boolean purchase(int stockNo, int quantity) {
        // Lấy danh sách cổ phiếu từ phương thức listAllStocks
        String allStocks = listAllStocks();
        // Phân tách danh sách cổ phiếu thành từng dòng
        String[] stockLines = allStocks.split("\n");
        Stock purchasedPossibleStock = null;
        // Tìm cổ phiếu mà người dùng muốn mua trong danh sách đã phân tách
        for (String stockLine : stockLines) {
            String[] stockInfo = stockLine.split(", ");
            int stockNumber = Integer.parseInt(stockInfo[0]);
            if (stockNumber == stockNo) {
                int stockQuantity = Integer.parseInt(stockInfo[1]);
                String stockName = stockInfo[2];
                double stockPrice = Double.parseDouble(stockInfo[3]);
                // Tạo đối tượng Stock tương ứng
                purchasedPossibleStock = new Stock(stockNumber, stockQuantity, stockName, stockPrice);
                break;
            }
        }
        // Kiểm tra xem cổ phiếu có tồn tại và số lượng mua hợp lệ
        if (purchasedPossibleStock != null && quantity > 0 && quantity <= purchasedPossibleStock.getQuanity()) {
            // Tạo thông tin cổ phiếu đã mua
            purchasedPossibleStock.setQuanity(quantity);
            LocalTime purchaseDate = LocalTime.now();
            StockInformation purchasedInfo = new StockInformation(1, purchasedPossibleStock, purchaseDate);
            // Thêm thông tin cổ phiếu đã mua vào danh sách userStocks
            userStocks.add(purchasedInfo);
            int remainingQuantity = remainingStockQuantities.get(stockNo) - quantity;
            remainingStockQuantities.put(stockNo, remainingQuantity);
            return true; // Trả về true nếu giao dịch mua cổ phiếu thành công
        }
        return false; // Trả về false nếu không mua được cổ phiếu
    }

    public String listOwnStocks() {
        StringBuilder builder = new StringBuilder();
        for (StockInformation stockInformation : userStocks) {
            int commandType = stockInformation.getCommandType();
            Stock stock = stockInformation.getStock();
            int stockNumber = stock.getStockNo();
            String stockName = stock.getName();
            int stockQuantity = stock.getQuanity();
            double stockPrice = stock.getPrice();
            LocalTime purchaseDate = stockInformation.getTransactionDate();
            builder .append("Command type: ").append(commandType).append(", ")
                    .append(stockNumber).append(", ")
                    .append("Stock No: ").append(stockName).append(", ")
                    .append(stockQuantity).append(", ")
                    .append("transaction date: ").append(purchaseDate).append(", ")
                    .append(stockPrice).append("\n");
        }

        return builder.toString();
    }


    public boolean sellStock(int stockNo, int quantity) {
        String myStocks = listOwnStocks();
        String[] myStocksLines = myStocks.split("\n");
        Stock possibleSelledStock = null;
        for (String myStocksLine : myStocksLines) {
            String[] stockInfo = myStocksLine.split(", ");
            if (stockInfo.length >= 4) {
                int stockNumber = Integer.parseInt(stockInfo[1]);
                if (stockNumber == stockNo) {
                    int stockQuantity = Integer.parseInt(stockInfo[3]);
                    String stockName = stockInfo[2];
                    double stockPrize = Double.parseDouble(stockInfo[5]);
                    possibleSelledStock = new Stock(stockNumber, stockQuantity, stockName, stockPrize);
                    break;
                }

            }
        }
        if (possibleSelledStock != null && quantity > 0 && quantity <= possibleSelledStock.getQuanity()) {
            possibleSelledStock.setQuanity(possibleSelledStock.getQuanity() - quantity);
            LocalTime purchaseDate = LocalTime.now();
            StockInformation soldInfo = new StockInformation(2, possibleSelledStock, purchaseDate);
            userStocks.add(soldInfo);
            updateUserStocks();
            return true;

        }
        return false;
    }

    private void updateUserStocks() {
        List<StockInformation> updatedUserStocks = new ArrayList<>();
        for (StockInformation stockInformation : userStocks) {
            if (stockInformation.getCommandType() == 2) {
                updatedUserStocks.add(stockInformation);
            }
        }
        userStocks = updatedUserStocks;
    }

//    public static void main(String[] args) {
//        StockServer stockServer = new StockServer();
//        System.out.println(stockServer.listAllStocks());
//        int stockNoToPurchase = 1; // Số thứ tự của cổ phiếu bạn muốn mua
//        int quantityToPurchase = 10; // Số lượng cổ phiếu bạn muốn mua
//        boolean purchaseResult = stockServer.purchase(stockNoToPurchase, quantityToPurchase);
//        if (purchaseResult == true) {
//            System.out.println("thành công");
//            System.out.println(stockServer.listOwnStocks());
//
//        } else {
//            System.out.println("không thành công");
//        }
//
//        int stockNoToSell = 1;
//        int stockQuantityToSell = 5;
//        if (purchaseResult == true) {
//            {
//                boolean sellResult = stockServer.sellStock(stockNoToSell, stockQuantityToSell);
//                if (sellResult == true) {
//                    System.out.println("bán thành công");
//                    System.out.println(stockServer.listOwnStocks());
//                }else{
//                    System.out.println("bán ko đc");
//                }
//
//            }
//            }
//        }
    }










