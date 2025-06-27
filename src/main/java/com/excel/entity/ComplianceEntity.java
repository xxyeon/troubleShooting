package com.excel.entity;

import java.util.UUID;

import com.excel.common.Constants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "compliance")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ComplianceEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "policy_id", nullable = true)
	private Long policyId;

	@Column(name = "resource_id", nullable = false, length = Constants.LENGTH_SMALL)
	private String resourceId;

	@Getter
	@Column(length = Constants.LENGTH_SMALL, nullable = false)
	private String rid;

	@Column(name = "scan_time", nullable = false)
	private String scanTime;

	@Column(name = "vulnerability_status", nullable = false, length = Constants.LENGTH_SMALL)
	private String vulnerabilityStatus;

	@Column(name = "policy_detail", nullable = true)
	private Long policyDetail;

	@Column(name = "policy_type", nullable = false, length = Constants.LENGTH_SMALL)
	private String policyType;

	@Column(name = "account_name", nullable = false, length = Constants.LENGTH_SMALL)
	private String accountName;

	@Column(nullable = false, length = Constants.LENGTH_SMALL)
	private String client;

	@Column(name = "policy_severity", nullable = false, length = Constants.LENGTH_SMALL)
	private String policySeverity;

	@Column(name = "policy_compliance", nullable = true, columnDefinition = "TEXT")
	private String policyCompliance;

	@Column(name = "policy_compliance_detail", nullable = true, columnDefinition = "TEXT")
	private String policyComplianceDetail;

	@Column(name = "policy_title", nullable = false, length = Constants.LENGTH_SMALL)
	private String policyTitle;

	@Column(name = "account_id", nullable = false, length = Constants.LENGTH_SMALL)
	private String accountId;

	@Column(nullable = false, length = Constants.LENGTH_SMALL)
	private String service;

	@Column(name = "policy_description", nullable = true, columnDefinition = "TEXT")
	private String policyDescription;

	@Column(name = "policy_response", nullable = true, columnDefinition = "TEXT")
	private String policyResponse;

	// 스캔 고유 번호
	private UUID scanId;

	@Builder
	private ComplianceEntity(Long policyId, String resourceId, String rid, String scanTime, String vulnerabilityStatus,
		Long policyDetail, String policyType, String accountName, String client, String policySeverity,
		String policyCompliance, String policyComplianceDetail, String policyTitle, String accountId, String service,
		String policyDescription, String policyResponse) {
		this.policyId = policyId;
		this.resourceId = resourceId;
		this.rid = rid;
		this.scanTime = scanTime;
		this.vulnerabilityStatus = vulnerabilityStatus;
		this.policyDetail = policyDetail;
		this.policyType = policyType;
		this.accountName = accountName;
		this.client = client;
		this.policySeverity = policySeverity;
		this.policyCompliance = policyCompliance;
		this.policyComplianceDetail = policyComplianceDetail;
		this.policyTitle = policyTitle;
		this.accountId = accountId;
		this.service = service;
		this.policyDescription = policyDescription;
		this.policyResponse = policyResponse;
	}
}
