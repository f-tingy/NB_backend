package com.tju.bclab.nb_backend.service.impl;

import com.tju.bclab.nb_backend.common.ResultCode;
import com.tju.bclab.nb_backend.entity.DataChain;
import com.tju.bclab.nb_backend.entity.NbBlockChain;
import com.tju.bclab.nb_backend.entity.NbData;
import com.tju.bclab.nb_backend.io.aelf.schemas.KeyPairInfo;
import com.tju.bclab.nb_backend.mapper.DataChainMapper;
import com.tju.bclab.nb_backend.mapper.NbBlockChainMapper;
import com.tju.bclab.nb_backend.mapper.NbDataMapper;
import com.tju.bclab.nb_backend.service.NbDataService;
import com.tju.bclab.nb_backend.exception.ApiException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tju.bclab.nb_backend.vo.AddDataReq;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 宁波数据表 服务实现类
 * </p>
 *
 * @author 范霆昱测试代码生成器
 * @since 2022-01-08
 */
@Service
public class NbDataServiceImpl extends ServiceImpl<NbDataMapper, NbData> implements NbDataService {
    @Autowired
    private NbDataMapper nbDataMapper;

    @Autowired
    private BlockChainServiceImpl blockChainService;

    @Autowired
    private DataChainMapper dataChainMapper;

    @Autowired
    private NbBlockChainMapper nbBlockChainMapper;

    //private String  privateKey="5fef898f1181ed737256be78e568c60737d19672147bf4b6ca79910c7a7c1437";
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String addData(AddDataReq addDataReq) throws Exception {
        //保存前端传的数据信息
        NbData nbData=new NbData();
        BeanUtils.copyProperties(addDataReq, nbData);
        int insertData = nbDataMapper.insert(nbData);
        // 如果insert小于0，则导入失败
        if (insertData < 0) {
            throw new ApiException(ResultCode.STORE_FAILED);
        }

//        KeyPairInfo wallet = blockChainService.createWallet();
//        try {
//            NbBlockChain flag = blockChainService.AddUserContract(wallet.getPrivateKey());
//        } catch (Exception e) {
//            throw new ApiException(ResultCode.CONTRACT_CALL_FAILED);
//        }
//
//        //数据上链
//        String hash=makeHash(addDataReq);
//        NbBlockChain nbBlockChain = blockChainService.VoteContract(hash, wallet.getPrivateKey());
//        //新增数据与区块链的关系表数据
//        DataChain dataChain=new DataChain();
//        dataChain.setDataId(addDataReq.getDataId());
//        dataChain.setTransactionId(nbBlockChain.getTransactionId());
//        int insertDataChain = dataChainMapper.insert(dataChain);
//        if (insertDataChain < 0) {
//            throw new ApiException(ResultCode.STORE_FAILED);
//        }
//
//        return nbBlockChain.getTransactionId();
        return "111";
    }

    @Override
    public NbBlockChain queryChain(String DataId) throws Exception {
        DataChain dataChain = dataChainMapper.selectById(DataId);
        String txId=dataChain.getTransactionId();
        NbBlockChain nbBlockChain = nbBlockChainMapper.selectById(txId);
        return nbBlockChain;
    }




    public String makeHash(AddDataReq addDataReq){
        if(addDataReq==null)
            return "";
        String hash=addDataReq.getDataId()+addDataReq.getCreateUser()+addDataReq.getGmtCreate().toString()+addDataReq.getUpdateUser()
                     +addDataReq.getGmtModified().toString()+addDataReq.getIsDelete().toString()+addDataReq.getRegisterId()+addDataReq.getRegisterName()
                     +addDataReq.getRegisterTime().toString()+addDataReq.getOrgId()+addDataReq.getOrgName()+addDataReq.getDisplayMode()
                     +addDataReq.getDisplayTime().toString()+addDataReq.getPublicTime().toString()+addDataReq.getTitle()+addDataReq.getContent()
                     +addDataReq.getRemark()+addDataReq.getState().toString()+addDataReq.getFileUpload();
        if(addDataReq.getTaskId()!=null)
            hash+=addDataReq.getTaskId();
        if(addDataReq.getTaskDefKey()!=null)
            hash+=addDataReq.getTaskDefKey();
        if(addDataReq.getInstanceId()!=null)
            hash+=addDataReq.getInstanceId();
        if(addDataReq.getTaskName()!=null)
            hash+=addDataReq.getTaskName();
        if(addDataReq.getSuspendState()!=null)
            hash+=addDataReq.getSuspendState();
        if(addDataReq.getCurrentUser()!=null)
            hash+=addDataReq.getCurrentUser();
        if(addDataReq.getCurrentUserList()!=null)
            hash+=addDataReq.getCurrentUserList();
        if(addDataReq.getCurrentRoleList()!=null)
            hash+=addDataReq.getCurrentRoleList();

        return hash;
    }
}
