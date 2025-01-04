package org.example.accounting.api.service;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.example.accounting.api.web.controller.dto.ReceiptInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class QrCodeService {

    private final MultiFormatReader multiFormatReader = new MultiFormatReader();

    public String readQrCode(MultipartFile file) throws IOException, NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(ImageIO.read(file.getInputStream()))));

        return multiFormatReader.decode(binaryBitmap).getText();
    }

    public ReceiptInfo parseDataFromReceiptQrCode(MultipartFile file) throws NotFoundException, IOException {
        var qrCodeData = readQrCode(file);

        var data = Arrays.stream(qrCodeData.split("&"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(s -> s[0], s -> s[1]));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm");
        LocalDateTime dateTime = LocalDateTime.parse(data.get("t"), formatter);
        double amount = Double.parseDouble(data.get("s"));

        return new ReceiptInfo(dateTime, amount);
    }
}
