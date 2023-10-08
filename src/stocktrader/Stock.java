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

    public Stock(int stockNo, int quanity, String name) {
        this.stockNo = stockNo;
        this.quanity = quanity;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
}
