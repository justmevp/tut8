package stocktrader.server;

import stocktrader.Stock;
import stocktrader.StockInformation;
import stocktrader.User;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class StockServer {

    private List<User> users = new ArrayList<>();

    List<StockInformation> userStocks = new ArrayList<>();


    public StockServer() {
        users.add(new User("user1", "12345"));
        users.add(new User("user2", "123456"));
        users.add(new User("user3", "1234567"));

        stocks.add(new Stock(1, 10, "vingroup"));
        stocks.add(new Stock(2, 12, "barcelona"));
        stocks.add(new Stock(3, 5, "intermiami"));


    }

    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                user.setLoggedIn(true);
                return true;
            }

        }
        return false;


    }


    private List<Stock> stocks = new ArrayList<>();

    public String listAllStocks() {
        StringBuilder builder = new StringBuilder();
        for (Stock stock : stocks) {
            builder.append(stock.getStockNo()).append(", ")
                    .append(stock.getQuanity()).append(", ")
                    .append(stock.getName()).append("\n");
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

                // Tạo đối tượng Stock tương ứng
                purchasedPossibleStock = new Stock(stockNumber, stockQuantity, stockName);
                break;
            }
        }

        // Kiểm tra xem cổ phiếu có tồn tại và số lượng mua hợp lệ
        if (purchasedPossibleStock != null && quantity > 0 && quantity <= purchasedPossibleStock.getQuanity()) {
            // Tạo thông tin cổ phiếu đã mua
            LocalTime purchaseDate = LocalTime.now();
            StockInformation purchasedInfo = new StockInformation(1, purchasedPossibleStock, purchaseDate);

            // Thêm thông tin cổ phiếu đã mua vào danh sách userStocks
            userStocks.add(purchasedInfo);

            // Trừ đi số lượng cổ phiếu đã mua khỏi danh sách cổ phiếu tồn kho
            purchasedPossibleStock.setQuanity(purchasedPossibleStock.getQuanity() - quantity);

            return true; // Trả về true nếu giao dịch mua cổ phiếu thành công
        }

        return false; // Trả về false nếu không mua được cổ phiếu
    }


    public static void main(String[] args) {
        StockServer stockServer = new StockServer();
        System.out.println(stockServer.listAllStocks());
        int stockNoToPurchase = 2; // Số thứ tự của cổ phiếu bạn muốn mua
        int quantityToPurchase = 13; // Số lượng cổ phiếu bạn muốn mua
   boolean purchaseResult =stockServer.purchase(stockNoToPurchase, quantityToPurchase);
   if (purchaseResult = true) {
       System.out.println("thành công");
   }else{
       System.out.println("không thành công");
   }
    }



}








