package org.example.accounting.api.web.controller;

import com.google.zxing.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.accounting.api.service.QrCodeService;
import org.example.accounting.api.web.controller.dto.ReceiptInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class QrCodeController {
    private final QrCodeService qrCodeService;

    @PostMapping("/qr")
    public ResponseEntity<ReceiptInfo> readQr(@RequestPart MultipartFile file) throws NotFoundException, IOException {
        return ResponseEntity.ok(qrCodeService.parseDataFromReceiptQrCode(file));
    }
}
