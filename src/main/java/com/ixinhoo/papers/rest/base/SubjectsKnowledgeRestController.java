package com.ixinhoo.papers.rest.base;


import com.chunecai.crumbs.api.client.ListDto;
import com.ixinhoo.papers.dto.tree.CommonTreeDto;
import com.ixinhoo.papers.service.base.SubjectsKnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/anon/subjects-knowledge")
public class SubjectsKnowledgeRestController {

    @Autowired
    private SubjectsKnowledgeService service;

    /**
     * 树形知识点
     *
     * @param stage     学段
     * @param subjectId 学科
     * @return
     */
    @RequestMapping(value = "tree", method = RequestMethod.POST)
    public ListDto<CommonTreeDto> listTree(Integer stage, Long subjectId) {
        return service.findSubjectKnowledgeTree(stage, subjectId);
    }


}