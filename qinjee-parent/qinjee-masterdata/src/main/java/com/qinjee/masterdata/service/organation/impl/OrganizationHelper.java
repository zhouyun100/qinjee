package com.qinjee.masterdata.service.organation.impl;

import com.alibaba.fastjson.JSON;
import com.github.liaochong.myexcel.core.DefaultExcelReader;
import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.organation.OrganizationDao;
import com.qinjee.masterdata.model.vo.organization.OrganizationVO;
import com.qinjee.masterdata.model.vo.staff.UserArchiveVo;
import com.qinjee.masterdata.redis.RedisClusterService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.utils.MyCollectionUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

/**
 * @program: eTalent
 * @description: 组织相关接口支持类
 * @author: penghs
 * @create: 2020-01-08 14:52
 **/
@Component
public class OrganizationHelper<T> {
    @Autowired
    private RedisClusterService redisService;

    private final static String xls = "xls";
    private final static String xlsx = "xlsx";
    @Autowired
    private OrganizationDao organizationDao;

    public List<Integer> getOrgIdList(Integer archiveId, Integer orgId, Integer layer, Short isEnable) {
        List<Integer> idsList = new ArrayList<>();

        //先查询到所有机构
        List<OrganizationVO> allOrgs = organizationDao.listAllOrganizationByArchiveId(archiveId, isEnable, new Date());
        //将机构的id和父id存入MultiMap,父id作为key，子id作为value，一对多
        MultiValuedMap<Integer, Integer> multiValuedMap = new HashSetValuedHashMap<>();
        for (OrganizationVO org : allOrgs) {
            multiValuedMap.put(org.getOrgParentId(), org.getOrgId());
        }
        //根据机构id递归，取出该机构下的所有子机构
        collectOrgIds(multiValuedMap, orgId, idsList, layer);
        return MyCollectionUtil.removeDuplicate(idsList);
    }

    private void collectOrgIds(MultiValuedMap<Integer, Integer> multiValuedMap, Integer orgId, List<Integer> idsList, Integer layer) {
        idsList.add(orgId);
        Collection<Integer> sonOrgIds = multiValuedMap.get(orgId);
        for (Integer sonOrgId : sonOrgIds) {

            if (layer != null && layer > 0) {
                idsList.add(sonOrgId);
                if (multiValuedMap.get(sonOrgId).size() > 0 && layer > 0) {
                    collectOrgIds(multiValuedMap, sonOrgId, idsList, layer);
                    layer--;
                }
            } else {
                idsList.add(sonOrgId);
                if (multiValuedMap.get(sonOrgId).size() > 0) {
                    collectOrgIds(multiValuedMap, sonOrgId, idsList, layer);
                }
            }
        }
    }
    //导入xls/xlsx文件基本校验
    public boolean basicCheck(MultipartFile multfile){
        //判断文件名
        String filename = multfile.getOriginalFilename();
        if (!(filename.endsWith(xls) || filename.endsWith(xlsx))) {
            //文件格式不对
            ExceptionCast.cast(CommonCode.FILE_FORMAT_ERROR);
        }

        return false;
    }

    public List<T> importFromExcel(MultipartFile multfile,Class<T> clazz) throws Exception {
        File tempFile = File.createTempFile("temp", ".xls");
        multfile.transferTo(tempFile);
        List<T> excelDataList = DefaultExcelReader.of(clazz).sheet(0).rowFilter(row -> row.getRowNum() > 0).read(tempFile);
        tempFile.delete();
        if (CollectionUtils.isEmpty(excelDataList)) {
            //excel为空
            ExceptionCast.cast(CommonCode.FILE_EMPTY);
        }
        return excelDataList;
    }

    //
    public List<T> check(List<T> voList, UserSession session){


        return null;
    }
}
