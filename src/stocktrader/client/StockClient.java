package stocktrader.client;
import stocktrader.model.entity.User;
import stocktrader.server.StockServer;
import java.util.Scanner;

public class StockClient  {

    StockServer stockServer = new StockServer();
    private boolean isLoggedIn = false;
    public void singUp(){
        System.out.println("1. Enter Usernam");
        Scanner scanner = new Scanner(System.in);
        String username= scanner.nextLine();
        Scanner scanner2 = new Scanner(System.in);
        System.out.println("2. Enter password");
        String password = scanner2.nextLine();
        User user = new User(username, password);
        stockServer.create(user);
    }
    public void logIn()  {
        System.out.println("Username: ");
        Scanner sc = new Scanner(System.in);
        String userName = sc.nextLine();

        System.out.println("Password");
        Scanner sc2 = new Scanner(System.in);
        String passWord = sc2.nextLine();
        boolean loginResult = stockServer.login(userName, passWord);
        if (loginResult) {
            isLoggedIn = true; // Đánh dấu đã đăng nhập thành công
        } else {
            isLoggedIn = false;
        }
    }

    public void seeAllStock() {
        System.out.println(stockServer.listAllStocks());
    }

    public void buyStock() {
        System.out.println("Choose stock: ");
        Scanner sc = new Scanner(System.in);
        int stockNo = sc.nextInt();
        // Lấy giá của cổ phiếu dựa trên số thứ tự
        double stockPrice = stockServer.getStockPrice(stockNo);
        System.out.println("How many stocks do you want to buy: ");
        Scanner sc1 = new Scanner(System.in);
        int stockQuantity = sc1.nextInt();
        double totalPrice = stockPrice * stockQuantity;
        // Thực hiện mua cổ phiếu
        boolean purchaseResult = stockServer.purchase(stockNo, stockQuantity);
        if (purchaseResult) {
            System.out.println("You have successfully purchased " + stockQuantity + " stock(s) of type " + stockNo +
                    " for a total price of " + totalPrice);
        } else {
            System.out.println("Fail to purchase");
        }
    }
    public void userStockHistory(){
        stockServer.listStockHistory();

    }

    public void myStock() {
        System.out.println("Your stocks: ");
        System.out.println(stockServer.listOwnStocks());

    }


    public void sellStock() {
        System.out.println("Choose stock");
        Scanner sc = new Scanner(System.in);
        int stockNo = sc.nextInt();
        double stockPrize = stockServer.getStockPrice(stockNo);
        System.out.println("How many stock do you want to sell: ");
        Scanner sc2 = new Scanner(System.in);
        int stockQuantity = sc2.nextInt();
        boolean soldStock = stockServer.sellStocks(stockNo, stockQuantity);
        if (soldStock == true) {
            System.out.println("You just sell " + stockQuantity + " stock " + stockNo + " with the prize of " + stockPrize * stockQuantity);
        } else {
            System.out.println("Fail to sell");
        }
    }
    public void checkBalance(){
        System.out.println(stockServer.checkBalance());
    }
    public void updateStockPrice(){
     stockServer.nextDay();
        System.out.println("Update Successfully");
    }

    public static void main(String[] args) {
        StockClient client = new StockClient();
        do {
            System.out.println("1.Sign Up:");
            System.out.println("2.Login: ");
            System.out.println("3.Check all available stock:");
            System.out.println("4.Buy stock:");
            System.out.println("5.Check your stocks:");
            System.out.println("6.Sell your stocks:");
            System.out.println("7.Check Balance: ");
            System.out.println("8.Update Stocks' price");
            System.out.println("9.Show History: ");
            Scanner sc = new Scanner(System.in);
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    client.singUp();
                    break;
                case 2:
                    client.logIn();
                    break;
                case 3:
                    if (client.isLoggedIn) {
                        client.seeAllStock();
                    } else {
                        System.out.println("Please login first");
                    }
                    break;
                case 4:
                    if (client.isLoggedIn) {
                        client.buyStock();
                    } else {
                        System.out.println("Please login first");
                    }
                    break;
                case 5:
                    if (client.isLoggedIn) {
                        client.myStock();
                    } else {
                        System.out.println("Please login first");
                    }
                    break;
                case 6:
                    if (client.isLoggedIn) {
                        client.sellStock();
                    } else {
                        System.out.println("Please login first");
                    }
                    break;
                case 7:
                    if (client.isLoggedIn) {
                        client.checkBalance();
                    } else {
                        System.out.println("Please login first");
                    }
                    break;
                case 8:
                    if (client.isLoggedIn) {
                        client.updateStockPrice();
                    } else {
                        System.out.println("Please login first");
                    }
                    break;
                case 9:
                    if (client.isLoggedIn) {
                        client.userStockHistory();
                    } else {
                        System.out.println("Please login first");
                    }
                    break;

            }
        } while (true);
    }
}
