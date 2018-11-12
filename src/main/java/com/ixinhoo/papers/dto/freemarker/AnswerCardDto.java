package com.ixinhoo.papers.dto.freemarker;

import java.util.List;

/**
 * 答题卡导出下载dto;
 * 普通和标准的options是List<List<String>> options（外层是行，内层是列）
 * 密集的options是List<List<List<String>> options （外层是行，内容是列，最内层是列中的数量内容）
 *
 * @author 448778074@qq.com (cici)
 */
public class AnswerCardDto implements java.io.Serializable{
    private String title;//标题
    private List<Object> options;//选择题(自己进行分组显示）普通10个、标准15个、密集3个
    private List<String> answers;//简答题

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Object> getOptions() {
        return options;
    }

    public void setOptions(List options) {
        this.options = options;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}
