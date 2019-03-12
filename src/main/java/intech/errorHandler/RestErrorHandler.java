package intech.errorHandler;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Valeev-RN on 28.02.2019.
 */
public class RestErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        //conversion logic for decoding conversion
        ByteArrayInputStream arrayInputStream = (ByteArrayInputStream) response.getBody();
        Scanner scanner = new Scanner(arrayInputStream);
        scanner.useDelimiter("\\Z");
        String data = "";
        if (scanner.hasNext())
            data = scanner.next();
        System.out.println(" ------------ error ------------ "+data);
    }
}
