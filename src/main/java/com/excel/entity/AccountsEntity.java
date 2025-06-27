package com.excel.entity;

import java.util.ArrayList;
import java.util.List;

import com.excel.common.Constants;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "accounts", schema = "cspm")
public class AccountsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/*
	 * 고객사 등록
	 */
	@Column(name = "account_id", length = Constants.LENGTH_SMALL, nullable = false)
	private String accountId;

	@Column(name = "client", length = Constants.LENGTH_SMALL, nullable = false)
	private String client;

	@Column(name = "code", length = Constants.LENGTH_SMALL, nullable = false)
	private String code;

	@Column(name = "account_name", length = Constants.LENGTH_SMALL, nullable = false)
	private String accountName;

	@Column(name = "access_key", length = Constants.LENGTH_MIDDLE, nullable = false)
	private String accessKey;

	@Column(name = "secret_key", length = Constants.LENGTH_MIDDLE, nullable = false)
	private String secretKey;

	@Column(name = "region", length = Constants.LENGTH_SMALL, nullable = false)
	private String region;

	// 비고
	@Column(name = "comment", columnDefinition = "TEXT")
	private String comment;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	/*
	 * '스캔 설정' 페이지
	 */
	// Account 등록 시간
	@Column(name = "register_time", length = Constants.LENGTH_SMALL, nullable = false)
	private String registerTime;

	// 최근 리소스 스캔 시간
	@Column(name = "last_update_describe_time", length = Constants.LENGTH_SMALL, nullable = false)
	private String lastUpdateDescribeTime;

	// 스캔 결과
	@Column(name = "describe_result", length = Constants.LENGTH_SMALL, nullable = false)
	private String describeResult;

	@OneToMany(mappedBy = "accountsEntity", cascade = CascadeType.ALL)
	private List<ResourceConfigEntity> resourceConfigs = new ArrayList<>();

}
