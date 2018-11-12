package com.ixinhoo.papers.service.base;

import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.code.JsTreeAjax;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.api.shiro.ShiroUser;
import com.chunecai.crumbs.api.util.UsersUtil;
import com.chunecai.crumbs.code.util.collection.Collections3;
import com.chunecai.crumbs.code.util.mapper.BeanMapper;
import com.chunecai.crumbs.code.util.mapper.JsonMapper;
import com.cnjy21.api.model.common.Book;
import com.cnjy21.api.model.common.Chapter;
import com.cnjy21.api.model.common.Version;
import com.cnjy21.api.service.APIDocumentService;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ixinhoo.papers.dao.base.ChaptersDao;
import com.ixinhoo.papers.dao.base.StageSubjectsDao;
import com.ixinhoo.papers.dao.common.InterfaceInfoDao;
import com.ixinhoo.papers.dto.base.ChaptersStageDto;
import com.ixinhoo.papers.dto.base.ChaptersTreeDto;
import com.ixinhoo.papers.dto.common.CommonIdAndNameDto;
import com.ixinhoo.papers.dto.tree.CommonTreeDto;
import com.ixinhoo.papers.dto.tree.TreeDto;
import com.ixinhoo.papers.dto.tree.TreeNodeDto;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.base.Chapters;
import com.ixinhoo.papers.entity.base.StageSubjects;
import com.ixinhoo.papers.entity.common.InterfaceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChaptersService extends BaseService<Chapters> {
    @Autowired
    private ChaptersDao dao;
    @Autowired
    private InterfaceInfoDao interfaceInfoDao;
    @Autowired
    private StageSubjectsDao stageSubjectsDao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    /**
     * 同步所有
     *
     * @return
     */
    @Transactional
    public StatusDto syncAllChapters() {
        StatusDto dto = syncVersion();
        if (dto.getStatus()) {
            dto = syncBook();
            if (dto.getStatus()) {
                dto = syncChapters();
            }
        }
        return dto;
//        List<Integer> types = Lists.newArrayList();
//        types.add(EntitySetting.CHAPTERS_TYPE_CHAPTER);
//        types.add(EntitySetting.CHAPTERS_TYPE_EXAM);
//        List<Chapters> chapterss = dao.selectByExample(new Example.Builder(Chapters.class)
//                .where(WeekendSqls.<Chapters>custom().andIn(Chapters::getType, types))
//                .build());
//        chapterss.forEach(d->{
//            String path = d.getPath();
//            if(path!=null){
//                String[] p = path.split(",");
////                Long supId = Long.parseLong(path.substring(path.lastIndexOf(",")+1));
//                Long supId = Long.parseLong(p[p.length-2]);
//                d.setSupId(supId);
//                dao.updateByPrimaryKey(d);
//            }
//        });
//        return null;
    }


    /**
     * 同步章节
     *
     * @return
     */
    @Transactional
    public StatusDto syncChapters() {
        StatusDto dto = new StatusDto(false);
        //查询册别
        Chapters chapters = new Chapters();
        chapters.setType(EntitySetting.CHAPTERS_TYPE_BOOK);
        List<Chapters> books = dao.select(chapters);
        if (Collections3.isEmpty(books)) {
            dto.setMsg("册别信息为空,请先册别版本信息");
        } else {
            //查询同步章节备课章节&备考,因为21世纪的章节和备考是放在一起的
            List<Integer> types = Lists.newArrayList();
            types.add(EntitySetting.CHAPTERS_TYPE_CHAPTER);
            types.add(EntitySetting.CHAPTERS_TYPE_EXAM);
            List<Chapters> chapterss = dao.selectByExample(new Example.Builder(Chapters.class)
                    .where(WeekendSqls.<Chapters>custom().andIn(Chapters::getType, types))
                    .build());
            List<String> codeList = chapterss.stream().map(d -> d.getCode()).collect(Collectors.toList());
            APIDocumentService service = new APIDocumentService();
            books.forEach(d -> {
                syncChaptersByStageAndSubject(service, d, codeList, EntitySetting.INTERFACE_CHAPTER);
            });
            dto.setStatus(true);

        }
        return dto;
    }


    /**
     * 同步版本
     *
     * @return
     */
    @Transactional
    public StatusDto syncVersion() {
        StatusDto dto = new StatusDto(false);
        //查询学段学科信息
        List<StageSubjects> stageSubjectss = stageSubjectsDao.selectAll();
        if (Collections3.isEmpty(stageSubjectss)) {
            dto.setMsg("学段学科信息为空,请先同步学段学科信息");
        } else {
            APIDocumentService service = new APIDocumentService();
            //查询版本信息
            Chapters chapters = new Chapters();
            chapters.setType(EntitySetting.CHAPTERS_TYPE_VERSION);
            List<Chapters> chapterss = dao.select(chapters);
            List<String> codeList = chapterss.stream().map(d -> d.getCode()).collect(Collectors.toList());
            stageSubjectss.forEach(d -> {
                syncVersionByStageAndSubject(service, d.getStage(), d.getCode(), d.getName(), codeList);
            });
            dto.setStatus(true);
        }
        return dto;
    }

    @Transactional
    private void syncVersionByStageAndSubject(APIDocumentService service, Integer stage, String subjectId, String subjectName, List<String> codeList) {
        if (subjectId.matches("[\\d]+")) {
            List<Version> versions = service.getVersions(stage, Integer.parseInt(subjectId));
            if (Collections3.isNotEmpty(versions)) {
                InterfaceInfo interfaceInfo = new InterfaceInfo();
                interfaceInfo.setTime(System.currentTimeMillis());
                interfaceInfo.setReqData("{\"stage\":" + stage + ",\"subjectId\":" + subjectId + "}");
                interfaceInfo.setType(EntitySetting.INTERFACE_VERSION);
                interfaceInfo.setRspData(JsonMapper.nonDefaultMapper().toJson(versions));
//                ShiroUser user = UsersUtil.ShiroUser();
                ShiroUser user = null;
                if (user != null) {
                    interfaceInfo.setCreateId(user.getId());
                    interfaceInfo.setUpdateId(user.getId());
                }
                interfaceInfo.setCreateTime(System.currentTimeMillis());
                interfaceInfo.setUpdateTime(System.currentTimeMillis());
                interfaceInfoDao.insert(interfaceInfo);
                List<Chapters> list = Lists.newArrayList();
                long i = 10;
                for (Version d : versions) {
                    if (!codeList.contains(d.getVersionId().toString())) {
                        Chapters temp = new Chapters();
                        temp.setId(d.getVersionId().longValue());
                        temp.setTerm(EntitySetting.GRADE_TERM_ALL);
                        temp.setName(d.getVersionName());
                        temp.setCode(d.getVersionId().toString());
                        temp.setStage(d.getStage());
                        temp.setSupId(0L);
                        temp.setPath(d.getPath());
                        temp.setSort(i++);
                        //temp.setGrade();
                        temp.setType(EntitySetting.CHAPTERS_TYPE_VERSION);
                        temp.setSubjectId(Long.parseLong(subjectId));
                        temp.setSubjectName(subjectName);
                        temp.setStatus(EntitySetting.COMMON_SUCCESS);
                        if (user != null) {
                            temp.setCreateId(user.getId());
                            temp.setUpdateId(user.getId());
                        }
                        temp.setCreateTime(System.currentTimeMillis());
                        temp.setUpdateTime(System.currentTimeMillis());
                        list.add(temp);
                    }
                }
                if (Collections3.isNotEmpty(list)) {
                    dao.insertList(list);
                }
            }
        }
    }

    /**
     * 同步册别
     *
     * @return
     */
    @Transactional
    public StatusDto syncBook() {
        StatusDto dto = new StatusDto(false);
        //查询版本信息
        Chapters chapters = new Chapters();
        chapters.setType(EntitySetting.CHAPTERS_TYPE_VERSION);
        List<Chapters> versions = dao.select(chapters);
        if (Collections3.isEmpty(versions)) {
            dto.setMsg("版本信息为空,请先同步版本信息");
        } else {
            APIDocumentService service = new APIDocumentService();
            //查询册别信息
            chapters.setType(EntitySetting.CHAPTERS_TYPE_BOOK);
            List<Chapters> chapterss = dao.select(chapters);
            List<String> codeList = chapterss.stream().map(d -> d.getCode()).collect(Collectors.toList());
            versions.forEach(d -> {
                syncBookByStageAndSubject(service, d, codeList);
            });
            dto.setStatus(true);
        }
        return dto;
    }

    @Transactional
    private void syncBookByStageAndSubject(APIDocumentService service, Chapters version, List<String> codeList) {
        if (version != null && version.getCode().matches("[\\d]+")) {//全部为数字，转化为21世纪的id进行查询
            List<Book> books = service.getBooks(Integer.parseInt(version.getCode()));
            if (Collections3.isNotEmpty(books)) {
                InterfaceInfo interfaceInfo = new InterfaceInfo();
                interfaceInfo.setTime(System.currentTimeMillis());
                interfaceInfo.setReqData(version.getCode());
                interfaceInfo.setType(EntitySetting.INTERFACE_BOOK);
                interfaceInfo.setRspData(JsonMapper.nonDefaultMapper().toJson(books));
//                ShiroUser user = UsersUtil.ShiroUser();
                ShiroUser user = null;
                if (user != null) {
                    interfaceInfo.setCreateId(user.getId());
                    interfaceInfo.setUpdateId(user.getId());
                }
                interfaceInfo.setCreateTime(System.currentTimeMillis());
                interfaceInfo.setUpdateTime(System.currentTimeMillis());
                interfaceInfoDao.insert(interfaceInfo);
                List<Chapters> list = Lists.newArrayList();
                long i = 100;
                for (Book d : books) {
                    if (!codeList.contains(d.getBookId().toString()) && !Strings.isNullOrEmpty(d.getBookName())) {
                        Chapters temp = new Chapters();
                        temp.setId(d.getBookId().longValue());
                        temp.setName(d.getBookName());
                        if (d.getBookName().indexOf("上册") != -1 || d.getBookName().indexOf("上学期") != -1) {
                            temp.setTerm(EntitySetting.GRADE_TERM_UP);
                        } else if (d.getBookName().indexOf("下册") != -1 || d.getBookName().indexOf("下学期") != -1) {
                            temp.setTerm(EntitySetting.GRADE_TERM_DOWN);
                        } else {
                            temp.setTerm(EntitySetting.GRADE_TERM_ALL);
                        }
                        temp.setCode(d.getBookId().toString());
                        temp.setStage(version.getStage());
                        temp.setSupId(version.getId());
                        temp.setPath(d.getPath());
                        temp.setSort(i++);
                        temp.setGrade(d.getGrade());
                        temp.setType(EntitySetting.CHAPTERS_TYPE_BOOK);
                        temp.setSubjectId(version.getSubjectId());
                        temp.setSubjectName(version.getSubjectName());
                        temp.setStatus(EntitySetting.COMMON_SUCCESS);
                        if (user != null) {
                            temp.setCreateId(user.getId());
                            temp.setUpdateId(user.getId());
                        }
                        temp.setCreateTime(System.currentTimeMillis());
                        temp.setUpdateTime(System.currentTimeMillis());
                        list.add(temp);
                    }
                }
                if (Collections3.isNotEmpty(list)) {
                    dao.insertList(list);
                }
            }
        }
    }

    @Transactional
    private void syncChaptersByStageAndSubject(APIDocumentService service, Chapters book, List<String> codeList, Integer interfaceType) {
        if (book != null && book.getCode().matches("[\\d]+")) {//全部为数字，转化为21世纪的id进行查询
            List<Chapter> chapters = service.getChapters(Integer.parseInt(book.getCode()));
            if (Collections3.isNotEmpty(chapters)) {
                InterfaceInfo interfaceInfo = new InterfaceInfo();
                interfaceInfo.setTime(System.currentTimeMillis());
                interfaceInfo.setReqData(book.getCode());
                interfaceInfo.setType(interfaceType);
                interfaceInfo.setRspData(JsonMapper.nonDefaultMapper().toJson(chapters));
//                ShiroUser user = UsersUtil.ShiroUser();
                ShiroUser user = null;
                if (user != null) {
                    interfaceInfo.setCreateId(user.getId());
                    interfaceInfo.setUpdateId(user.getId());
                }
                interfaceInfo.setCreateTime(System.currentTimeMillis());
                interfaceInfo.setUpdateTime(System.currentTimeMillis());
                interfaceInfoDao.insert(interfaceInfo);
                List<Chapters> list = chaptersList(chapters, book, codeList,book.getId());
                if (Collections3.isNotEmpty(list)) {
                    dao.insertList(list);
                }
            }
        }
    }

    private List<Chapters> chaptersList(List<Chapter> chapters, Chapters book, List<String> codeList,Long supId) {
//        ShiroUser user = UsersUtil.ShiroUser();
        ShiroUser user = null;
        List<Chapters> list = Lists.newArrayList();
        long i = 1000;
        for (Chapter d : chapters) {
            if (!codeList.contains(d.getId().toString())) {
                Chapters temp = new Chapters();
                temp.setId(d.getId().longValue());
                temp.setName(d.getName().trim());
                temp.setCode(d.getId().toString());
                temp.setStage(book.getStage());
                temp.setSupId(supId);
                temp.setPath(d.getPath());
                temp.setSort(i++);
                //根据册别的上下学期决定章节的上下学期
                temp.setTerm(book.getTerm());
                //根据返回的isSync判断是否是同步备课章节
                if (d.getIsSync() != null && 0 == d.getIsSync()) {
                    temp.setType(EntitySetting.CHAPTERS_TYPE_EXAM);
                } else {
                    temp.setType(EntitySetting.CHAPTERS_TYPE_CHAPTER);
                }
                temp.setSubjectId(book.getSubjectId());
                temp.setSubjectName(book.getSubjectName());
                temp.setStatus(EntitySetting.COMMON_SUCCESS);
                temp.setGrade(book.getGrade());
                if (user != null) {
                    temp.setCreateId(user.getId());
                    temp.setUpdateId(user.getId());
                }
                temp.setCreateTime(System.currentTimeMillis());
                temp.setUpdateTime(System.currentTimeMillis());
                list.add(temp);
                if (Collections3.isNotEmpty(d.getChilds())) {
                    list.addAll(chaptersList(d.getChilds(), book, codeList,temp.getId()));
                }
            }
        }
        return list;
    }


    public ListDto<ChaptersTreeDto> listTreeByType(Integer type) {
        ListDto<ChaptersTreeDto> dto = new ListDto<>();
        dto.setStatus(false);
        if (type == null) {
            dto.setMsg("传输参数为空");
        } else {
            dto.setStatus(true);
            if (EntitySetting.CHAPTERS_TYPE_CHAPTER == type) {//备课
                //查询所有的分组学科
                List<Chapters> chapterss = dao.selectByExample(new Example.Builder(Chapters.class)
                        .where(WeekendSqls.<Chapters>custom().andEqualTo(Chapters::getType, type)
                                .andEqualTo(Chapters::getStatus, EntitySetting.COMMON_SUCCESS)
                        ).selectDistinct("subjectId", "subjectName", "stage", "grade")
                        .build());
                if (!Collections3.isEmpty(chapterss)) {
                    List<ChaptersTreeDto> list = Lists.newArrayList();
                    Map<Long, Set<Map<Integer, Set<Integer>>>> map = Maps.newHashMap();
                    Map<Long, String> nameMap = Maps.newHashMap();
                    chapterss.forEach(d -> {
                        if (d.getSubjectId() != null) {
                            nameMap.put(d.getSubjectId(), d.getSubjectName());
                            Set<Map<Integer, Set<Integer>>> tempList;
                            if (map.containsKey(d.getSubjectId())) {
                                tempList = map.get(d.getSubjectId());
                                Map<Integer, Set<Integer>> map2 = null;
                                for (Map<Integer, Set<Integer>> d2 : tempList) {
                                    if (d2.containsKey(d.getStage())) {
                                        Set<Integer> list2 = d2.get(d.getStage());
                                        list2.add(d.getGrade());
                                        map2 = Maps.newHashMap();
                                        map2.put(d.getStage(), list2);
                                        break;
                                    }
                                }
                                if (map2 == null) {
                                    map2 = Maps.newHashMap();
                                    map2.put(d.getStage(), Sets.newHashSet(d.getGrade()));
                                }
                                tempList.add(map2);
                            } else {
                                tempList = Sets.newHashSet();
                                Map<Integer, Set<Integer>> tempMap = Maps.newHashMap();
                                tempMap.put(d.getStage(), Sets.newHashSet(d.getGrade()));
                                tempList.add(tempMap);
                            }
                            map.put(d.getSubjectId(), tempList);
                        }
                    });
                    map.forEach((k, v) -> {
                        ChaptersTreeDto temp = new ChaptersTreeDto();
                        temp.setId(k);
                        temp.setName(nameMap.get(k));
                        List<ChaptersStageDto> stageList = Lists.newArrayList();
                        List<Integer> stageTempList = Lists.newArrayList();
                        v.forEach(d -> {
                            d.forEach((k1, v1) -> {
                                if (!stageTempList.contains(k1)) {
                                    ChaptersStageDto temp1 = new ChaptersStageDto();
                                    temp1.setStage(k1);
                                    temp1.setGrades(v1);
                                    stageList.add(temp1);
                                    stageTempList.add(k1);
                                }
                            });
                        });
                        temp.setStages(stageList);
                        list.add(temp);
                    });
                    dto.setList(list);
                }
            } else if (EntitySetting.CHAPTERS_TYPE_EXAM == type) {//备考
                List<Chapters> chapterss = dao.selectByExample(new Example.Builder(Chapters.class)
                        .where(WeekendSqls.<Chapters>custom().andEqualTo(Chapters::getType, type)
                                .andEqualTo(Chapters::getStatus, EntitySetting.COMMON_SUCCESS)
                        ).selectDistinct("name", "stage", "grade")
                        .build());
                if (!Collections3.isEmpty(chapterss)) {
                    List<ChaptersTreeDto> list = Lists.newArrayList();
                    Map<String, Set<Map<Integer, Set<Integer>>>> map = Maps.newHashMap();
                    chapterss.forEach(d -> {
                        if (!Strings.isNullOrEmpty(d.getName()) && !Strings.isNullOrEmpty(d.getName().trim())) {
                            Set<Map<Integer, Set<Integer>>> tempList;
                            if (map.containsKey(d.getName().trim())) {
                                tempList = map.get(d.getName().trim());
                                Map<Integer, Set<Integer>> map2 = null;
                                for (Map<Integer, Set<Integer>> d2 : tempList) {
                                    if (d2.containsKey(d.getStage())) {
                                        Set<Integer> list2 = d2.get(d.getStage());
                                        list2.add(d.getGrade());
                                        map2 = Maps.newHashMap();
                                        map2.put(d.getStage(), list2);
                                        break;
                                    }
                                }
                                if (map2 == null) {
                                    map2 = Maps.newHashMap();
                                    map2.put(d.getStage(), Sets.newHashSet(d.getGrade()));
                                }
                                tempList.add(map2);
                            } else {
                                tempList = Sets.newHashSet();
                                Map<Integer, Set<Integer>> tempMap = Maps.newHashMap();
                                tempMap.put(d.getStage(), Sets.newHashSet(d.getGrade()));
                                tempList.add(tempMap);
                            }
                            map.put(d.getName().trim(), tempList);
                        }
                    });
                    map.forEach((k, v) -> {
                        ChaptersTreeDto temp = new ChaptersTreeDto();
                        temp.setName(k);
                        List<ChaptersStageDto> stageList = Lists.newArrayList();
                        List<Integer> stageTempList = Lists.newArrayList();
                        v.forEach(d -> {
                            d.forEach((k1, v1) -> {
                                if (!stageTempList.contains(k1)) {
                                    ChaptersStageDto temp1 = new ChaptersStageDto();
                                    temp1.setStage(k1);
                                    temp1.setGrades(v1);
                                    stageList.add(temp1);
                                    stageTempList.add(k1);
                                }
                            });
                        });
                        temp.setStages(stageList);
                        list.add(temp);
                    });
                    dto.setList(list);
                }

            }
        }
        return dto;
    }

    public ListDto<CommonIdAndNameDto> findChaptersBySubjectIdAndType(Integer stage,Long subjectId, Integer grade, Integer type) {
        ListDto<CommonIdAndNameDto> dto = new ListDto<>(false);
        if (type == null) {
            dto.setMsg("类型为空");
        } else {
            dto.setStatus(true);
            List<CommonIdAndNameDto> list = Lists.newArrayList();
            Chapters chapters = new Chapters();
            chapters.setType(type);
            if (stage != null && stage != 0L) {
                chapters.setStage(stage);
            }
            if (subjectId != null && subjectId != 0L) {
                chapters.setSubjectId(subjectId);
            }
            if (grade != null && grade != 0L) {
                chapters.setGrade(grade);
            }
            List<Chapters> chapterss = dao.select(chapters);
            chapterss.forEach(d -> {
                CommonIdAndNameDto temp = new CommonIdAndNameDto();
                temp.setId(d.getId());
                temp.setName(d.getName());
                list.add(temp);
            });
            dto.setList(list);
        }
        return dto;
    }


    public ListDto<CommonIdAndNameDto> findChaptersBySupIdAndType(Long supId, Integer type) {
        ListDto<CommonIdAndNameDto> dto = new ListDto<>(true);
        if (supId == null || supId == 0L) {
            dto.setMsg("父级信息为空");
        } else {
            Chapters chapters = new Chapters();
            chapters.setStatus(EntitySetting.COMMON_SUCCESS);
            chapters.setSupId(supId);
            chapters.setType(type);
            List<Chapters> chapterss = dao.select(chapters);
            List<CommonIdAndNameDto> list = Lists.newArrayList();
            chapterss.forEach(d -> {
                CommonIdAndNameDto temp = new CommonIdAndNameDto();
                temp.setId(d.getId());
                temp.setName(d.getName());
                list.add(temp);
            });
            dto.setList(list);
        }
        return dto;
    }

    public ListDto<CommonIdAndNameDto> findVersionChapters(Integer grade, Integer term, Long versionId) {
        ListDto<CommonIdAndNameDto> dto = new ListDto<>(true);
        if (versionId == null || versionId == 0L) {
            dto.setMsg("版本信息为空");
        } else {
            Chapters chapters = new Chapters();
            //先根据版本信息查询册别之后再查询册别下的章节
            chapters.setStatus(EntitySetting.COMMON_SUCCESS);
            chapters.setSupId(versionId);
            chapters.setType(EntitySetting.CHAPTERS_TYPE_BOOK);
            if (grade != null) {
                chapters.setGrade(grade);
            }
            if (term != null) {
                chapters.setTerm(term);
            }
            List<Chapters> chapterss = dao.select(chapters);
            if(Collections3.isNotEmpty(chapterss)){
                List<Long> ids = chapterss.stream().map(d->d.getId()).collect(Collectors.toList());
                List<Chapters> versionChapterss = dao.selectByExample(new Example.Builder(Chapters.class)
                        .where(WeekendSqls.<Chapters>custom()
                        .andEqualTo(Chapters::getStatus,EntitySetting.COMMON_SUCCESS)
                        .andEqualTo(Chapters::getType,EntitySetting.CHAPTERS_TYPE_CHAPTER)
                        .andIn(Chapters::getSupId,ids)).build());
                List<CommonIdAndNameDto> list = Lists.newArrayList();
                versionChapterss.forEach(d -> {
                    CommonIdAndNameDto temp = new CommonIdAndNameDto();
                    temp.setId(d.getId());
                    temp.setName(d.getName());
                    list.add(temp);
                });
                dto.setList(list);
            }
        }
        return dto;
    }
    public ListDto<CommonTreeDto> findVersionChaptersTree(Integer grade, Integer term, Long versionId) {
        ListDto<CommonTreeDto> dto = new ListDto<>(true);
        if (versionId == null || versionId == 0L) {
            dto.setMsg("版本信息为空");
        } else {
            Chapters chapters = new Chapters();
            //先根据版本信息查询册别之后再查询册别下的章节
            chapters.setStatus(EntitySetting.COMMON_SUCCESS);
            chapters.setSupId(versionId);
            chapters.setType(EntitySetting.CHAPTERS_TYPE_BOOK);
            if (grade != null) {
                chapters.setGrade(grade);
            }
            if (term != null) {
                chapters.setTerm(term);
            }
            List<Chapters> chapterss = dao.select(chapters);
            if(Collections3.isNotEmpty(chapterss)){
                chapterss.forEach(d1->{
                    //查询所有子节点
                    List<Chapters> versionChapterss = dao.selectByExample(new Example.Builder(Chapters.class)
                            .where(WeekendSqls.<Chapters>custom()
                                    .andEqualTo(Chapters::getStatus,EntitySetting.COMMON_SUCCESS)
                                    .andEqualTo(Chapters::getType,EntitySetting.CHAPTERS_TYPE_CHAPTER)
                                    .andLike(Chapters::getPath,d1.getPath()+",%")).build());
                    //将数据进行分组
                    List<Long> chaptersIds = versionChapterss.stream().map(d->d.getId()).collect(Collectors.toList());
                    List<TreeNodeDto> nodeDtoList = Lists.newArrayList();
                    versionChapterss.forEach(d -> {
                        TreeNodeDto temp = new TreeNodeDto();
                        temp.setCode(d.getId());
                        temp.setName(d.getName());
                        if(chaptersIds.contains(d.getSupId())){
                            temp.setParent(d.getSupId());
                        }
                        nodeDtoList.add(temp);
                    });
                    TreeDto treeDto = new TreeDto(nodeDtoList);
                    dto.setList(BeanMapper.mapList(treeDto.getRoot(),CommonTreeDto.class));
                });
            }
        }
        return dto;
    }

    public ListDto<String> findChaptersNameByGradeAndType(Integer grade, Integer type) {
        ListDto<String> dto = new ListDto<>(true);
        if (grade == null || grade == 0L) {
            dto.setMsg("年级信息为空");
        } else {
            Chapters chapters = new Chapters();
            chapters.setStatus(EntitySetting.COMMON_SUCCESS);
            chapters.setGrade(grade);
            chapters.setType(type);
            List<Chapters> chapterss = dao.select(chapters);
            List<String> list = Lists.newArrayList();
            chapterss.forEach(d -> {
                if (!list.contains(d.getName())) {
                    list.add(d.getName());
                }
            });
            dto.setList(list);
        }
        return dto;
    }

    public List<JsTreeAjax> findChaptersByStageAndSubjectAndSupId(Integer stage, Long subjectId, Long supId) {
        List<JsTreeAjax> list = Lists.newArrayList();
        Chapters chapters = new Chapters();
        chapters.setStatus(EntitySetting.COMMON_SUCCESS);
        boolean flag = false;
        if (supId == null || supId == 0L) {//查询版本
            if (stage != null || subjectId != null) {
                flag = true;
                chapters.setStage(stage);
                chapters.setSubjectId(subjectId);
                chapters.setType(EntitySetting.CHAPTERS_TYPE_VERSION);
            }
        } else {
            flag = true;
            chapters.setSupId(supId);
        }
        if (flag) {
            List<Chapters> chapterss = dao.select(chapters);
            chapterss.forEach(d -> {
                JsTreeAjax temp = new JsTreeAjax();
                temp.setId(d.getId().toString());
                temp.setText(d.getName());
                temp.setChildren(true);
                if (d.getSupId() == null || d.getSupId() == 0L) {
                    temp.setType("root");
                    temp.setParent("#");
                } else {
                    temp.setParent(d.getSupId().toString());
                }
                list.add(temp);
            });
        }
        return list;
    }



    public List<Chapters> findByChapter(Integer stage,Long subjectId){
        List<Chapters> chaptersList =  dao.findByChapter(stage,subjectId);
    return chaptersList;
    }

}