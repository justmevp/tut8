package stocktrader.server;

import stocktrader.model.FileHandle;
import stocktrader.model.entity.Stock;
import stocktrader.model.entity.StockInformation;
import stocktrader.model.entity.User;
import stocktrader.model.repository.StockCommandRepository;
import stocktrader.model.repository.StockRepository;
import stocktrader.model.repository.UserListRepository;
import stocktrader.model.repository.UserRepository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class StockServer {

    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Stock> stocks = new ArrayList<>();
    private ArrayList<StockInformation> userStocks = new ArrayList<>();
    private ArrayList<StockInformation> userHistory = new ArrayList<>();
    private Map<Integer, Integer> remainingStockQuantities = new HashMap<>();
    private double userMoney = 900000;
    private boolean isLoggedIn = false;
    private FileHandle fileHandle = new FileHandle("userlist.txt");
    private FileHandle fileHandle2 = new FileHandle("stocklist.txt");
    private FileHandle fileHandle3 = new FileHandle("userhistory.txt");
    private FileHandle fileHandle4 = new FileHandle("userbalance.txt");
    UserListRepository userListRepository = new UserListRepository(fileHandle);
    StockRepository stockRepository = new StockRepository(fileHandle2);
    StockCommandRepository stockCommandRepository = new StockCommandRepository(fileHandle3);
    UserRepository userRepository = new UserRepository(fileHandle4);


    public StockServer() {
        stocks = stockRepository.getStocks();
        for (int i = 0; i < stocks.size(); i++) {

            Stock stock = stocks.get(i);
            int stockNumber = i;
            remainingStockQuantities.put(stockNumber, stock.getQuanity());
        }
    }

    public void create(User user) {
        users.add(user);
        userListRepository.StoreUserList(users);
    }


    public boolean login(String username, String password) {
        users = userListRepository.getUserList();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                isLoggedIn = true;
                System.out.println("Success");
                return true;
            }
        }
        System.out.println("Invalid username or password. Please try again.");
        return false;
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

    public String listAllStocks() {
        StringBuilder builder = new StringBuilder();
        stocks = stockRepository.getStocks();
        for (int i = 0; i < stocks.size(); i++) {
            Stock stock = stocks.get(i);

            // Kiểm tra xem giá trị trả về từ remainingStockQuantities.get(stockNo) có null hay không
            Integer remainingQuantity = remainingStockQuantities.get(i);
            if (remainingQuantity != null) {
                int remainingQuantityValue = remainingQuantity.intValue(); // Lấy giá trị số nguyên
                String stockName = stock.getName();
                double stockPrice = stock.getPrice();
                builder.append(i).append(", ")
                        .append(stockName).append(", ")
                        .append(remainingQuantityValue).append(", ") // Hiển thị số lượng còn lại
                        .append(stockPrice).append("\n");
            }
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
                int stockQuantity = Integer.parseInt(stockInfo[2]);
                String stockName = stockInfo[1];
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
                double totalPurchasePrice = matchingStock.getPrice() * quantity;
                if (userMoney >= totalPurchasePrice) {
                    userMoney -= totalPurchasePrice;

                    boolean stockPurchased = false;
                    for (StockInformation info : userStocks) {
                        if (info.getStock().getName().equals(stockName)) {
                            // Nếu đã mua cổ phiếu trước đó, cập nhật số lượng
                            int currentQuantity = info.getStock().getQuanity();
                            info.getStock().setQuanity(currentQuantity + quantity);
                            Stock stockinfo = new Stock(info.getStock().getName(),quantity,info.getStock().getPrice());
                            LocalTime purchaseTime3 = LocalTime.now();
                            StockInformation purchasedInfo3 = new StockInformation(1, stockinfo, purchaseTime3);
                            userHistory.add(purchasedInfo3);
                            stockPurchased = true;
                            break;
                        }
                    }
                    if (!stockPurchased) {
                        // Tạo thông tin cổ phiếu đã mua cho danh sách userStocks
                        matchingStock.setQuanity(quantity);
                        LocalTime purchaseTime = LocalTime.now();
                        Stock stockinfo = new Stock(matchingStock.getName(),quantity,matchingStock.getPrice());
                        StockInformation purchasedInfo = new StockInformation(1, stockinfo, purchaseTime);

                        StockInformation purchasedInfo2 = new StockInformation(1, stockinfo, purchaseTime);
                        // Thêm thông tin cổ phiếu đã mua vào danh sách userStocks
                        userStocks.add(purchasedInfo);
                        userHistory.add(purchasedInfo2);
                    }
                    int remainingQuantity = remainingStockQuantities.get(stockNo) - quantity;
                    remainingStockQuantities.put(stockNo, remainingQuantity);
                    matchingStock.setQuanity(quantity);
                    stockCommandRepository.storeStockCommands(userHistory);
                    userRepository.storeUserInfo(userStocks, userMoney);
                    return true; // Trả về true nếu giao dịch mua cổ phiếu thành công
                } else {
                    System.out.println("Not enough money");
                }
            }
        }
        return false; // Trả về false nếu không mua được cổ phiếu
    }

    public void listStockHistory() {
        ArrayList<StockInformation> stockHistory = stockCommandRepository.getStockCommands();
        for (StockInformation stockInfo : stockHistory) {
            if (stockInfo.getCommandType() == 1) {
                System.out.println("Command Type: " + stockInfo.getCommandType() + ":Buy");
                System.out.println("Stock Name: " + stockInfo.getStock().getName());
                System.out.println("Quantity: " + stockInfo.getStock().getQuanity());
                System.out.println("Price: " + stockInfo.getStock().getPrice());
                System.out.println("Transaction Date: " + stockInfo.getTransactionDate());
                System.out.println("-------------");
            } else {
                System.out.println("Command Type: " + stockInfo.getCommandType() + ":Sell");
                System.out.println("Stock Name: " + stockInfo.getStock().getName());
                System.out.println("Quantity: " + stockInfo.getStock().getQuanity());
                System.out.println("Price: " + stockInfo.getStock().getPrice());
                System.out.println("Transaction Date: " + stockInfo.getTransactionDate());
                System.out.println("-------------");
            }

        }
    }

    public String listOwnStocks() {
        StringBuilder builder = new StringBuilder();
        int stockNumber = 0;
        for (StockInformation stockInformation : userStocks) {
            String stockName = stockInformation.getStock().getName();
            int stockQuantity = stockInformation.getStock().getQuanity();
            double stockPrice = stockInformation.getStock().getPrice();
            LocalTime purchaseDate = stockInformation.getTransactionDate();
            builder.append(stockNumber).append(", ")
                    .append("Stock name: ").append(stockName).append(", ")
                    .append(stockQuantity).append(", ")
                    .append("transaction date: ").append(purchaseDate).append(", ")
                    .append(stockPrice).append("\n");
            stockNumber++;
        }
        // Lưu thông tin cổ phiếu đã mua vào tệp userbalance

        return builder.toString();
    }

    public boolean sellStocks(int stockNo, int quantity) {
        String myStocks = listOwnStocks();
        String[] myStocksLines = myStocks.split("\n");
        StockInformation possibleSelledStockInfo = null;
        for (String myStocksLine : myStocksLines) {
            String[] stockInfo = myStocksLine.split(", ");
            if (stockInfo.length >= 4) {
                int stockNumber = Integer.parseInt(stockInfo[0]);
                if (stockNumber == stockNo) {
                    possibleSelledStockInfo = userStocks.get(stockNumber);
                    break;
                }
            }
        }
        if (possibleSelledStockInfo != null && quantity > 0 && quantity <= possibleSelledStockInfo.getStock().getQuanity()) {
            double sellPrice = possibleSelledStockInfo.getStock().getPrice() * quantity;
            userMoney += sellPrice;
            int currentQuantity = possibleSelledStockInfo.getStock().getQuanity();
            int remainingQuantity = currentQuantity - quantity;
            possibleSelledStockInfo.getStock().setQuanity(remainingQuantity);
            if (remainingQuantity == 0) {
                userStocks.remove(possibleSelledStockInfo);
            }
            Stock stockinfo = new Stock(possibleSelledStockInfo.getStock().getName(),quantity,possibleSelledStockInfo.getStock().getPrice());
            LocalTime sellTime = LocalTime.now();
            StockInformation soldInfo = new StockInformation(2, stockinfo, sellTime);
            userHistory.add(soldInfo);
            remainingStockQuantities.put(stockNo, remainingStockQuantities.get(stockNo) + quantity);
            stockCommandRepository.storeStockCommands(userHistory);
            userRepository.storeUserInfo(userStocks, userMoney);
            return true;
        }
        return false;
    }


    public void nextDay() {
        for (Stock stock : stocks) {
            double stockPriceChange = ThreadLocalRandom.current().nextDouble(-10.0, 10.0);
            double newPrice = stock.getPrice() + stockPriceChange;
            stock.setPrice(newPrice);

        }
        // Lưu lại dữ liệu cổ phiếu sau mỗi lần cập nhật
        stockRepository.storeStocks();
        // Sau khi cập nhật toàn bộ danh sách cổ phiếu, hiển thị giá
    }


    public String checkBalance() {
        double userMoney = userRepository.getUserMoney(); // Lấy số tiền của người dùng
        StringBuilder balanceInfo = new StringBuilder("Số dư: " + userMoney + "\n");

        // Tạo một danh sách tạm thời để theo dõi thông tin cổ phiếu đã hiển thị
        ArrayList<String> displayedStocks = new ArrayList<>();

        for (StockInformation stockInfo : userStocks) {
            String stockName = stockInfo.getStock().getName();
            int quantity = stockInfo.getStock().getQuanity();
            double stockPrice = stockInfo.getStock().getPrice();

            // Kiểm tra xem thông tin cổ phiếu này đã hiển thị hay chưa
            String stockKey = stockName + quantity + stockPrice;
            if (!displayedStocks.contains(stockKey)) {
                balanceInfo.append("Stock Name: ")
                        .append(stockName)
                        .append(", Quantity: ")
                        .append(quantity)
                        .append(", Price: ")
                        .append(stockPrice)
                        .append("\n");
                displayedStocks.add(stockKey); // Đánh dấu đã hiển thị
            }
        }

        return balanceInfo.toString();
    }


    public static void main(String[] args) {
//        StockServer stockServer = new StockServer();
//                    System.out.println(stockServer.listOwnStocks());
//                    int stockNoToPurchase = 1; // Số thứ tự của cổ phiếu bạn muốn mua
//                    int quantityToPurchase = 10; // Số lượng cổ phiếu bạn muốn mua
//                    boolean purchaseResult = stockServer.purchase(stockNoToPurchase, quantityToPurchase);
////                    int stocktopurchase2 = 1;
////                    int quantitytopurchase2 = 5;
////                    boolean purchaseResult2 = stockServer.purchase(stocktopurchase2, quantitytopurchase2);
////                    int stocktopurchase3 = 4;
////                    int quantitytopurchase3 = 2;
////                    boolean purchaseResult3 = stockServer.purchase(stocktopurchase3, quantitytopurchase3);
//                    if (purchaseResult == true) {
//                        System.out.println("thành công");
//                        System.out.println(stockServer.listOwnStocks());
//                    } else {
//                        System.out.println("không thành công");
//                    }
//                    if (purchaseResult2 == true) {
//                        System.out.println("thành công");
//                        System.out.println(stockServer.listOwnStocks());
//                    } else {
//                        System.out.println("không thành công");
//                    }
//                    if (purchaseResult3 == true) {
//                        System.out.println("thành công");
//                        System.out.println(stockServer.listOwnStocks());
//                    } else {
//                        System.out.println("không thành công");
//                    }
//        int stockNoToSell = 0;
//        int quantityToSell = 5;
//        boolean sellResult = stockServer.sellStocks(stockNoToSell, quantityToSell);
//        if (sellResult == true) {
//            System.out.println("thành công");
//            System.out.println(stockServer.listOwnStocks());
//        } else {
//            System.out.println("không thành công");
//        }
//
//        System.out.println("Đường dẫn tệp userhistory.txt: " + stockServer.fileHandle3.getFilename("userhistory.txt"));

//        try {
//            BufferedReader reader = new BufferedReader(new FileReader("userhistory.txt"));
//            String line;
//            System.out.println("Nội dung tệp userhistory.txt:");
//            while ((line = reader.readLine()) != null) {
//
//                System.out.println(line);
//            }
//            reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("Lỗi khi đọc tệp: " + e.getMessage()); // Thêm thông báo lỗi cụ thể
//        }
//
//    }
    }
}


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

