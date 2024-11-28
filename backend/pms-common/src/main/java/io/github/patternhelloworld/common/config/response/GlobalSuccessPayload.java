package io.github.patternhelloworld.common.config.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class GlobalSuccessPayload<T> {

    private T data;
    private Date timestamp;
    private String details;

    public GlobalSuccessPayload(T data) {
        this.data = data;
        this.timestamp = TimestampUtil.getPayloadTimestamp();
        this.details = getRequestUri();
    }

    public GlobalSuccessPayload(String dataKey, T dataValue) {
        this.data = (T) new HashMap<String, T>();
        ((Map<String, T>)this.data).put(dataKey, dataValue);
        this.timestamp = TimestampUtil.getPayloadTimestamp();
        this.details = getRequestUri();
    }


    private String getRequestUri() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return attributes.getRequest().getRequestURI();
        } catch (Exception e){
            // details 파라매터의 값은 다만 형식 적이기 때문에 로그도 남기지 않는다.
        }
        return null;
    }

}
