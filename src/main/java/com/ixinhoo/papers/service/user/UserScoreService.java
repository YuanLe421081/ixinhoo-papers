package com.ixinhoo.papers.service.user;

import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.ixinhoo.papers.dao.resources.DocumentExtendDao;
import com.ixinhoo.papers.dao.user.UserDao;
import com.ixinhoo.papers.dao.user.UserScoreDao;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.resources.DocumentExtend01;
import com.ixinhoo.papers.entity.user.User;
import com.ixinhoo.papers.entity.user.UserScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserScoreService extends BaseService<UserScore> {
    @Autowired
    private UserScoreDao dao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private DocumentExtendDao documentExtendDao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    @Transactional
    public StatusDto createByUserAndDocument(Long userId, Long documentId, Integer score) {
        StatusDto dto = new StatusDto(false);
        if (userId == null || userId == 0L) {
            dto.setMsg("用户未传输");
        } else if (documentId == null || documentId == 0L) {
            dto.setMsg("文档未传输");
        } else if (score == null || score == 0L) {
            dto.setMsg("分数未传输");
        } else {
            User user = userDao.selectByPrimaryKey(userId);
            if (user == null) {
                dto.setMsg("用户不存在");
            } else {
                UserScore userScore = new UserScore();
                userScore.setDataId(documentId);
                userScore.setDataType(EntitySetting.DATA_TYPE_DOCUMENT);
                userScore.setUserId(userId);
                userScore = dao.selectOne(userScore);
                if (userScore != null) {
                    dto.setMsg("该用户已评价此资源,请勿重复评价");
                } else {
                    //查询资源信息
                    DocumentExtend01 documentExtend = documentExtendDao.selectByPrimaryKey(documentId);
                    if (documentExtend == null) {
                        dto.setMsg("资源信息不存在");
                    } else {
                        documentExtend.setScore(documentExtend.getScore() == null ? score : score + documentExtend.getScore());
                        documentExtend.setScoreNum(documentExtend.getScoreNum() == null ? 1L : documentExtend.getScoreNum() + 1L);
                        documentExtendDao.updateByPrimaryKey(documentExtend);
                        userScore = new UserScore();
                        userScore.setDataId(documentId);
                        userScore.setDataType(EntitySetting.DATA_TYPE_DOCUMENT);
                        userScore.setUserId(userId);
                        userScore.setTime(System.currentTimeMillis());
                        userScore.setScore(score);
                        super.create(userScore);
                        dto.setStatus(true);
                    }
                }
            }
        }
        return dto;
    }
}