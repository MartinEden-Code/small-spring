package com.example.demo.api;

import com.example.demo.api.base.BaseApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/2/23 8:45
 */

@RestController
@RequestMapping("/service/patient-info")
@Api("value-PatientInfoApi")
public class PatientInfoApi extends BaseApi {

    @PostMapping("query")
    @ApiOperation(value = "查询患者信息",notes = "查询患者信息标签notes",httpMethod = "POST")
    public <T> T queryPatient(T queryPatientInfoDTO){
        return null;
    }
}
