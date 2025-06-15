package com.bill.BillGenerator.controller;

import com.bill.BillGenerator.Service.FuelBillService;
import com.bill.BillGenerator.dto.FuelBillRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fuel-bill")
public class FuelBillController {

    @Autowired
    private FuelBillService fuelBillService;

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateBill(@RequestBody FuelBillRequest request) {
        try {
            byte[] pdfBytes = fuelBillService.generateBillFromInput(request);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "fuel-bill.pdf");
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
