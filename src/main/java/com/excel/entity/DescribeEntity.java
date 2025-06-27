package com.excel.entity;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.excel.common.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "resources_describe", schema = "cspm")
public class DescribeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "scan_time", length = Constants.LENGTH_SMALL, nullable = false)
	private String scanTime;

	@Column(name = "account_id", length = Constants.LENGTH_SMALL, nullable = false)
	private String accountId;

	@Column(name = "account_name", length = Constants.LENGTH_SMALL, nullable = false)
	private String accountName;

	@Column(name = "client", length = Constants.LENGTH_SMALL)
	private String client;

	@Column(name = "client_code", length = Constants.LENGTH_SMALL)
	private String clientCode;

	@Column(name = "resource_id", columnDefinition = "Text")
	private String resourceId;

	@Column(name = "availability_zone", length = Constants.LENGTH_SMALL)
	private String availabilityZone;

	@Column(name = "service", length = Constants.LENGTH_SMALL, nullable = false)
	private String service;

	@Column(name = "service_detail", length = Constants.LENGTH_SMALL, nullable = false)
	private String serviceDetail;

	@Column(name = "tag", columnDefinition = "Text")
	private String tag;

	@Column(name = "resource_json", columnDefinition = "Text")
	private String resourceJson;

	@Column(name = "resource_value1", columnDefinition = "Text")
	private String resourceValue1;

	@Column(name = "resource_value2", columnDefinition = "Text")
	private String resourceValue2;

	@Column(name = "resource_value3", columnDefinition = "Text")
	private String resourceValue3;

	@Column(name = "resource_value4", columnDefinition = "Text")
	private String resourceValue4;

	@Column(name = "resource_value5", columnDefinition = "Text")
	private String resourceValue5;

	// 스캔 고유 번호
	@Column(name = "scan_id", columnDefinition = "Text")
	private UUID scanId;

	@ManyToOne
	@JoinColumn(name = "resource_config_id", nullable = true)
	private ResourceConfigEntity resourceConfigEntity;

	// 최신 데이터와 스캔 데이터 비교를 위한 메서드
	@Override
	public boolean equals(Object o) {
		DescribeEntity that = (DescribeEntity) o;
		if (that == null) return false;
		return Objects.equals(client, that.client) &&
			Objects.equals(clientCode, that.clientCode) &&
			Objects.equals(accountId, that.accountId) &&
			Objects.equals(accountName, that.accountName) &&
			Objects.equals(service, that.service) &&
			Objects.equals(serviceDetail, that.serviceDetail) &&
			Objects.equals(tag, that.tag) &&
			compareJson(resourceJson, that.resourceJson);
	}

	private boolean compareJson(String json1, String json2) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			// JSON 문자열을 Map으로 변환하여 비교
			Map<String, String> map1 = mapper.readValue(json1, Map.class);
			Map<String, String> map2 = mapper.readValue(json2, Map.class);
			return map1.equals(map2);
		} catch (JsonProcessingException e) {
			e.fillInStackTrace();
			return false;
		}
	}
}
