package stocktrader.model.entity;

public class Stock {


    public int getQuanity() {
        return quanity;
    }

    public void setQuanity(int quanity) {
        this.quanity = quanity;
    }


    private int quanity;

    private String name;
    private double price;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Stock(String name, int  quanity, double price) {

        this.quanity = quanity;
        this.name = name;
        this.price = price;
    }
}
