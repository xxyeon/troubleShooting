package com.excel.v1.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.excel.v1.common.Constants;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "resource_config")
public class ResourceConfigEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "command_1", columnDefinition = "Text")
	private String command1;

	@Column(name = "command_2", columnDefinition = "Text")
	private String command2;

	@Column(name = "status", length = Constants.LENGTH_SMALL)
	private String status;

	@Column(name = "scan_time", length = Constants.LENGTH_SMALL)
	private LocalDateTime scanTime;

	@Column(name = "account_name", length = Constants.LENGTH_SMALL)
	private String accountName;

	@Column(name = "account_aws_id", length = Constants.LENGTH_SMALL)
	private String accountId;

	@Column(name = "resource_id", length = Constants.LENGTH_SMALL)
	private String resourceId;

	@Column(name = "resource_json", columnDefinition = "Text")
	private String resourceJson;

	@Column(name = "resource_key1", columnDefinition = "Text")
	private String resourceKey1;

	@Column(name = "resource_key2", columnDefinition = "Text")
	private String resourceKey2;

	@Column(name = "resource_key3", columnDefinition = "Text")
	private String resourceKey3;

	@Column(name = "resource_key4", columnDefinition = "Text")
	private String resourceKey4;

	@Column(name = "resource_key5", columnDefinition = "Text")
	private String resourceKey5;

	@Column(name = "service", length = Constants.LENGTH_SMALL)
	private String service;

	@Column(name = "service_detail", length = Constants.LENGTH_SMALL)
	private String serviceDetail;

	@Column(name = "service_id", length = Constants.LENGTH_SMALL)
	private String serviceId;

	@Column(name = "is_success")
	private Boolean isSuccess;

	@ManyToOne
	@JoinColumn(name = "account_id")
	private AccountsEntity accountsEntity;

	@OneToMany(mappedBy = "resourceConfigEntity", cascade = CascadeType.PERSIST)
	private List<DescribeEntity> describeEntityList = new ArrayList<>();

}