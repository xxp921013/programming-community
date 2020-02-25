package com.xu.majiangcommunity;

import com.xu.majiangcommunity.enums.ExcetionEnmu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GlobalException extends RuntimeException {
    private ExcetionEnmu excetionEnmu;
}
