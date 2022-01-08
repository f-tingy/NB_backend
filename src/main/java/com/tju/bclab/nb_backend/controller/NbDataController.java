package com.tju.bclab.nb_backend.controller;


import com.tju.bclab.nb_backend.common.R;
import com.tju.bclab.nb_backend.entity.NbBlockChain;
import com.tju.bclab.nb_backend.service.NbDataService;
import com.tju.bclab.nb_backend.vo.AddDataReq;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 宁波数据表 前端控制器
 * </p>
 *
 * @author 范霆昱测试代码生成器
 * @since 2022-01-08
 */
@RestController
@RequestMapping("/nb_backend/nb-data")
public class NbDataController {

    @Autowired
    private NbDataService nbDataService;

    @ApiOperation(value = "用户数据上链")
    @PostMapping("/add")
    public R<String> AddData(@RequestBody AddDataReq addDataReq) throws Exception {
        String result=nbDataService.addData(addDataReq);
        return R.ok(result);
    }

    @ApiOperation(value = "查询上链数据")
    @GetMapping("/queryChain")
    public R<NbBlockChain> QueryChain(String DataId) throws Exception {
        NbBlockChain result = nbDataService.queryChain(DataId);
        return R.ok(result);
    }

}

