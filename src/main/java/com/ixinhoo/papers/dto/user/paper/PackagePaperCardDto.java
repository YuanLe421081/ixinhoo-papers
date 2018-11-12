package com.ixinhoo.papers.dto.user.paper;

import java.util.List;

/**
 * 下载答题卡DTO
 *
 * @author 448778074@qq.com (cici)
 */
public class PackagePaperCardDto implements java.io.Serializable{
    private String paperTitle;//标题
    private List<PackagePaperCardTypeDto> type;
    private Integer answerCard;//答题卡,1-普通表格型,2-标准题卡型,3-选择密集型

    public String getPaperTitle() {
        return paperTitle;
    }

    public void setPaperTitle(String paperTitle) {
        this.paperTitle = paperTitle;
    }

    public List<PackagePaperCardTypeDto> getType() {
        return type;
    }

    public void setType(List<PackagePaperCardTypeDto> type) {
        this.type = type;
    }

    public Integer getAnswerCard() {
        return answerCard;
    }

    public void setAnswerCard(Integer answerCard) {
        this.answerCard = answerCard;
    }
}
