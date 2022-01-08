package com.tju.bclab.nb_backend.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 宁波数据表
 * </p>
 *
 * @author 范霆昱测试代码生成器
 * @since 2022-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="NbData对象", description="宁波数据表")
public class NbData implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "宁波数据id")
    private String dataId;

    @ApiModelProperty(value = "创建者id")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新者id")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @ApiModelProperty(value = "0未删除 1已删除")
    private Boolean isDelete;

    @ApiModelProperty(value = "任务id")
    private String taskId;

    private String taskDefKey;

    @ApiModelProperty(value = "实例id")
    private String instanceId;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    private Boolean suspendState;

    private String currentUserList;

    private String currentRoleList;

    @ApiModelProperty(value = "当前用户")
    private String currentUser;

    @ApiModelProperty(value = "注册id")
    private String registerId;

    @ApiModelProperty(value = "注册名称")
    private String registerName;

    @ApiModelProperty(value = "注册时间")
    private Date registerTime;

    @ApiModelProperty(value = "组织id")
    private String orgId;

    @ApiModelProperty(value = "组织名称")
    private String orgName;

    private String displayMode;

    private Date displayTime;

    @ApiModelProperty(value = "公开时间")
    private Date publicTime;

    @ApiModelProperty(value = "题目")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "评论")
    private String remark;

    private Boolean state;

    @TableField("fileUpload")
    private String fileUpload;


}
