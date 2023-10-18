package stocktrader.model;
import java.io.*;
public class FileHandle {
    private String filename;
    public FileHandle(String filename) {
        this.filename = filename;
    }

    public String readLine() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            return reader.readLine();
        }
    }
    public String getFilename(String s) {
        return filename;
    }

    //    public void open() {
//        // Thực hiện logic mở tệp
//    }
//
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
