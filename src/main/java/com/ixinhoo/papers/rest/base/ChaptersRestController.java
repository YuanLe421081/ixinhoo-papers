package com.ixinhoo.papers.rest.base;


import com.chunecai.crumbs.api.client.ListDto;
import com.ixinhoo.papers.dto.base.ChaptersTreeDto;
import com.ixinhoo.papers.dto.common.CommonIdAndNameDto;
import com.ixinhoo.papers.dto.tree.CommonTreeDto;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.base.Chapters;
import com.ixinhoo.papers.service.base.ChaptersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/anon/chapters")
public class ChaptersRestController {

    @Autowired
    private ChaptersService service;


    /**
     * 树形菜单
     *
     * @param type
     * @return
     */
    @RequestMapping(value = "tree", method = RequestMethod.POST)
    public ListDto<ChaptersTreeDto> tree(Integer type) {
        return service.listTreeByType(type);
    }


    /**
     * 根据学科id获取学科下面的版本;
     *
     * @return
     */
    @RequestMapping(value = "version", method = RequestMethod.POST)
    public ListDto<CommonIdAndNameDto> version(Integer stage,Long subjectId, Integer grade) {
        return service.findChaptersBySubjectIdAndType(stage,subjectId, grade, EntitySetting.CHAPTERS_TYPE_VERSION);
    }


    /**
     * 根据版本、年级、册别查询章节信息、一级一级查询
     *
     * @return
     */
    @RequestMapping(value = "version-chapter", method = RequestMethod.POST)
    public ListDto<CommonIdAndNameDto> versionChapter(Integer grade,Integer term,Long versionId) {
        return service.findVersionChapters(grade,term,versionId);
    }



    /**
     * 根据版本、年级、册别查询章节信息、查询所有、属性展示
     *
     * @return
     */
    @RequestMapping(value = "version-chapter-tree", method = RequestMethod.POST)
    public ListDto<CommonTreeDto> versionChapterTree(Integer grade, Integer term, Long versionId) {
        return service.findVersionChaptersTree(grade,term,versionId);
    }



    /**
     * 根据父级节点查询章节信息
     *
     * @return
     */
    @RequestMapping(value = "chapter", method = RequestMethod.POST)
    public ListDto<CommonIdAndNameDto> chapter(Long supId) {
        return service.findChaptersBySupIdAndType(supId, EntitySetting.CHAPTERS_TYPE_CHAPTER);
    }

    /**
     * 根据年级查询年级下面的册别信息
     *
     * @return
     */
    @RequestMapping(value = "grade", method = RequestMethod.POST)
    public ListDto<String> grade(Integer grade) {
        return service.findChaptersNameByGradeAndType(grade, EntitySetting.CHAPTERS_TYPE_BOOK);
    }
    @RequestMapping(value = "test", method = RequestMethod.POST)
    public List<Chapters> test(Integer stage,Long subject_id) {
        return service.findByChapter(stage,subject_id);
    }

}