package intech.config;

/**
 * Created by Valeev-RN on 21.02.2019.
 * <p>
 * В этом товарище будем хранить подписанный клиент секрет, который нам подпишет сервер подписаний
 * Нужен только для отладки наверное, потому надо выпилить к херам...
 */
public class SecretSingl {

    private static String clientSecret = "";

    private static SecretSingl ourInstance = new SecretSingl();

    public static SecretSingl getInstance() {
        return ourInstance;
    }

    private SecretSingl() {
    }

    public static String getClientSecret() {
        return clientSecret;
    }

    public static void setClientSecret(String clientSecret) {
        SecretSingl.clientSecret = clientSecret;
    }
}
