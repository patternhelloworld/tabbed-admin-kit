package io.github.patternhelloworld.tak.domain.common.user.entity;


import io.github.patternhelloworld.tak.config.database.typeconverter.ManagementDepartment;
import io.github.patternhelloworld.tak.config.database.typeconverter.ViewPermission;
import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.common.dept.entity.Dept;


import io.github.patternhelloworld.tak.domain.common.user.dto.UserCommonDTO;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name="user", uniqueConstraints = @UniqueConstraint(columnNames = "userId"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User
{
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "user_idx")
	private Long userIdx;

	@Column(name = "user_id")
	private String userId;

	@Column(name="name")
	private String name;

	@Column(name="otp_secret_key")
	private String otpSecretKey;

	@Column(name="otp_secret_qr_url")
	private String otpSecretQrUrl;

	@Column(name="description")
	private String description;

	@Embedded
	private Password password;


	@Column(name = "dept_idx", insertable = false, updatable = false)
	private Integer deptIdx;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dept_idx")
	private Dept dept;

	@Column(name = "position", length = 255)
	private String position;

	@Column(name = "birth_date")
	private LocalDate birthDate;

	@Column(name = "joining_date")
	private LocalDate joiningDate;

	@Column(name = "resignation_date")
	private LocalDate resignationDate;

	@Column(name = "phone_number", length = 20)
	private String phoneNumber;

	@Column(name = "zipcode", length = 10)
	private String zipcode;

	@Column(name = "addr1", length = 150)
	private String addr1;

	@Column(name = "addr2", length = 150)
	private String addr2;

	@Column(name = "addr_si")
	private String addrSi;

	@Column(name = "addr_gugun")
	private String addrGugun;

	@Column(name = "addr_bname")
	private String addrBname;

	@Enumerated(EnumType.STRING)
	@Column(name = "management_department")
	private ManagementDepartment managementDepartment;

	@Enumerated(EnumType.STRING)
	@Column(name = "view_permission")
	private ViewPermission viewPermission;


	@Column(name = "reg_dt", nullable = true)
	private LocalDateTime regDt;

	@Column(name = "reg_user_id", nullable = true, length = 100)
	private String regUserId;

	@Column(name = "mod_dt", nullable = true)
	private LocalDateTime modDt;

	@Column(name = "mod_user_id", nullable = true, length = 100)
	private String modUserId;

	@Column(name = "del_dt", nullable = true)
	private LocalDateTime delDt;
	@Column(name = "del_user_id", nullable = true, length = 100)
	private String delUserId;
	@Enumerated(EnumType.STRING)
	@Column(name = "del_yn", nullable = false, columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
	private YNCode delYn = YNCode.N;


	@Column(name = "out_dt", nullable = true)
	private LocalDate outDt;
	@Enumerated(EnumType.STRING)
	@Column(name = "out_yn", nullable = false, columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
	private YNCode outYn = YNCode.N;

	// updateUser 메서드 추가
	public void updateUser(UserCommonDTO.OneWithDeptDealer dto, Dept dept, String modUserId) {
		this.userId = dto.getUserId();
		if (dto.getPassword() != null) {
			this.password = Password.builder()
					.value(dto.getPassword())
					.changedDate(LocalDateTime.now())
					.failedCount(dto.getPasswordFailedCount())
					.build();
		}
		this.name = dto.getName();
		this.position = dto.getPosition();
		this.joiningDate = dto.getJoiningDate();
		this.birthDate = dto.getBirthDate();
		this.resignationDate = dto.getResignationDate();
		this.phoneNumber = dto.getPhoneNumber();
		this.zipcode = dto.getZipcode();
		this.addr1 = dto.getAddr1();
		this.addr2 = dto.getAddr2();
		this.addrSi = dto.getAddrSi();
		this.addrGugun = dto.getAddrGugun();
		this.addrBname = dto.getAddrBname();
		this.managementDepartment = ManagementDepartment.valueOf(dto.getManagementDepartment());
		this.viewPermission = ViewPermission.valueOf(dto.getViewPermission());
		this.regDt = dto.getRegDt();
		this.regUserId = dto.getRegUserId();
		this.modDt = LocalDateTime.now();
		this.modUserId = modUserId;
		this.delDt = dto.getDelDt();
		this.delUserId = dto.getDelUserId();
		this.delYn = dto.getDelDt() != null ? YNCode.Y : YNCode.N;
		this.outDt = dto.getOutDt();
		this.outYn = dto.getOutYn();

		this.dept = dept;
	}

}
