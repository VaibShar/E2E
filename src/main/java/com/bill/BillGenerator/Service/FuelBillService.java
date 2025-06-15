package com.bill.BillGenerator.Service;

import com.bill.BillGenerator.dto.FuelBillRequest;
import com.bill.BillGenerator.fuel.JsonReader;
import com.bill.BillGenerator.pojo.PDFFIllData;
import com.bill.BillGenerator.utils.CommonUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FuelBillService {

    @Value("${layout.json.path}")
    private String layoutJsonPath;

    @Value("${template.pdf.path}")
    private String templatePdfPath;

    public byte[] generateBillFromInput(FuelBillRequest input) throws Exception {
        // 1. Load layout config
        JsonReader reader = new JsonReader();
        List<PDFFIllData> layoutData = reader.read(layoutJsonPath);

        // 2. Populate fields
        populateFieldsFromRequest(layoutData, input);

        // 3. PDF Generation
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfReader pdfReader = new PdfReader(templatePdfPath);
        PdfStamper stamper = new PdfStamper(pdfReader, outputStream);
        PdfContentByte over = stamper.getOverContent(1);

        for (PDFFIllData data : layoutData) {
            String text = data.getText();
            if ("DateToday".equalsIgnoreCase(text)) {
                text = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            } else if ("TimeNow".equalsIgnoreCase(text)) {
                text = new SimpleDateFormat("HH:mm:ss").format(new Date());
            } else if ("RandomNumber".equalsIgnoreCase(data.getEnricher())) {
                text = String.valueOf(new Random().nextInt(999999));
            }

            BaseFont font = BaseFont.createFont("fuel/fonts/" + data.getFont(), BaseFont.CP1252, BaseFont.EMBEDDED);
            List<Integer> rgb = CommonUtils.hexToRGB(data.getColor());
            over.beginText();
            over.setColorFill(new BaseColor(rgb.get(0), rgb.get(1), rgb.get(2)));
            over.setFontAndSize(font, data.getFontSize());
            over.setTextMatrix(data.getLocationX(), data.getLocationY());
            over.showText(text);
            over.endText();
        }

        stamper.close();
        pdfReader.close();

        return outputStream.toByteArray();
    }

    private void populateFieldsFromRequest(List<PDFFIllData> layoutData, FuelBillRequest input) {
        for (PDFFIllData data : layoutData) {
            if (data.getField() == null) continue;

            switch (data.getField()) {
                case "HEADER1": data.setText(input.header1); break;
                case "HEADER2": data.setText(input.header2); break;
                case "HEADER3": data.setText(input.header3); break;
                case "TYPE": data.setText(input.type); break;
                case "CAR_NUMBER": data.setText(input.carNumber); break;
                case "PHONE_NUMBER": data.setText(input.phoneNumber); break;
                case "TOTAL_PRICE": data.setText(input.totalPrice); break;
                case "PRICE": data.setText(input.price); break;
                case "TOTAL_FUEL": data.setText(input.totalFuel); break;
                case "PAYMENT_MODE": data.setText(input.paymentMode); break;
                case "QUANTITY": data.setText(input.quantity); break;
                case "NOZZLE": data.setText(input.nozzle); break;
                case "PUMP_NO": data.setText(input.pumpNo); break;
                case "PRODUCT": data.setText(input.product); break;
                case "TRX_ID": data.setText(input.trxId); break;
                case "RECEIPT_ID": data.setText(input.receiptId); break;
            }
        }

        // Handle DateToday and TimeNow fields
        for (PDFFIllData data : layoutData) {
            if ("DateToday".equalsIgnoreCase(data.getText())) {
                data.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
            } else if ("TimeNow".equalsIgnoreCase(data.getText())) {
                data.setText(new SimpleDateFormat("HH:mm:ss").format(new Date()));
            }
        }
    }
}
