package com.tju.bclab.nb_backend.service;

import com.tju.bclab.nb_backend.entity.NbBlockChain;
import com.tju.bclab.nb_backend.entity.NbData;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tju.bclab.nb_backend.vo.AddDataReq;

/**
 * <p>
 * 宁波数据表 服务类
 * </p>
 *
 * @author 范霆昱测试代码生成器
 * @since 2022-01-08
 */
public interface NbDataService extends IService<NbData> {
    String addData(AddDataReq addDataReq) throws Exception;
    NbBlockChain queryChain(String DataId) throws Exception;
}
