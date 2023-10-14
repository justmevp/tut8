package stocktrader.model;

import stocktrader.server.StockServer;

import java.io.*;
import java.util.ArrayList;

public class FileHandle {
    private String filename;
//    FileHandle userlistFileHandle = new FileHandle("userlist.txt");
    public FileHandle(String filename) {
        this.filename = filename;
    }
//    public void open() {
//        // Thực hiện logic mở tệp
//    }
//
    public String readLine() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            return reader.readLine();
        }
    }
    //    public void write(String data) {
//        // Thực hiện logic ghi tệp
//    }
//
//    public void append(String data) {
//        // Thực hiện logic thêm dữ liệu vào tệp
//    }
//
    public void close() {
        // Thực hiện logic đóng tệp
    }
}
