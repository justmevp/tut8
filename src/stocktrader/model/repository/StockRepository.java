package stocktrader.model.repository;

import stocktrader.model.FileHandle;
import stocktrader.model.entity.Stock;

import java.util.ArrayList;

public class StockRepository {
    private ArrayList<Stock> stocks = new ArrayList<>();
    private FileHandle fileHandle;

    public StockRepository(FileHandle fileHandle) {
        this.fileHandle = fileHandle;
    }

    public ArrayList<Stock> getStocks() {
        // Thực hiện logic để lấy thông tin cổ phiếu từ tệp
        return stocks;
    }

    public boolean storeStocks() {
        // Thực hiện logic để lưu trữ thông tin cổ phiếu vào tệp
        return false;
    }
}
