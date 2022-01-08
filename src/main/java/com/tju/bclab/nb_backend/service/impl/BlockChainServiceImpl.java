package com.tju.bclab.nb_backend.service.impl;

import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import com.tju.bclab.nb_backend.common.ResultCode;
import com.tju.bclab.nb_backend.entity.NbBlockChain;
import com.tju.bclab.nb_backend.exception.ApiException;
import com.tju.bclab.nb_backend.io.aelf.protobuf.generated.Client;
import com.tju.bclab.nb_backend.io.aelf.protobuf.generated.Core;
import com.tju.bclab.nb_backend.io.aelf.protobuf.generated.DataStoreContractOuterClass;
import com.tju.bclab.nb_backend.io.aelf.schemas.KeyPairInfo;
import com.tju.bclab.nb_backend.io.aelf.schemas.SendTransactionInput;
import com.tju.bclab.nb_backend.io.aelf.schemas.SendTransactionOutput;
import com.tju.bclab.nb_backend.io.aelf.schemas.TransactionResultDto;
import com.tju.bclab.nb_backend.io.aelf.sdk.AElfClient;
import com.tju.bclab.nb_backend.io.aelf.utils.ByteArrayHelper;
import com.tju.bclab.nb_backend.mapper.NbBlockChainMapper;
import com.tju.bclab.nb_backend.service.BlockChainService;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Base58;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BlockChainServiceImpl implements BlockChainService {
    @Autowired
    private  NbBlockChainMapper nbBlockChainMapper;

    private AElfClient client;
    private String dataStoreContractAddress;
    private List<AElfClient> clients;

    public BlockChainServiceImpl(){
        this.client=new AElfClient("http://39.103.194.213:1235");
        this.dataStoreContractAddress="2LUmicHyH4RXrMjG4beDwuDsiWJESyLkgkwPdGTR8kahRzq5XS";
        AElfClient client2 = new AElfClient("http://39.103.156.177:1235");
        AElfClient client3 = new AElfClient("http://101.201.46.135:1235");
        AElfClient client4 = new AElfClient("http://123.56.133.165:1235");
        this.clients = Lists.newArrayList(client,client2,client3,client4);
    }

    @Override
    public NbBlockChain InitialContract(String hash, String privateKey) throws Exception {
        // 通过节点 privateKey 获取节点地址，该地址即为合约的 owner
        String ownerAddress = client.getAddressFromPrivateKey(privateKey);
        Client.Address.Builder owner = Client.Address.newBuilder();
        owner.setValue(ByteString.copyFrom(Base58.decodeChecked(ownerAddress)));

        // 构建合约调用时需要传递的参数
        // 具体定义见 io.aelf 包中的 proto 文件
        DataStoreContractOuterClass.HashStoreInput.Builder datastoreinput = DataStoreContractOuterClass.HashStoreInput.newBuilder();
        // 对不同字段设置相应值
        datastoreinput.setValue(hash);
        DataStoreContractOuterClass.HashStoreInput datastoreObj = datastoreinput.build();
        // 构建调用智能合约方法对应的参数
        Core.Transaction.Builder transactionDataStore = client.generateTransaction(ownerAddress, dataStoreContractAddress, "InitiationHashStore", datastoreObj.toByteArray());
        Core.Transaction transactionDataStoreObj = transactionDataStore.build();
        // 用 owner 地址对该交易签名
        String signature = client.signTransaction(privateKey, transactionDataStoreObj);
        transactionDataStore.setSignature(ByteString.copyFrom(ByteArrayHelper.hexToByteArray(signature)));
        transactionDataStoreObj = transactionDataStore.build();

        // 发送交易，该逻辑主要对应合约中的set方法
        SendTransactionInput sendTransactionInputObj = new SendTransactionInput();
        sendTransactionInputObj.setRawTransaction(Hex.toHexString(transactionDataStoreObj.toByteArray()));
        SendTransactionOutput sendResult = client.sendTransaction(sendTransactionInputObj);
        TransactionResultDto transactionResult;
        // 循环查询接口，根据id获得交易执行结果
        while (true) {
            transactionResult = client.getTransactionResult(sendResult.getTransactionId());
            if ("MINED".equals(transactionResult.getStatus())) {
                NbBlockChain nbBlockChain = new NbBlockChain();
                nbBlockChain.setTransactionId(transactionResult.getTransactionId());
                nbBlockChain.setBlockHeight(transactionResult.getBlockNumber());
                nbBlockChain.setBlockHash(transactionResult.getBlockHash());
                nbBlockChain.setChainStatus(transactionResult.getStatus());
                nbBlockChainMapper.insert(nbBlockChain);
                // 当状态为MINED表示执行成功，直接返回

                return  nbBlockChain ;
            } else if ("PENDING".equals(transactionResult.getStatus())) {
                // 当状态为PENDING表示还未获取到结果，等待
                Thread.sleep(300);
            } else {
                // 若其他结果则抛出异常
                throw new ApiException(ResultCode.CONTRACT_CALL_FAILED);
            }
        }

    }

    @Override
    public NbBlockChain VoteContract(String hash, String privateKey) throws Exception {
        // 通过节点 privateKey 获取节点地址，该地址即为合约的 owner
        String ownerAddress = client.getAddressFromPrivateKey(privateKey);
        Client.Address.Builder owner = Client.Address.newBuilder();
        owner.setValue(ByteString.copyFrom(Base58.decodeChecked(ownerAddress)));

        // 构建合约调用时需要传递的参数
        // 具体定义见 io.aelf 包中的 proto 文件
        DataStoreContractOuterClass.HashStoreInput.Builder datastoreinput = DataStoreContractOuterClass.HashStoreInput.newBuilder();
        // 对不同字段设置相应值
        datastoreinput.setValue(hash);
        DataStoreContractOuterClass.HashStoreInput datastoreObj = datastoreinput.build();
        // 构建调用智能合约方法对应的参数
        Core.Transaction.Builder transactionDataStore = client.generateTransaction(ownerAddress, dataStoreContractAddress, "VoteHashStore", datastoreObj.toByteArray());
        Core.Transaction transactionDataStoreObj = transactionDataStore.build();
        // 用 owner 地址对该交易签名
        String signature = client.signTransaction(privateKey, transactionDataStoreObj);
        transactionDataStore.setSignature(ByteString.copyFrom(ByteArrayHelper.hexToByteArray(signature)));
        transactionDataStoreObj = transactionDataStore.build();

        // 发送交易，该逻辑主要对应合约中的set方法
        SendTransactionInput sendTransactionInputObj = new SendTransactionInput();
        sendTransactionInputObj.setRawTransaction(Hex.toHexString(transactionDataStoreObj.toByteArray()));
        SendTransactionOutput sendResult = client.sendTransaction(sendTransactionInputObj);
        TransactionResultDto transactionResult;
        // 循环查询接口，根据id获得交易执行结果
        while (true) {
            transactionResult = client.getTransactionResult(sendResult.getTransactionId());
            if ("MINED".equals(transactionResult.getStatus())) {
                NbBlockChain nbBlockChain = new NbBlockChain();
                nbBlockChain.setTransactionId(transactionResult.getTransactionId());
                nbBlockChain.setBlockHeight(transactionResult.getBlockNumber());
                nbBlockChain.setBlockHash(transactionResult.getBlockHash());
                nbBlockChain.setChainStatus(transactionResult.getStatus());
                nbBlockChainMapper.insert(nbBlockChain);
                // 当状态为MINED表示执行成功，直接返回

                return  nbBlockChain ;
            } else if ("PENDING".equals(transactionResult.getStatus())) {
                // 当状态为PENDING表示还未获取到结果，等待
                Thread.sleep(300);
            } else {
                // 若其他结果则抛出异常
                throw new ApiException(ResultCode.CONTRACT_CALL_FAILED);
            }
        }
    }

    @Override
    public NbBlockChain ResultContract(String hash, String privateKey) throws Exception {
        // 通过节点 privateKey 获取节点地址，该地址即为合约的 owner
        String ownerAddress = client.getAddressFromPrivateKey(privateKey);
        Client.Address.Builder owner = Client.Address.newBuilder();
        owner.setValue(ByteString.copyFrom(Base58.decodeChecked(ownerAddress)));

        // 构建合约调用时需要传递的参数
        // 具体定义见 io.aelf 包中的 proto 文件
        DataStoreContractOuterClass.HashStoreInput.Builder datastoreinput = DataStoreContractOuterClass.HashStoreInput.newBuilder();
        // 对不同字段设置相应值
        datastoreinput.setValue(hash);
        DataStoreContractOuterClass.HashStoreInput datastoreObj = datastoreinput.build();
        // 构建调用智能合约方法对应的参数
        Core.Transaction.Builder transactionDataStore = client.generateTransaction(ownerAddress, dataStoreContractAddress, "ResultHashStore", datastoreObj.toByteArray());
        Core.Transaction transactionDataStoreObj = transactionDataStore.build();
        // 用 owner 地址对该交易签名
        String signature = client.signTransaction(privateKey, transactionDataStoreObj);
        transactionDataStore.setSignature(ByteString.copyFrom(ByteArrayHelper.hexToByteArray(signature)));
        transactionDataStoreObj = transactionDataStore.build();

        // 发送交易，该逻辑主要对应合约中的set方法
        SendTransactionInput sendTransactionInputObj = new SendTransactionInput();
        sendTransactionInputObj.setRawTransaction(Hex.toHexString(transactionDataStoreObj.toByteArray()));
        SendTransactionOutput sendResult = client.sendTransaction(sendTransactionInputObj);
        TransactionResultDto transactionResult;
        // 循环查询接口，根据id获得交易执行结果
        while (true) {
            transactionResult = client.getTransactionResult(sendResult.getTransactionId());
            if ("MINED".equals(transactionResult.getStatus())) {
                NbBlockChain nbBlockChain = new NbBlockChain();
                nbBlockChain.setTransactionId(transactionResult.getTransactionId());
                nbBlockChain.setBlockHeight(transactionResult.getBlockNumber());
                nbBlockChain.setBlockHash(transactionResult.getBlockHash());
                nbBlockChain.setChainStatus(transactionResult.getStatus());
                nbBlockChainMapper.insert(nbBlockChain);
                // 当状态为MINED表示执行成功，直接返回

                return  nbBlockChain ;
            } else if ("PENDING".equals(transactionResult.getStatus())) {
                // 当状态为PENDING表示还未获取到结果，等待
                Thread.sleep(300);
            } else {
                // 若其他结果则抛出异常
                throw new ApiException(ResultCode.CONTRACT_CALL_FAILED);
            }
        }

    }


    // 该方法暂时不用
//    @Override
//    public List<VerifyHaveModified> voteVerify( Long blockHeight) throws Exception{
//        List<VerifyHaveModified> list = new ArrayList<>();
//        String hashArr[] = new String[clients.size()];
//        for(int i = 0;i < clients.size(); i ++){
//            VerifyHaveModified obj = new VerifyHaveModified();
//            obj.setClientIp(clients.get(i).AElfClientUrl );
//            String blockHash = clients.get(i).getBlockByHeight(blockHeight).getBlockHash();
//            obj.setBlockHash(blockHash);
//            list.add(obj);
//        }
//        return list;
//    }

    @Override
    public KeyPairInfo createWallet() {
        try {
            return client.generateKeyPairInfo();
        } catch (Exception e) {
            throw new ApiException(ResultCode.CREATE_WALLET_FAILED);
        }
    }


    @Override
    public NbBlockChain InitializeContract(String privateKey) throws Exception {
        // 通过节点 privateKey 获取节点地址，该地址即为合约的 owner
        String ownerAddress = client.getAddressFromPrivateKey(privateKey);
        Client.Address.Builder owner = Client.Address.newBuilder();
        owner.setValue(ByteString.copyFrom(Base58.decodeChecked(ownerAddress)));

        // 构建合约调用时需要传递的参数
        // 具体定义见 io.aelf 包中的 proto 文件
        DataStoreContractOuterClass.InitializeInput.Builder initializeInput = DataStoreContractOuterClass.InitializeInput.newBuilder();
        // 对不同字段设置相应值
        initializeInput.setFlag("0");
        DataStoreContractOuterClass.InitializeInput initializeObj = initializeInput.build();

        /// 构建调用智能合约方法对应的参数
        Core.Transaction.Builder transactionDataStore = client.generateTransaction(ownerAddress, dataStoreContractAddress, "Initialize", initializeObj.toByteArray());
        Core.Transaction transactionDataStoreObj = transactionDataStore.build();
        // 用 owner 地址对该交易签名
        String signature = client.signTransaction(privateKey, transactionDataStoreObj);
        transactionDataStore.setSignature(ByteString.copyFrom(ByteArrayHelper.hexToByteArray(signature)));
        transactionDataStoreObj = transactionDataStore.build();

        // 发送交易，该逻辑主要对应合约中的set方法
        SendTransactionInput sendTransactionInputObj = new SendTransactionInput();
        sendTransactionInputObj.setRawTransaction(Hex.toHexString(transactionDataStoreObj.toByteArray()));
        SendTransactionOutput sendResult = client.sendTransaction(sendTransactionInputObj);
        TransactionResultDto transactionResult;
        // 循环查询接口，根据id获得交易执行结果
        while (true) {
            transactionResult = client.getTransactionResult(sendResult.getTransactionId());
            if ("MINED".equals(transactionResult.getStatus())) {
                NbBlockChain nbBlockChain = new NbBlockChain();
                nbBlockChain.setTransactionId(transactionResult.getTransactionId());
                nbBlockChain.setBlockHeight(transactionResult.getBlockNumber());
                nbBlockChain.setBlockHash(transactionResult.getBlockHash());
                nbBlockChain.setChainStatus(transactionResult.getStatus());
                nbBlockChainMapper.insert(nbBlockChain);
                // 当状态为MINED表示执行成功，直接返回

                return  nbBlockChain ;
            } else if ("PENDING".equals(transactionResult.getStatus())) {
                // 当状态为PENDING表示还未获取到结果，等待
                Thread.sleep(300);
            } else {
                // 若其他结果则抛出异常
                throw new ApiException(ResultCode.CONTRACT_CALL_FAILED);
            }
        }
    }

    @Override
    public NbBlockChain AddUserContract(String privateKey) throws Exception {
        // 通过节点 privateKey 获取节点地址，该地址即为合约的 owner
        String ownerAddress = client.getAddressFromPrivateKey(privateKey);
        Client.Address.Builder owner = Client.Address.newBuilder();
        owner.setValue(ByteString.copyFrom(Base58.decodeChecked(ownerAddress)));

        // 构建合约调用时需要传递的参数
        // 具体定义见 io.aelf 包中的 proto 文件
        Client.Address.Builder user = Client.Address.newBuilder();
        user.setValue(ByteString.copyFrom(Base58.decodeChecked(ownerAddress)));
        Client.Address userObj = user.build();

        /// 构建调用智能合约方法对应的参数
        Core.Transaction.Builder transactionDataStore = client.generateTransaction(ownerAddress, dataStoreContractAddress, "AddUser", userObj.toByteArray());
        Core.Transaction transactionDataStoreObj = transactionDataStore.build();
        // 用 owner 地址对该交易签名
        String signature = client.signTransaction(privateKey, transactionDataStoreObj);
        transactionDataStore.setSignature(ByteString.copyFrom(ByteArrayHelper.hexToByteArray(signature)));
        transactionDataStoreObj = transactionDataStore.build();

        // 发送交易，该逻辑主要对应合约中的set方法
        SendTransactionInput sendTransactionInputObj = new SendTransactionInput();
        sendTransactionInputObj.setRawTransaction(Hex.toHexString(transactionDataStoreObj.toByteArray()));
        SendTransactionOutput sendResult = client.sendTransaction(sendTransactionInputObj);
        TransactionResultDto transactionResult;
        // 循环查询接口，根据id获得交易执行结果
        while (true) {
            transactionResult = client.getTransactionResult(sendResult.getTransactionId());
            if ("MINED".equals(transactionResult.getStatus())) {
                NbBlockChain nbBlockChain = new NbBlockChain();
                nbBlockChain.setTransactionId(transactionResult.getTransactionId());
                nbBlockChain.setBlockHeight(transactionResult.getBlockNumber());
                nbBlockChain.setBlockHash(transactionResult.getBlockHash());
                nbBlockChain.setChainStatus(transactionResult.getStatus());
                nbBlockChainMapper.insert(nbBlockChain);
                // 当状态为MINED表示执行成功，直接返回

                return  nbBlockChain ;
            } else if ("PENDING".equals(transactionResult.getStatus())) {
                // 当状态为PENDING表示还未获取到结果，等待
                Thread.sleep(300);
            } else {
                // 若其他结果则抛出异常
                throw new ApiException(ResultCode.CONTRACT_CALL_FAILED);
            }
        }
    }
}
