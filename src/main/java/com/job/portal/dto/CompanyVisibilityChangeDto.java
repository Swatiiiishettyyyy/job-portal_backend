package com.job.portal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyVisibilityChangeDto {

	private long companyId;

	private boolean visibility;
}
