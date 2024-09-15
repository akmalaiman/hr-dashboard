package aaiman.hrdashboardapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponseDto {

        private String accessToken;
        private String authorities;
        private int status;
        private boolean ok;

}
