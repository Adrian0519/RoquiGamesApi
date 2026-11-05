package Api.RoquiGames.RoquiGames.Servicios;

import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Locale;

@Component
public class CifradoService {

    private static final String ALGORITMO_AES = "AES/GCM/NoPadding";
    private static final String ALGORITMO_HMAC = "HmacSHA256";

    private static final int TAMANO_NONCE = 12;
    private static final int TAMANO_TAG = 128;

    private final SecretKeySpec claveAES;
    private final SecretKeySpec claveHMAC;

    private final SecureRandom secureRandom = new SecureRandom();

    public CifradoService() {

        String aesKey = System.getenv("ROQUI_AES_KEY");
        String hmacKey = System.getenv("ROQUI_HMAC_KEY");

        if (aesKey == null || hmacKey == null) {
            throw new IllegalStateException(
                    "No se han configurado las claves de seguridad de Roqui Games."
            );
        }

        byte[] aesBytes = Base64.getDecoder().decode(aesKey);
        byte[] hmacBytes = Base64.getDecoder().decode(hmacKey);

        if (aesBytes.length != 32 || hmacBytes.length != 32) {
            throw new IllegalStateException(
                    "Las claves AES y HMAC deben tener 32 bytes."
            );
        }

        this.claveAES = new SecretKeySpec(aesBytes, "AES");
        this.claveHMAC = new SecretKeySpec(hmacBytes, ALGORITMO_HMAC);
    }


    // cifrar datos con AES-256-GCM

    public String cifrar(String texto) {

        if (texto == null) {
            return null;
        }

        try {
            byte[] nonce = new byte[TAMANO_NONCE];
            secureRandom.nextBytes(nonce);

            Cipher cipher = Cipher.getInstance(ALGORITMO_AES);

            GCMParameterSpec parameterSpec =
                    new GCMParameterSpec(TAMANO_TAG, nonce);

            cipher.init(Cipher.ENCRYPT_MODE, claveAES, parameterSpec);

            byte[] textoCifrado = cipher.doFinal(
                    texto.getBytes(StandardCharsets.UTF_8)
            );

            // Guardamos nonce + texto cifrado + etiqueta de autenticacion.
            byte[] resultado = new byte[nonce.length + textoCifrado.length];

            System.arraycopy(nonce, 0, resultado, 0, nonce.length);

            System.arraycopy(
                    textoCifrado,
                    0,
                    resultado,
                    nonce.length,
                    textoCifrado.length
            );

            return Base64.getEncoder().encodeToString(resultado);

        } catch (Exception e) {
            throw new IllegalStateException("Error al cifrar el dato.", e);
        }
    }


    // descifrar datos


    public String descifrar(String textoCifrado) {

        if (textoCifrado == null) {
            return null;
        }

        try {
            byte[] datos = Base64.getDecoder().decode(textoCifrado);

            if (datos.length <= TAMANO_NONCE) {
                throw new IllegalArgumentException("Dato cifrado no válido.");
            }

            byte[] nonce = Arrays.copyOfRange(
                    datos, 0, TAMANO_NONCE
            );

            byte[] contenido = Arrays.copyOfRange(
                    datos, TAMANO_NONCE, datos.length
            );

            Cipher cipher = Cipher.getInstance(ALGORITMO_AES);

            GCMParameterSpec parameterSpec =
                    new GCMParameterSpec(TAMANO_TAG, nonce);

            cipher.init(Cipher.DECRYPT_MODE, claveAES, parameterSpec);

            byte[] resultado = cipher.doFinal(contenido);

            return new String(resultado, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new IllegalStateException("Error al descifrar el dato.", e);
        }
    }

    // generar HMAC del correo

    public String generarHashEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacío.");
        }

        try {
            String emailNormalizado = normalizarEmail(email);

            Mac mac = Mac.getInstance(ALGORITMO_HMAC);
            mac.init(claveHMAC);

            byte[] hash = mac.doFinal(
                    emailNormalizado.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (Exception e) {
            throw new IllegalStateException("Error al generar el hash del email.", e);
        }
    }

    //Normalizar el correo

    public String normalizarEmail(String email) {

        if (email == null) {
            return null;
        }

        return email.trim().toLowerCase(Locale.ROOT);
    }
}

