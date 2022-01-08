package com.tju.bclab.nb_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 区块链关联表
 * </p>
 *
 * @author 范霆昱测试代码生成器
 * @since 2022-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DataChain对象", description="区块链关联表")
public class DataChain implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "对应宁波id")
    @TableId(value = "data_id", type = IdType.INPUT)
    private String dataId;

    @ApiModelProperty(value = "对应交易id")
    private String transactionId;


}
