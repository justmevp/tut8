package stocktrader.model.repository;

import stocktrader.model.FileHandle;
import stocktrader.model.entity.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class UserListRepository {
    private ArrayList<User> users = new ArrayList<>();
    private FileHandle fileHandle;

    public UserListRepository(FileHandle fileHandle) {
        this.fileHandle = fileHandle;
    }



    public ArrayList<User> getUserList(FileHandle handle) {
        try {
            FileInputStream inputStream = new FileInputStream(String.valueOf(handle));
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            ArrayList<User> list = (ArrayList<User>) objectInputStream.readObject();
            objectInputStream.close();
            return list;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
//    public boolean storeUserList() {
//        // Thực hiện logic để lưu trữ danh sách người dùng vào tệp
//        return false;
//    }
}
