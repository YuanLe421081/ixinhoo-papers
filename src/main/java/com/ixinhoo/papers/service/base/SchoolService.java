package com.ixinhoo.papers.service.base;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.google.common.base.Strings;
import com.ixinhoo.papers.dao.base.SchoolDao;
import com.ixinhoo.papers.dto.base.SchoolReqDto;
import com.ixinhoo.papers.entity.base.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class SchoolService extends BaseService<School> {
    @Autowired
    private SchoolDao dao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }


    public List<School> findByProvinceId(SchoolReqDto reqDto) {
        Example example = new Example(School.class);
        if (reqDto != null) {
            example.createCriteria();
            if (!Strings.isNullOrEmpty(reqDto.getSchoolName())) {
                example.and().andLike("name", "%" + reqDto.getSchoolName() + "%");
            }
            if (reqDto.getStage() != null && reqDto.getStage() != 0L) {
                example.and().andEqualTo("type", reqDto.getStage());
            }
            if (reqDto.getProvinceId() != null && reqDto.getProvinceId() != 0L) {
                example.and().andEqualTo("provinceId", reqDto.getProvinceId());
            }
        }
        return dao.selectByExample(example);
    }
}