package stocktrader;

public class Stock {
    public int getStockNo() {
        return stockNo;
    }

    public void setStockNo(int stockNo) {
        this.stockNo = stockNo;
    }

    public int getQuanity() {
        return quanity;
    }

    public void setQuanity(int quanity) {
        this.quanity = quanity;
    }



    private int stockNo;
    private int quanity;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Stock(int stockNo, int quanity, String name, double price) {
        this.stockNo = stockNo;
        this.quanity = quanity;
        this.name = name;
        this.price = price;
    }
}
