package study.spring.simplejdbcdemo.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author wuguokai
 */
@Data
@Builder
public class Foo {
    private Long id;
    private String bar;
}
