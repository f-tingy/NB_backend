package com.tju.bclab.nb_backend.service;

import com.tju.bclab.nb_backend.entity.NbBlockChain;
import com.tju.bclab.nb_backend.io.aelf.schemas.KeyPairInfo;

import java.util.List;


public interface BlockChainService {

    /**
     * @param hash：未上链的哈希,由userId voteId optionId time组成
     * @param privateKey:用户的私钥
     * @return 成功上链后返回true,否则抛出合约调用失败异常
     */
    NbBlockChain VoteContract(String hash, String privateKey)throws Exception;

    /**
     * 存储结果合约调用
     * @param hash：未上链的哈希
     * @param privateKey：用户的私钥
     * @return 成功上链后返回true，否则抛出合约调用失败异常
     * @throws Exception
     */
    NbBlockChain ResultContract(String hash, String privateKey) throws Exception;

    NbBlockChain InitialContract(String hash, String privateKey) throws Exception;

    NbBlockChain InitializeContract(String privateKey) throws Exception;

    NbBlockChain AddUserContract(String privateKey) throws Exception;

    KeyPairInfo createWallet()throws Exception;

    //List<NbBlockChain>voteVerify( Long blockHeight)throws Exception;
}
