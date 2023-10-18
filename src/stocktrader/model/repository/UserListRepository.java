package stocktrader.model.repository;

import stocktrader.model.FileHandle;
import stocktrader.model.entity.User;

import java.io.*;
import java.util.ArrayList;

public class UserListRepository {
    private FileHandle fileHandle;
    ArrayList<User> users = new ArrayList<>();

    public UserListRepository(FileHandle fileHandle) {
        this.fileHandle = fileHandle;
    }

    public boolean StoreUserList(ArrayList<User> users) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileHandle.getFilename("userlist.txt")))) {
            for (User user : users) {
                String userLine = user.getUsername() + "," + user.getPassword();
                writer.write(userLine);
                writer.newLine(); // Xuống dòng sau mỗi người dùng
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<User> getUserList() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileHandle.getFilename("userlist.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length >= 2) {
                    String username = tokens[0];
                    String password = tokens[1];
                    User user = new User(username, password);
                    users.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }
}
