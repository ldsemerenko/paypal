import com.paypal.api.openidconnect.Session;
import com.paypal.api.openidconnect.Tokeninfo;
import com.paypal.api.openidconnect.Userinfo;
import com.paypal.api.payments.CreditCard;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String clientId = "AQtbjPgLZMZx93Zwj3d5vY5pe9JKNBvrd83CcADbKADqqwYjGu6JBnlA6B19VEIapnDWXmVqGD20pDBJ";
        String clientSecret = "EGZ4gsmdLCAzaOSTdO331G9UfYxlKU99AdVFpBr7iWH5Abn8A00a8mt6pjTibE_JN9Sf8ubGmcE_l0YJ";

        CreditCard card = new CreditCard().setType("visa").setNumber("4417119669820331").setExpireMonth(11).setExpireYear(2019).setCvv2(012).setFirstName("Joe").setLastName("Shopper");

        APIContext context = null;
        try {
            context = new APIContext(clientId, clientSecret, "sandbox");
            card.create(context);
            System.out.println(card.toJSON());
        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
        }

        List<String> scopes = new ArrayList<String>() {{
            /**
             * 'openid'
             * 'profile'
             * 'address'
             * 'email'
             * 'phone'
             * 'https://uri.paypal.com/services/paypalattributes'
             * 'https://uri.paypal.com/services/expresscheckout'
             * 'https://uri.paypal.com/services/invoicing'
             */
            add("openid");
            add("profile");
            add("email");
        }};
        String redirectUrl = Session.getRedirectURL("UserConsent", scopes, context);
        System.out.println(redirectUrl);



        Tokeninfo info = null;
        try {
            info = Tokeninfo.createFromAuthorizationCode(context, "");
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        String accessToken = info.getAccessToken();
        String refreshToken = info.getRefreshToken();

        System.out.println(accessToken + "  " + refreshToken);

        // Initialize apiContext with proper credentials and environment. Also, set the refreshToken retrieved from previous step.
        APIContext userAPIContext = new APIContext(clientId, clientSecret, "sandbox").setRefreshToken(info.getRefreshToken());

        Userinfo userinfo = null;
        try {
            userinfo = Userinfo.getUserinfo(userAPIContext);
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        System.out.println(userinfo);

    }
}
