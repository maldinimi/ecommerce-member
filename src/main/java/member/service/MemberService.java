package member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import member.entity.MemberEntity;
import member.repo.MemberRepo;
import member.request.MemberRequest;

@Service
public class MemberService {

	@Autowired
	private MemberRepo memberRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public MemberEntity save(MemberEntity member) {
		this.memberRepo.save(member);
		return member;
	}

	public MemberEntity signup(MemberRequest request) {
		MemberEntity member = new MemberEntity();

		member.setMobileNumber(request.getMobileNumber());
		member.setEmail(request.getEmail());
		member.setFirstName(request.getFirstName());
		member.setLastName(request.getLastName());
		member.setGender(request.getGender());
		member.setDateOfBirth(request.getDateOfBirth());
		member.setPassword(passwordEncoder.encode(request.getPassword()));

		return this.memberRepo.save(member);
	}

	public MemberEntity update(MemberRequest request) {
		MemberEntity member = this.findById(request.getId());

		member.setMobileNumber(request.getMobileNumber());
		member.setEmail(request.getEmail());
		member.setFirstName(request.getFirstName());
		member.setLastName(request.getLastName());
		member.setGender(request.getGender());
		member.setDateOfBirth(request.getDateOfBirth());

		if (request.getPassword() != null) {
			member.setPassword(passwordEncoder.encode(request.getPassword()));
		}

		return this.memberRepo.save(member);
	}

	public MemberEntity findByMobileNumberOrEmail(String mobileNumber, String email) {
		return this.memberRepo.findByMobileNumberOrEmail(mobileNumber, email);
	}

	public Integer countByMobileNumberOrEmail(String mobileNumber, String email) {
		return this.memberRepo.countByMobileNumberOrEmail(mobileNumber, email);
	}

	public MemberEntity findById(Long id) {
		return this.memberRepo.findById(id).get();
	}
}
