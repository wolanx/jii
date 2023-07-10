package jii.web;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.ToString;

/**
 * @author admin
 */
@ToString
public class ApiData {

    @Getter
    private int status = 200;
    @Getter
    private int code = 0;
    @Getter
    private String message;
    @Getter
    private Object data;

    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object pagination;

    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object traceback;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPagination(Object pagination) {
        this.pagination = pagination;
    }

    public void setTraceback(Exception e) {
        this.traceback = Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString);
    }

    // ins
    public ApiData() {}

    public ApiData(String message) {
        this.setMessage(message);
    }

    public ApiData(String message, int status) {
        this.setMessage(message);
        this.setStatus(status);
    }

    // quick

    public static ApiData success(String message) {
        return new ApiData(message);
    }

    public static ApiData success(Object data) {
        ApiData ret = new ApiData("success");
        ret.setData(data);
        return ret;
    }

    public static ApiData success(Object data, String message) {
        ApiData ret = new ApiData(message == null ? "success" : message);
        ret.setData(data);
        return ret;
    }

    public static ApiData error(String message, int status) {
        return new ApiData(message, status);
    }

    public static ApiData pageable(Page<?> res) {
        ApiData ret = new ApiData("success");
        ret.setData(res.getContent());

        Map<String, Integer> p = new LinkedHashMap<>();
        p.put("page", res.getPageable().getPageNumber() + 1);
        p.put("pageCount", res.getTotalPages());
        p.put("pageSize", res.getSize());
        p.put("total", (int)res.getTotalElements());
        ret.setPagination(p);

        return ret;
    }

}
