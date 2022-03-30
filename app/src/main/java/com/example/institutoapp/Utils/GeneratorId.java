package com.example.institutoapp.Utils;

public class GeneratorId {

    public static String NUMEROS = "0123456789";

    public static String MAYUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String MINUSCULAS = "abcdefghijklmnopqrstuvwxyz";

    public static String ESPECIALES = "ñÑ";

    //
    public  String getPinNumber() {
        return getPassword(NUMEROS, 4);
    }

    public  String getPassword() {
        return getPassword(8);
    }

    public  String getPassword(int length) {
        return getPassword(NUMEROS + MAYUSCULAS + MINUSCULAS, length);
    }

    public  String getPassword(String key, int length) {
        String pswd = "";

        for (int i = 0; i < length; i++) {
            pswd+=(key.charAt((int)(Math.random() * key.length())));
        }

        return pswd;
    }
}