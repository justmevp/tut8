package stocktrader.model.repository;

import stocktrader.model.FileHandle;
import stocktrader.model.entity.StockInformation;

import java.util.ArrayList;

public class StockCommandRepository {
    private ArrayList<StockInformation> userHistory = new ArrayList<>();
    private FileHandle fileHandle;

    public StockCommandRepository(FileHandle fileHandle) {
        this.fileHandle = fileHandle;
    }

    public ArrayList<StockInformation> getStockCommands() {
        // Thực hiện logic để lấy lệnh cổ phiếu từ tệp
        return userHistory;
    }

    public boolean storeStockCommands() {
        // Thực hiện logic để lưu trữ lệnh cổ phiếu vào tệp
        return false;
    }

    public boolean appendLastStockCommand() {
        // Thực hiện logic để thêm lệnh cổ phiếu cuối cùng vào tệp
        return false;
    }
}
