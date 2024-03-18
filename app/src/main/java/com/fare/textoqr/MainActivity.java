package com.fare.textoqr;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private DbManager dbManager;
    private ImageView qrCodeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DbManager(this);

        qrCodeImageView = findViewById(R.id.qrCodeImageView);

        Button Btn1 = findViewById(R.id.Btn1);
        Button Btn2 = findViewById(R.id.Btn2);

        // Get the real text from Btn1's tag
        String realTextBtn1 = (String) Btn1.getTag();

        Btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTextToDatabase(realTextBtn1); // Save the real text to the database
                generateAndDisplayQRCode(realTextBtn1); // Generate and display QR code based on real text
            }
        });

        Btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = Btn2.getText().toString();
                saveTextToDatabase(text);
                generateAndDisplayQRCode(text);
            }
        });
    }

    private void saveTextToDatabase(String text) {
        dbManager.saveDriver(text);
        // Optionally, you can show a toast or log a message here to indicate successful saving
    }

    private void generateAndDisplayQRCode(String text) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 500, 500);
            Bitmap bitmap = bitMatrixToBitmap(bitMatrix);
            qrCodeImageView.setImageBitmap(bitmap);
            saveBitmapAsImage(bitmap, "QRCodeImage.png");
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private Bitmap bitMatrixToBitmap(BitMatrix bitMatrix) {
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bitmap;
    }

    private void saveBitmapAsImage(Bitmap bitmap, String fileName) {
        try {
            // Get the DCIM/TexttoQr directory
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "TexttoQr");
            if (!directory.exists()) {
                directory.mkdirs(); // Create the directory if it doesn't exist
            }

            // Save the file in the DCIM/TexttoQr directory
            File file = new File(directory, fileName);
            OutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();

            // Show a toast message with the file path
            Toast.makeText(this, "QR Code image saved as " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
