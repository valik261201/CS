package lab5;

import lab1.caesarCypher;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import server.dto.EncryptionDto;
import server.repository.CredentialRepository;
import server.dto.ValidateCodeDto;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class Controller {

    private final GoogleAuthenticator gAuth;
    private final CredentialRepository credentialRepository;

    @SneakyThrows
    @GetMapping("/generate/{username}")
    public void generate(@PathVariable String username, HttpServletResponse response) {
        final GoogleAuthenticatorKey key = gAuth.createCredentials(username);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("CS-LAB-DEMO", username, key);

        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthURL, BarcodeFormat.QR_CODE, 200, 200);

        ServletOutputStream outputStream = response.getOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        outputStream.close();
    }

    @PostMapping("/validate/key")
    public String validateKey(@RequestBody ValidateCodeDto body) {
        if (gAuth.authorizeUser(body.getUsername(), body.getCode())) {
            return "Valid 2FA Code";
        }
        else return "Not valid 2FA Code";
    }

    @PostMapping("/classic/caesar/encrypt")
    public String caesarEncrypt(@RequestBody EncryptionDto body) {
        if (gAuth.authorizeUser(body.getUsername(), body.getCode())) {
            return new caesarCypher(body.getKey()).encrypt(body.getMessage());
        }
        else return "Not valid 2FA Code";
    }

    @PostMapping("/classic/caesar/decrypt")
    public String caesarDecrypt(@RequestBody EncryptionDto body) {
        if (gAuth.authorizeUser(body.getUsername(), body.getCode())) {
            return new caesarCypher(body.getKey()).decrypt(body.getMessage());
        }
        else return "Not valid 2FA Code";
    }
}
