package intech.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import intech.config.SecretSingl;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Valeev-RN on 15.02.2019.
 */
@Controller
@RequestMapping(value = "/auth")
public class EsiaController {


//    private static final String AUTH_GET_CODE = "/aas/oauth2/ac";
//    private static final String AUTH_GET_TOKEN = "/aas/oauth2/te";

    private static final String SIGN_URI = "/bsb/crypto/sign/pkcs7";

    private static final String ESIA_SERV = "https://esia-portal1.test.gosuslugi.ru";
    //    private static final String ESIA_SERV = "https://esia.gosuslugi.ru";
    private static final String ESIA_CODE_POINT = "/aas/oauth2/ac";
    private static final String ESIA_TOKEN_POINT = "/aas/oauth2/te";
    private static final String ESIA_INFO_POINT = "/rs/prns/";

    private static final String STATE = UUID.randomUUID().toString();
    private static final String REDIRECT_URL = "http://localhost:8080/esia-ok";
    private static String CLIENT_ID = "CLIENT_ID";
    private static String SCOPE = "email fullname";
    private static final String ACCESS_TYPE = "online";

    @Autowired
    RestTemplate restTemplate;

    @GetMapping(value = "/getCode")
    public ModelAndView getCode(@RequestParam(value = "scope") String scope,
                                @RequestParam(value = "hostGet") String host,
                                @RequestParam(value = "client_id") String client_id,
                                @RequestParam(value = "backUrl") String backUrl,
                                @RequestParam(value = "accessType") String accessType,
                                @RequestParam(value = "sign") String sign) throws OAuthSystemException, IOException {

        if (scope != null && !scope.isEmpty())
            SCOPE = scope;
        if (client_id != null && !client_id.isEmpty())
            CLIENT_ID = client_id;

        String timestamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss Z").format(new Date());

        String client_secret = SCOPE + timestamp + CLIENT_ID + STATE;
        System.out.println("----> 1. esiaTest - go to sign server - > " + client_secret);

        //для начала обратимся к серверу подписаний и подпишем наш client_secret
        String encodeBase64String = Base64.encodeBase64String(client_secret.getBytes());
        System.out.println("----> 2. esiaTest - go to sign server - > " + encodeBase64String);

//        String sign = getSign(hostSigner, bearer, encodeBase64String);

        //todo
//        final HttpHeaders headers = new HttpHeaders();
//
//        headers.add("client_secret", SecretSingl.getClientSecret());
//        headers.add("client_id", client_id);
//        headers.add("redirect_uri", backUrl);
//        headers.add("scope", scope);
//        headers.add("response_type", "code");
//        headers.add("timestamp", timestamp.toString());
//        headers.add("access_type", "online");
//
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        final HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
//
//        String url = host + AUTH_GET_CODE;
//
//        ResponseEntity e2 = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        OAuthClientRequest request = OAuthClientRequest.authorizationLocation(host + ESIA_CODE_POINT)
                .setClientId(client_id)
                .setParameter("client_secret", sign)
                .setRedirectURI(backUrl)
                .setScope(scope)
                .setResponseType("code")
                .setState(STATE)
                .setParameter("timestamp", timestamp)
                .setParameter("access_type", accessType)
                .buildQueryMessage();

        System.out.println("----> 4. esiaTest - send to esia: - > " + request.getLocationUri() + "; body: -> " + request);

        return new ModelAndView(new RedirectView(request.getLocationUri()));

//        return new ModelAndView();
    }

    @GetMapping(value = "/recieveCode")
    public ModelAndView recieveCode(HttpResponse response) {


        return new ModelAndView();
    }

