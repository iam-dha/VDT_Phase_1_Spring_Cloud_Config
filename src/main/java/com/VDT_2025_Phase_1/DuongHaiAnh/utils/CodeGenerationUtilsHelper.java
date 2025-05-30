package com.VDT_2025_Phase_1.DuongHaiAnh.utils;

import java.security.SecureRandom;
import java.util.Random;

public class CodeGenerationUtilsHelper {
    public static String generateOtp(){
        int otp = new Random().nextInt(900_000) + 100_000; // 6 digits otp
        return String.valueOf(otp);
    }
    public static String generate32CharHexToken() {
        byte[] bytes = new byte[16]; // 16 bytes = 128 bits = 32 hex chars
        new SecureRandom().nextBytes(bytes);


        StringBuilder hexString = new StringBuilder(32);
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b)); // 2 hex chars per byte
        }
        return hexString.toString();
    }
}
