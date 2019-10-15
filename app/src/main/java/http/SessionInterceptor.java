package http;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import util.SpUtils;

/**
 * Created by maoqi on 2019/10/14.
 */
public class SessionInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String cookie = SpUtils.getString("cookie", "");
        Request.Builder builder = request.newBuilder();
        if (!cookie.isEmpty()) {
            builder.addHeader("cookie", cookie);
        }

        Response proceed = chain.proceed(builder.build());

        Headers headers = proceed.headers();
        String header = headers.get("Set-Cookie");
        if (header != null) {
            SpUtils.putString("cookie", header.substring(0, header.indexOf(";")));
        }

        return proceed;
    }
}
