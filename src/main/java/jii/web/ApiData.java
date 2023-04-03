package jii.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author admin
 */
public class ApiData {

    @Getter
    private int status = 200;
    @Getter
    private final int code = 0;
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

    public void setData(Object data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPagination(Object pagination) {
        this.pagination = pagination;
    }

    public void setTraceback(Object traceback) {
        this.traceback = traceback;
    }

    // ins

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
        p.put("total", (int) res.getTotalElements());
        ret.setPagination(p);

        return ret;
    }

}