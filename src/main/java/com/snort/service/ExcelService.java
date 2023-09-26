package com.snort.service;

import com.snort.entity.User;
import com.snort.helper.EncryptionHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {

    public byte[] generateExcel(List<User> users) throws IOException {
        //excel generation with decryption
        try(Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet();
            //creating new header row

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("User ID");
            headerRow.createCell(1).setCellValue("First Name");
            headerRow.createCell(2).setCellValue("Last Name");
            headerRow.createCell(3).setCellValue("User Email");
            headerRow.createCell(4).setCellValue("User Designation");

            //create data rows
            //decrypting the encrypted data while creating rows
            int rowNum=1;
            for (User user : users){
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(user.getUserId());
                row.createCell(1).setCellValue(EncryptionHelper.decrypt(user.getFirstName()));
                row.createCell(2).setCellValue(EncryptionHelper.decrypt(user.getLastName()));
                row.createCell(3).setCellValue(EncryptionHelper.decrypt(user.getEmail()));
                row.createCell(4).setCellValue(EncryptionHelper.decrypt(user.getDesignation()));
            }

            //write to byte Array output stream
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
            catch (IOException e){
                throw new RuntimeException("Failed to generate Excel: "+e.getMessage(),e);
            }
            }
        }
    }
