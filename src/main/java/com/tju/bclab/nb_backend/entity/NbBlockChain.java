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
 * 区块链表
 * </p>
 *
 * @author 范霆昱测试代码生成器
 * @since 2022-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="NbBlockChain对象", description="区块链表")
public class NbBlockChain implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "交易id")
    @TableId(value = "transaction_id", type = IdType.INPUT)
    private String transactionId;

    @ApiModelProperty(value = "块hash")
    private String blockHash;

    @ApiModelProperty(value = "链状态")
    private String chainStatus;

    @ApiModelProperty(value = "块高")
    private Long blockHeight;


}