    @RequestMapping(value = "/esiacallback", method = RequestMethod.GET)
    public void handleRedirect(HttpServletRequest request)
            throws OAuthSystemException, OAuthProblemException {
        System.out.println("----> 5. esia-ok: - > " + request);
        OAuthAuthzResponse oar;
        try {
            oar = OAuthAuthzResponse.oauthCodeAuthzResponse(request);
            String code = oar.getCode();
            String state = oar.getState();
            if (STATE.equals(state)) {
                String timestamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss Z").format(new Date());
                String clientSecret = SecretSingl.getClientSecret();
                OAuthClientRequest oAuthClientRequest;
                oAuthClientRequest = OAuthClientRequest
                        .tokenLocation(ESIA_SERV + ESIA_TOKEN_POINT)
                        .setClientId(CLIENT_ID)
                        .setCode(code)
                        .setGrantType(GrantType.AUTHORIZATION_CODE)
                        .setClientSecret(clientSecret)
                        .setParameter("state", STATE)
                        .setRedirectURI(REDIRECT_URL)
                        .setScope(SCOPE)
                        .setParameter("timestamp", timestamp)
                        .setParameter("token_type", "Bearer")
                        .setParameter("access_type", ACCESS_TYPE)
                        .buildBodyMessage();

                OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
                OAuthJSONAccessTokenResponse oauthResponse = oAuthClient
                        .accessToken(oAuthClientRequest, OAuth.HttpMethod.POST, OAuthJSONAccessTokenResponse.class);
                String[] accessParts = oauthResponse.getAccessToken().split("\\.");
                ObjectMapper mapper = new ObjectMapper();
                HttpClient client = HttpClientBuilder.create().build();
                byte[] decode = Base64.decodeBase64(accessParts[1]);
//                        Base64.getDecoder().decode(accessParts[1]);
                Map<String, String> info = mapper.readValue(new String(decode, "UTF-8"),
                        new TypeReference<Map<String, String>>() {
                        });
                String username = info.get("urn:esia:sbj_id");
                Map<String, Object> userInfo = getUserInfo(client, mapper, username, oauthResponse);

                System.out.println("----> 6. esia-ok: - > x3 -> " + userInfo);
            }
        } catch (OAuthProblemException | OAuthSystemException | IOException ignored) {
            //NOP
        }
    }

    private Map<String, Object> getUserInfo(HttpClient client, ObjectMapper mapper, String username,
                                            OAuthJSONAccessTokenResponse oauthResponse) throws IOException {
        HttpGet get = new HttpGet(ESIA_SERV + ESIA_INFO_POINT + username);
        String result = getResponse(client, oauthResponse, get);
        Map<String, Object> userMail = getUserEmail(client, mapper, username, oauthResponse);
        Map<String, Object> userInfo = mapper.readValue(result,
                new TypeReference<Map<String, Object>>() {
                });
        if (userMail != null && userMail.containsKey("value")) {
            if ("EML".equals(userMail.get("type"))) {
                userInfo.put("email", userMail.get("value"));
            }
        }
        return userInfo;
    }

    private String getResponse(HttpClient client, OAuthJSONAccessTokenResponse oauthResponse, HttpGet get) throws IOException {
        get.addHeader(OAuth.HeaderType.CONTENT_TYPE, OAuth.ContentType.URL_ENCODED);
        get.addHeader(OAuth.HeaderType.AUTHORIZATION, String.format("%s %s", oauthResponse.getTokenType(), oauthResponse.getAccessToken()));
        HttpResponse response = client.execute(get);
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = response.getEntity().getContent().read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }

    private Map<String, Object> getUserEmail(HttpClient client, ObjectMapper mapper, String username,
                                             OAuthJSONAccessTokenResponse oauthResponse) throws IOException {
        HttpGet get = new HttpGet(ESIA_SERV + ESIA_INFO_POINT + username + "/ctts");
        String result = getResponse(client, oauthResponse, get);
        Map<String, Object> map = new ObjectMapper().readValue(result,
                new TypeReference<Map<String, Object>>() {
                });
        String uri;
        try {
            uri = ((ArrayList) map.get("elements")).get(0).toString();
        } catch (Exception e) {
            return null;
        }
        get = new HttpGet(uri);
        result = getResponse(client, oauthResponse, get);
        return mapper.readValue(result, new TypeReference<Map<String, Object>>() {
        });
    }


    @GetMapping(value = "/getCode2")
    @ResponseBody
    public String getCode2(@RequestParam(value = "hostSigner2") String hostSigner,
                           @RequestParam(value = "bearer2") String bearer) throws OAuthSystemException, IOException {
        String timestamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss Z").format(new Date());

        String client_secret = SCOPE + timestamp + CLIENT_ID + STATE;
        System.out.println("----> 1. esiaTest2 - go to sign server - > " + client_secret);

        String encodeBase64String = Base64.encodeBase64String(client_secret.getBytes());

        String output = getSign(hostSigner, bearer, encodeBase64String);
        return output;
    }

    private String getSign(String hostSigner, String bearer, String encodeBase64String) throws IOException {
        URL url = new URL(hostSigner);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/octet-stream");
        conn.setRequestProperty("Accept", "application/octet-stream");
        conn.setRequestProperty("Authorization", bearer);

        OutputStream os = conn.getOutputStream();
        os.write(encodeBase64String.getBytes());
        os.flush();

        StringBuffer sb=new StringBuffer();

        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        System.out.println("----> 2. signTest2 - go to sign server " + encodeBase64String);
        String output;
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            sb.append(output);
            System.out.println("----> 3. signTest - signed response: - > " + output);
        }
        conn.disconnect();

        System.out.println("----> 3.1 signTest - signed response: - > " + output);
        System.out.println("----> 3.2 signTest - signed response: - > " + sb.toString());
        return sb.toString();
    }

}