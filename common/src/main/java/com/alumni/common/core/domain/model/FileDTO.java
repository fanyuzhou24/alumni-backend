package com.alumni.common.core.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 *
 * @author: LiFeng
 */
@Data
@ApiModel("文件")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {
    @ApiModelProperty("文件名")
    private String name;

    @ApiModelProperty("文件URL")
    @NotBlank
    private String url;
}
