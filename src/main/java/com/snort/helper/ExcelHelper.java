package com.snort.helper;

import com.snort.entity.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {

    //checking the given type is Excel format or not
    public static boolean checkExcelFormat(MultipartFile file) {
        String contentType = file.getContentType();

        if (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            return true;
        } else {
            return false;
        }
    }

    //converting excel to list of products
    public static List<User> convertExcelToListOfUser(InputStream is) {
        List<User> list = new ArrayList<>();

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheet("data");

            int rowNumber = 0;
            Iterator<Row> iterator = sheet.iterator();

            while (iterator.hasNext()) {
                Row row = iterator.next();

                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cells = row.iterator();
                int cid = 0;

                User u = new User();
                while (cells.hasNext()) {
                    Cell cell = cells.next();

                    switch (cid) {
                        case 0:
                            u.setUserId((int) cell.getNumericCellValue());
                            break;
                        case 1:
                            u.setFirstName(cell.getStringCellValue());
                            break;

                        case 2:
                            u.setLastName(cell.getStringCellValue());
                            break;
                        case 3:
                            u.setEmail(cell.getStringCellValue());
                            break;
                        case 4:
                            u.setDesignation(cell.getStringCellValue());
                            break;
                    }
                    cid++;
                }
                //encrypting the users while adding
                u.setFirstName(EncryptionHelper.encrypt(u.getFirstName()));
                u.setLastName(EncryptionHelper.encrypt(u.getLastName()));
                u.setEmail(EncryptionHelper.encrypt(u.getEmail()));
                u.setDesignation(EncryptionHelper.encrypt(u.getDesignation()));

                list.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
