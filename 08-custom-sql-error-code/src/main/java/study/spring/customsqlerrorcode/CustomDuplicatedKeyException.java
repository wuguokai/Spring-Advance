package study.spring.customsqlerrorcode;

import org.springframework.dao.DuplicateKeyException;

/**
 * @author wuguokai
 */
public class CustomDuplicatedKeyException extends DuplicateKeyException {
    public CustomDuplicatedKeyException(String msg) {
        super(msg);
    }

    public CustomDuplicatedKeyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
