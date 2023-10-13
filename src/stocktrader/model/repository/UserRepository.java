package stocktrader.model.repository;

import stocktrader.model.FileHandle;
import stocktrader.model.entity.StockInformation;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class UserRepository {
    //    private ArrayList<StockInformation>  = new ArrayList<>();
    private double userMoney;
    private FileHandle fileHandle;

    public UserRepository(FileHandle fileHandle) {
        this.fileHandle = fileHandle;
    }

//    public void ArrayList<StockInformation> getUserInfo(){}
//        try {
//            FileInputStream inputStream = new FileInputStream(fileHandle);
//            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
//
//        }catch ()
//        // Thực hiện logic để lấy thông tin của người dùng từ tệp
//
//    }
//
    public boolean storeUserInfo() {
//         Thực hiện logic để lưu trữ thông tin của người dùng vào tệp
        return false;
    }

    public double getUserMoney() {
        // Thực hiện logic để lấy số tiền của người dùng từ tệp
        return userMoney;
    }
}
