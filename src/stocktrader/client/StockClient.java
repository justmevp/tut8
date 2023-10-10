package stocktrader.client;

import stocktrader.server.StockServer;

import java.util.Scanner;

public class StockClient {
    StockServer stockServer = new StockServer();
    private boolean isLoggedIn = false;


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
            System.out.println("Unable to purchase the requested amount of stock.");
        }
    }


    public void myStock() {
        System.out.println("Your stocks: ");
        System.out.println(stockServer.listOwnStocks());

    }

    public void logIn() {
        System.out.println("Username: ");
        Scanner sc = new Scanner(System.in);
        String userName = sc.nextLine();

        System.out.println("Password");
        Scanner sc2 = new Scanner(System.in);
        String passWord = sc2.nextLine();
        boolean loginResult =stockServer.login(userName, passWord);
        if (loginResult) {
            isLoggedIn = true; // Đánh dấu đã đăng nhập thành công
        } else {
       isLoggedIn= false;
        }
    }


    public static void main(String[] args) {
        StockClient client = new StockClient();
        do {
            System.out.println("1.Login: ");
            System.out.println("2.Check all available stock:");
            System.out.println("3.Buy stock");
            System.out.println("4.Check your stocks");
            Scanner sc = new Scanner(System.in);
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    client.logIn();
                    break;
                case 2:
                    if (client.isLoggedIn) {
                        client.seeAllStock();
                    }else{
                        System.out.println("Please login first");
                    }
                    break;
                case 3:
                    if (client.isLoggedIn) {
                        client.buyStock();
                    }else{
                        System.out.println("Please login first");
                    }
                    break;
                case 4:
                    if (client.isLoggedIn) {
                        client.myStock();
                    }else{
                        System.out.println("Please login first");
                    }
                    break;
            }
        } while (true);
    }
}
