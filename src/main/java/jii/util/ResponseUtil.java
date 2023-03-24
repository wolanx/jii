package jii.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import jii.web.ApiData;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * @author admin
 */
public class ResponseUtil {

    public static void flush(HttpServletResponse resp, ApiData json) throws IOException {
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
        resp.setCharacterEncoding("utf-8");

        resp.setStatus(json.getStatus());
        resp.getWriter().print(new ObjectMapper().writeValueAsString(json));

        resp.flushBuffer();
    }

}
