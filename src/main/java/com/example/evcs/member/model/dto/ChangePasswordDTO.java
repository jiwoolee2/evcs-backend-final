package com.example.evcs.member.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class ChangePasswordDTO {
	
	private String currentPassword;
	
	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Size(min = 4, max = 20, message = "비밀번호는 4자 이상 20자 이하로 입력해주세요.")
	@Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]*$", message = "비밀번호는 영어, 숫자, 특수문자만 사용할 수 있습니다.")
	private String newPassword;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Size(min = 4, max = 20, message = "비밀번호는 4자 이상 20자 이하로 입력해주세요.")
	@Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]*$", message = "비밀번호는 영어, 숫자, 특수문자만 사용할 수 있습니다.")
	private String confirmNewPassword;


}
