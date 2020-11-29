package member.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import member.entity.MemberEntity;

@Repository
public interface MemberRepo extends JpaRepository<MemberEntity, Long> {

	public MemberEntity findByMobileNumberOrEmail(String mobileNumber, String email);

	public Integer countByMobileNumberOrEmail(String mobileNumber, String email);
}
