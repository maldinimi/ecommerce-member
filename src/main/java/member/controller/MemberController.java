package member.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import member.entity.MemberEntity;
import member.request.MemberRequest;
import member.response.GeneralResponse;
import member.service.MemberService;

@RestController
public class MemberController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// public
	@PostMapping("/signup")
	public String signup(@RequestBody MemberRequest request) throws JsonProcessingException {
		GeneralResponse<MemberEntity> response = new GeneralResponse<>();

		Integer checkMember = this.memberService.countByMobileNumberOrEmail(request.getMobileNumber(),
				request.getEmail());
		if (checkMember > 0) {
			response.setResponseCode("XX");
			response.setResponseMessage("Member already exist");
		} else {
			response.setData(this.memberService.signup(request));
			response.setResponseCode("00");
			response.setResponseMessage("Signup success");
		}

		return new ObjectMapper().writeValueAsString(response);
	}

	// public
	@PostMapping("/login")
	public String login(@RequestBody MemberRequest request) throws JsonProcessingException {
		GeneralResponse<MemberEntity> response = new GeneralResponse<>();

		MemberEntity member = this.memberService.findByMobileNumberOrEmail(request.getMobileNumber(),
				request.getEmail());
		if (member == null) {
			response.setResponseCode("XX");
			response.setResponseMessage("Member not registered");
		} else {
			if (passwordEncoder.matches(request.getPassword(), member.getPassword())) {
				member.setSessionKey(UUID.randomUUID().toString());

				response.setData(this.memberService.save(member));
				response.setResponseCode("00");
				response.setResponseMessage("Login success");
			} else {
				response.setResponseCode("XX");
				response.setResponseMessage("Password did not match with registered member");
			}
		}

		return new ObjectMapper().writeValueAsString(response);
	}

	// auth
	@PostMapping("/update")
	public String update(@RequestBody MemberRequest request) throws JsonProcessingException {
		GeneralResponse<MemberEntity> response = new GeneralResponse<>();

		MemberEntity memberValid = this.memberService.findById(request.getId());
		if (!request.getSessionKey().equals(memberValid.getSessionKey())) {
			response.setResponseCode("XX");
			response.setResponseMessage("Authentication failed");
		} else {
			response.setData(this.memberService.update(request));
			response.setResponseCode("00");
			response.setResponseMessage("Update success");
		}

		return new ObjectMapper().writeValueAsString(response);
	}

	// auth
	@PostMapping("/view")
	public String view(@RequestBody MemberRequest request) throws JsonProcessingException {
		GeneralResponse<MemberEntity> response = new GeneralResponse<>();

		MemberEntity memberValid = this.memberService.findById(request.getId());
		if (!request.getSessionKey().equals(memberValid.getSessionKey())) {
			response.setResponseCode("XX");
			response.setResponseMessage("Authentication failed");
		} else {
			response.setData(memberValid);
			response.setResponseCode("00");
			response.setResponseMessage("View success");
		}

		return new ObjectMapper().writeValueAsString(response);
	}
}
