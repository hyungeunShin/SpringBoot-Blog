package com.example.springbootdeveloper.dto;

import com.example.springbootdeveloper.domain.Article;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

/*
    @NotNull            : null X
    @NotEmpty           : null, 공백 또는 공백만으로 채워진 문자열 X
    @NotBlank           : null, 공백 X
    @Size(min=?, max=?) : 최소, 최대길이 제한
    @Null               : null만 가능
    
    @Positive           : 양수만
    @PositiveOrZero     : 양수와 0만
    @Negative           : 음수
    @NegativeOrZero     : 음수와 0만
    @Min(?)             : 최소
    @Max(?)             : 최대
    
    @Email              : 이메일 형식만
    @Pattern(regexp="?"): 직접 작성한 정규식에 맞는 문자열
*/

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest {
    @NotNull
    @Size(min = 1, max = 10)
    private String title;

    @NotNull
    private String content;

    public Article toEntity(String author) {
        return Article.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}
