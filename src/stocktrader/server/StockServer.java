package stocktrader.server;

import stocktrader.model.FileHandle;
import stocktrader.model.entity.Stock;
import stocktrader.model.entity.StockInformation;
import stocktrader.model.entity.User;
import stocktrader.model.repository.UserListRepository;
import stocktrader.model.repository.UserRepository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class StockServer {

    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Stock> stocks = new ArrayList<>();
    private ArrayList<StockInformation> userStocks = new ArrayList<>();
    private ArrayList<StockInformation> userHistory = new ArrayList<>();

    private Map<Integer, Integer> remainingStockQuantities = new HashMap<>();
    private double userMoney = 20000;
    private boolean isLoggedIn = false;
    private UserListRepository userListRepository;
    public StockServer(FileHandle handle) {
        try {
            userListRepository = new UserListRepository(handle);
            users = userListRepository.getUserList(handle);
        }catch (Exception e ) {
            users = new ArrayList<>();
        }

        users.add(new User("user1", "12345"));
        users.add(new User("user2", "123456"));
        users.add(new User("user3", "1234567"));

        stocks.add(new Stock("vingroup", 15, 1000));
        stocks.add(new Stock("barcelona", 20, 2000));
        stocks.add(new Stock("intermiami", 6, 3000));


        for (int i = 0; i < stocks.size(); i++) {
            Stock stock = stocks.get(i);
            int stockNumber = i;
            remainingStockQuantities.put(stockNumber, stock.getQuanity());
        }
    }


    public double getStockPrice(int stockNo) {
        for (int i = 0; i < stocks.size(); i++) {
            Stock stock = stocks.get(i);
            int stockNumber = i;
            if (stockNumber == stockNo) {
                return stock.getPrice();
            }
        }
        return 0; // Trả về 0 nếu không tìm thấy cổ phiếu với số thứ tự tương ứng
    }


    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                StockServer stockServer = new StockServer();
                if (stockServer.isLoggedIn = true) {
                    System.out.println("Success");
                    return true;
                }
            }
        }
        System.out.println("Invalid username or password. Please try again.");
        return false;
    }


    public String listAllStocks() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < stocks.size(); i++) {
            Stock stock = stocks.get(i);
            int stockNo = i;
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
                // Tìm cổ phiếu tương ứng trong danh sách stocks
                Stock matchingStock = null;
                for (Stock s : stocks) {
                    if (s.getName().equals(stockName)) {
                        matchingStock = s;
                        break;
                    }
                }
                if (matchingStock == null) {
                    // Xử lý trường hợp không tìm thấy cổ phiếu tương ứng
                    return false;
                }
                // Tạo đối tượng Stock tương ứng và thay đổi số lượng
                matchingStock.setQuanity(stockQuantity);
                double totalPurchasePrize = matchingStock.getPrice() * quantity;
                if (userMoney >= totalPurchasePrize) {
                    // Tạo thông tin cổ phiếu đã mua cho danh sách userStocks
                    matchingStock.setQuanity(quantity);
                    LocalTime purchaseDate = LocalTime.now();
                    StockInformation purchasedInfo = new StockInformation(1, matchingStock, purchaseDate);
                    // Thêm thông tin cổ phiếu đã mua vào danh sách userStocks
                    userStocks.add(purchasedInfo);
                    int remainingQuantity = remainingStockQuantities.get(stockNo) - quantity;
                    remainingStockQuantities.put(stockNo, remainingQuantity);
                    return true; // Trả về true nếu giao dịch mua cổ phiếu thành công
                } else {
                    System.out.println("Not enough money");
                }
            }
        }
        return false; // Trả về false nếu không mua được cổ phiếu
    }

    public String listOwnStocks() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < userStocks.size(); i++) {
            StockInformation stockInformation = userStocks.get(i);
            int stockNumber = i;
            String stockName = stockInformation.getStock().getName();
            int stockQuantity = stockInformation.getStock().getQuanity();
            double stockPrice = stockInformation.getStock().getPrice();
            LocalTime purchaseDate = stockInformation.getTransactionDate();
            builder.append(stockNumber).append(", ")
                    .append("Stock name: ").append(stockName).append(", ")
                    .append(stockQuantity).append(", ")
                    .append("transaction date: ").append(purchaseDate).append(", ")
                    .append(stockPrice).append("\n");
        }

        return builder.toString();
    }


    public boolean sellStocks(int stockNo, int quantity) {
        String myStocks = listOwnStocks();
        String[] myStocksLines = myStocks.split("\n");
        Stock possibleSelledStock = null;
        for (String myStocksLine : myStocksLines) {
            String[] stockInfo = myStocksLine.split(", ");
            if (stockInfo.length >= 4) {
                int stockNumber = Integer.parseInt(stockInfo[0]);
                if (stockNumber == stockNo) {
                    int stockQuantity = Integer.parseInt(stockInfo[2]);
                    String stockName = stockInfo[1];
                    double stockPrice = Double.parseDouble(stockInfo[4]);
                    possibleSelledStock = new Stock(stockName, stockQuantity, stockPrice);
                    break;
                }
            }
        }
        if (possibleSelledStock != null && quantity > 0 && quantity <= possibleSelledStock.getQuanity()) {
            double totalPurchasePrize = possibleSelledStock.getPrice() * quantity;
            possibleSelledStock.setQuanity(possibleSelledStock.getQuanity() - quantity);
            LocalTime purchaseDate = LocalTime.now();
            StockInformation soldInfo = new StockInformation(2, possibleSelledStock, purchaseDate);
            userStocks.add(soldInfo);
            updateUserStocks(); // update số lượng stock sau khi bán
            remainingStockQuantities.put(stockNo, remainingStockQuantities.get(stockNo) + quantity); //update lại số lượng stock trên thị trường
            return true;
        }
        return false;
    }


    private void updateUserStocks() {
        ArrayList<StockInformation> updatedUserStocks = new ArrayList<>();
        for (StockInformation stockInformation : userStocks) {
            if (stockInformation.getCommandType() == 2) {
                updatedUserStocks.add(stockInformation);
            }
        }
        userStocks = updatedUserStocks;
    }

    public void nextDay() {
        for (Stock stock : stocks) {
            double stockPriceChange = ThreadLocalRandom.current().nextDouble(-10.0, 10.0);
            double newPrice = stock.getPrice() + stockPriceChange;
            stock.setPrice(newPrice); // Cập nhật giá cổ phiếu
            System.out.println("Giá cổ phiếu " + stock.getName() + " (trong danh sách stocks): " + newPrice);
        }

//        for (StockInformation stockInformation : userStocks) {
//            Stock stock = stockInformation.getStock();
//            double stockPriceChange = ThreadLocalRandom.current().nextDouble(-10.0, 10.0);
//            double newPrice = stock.getPrice() + stockPriceChange;
//            stock.setPrice(newPrice); // Cập nhật giá cổ phiếu
//            System.out.println("Giá cổ phiếu " + stock.getName() + " (trong danh sách userStocks): " + newPrice);
//        }
//    }
    }


    public Double checkBalance() {
//        double stockValue = 0;
//        for (StockInformation stockInformation : userStocks) {
//            Stock stock = stockInformation.getStock();
//            int stockQuantity = stock.getQuanity();
//            double stockPrice = stock.getPrice();
//            stockValue = stockPrice * stockQuantity;
//        }
        return userMoney;
    }


//    public static void main(String[] args) {
//        StockServer stockServer = new StockServer();
//        System.out.println(stockServer.listAllStocks());
//        int stockNoToPurchase = 1; // Số thứ tự của cổ phiếu bạn muốn mua
//        int quantityToPurchase = 15; // Số lượng cổ phiếu bạn muốn mua
//        boolean purchaseResult = stockServer.purchase(stockNoToPurchase, quantityToPurchase);
//        if (purchaseResult == true) {
//            System.out.println("thành công");
//            System.out.println(stockServer.listOwnStocks());
//
//        } else {
//            System.out.println("không thành công");
//        }
//
//        int stockNoToSell = 0;
//        int stockQuantityToSell = 5;
//        if (purchaseResult == true) {
//            {
//                boolean sellResult = stockServer.sellStocks(stockNoToSell, stockQuantityToSell);
//                if (sellResult == true) {
//                    System.out.println("bán thành công");
//                    System.out.println(stockServer.listOwnStocks());
//                } else {
//                    System.out.println("bán ko đc");
//                }
//            }
//        }
//        System.out.println(stockServer.checkBalance());
//    }
}