package com.example.qrcodescanner.qrcode;

public interface QRCodeFoundListener {

    void onQRCodeFound(String qrCode);
    void qrCodeNotFound();
}

