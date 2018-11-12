package com.ixinhoo.papers.dto.user.paper;

import com.ixinhoo.papers.dto.common.CommonIdAndNameDto;

import java.util.List;

/**
 * 用户模板保存DTO--题型--题目&知识点
 *
 * @author 448778074@qq.com (cici)
 */
public class UserPaperTemplateSaveTypeKnowledgeDto implements java.io.Serializable {
    private List<CommonIdAndNameDto> knowledge;//知识点id集合
    private Integer difficult;//难度

    public List<CommonIdAndNameDto> getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(List<CommonIdAndNameDto> knowledge) {
        this.knowledge = knowledge;
    }

    public Integer getDifficult() {
        return difficult;
    }

    public void setDifficult(Integer difficult) {
        this.difficult = difficult;
    }
}
