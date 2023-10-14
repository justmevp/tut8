package stocktrader.model.repository;

import stocktrader.model.FileHandle;
import stocktrader.model.entity.User;

import java.io.*;
import java.util.ArrayList;

public class UserListRepository {
    private ArrayList<User> users = new ArrayList<>();
    private FileHandle fileHandle;

    public UserListRepository(FileHandle fileHandle) {
       this.fileHandle = fileHandle;
    }



    public ArrayList<User> getUserList() throws IOException {
        ArrayList<User> users = new ArrayList<>();

        String line;
        while ((line = this.fileHandle.readLine()) != null) {
            String[] tokens = line.split(",");
            if (tokens.length >= 3) {
                String username = tokens[0];
                String password = tokens[1];
                User user = new User(username, password);
                users.add(user);
            }
        }
        return users;
    }













//    public void addUser(User user) throws IOException {
//        this.fileHandle.appendLine(user.getId() + "," + user.getName() + "," + user.getMoney());
//    }
//
//    }
//    public boolean storeUserList() {
//        // Thực hiện logic để lưu trữ danh sách người dùng vào tệp
//        return false;
//    }
}
