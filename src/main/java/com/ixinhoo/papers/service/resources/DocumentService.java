package com.ixinhoo.papers.service.resources;

import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.PageDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.api.shiro.ShiroUser;
import com.chunecai.crumbs.api.util.UsersUtil;
import com.chunecai.crumbs.code.util.collection.Collections3;
import com.chunecai.crumbs.code.util.key.UuidMaker;
import com.chunecai.crumbs.code.util.mapper.BeanMapper;
import com.chunecai.crumbs.code.util.mapper.JsonMapper;
import com.cnjy21.api.constant.APIConstant;
import com.cnjy21.api.model.document.Preview;
import com.cnjy21.api.service.APIDocumentService;
import com.cnjy21.api.service.APIRequestParams;
import com.cnjy21.api.util.APIResult;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ixinhoo.papers.dao.base.ChaptersDao;
import com.ixinhoo.papers.dao.common.InterfaceInfoDao;
import com.ixinhoo.papers.dao.resources.*;
import com.ixinhoo.papers.dto.file.FileUploadDto;
import com.ixinhoo.papers.dto.resources.*;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.authority.DocumentAuditRecord;
import com.ixinhoo.papers.entity.base.Chapters;
import com.ixinhoo.papers.entity.common.InterfaceInfo;
import com.ixinhoo.papers.entity.resources.*;
import com.ixinhoo.papers.service.authority.DocumentAuditRecordService;
import com.ixinhoo.papers.service.file.QiniuFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentService extends BaseService<Document> {
    private static Logger logger = LoggerFactory.getLogger(DocumentService.class);
    @Autowired
    private DocumentDao dao;
    @Autowired
    private DocumentStage1Dao documentStage1Dao;
    @Autowired
    private DocumentStage2Dao documentStage2Dao;
    @Autowired
    private DocumentStage3Dao documentStage3Dao;
    @Autowired
    private DocumentExtendDao documentExtendDao;
    @Autowired
    private DocumentChapterDao documentChapterDao;
    @Autowired
    private DocumentViewDao documentViewDao;
    @Autowired
    private InterfaceInfoDao interfaceInfoDao;
    @Autowired
    private ChaptersDao chaptersDao;
    @Autowired
    private QiniuFileService qiniuFileService;
    @Autowired
    private DocumentAuditRecordService documentAuditRecordService;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    /**
     * 同步所有文档资源
     *
     * @return
     */
    @Transactional
    public StatusDto syncDocument() {
        StatusDto dto = new StatusDto(true);
        syncDocumentStage1();
        syncDocumentStage2();
        syncDocumentStage3();
        return dto;
    }

    /**
     * 同步高中资源文档
     *
     * @return
     */
    @Transactional
    public StatusDto syncDocumentStage3() {
        logger.error("开始同步syncDocumentStage3======");
        StatusDto dto = new StatusDto(true);
        APIDocumentService service = new APIDocumentService();
        //需要查询原来的数据进行增量更新，
        List<Integer> types = Lists.newArrayList();
        types.add(EntitySetting.CHAPTERS_TYPE_CHAPTER);
        types.add(EntitySetting.CHAPTERS_TYPE_EXAM);
        List<Chapters> chapterss = chaptersDao.selectByExample(new Example.Builder(Chapters.class)
                .where(WeekendSqls.<Chapters>custom().andIn(Chapters::getType, types).andEqualTo(Chapters::getStage, APIConstant.STAGE_GAOZHONG))
                .build());
        if (Collections3.isEmpty(chapterss)) {
            dto.setMsg("章节为空,请先同步章节");
        } else {
            List<DocumentStage3> documentStages = documentStage3Dao.selectAll();
            List<String> codeList = documentStages.stream().map(d -> d.getCode()).collect(Collectors.toList());
            //根据章节获取题目
            if (EntitySetting.TEST_ENV) {
                int size = codeList.size();
                for (Chapters d : chapterss) {
                    codeList = syncDocumentStage(codeList, service, d, EntitySetting.INTERFACE_FIRST_PAGE, EntitySetting.INTERFACE_MAX_PERPAGE);
                    if (codeList.size() != size) {
                        break;
                    }
                }
            } else {
                for (Chapters d : chapterss) {
                    codeList = syncDocumentStage(codeList, service, d, EntitySetting.INTERFACE_FIRST_PAGE, EntitySetting.INTERFACE_MAX_PERPAGE);
                }
            }
            dto.setStatus(true);
        }
        logger.error("结束同步syncDocumentStage3======");
        return dto;
    }

    /**
     * 同步初中资源文档
     *
     * @return
     */
    @Transactional
    public StatusDto syncDocumentStage2() {
        logger.error("开始同步syncDocumentStage2======");
        StatusDto dto = new StatusDto(false);
        APIDocumentService service = new APIDocumentService();
        //需要查询原来的数据进行增量更新，
        List<Integer> types = Lists.newArrayList();
        types.add(EntitySetting.CHAPTERS_TYPE_CHAPTER);
        types.add(EntitySetting.CHAPTERS_TYPE_EXAM);
        List<Chapters> chapterss = chaptersDao.selectByExample(new Example.Builder(Chapters.class)
                .where(WeekendSqls.<Chapters>custom().andIn(Chapters::getType, types).andEqualTo(Chapters::getStage, APIConstant.STAGE_CHUZHONG))
                .build());
        if (Collections3.isEmpty(chapterss)) {
            dto.setMsg("章节为空,请先同步章节");
        } else {
            List<DocumentStage2> documentStages = documentStage2Dao.selectAll();
            List<String> codeList = documentStages.stream().map(d -> d.getCode()).collect(Collectors.toList());
            if (EntitySetting.TEST_ENV) {
                int size = codeList.size();
                for (Chapters d : chapterss) {
                    codeList = syncDocumentStage(codeList, service, d, EntitySetting.INTERFACE_FIRST_PAGE, EntitySetting.INTERFACE_MAX_PERPAGE);
                    if (codeList.size() != size) {
                        break;
                    }
                }
            } else {
                for (Chapters d : chapterss) {
                    codeList = syncDocumentStage(codeList, service, d, EntitySetting.INTERFACE_FIRST_PAGE, EntitySetting.INTERFACE_MAX_PERPAGE);
                }
            }
            dto.setStatus(true);
        }
        logger.error("结束同步syncDocumentStage2======");
        return dto;
    }

    /**
     * 同步小学资源文档
     *
     * @return
     */
    @Transactional
    public StatusDto syncDocumentStage1() {
        logger.error("开始同步syncDocumentStage1======");
        StatusDto dto = new StatusDto(true);
        APIDocumentService service = new APIDocumentService();
        //需要查询原来的数据进行增量更新，
        //查询小学的章节
        List<Integer> types = Lists.newArrayList();
        types.add(EntitySetting.CHAPTERS_TYPE_CHAPTER);
        types.add(EntitySetting.CHAPTERS_TYPE_EXAM);
        List<Chapters> chapterss = chaptersDao.selectByExample(new Example.Builder(Chapters.class)
                .where(WeekendSqls.<Chapters>custom().andIn(Chapters::getType, types).andEqualTo(Chapters::getStage, APIConstant.STAGE_XIAOXUE))
                .build());
        if (Collections3.isEmpty(chapterss)) {
            dto.setMsg("章节为空,请先同步章节");
        } else {
            List<DocumentStage1> documentStages = documentStage1Dao.selectAll();
            List<String> codeList = documentStages.stream().map(d -> d.getCode()).collect(Collectors.toList());
            if (EntitySetting.TEST_ENV) {
                int size = codeList.size();
                for (Chapters d : chapterss) {
                    codeList = syncDocumentStage(codeList, service, d, EntitySetting.INTERFACE_FIRST_PAGE, EntitySetting.INTERFACE_MAX_PERPAGE);
                    if (codeList.size() != size) {
                        break;
                    }
                }
            } else {
                for (Chapters d : chapterss) {
                    codeList = syncDocumentStage(codeList, service, d, EntitySetting.INTERFACE_FIRST_PAGE, EntitySetting.INTERFACE_MAX_PERPAGE);
                }
            }
            dto.setStatus(true);
        }
        logger.error("结束同步syncDocumentStage1======");
        return dto;
    }

    @Transactional
    private List<String> syncDocumentStage(List<String> codeList, APIDocumentService service, Chapters chapters, int page, int perPage) {
        if (chapters.getCode().matches("[\\d]+")) {
            APIRequestParams apiRequestParams = new APIRequestParams();
            apiRequestParams.page = page;
            apiRequestParams.perPage = perPage;
            apiRequestParams.stage = chapters.getStage();
            apiRequestParams.subjectId = chapters.getSubjectId().intValue();
            apiRequestParams.chapterId = Integer.parseInt(chapters.getCode());
            APIResult<com.cnjy21.api.model.document.Document> result = service.getDocuments(apiRequestParams);
            if (result != null && Collections3.isNotEmpty(result.getData())) {
                InterfaceInfo interfaceInfo = new InterfaceInfo();
                interfaceInfo.setTime(System.currentTimeMillis());
                interfaceInfo.setType(EntitySetting.INTERFACE_DOCUMENT_STAGE);
                interfaceInfo.setReqData(JsonMapper.nonDefaultMapper().toJson(apiRequestParams));
                interfaceInfo.setRspData(JsonMapper.nonDefaultMapper().toJson(result));
//                ShiroUser user = UsersUtil.ShiroUser();
                ShiroUser user = null;
                if (user != null) {
                    interfaceInfo.setCreateId(user.getId());
                    interfaceInfo.setUpdateId(user.getId());
                }
                interfaceInfo.setCreateTime(System.currentTimeMillis());
                interfaceInfo.setUpdateTime(System.currentTimeMillis());
                interfaceInfoDao.insert(interfaceInfo);
                //判断页数与返回的数据是否一致循环获取试卷列表
                List<DocumentExtend01> extendList = Lists.newArrayList();
                List<DocumentChapter> chapterList = Lists.newArrayList();
                List<DocumentView> viewList = Lists.newArrayList();
                result.getData().forEach(d -> {
                    if (!codeList.contains(d.getItemId().toString())) {
                        codeList.add(d.getItemId().toString());
                        //TODO cici 将返回的预览图存储起来
                        Document temp = new Document();
                        if (user != null) {
                            temp.setCreateId(user.getId());
                            temp.setUpdateId(user.getId());
                        }
                        temp.setCreateTime(System.currentTimeMillis());
                        temp.setUpdateTime(System.currentTimeMillis());
                        temp.setCode(d.getItemId().toString());
                        if (d.getSubjectId() != null) {
                            temp.setSubjectId(d.getSubjectId().longValue());
                        }
                        if(Strings.isNullOrEmpty(d.getPoint())){
                            temp.setCoin(0);
                        }else{
                            temp.setCoin(Integer.parseInt(d.getPoint()));
                        }
                        temp.setTerm(chapters.getTerm());
                        temp.setVersionId(d.getVersionId().longValue());
                        temp.setVersionName(d.getVersionName());
                        if(Strings.isNullOrEmpty(d.getYear())){
                            temp.setYear(0);
                        }else{
                            temp.setYear(Integer.parseInt(d.getYear()));
                        }
                        temp.setStatus(EntitySetting.COMMON_SUCCESS);
                        temp.setCreatedAt(d.getDateline() * 1000);
                        if (d.getUpdatedAt() != null) {
                            temp.setUpdatedAt(d.getUpdatedAt() * 1000);
                        } else {
                            temp.setUpdatedAt(d.getDateline() * 1000);
                        }
                        temp.setIntro(d.getIntro());
                        temp.setProvinceName(d.getCityName());
                        if (d.getCityId() != null) {
                            temp.setProvinceId(d.getCityId().longValue());
                        }
                        temp.setTypeName(d.getTypeName());
                        temp.setTypeId(d.getType());
                        temp.setStage(d.getStage());
                        temp.setSubjectId(d.getSubjectId().longValue());
                        temp.setSubjectName(d.getSubjectName());
                        temp.setTitle(d.getTitle());
                        //查询资源详情和资源预览信息
                        List<Preview> previewList = service.getPreview(d.getItemId());
                        if (Collections3.isNotEmpty(previewList)) {
                            temp.setViewFlag(EntitySetting.COMMON_SUCCESS);
                        }
                        com.cnjy21.api.model.document.Document document = service.getDocumentByItemId(d.getItemId());
                        DocumentStage documentStage = BeanMapper.map(temp, DocumentStage.class);
                        if (document != null) {
                            temp.setFileType(document.getFileType());
                            if (!Strings.isNullOrEmpty(document.getDownloadUrl())) {
                                //TODO cici 下载资源文件后重新上传到自己的文件服务器中，将自己的路径进行返回
                                FileUploadDto fileUploadDto = qiniuFileService.uploadFileByUrl(document.getDownloadUrl(), d.getStage(), true);
                                if (fileUploadDto != null && fileUploadDto.getStatus()) {
                                    documentStage.setDownloadAddress(fileUploadDto.getAddress());
                                } else {
                                    documentStage.setDownloadAddress(document.getDownloadUrl());
                                }
                            }
                            documentStage.setFileType(document.getFileType());
                            documentStage.setFileSize(document.getFileSize());
                        }
                        dao.insert(temp);
                        if (Collections3.isNotEmpty(previewList)) {
                            previewList.forEach(d1 -> {
                                if (Collections3.isNotEmpty(d1.getPreviewFiles())) {
                                    d1.getPreviewFiles().forEach(d2 -> {
                                        DocumentView documentView = new DocumentView();
                                        documentView.setDocumentCode(temp.getCode());
                                        documentView.setDocumentId(temp.getId());
                                        documentView.setFileType(d2.getFileType());
                                        if (!Strings.isNullOrEmpty(d2.getFileUrl())) {
                                            //TODO cici 下载后重新上传至本地服务器
                                            FileUploadDto fileUploadDto = qiniuFileService.uploadFileByUrl(d2.getFileUrl(), d.getStage(), false);
                                            if (fileUploadDto != null && fileUploadDto.getStatus()) {
                                                documentView.setFileUrl(fileUploadDto.getAddress());
                                            } else {
                                                documentView.setFileUrl(d2.getFileUrl());
                                            }
                                        }
                                        documentView.setTitle(temp.getTitle());
                                        if (user != null) {
                                            documentView.setCreateId(user.getId());
                                            documentView.setUpdateId(user.getId());
                                        }
                                        documentView.setCreateTime(System.currentTimeMillis());
                                        documentView.setUpdateTime(System.currentTimeMillis());
                                        viewList.add(documentView);
                                    });
                                }
                            });
                        }
                        if (d.getChapterPath() != null && d.getChapterPath().size() != 0) {
                            d.getChapterPath().forEach(d1 -> {
                                DocumentChapter documentChapter = new DocumentChapter();
                                documentChapter.setDocumentId(temp.getId());
                                documentChapter.setChapterId(d1.getId().longValue());
                                documentChapter.setChapterName(d1.getName());
                                documentChapter.setChapterPath(d1.getPath());
                                documentChapter.setDocumentCode(temp.getCode());
                                if (user != null) {
                                    documentChapter.setCreateId(user.getId());
                                    documentChapter.setUpdateId(user.getId());
                                }
                                documentChapter.setCreateTime(System.currentTimeMillis());
                                documentChapter.setUpdateTime(System.currentTimeMillis());
                                chapterList.add(documentChapter);
                            });
                        }
                        documentStage.setDocumentId(temp.getId());
                        if (APIConstant.STAGE_XIAOXUE == chapters.getStage()) {
                            documentStage1Dao.insert(BeanMapper.map(documentStage, DocumentStage1.class));
                        } else if (APIConstant.STAGE_CHUZHONG == chapters.getStage()) {
                            documentStage2Dao.insert(BeanMapper.map(documentStage, DocumentStage2.class));
                        } else if (APIConstant.STAGE_GAOZHONG == chapters.getStage()) {
                            documentStage3Dao.insert(BeanMapper.map(documentStage, DocumentStage3.class));
                        }
                        DocumentExtend01 documentExtend = new DocumentExtend01();
                        documentExtend.setId(temp.getId());
                        if (Strings.isNullOrEmpty(d.getStars())) {
                            documentExtend.setStars(0d);
                        }else{
                            documentExtend.setStars(Double.parseDouble(d.getStars()));
                        }
                        extendList.add(documentExtend);
                    }
                });
                if (Collections3.isNotEmpty(extendList)) {
                    documentExtendDao.insertList(extendList);
                }
                if (Collections3.isNotEmpty(chapterList)) {
                    documentChapterDao.insertList(chapterList);
                }
                if (Collections3.isNotEmpty(viewList)) {
                    documentViewDao.insertList(viewList);
                }
                if (!EntitySetting.TEST_ENV && result.getPage() != null) {
                    if (result.getPage().getCurrentPage() < result.getPage().getPageCount()) {
                        syncDocumentStage(codeList, service, chapters, page + 1, perPage);
                    }
                }
            }
        }
        return codeList;
    }


    public PageDto<DocumentSearchRspDto> searchDocument(DocumentSearchReqDto reqDto) {
        PageDto<DocumentSearchRspDto> dto = new PageDto<>(true);
        if (reqDto == null) {
            dto.setMsg("参数为空");
        } else {
            Integer count = dao.selectCountBySearch(reqDto);
            dto.setCount(count);
            dto.setPage(reqDto.getP());
            dto.setSize(reqDto.getS());
            if (reqDto.getS() != null) {
                dto.setCountPage(count % reqDto.getS() == 0 ? count / reqDto.getS() : count / reqDto.getS() + 1);
            }
            if (count != null && count != 0) {
                List<DocumentSearchRspDto> list = dao.selectBySearch(reqDto);
                dto.setList(list);
            }
        }
        return dto;
    }

    public DetailDto<DocumentDetailDto> findDocumentDetailByIdAndStage(Long id, Integer stage) {
        DetailDto<DocumentDetailDto> dto = new DetailDto<>(false);
        if (id == null || id == 0L) {
            dto.setMsg("资源id参数空");
        } else if (stage == null) {
            dto.setMsg("学段信息为空");
        } else {
            DocumentStage documentStage = null;
            if (APIConstant.STAGE_XIAOXUE == stage) {
                DocumentStage1 temp = new DocumentStage1();
                temp.setDocumentId(id);
                documentStage = BeanMapper.map(documentStage1Dao.selectOne(temp), DocumentStage.class);
            } else if (APIConstant.STAGE_CHUZHONG == stage) {
                DocumentStage2 temp = new DocumentStage2();
                temp.setDocumentId(id);
                documentStage = BeanMapper.map(documentStage2Dao.selectOne(temp), DocumentStage.class);
            } else if (APIConstant.STAGE_GAOZHONG == stage) {
                DocumentStage3 temp = new DocumentStage3();
                temp.setDocumentId(id);
                documentStage = BeanMapper.map(documentStage3Dao.selectOne(temp), DocumentStage.class);
            }
            if (documentStage != null) {
                DocumentDetailDto detail = BeanMapper.map(documentStage, DocumentDetailDto.class);
                //查询扩展表
                DocumentExtend01 documentExtend = documentExtendDao.selectByPrimaryKey(id);
                if (documentExtend != null) {
                    detail.setDownloadNum(documentExtend.getDownloadNum());
                    detail.setScoreNum(documentExtend.getScoreNum());
                    detail.setScore(documentExtend.getScore());
                }
                //查询预览信息
                DocumentView documentView = new DocumentView();
                documentView.setDocumentId(id);
                List<DocumentView> viewList = documentViewDao.select(documentView);
                detail.setViewList(BeanMapper.mapList(viewList, DocumentViewDto.class));
                dto.setDetail(detail);
            }
            dto.setStatus(true);
        }
        return dto;
    }

    /**
     * 1)	根据老师所浏览的资料的主题（通常为教材中的章节信息）推荐相关资料
     * 2)	找出与当前资料主题、教材版本及类型一致的资料
     * 3)	从步骤2中筛选出需要付费，且下载次数前5的5份资料推荐给用户
     * 4)	如果步骤3中选出的资料不足5份（如差2份），则重复步骤3，把“付费”这个条件去掉，从中选择2份下载次数前2的资料补足
     *
     * @param id
     * @return
     */
    public ListDto<DocumentSearchRspDto> findSameTypeDocumentById(Long id) {
        ListDto<DocumentSearchRspDto> dto = new ListDto<>(false);
        if (id == null || id == 0L) {
            dto.setMsg("传输参数为空");
        } else {
            Document document = dao.selectByPrimaryKey(id);
            if (document != null) {
                //查询资源的所属章节
                DocumentChapter documentChapter = new DocumentChapter();
                documentChapter.setDocumentId(id);
                List<DocumentChapter> chapterList = documentChapterDao.select(documentChapter);
                List<Long> chapterIds = chapterList.stream().map(d -> d.getChapterId()).collect(Collectors.toList());
                List<DocumentSearchRspDto> documents = dao.selectSameTypeById(chapterIds, document.getVersionId(), document.getTypeId(), true);
                if (Collections3.isEmpty(documents) || documents.size() < 5) {//不足5份,去除条件再次查询
                    List<DocumentSearchRspDto> documents1 = dao.selectSameTypeById(chapterIds, document.getVersionId(), document.getTypeId(), false);
                    if (documents.size() + documents1.size() < 5) {
                        documents1.addAll(dao.selectSameTypeById(null, null, null, false));
                    }
                    for (int i = 0; i < 5 - documents.size(); i++) {
                        documents.add(documents1.get(i));
                    }
                }
                dto.setList(documents);
                dto.setStatus(true);
            } else {
                dto.setMsg("文档不存在");
            }
        }
        return dto;
    }

    /**
     * 1)	根据老师所浏览的资料的主题（通常为教材中的章节信息）推荐相关资料
     * 2)	找出与当前资料主题、教材版本相同，但资料类型不同的资料
     * 3)	从步骤2中筛选出需要付费，且下载次数前5的5份资料推荐给用户
     * 4)	在步骤3中，注意同一类型（如教案）的资料最多推荐两份
     *
     * @param id
     * @param p
     * @param s
     * @return
     */
    public ListDto<DocumentSearchRspDto> findGuessDocumentByIdAndPage(Long id, Integer p, Integer s) {
        ListDto<DocumentSearchRspDto> dto = new ListDto<>(false);
        if (id == null || id == 0L || p == null || s == null) {
            dto.setMsg("传输参数为空");
        } else {
            Document document = dao.selectByPrimaryKey(id);
            if (document != null) {
                //查询文档中的所有类型-去重
                List<Document> typeDocs = dao.selectByExample(new Example.Builder(Document.class)
                        .where(WeekendSqls.<Document>custom().andEqualTo(Document::getStatus, EntitySetting.COMMON_SUCCESS))
                        .selectDistinct("typeId")
                        .build());
                List<Integer> typeIds = typeDocs.stream().map(d -> d.getTypeId()).collect(Collectors.toList());
                List<DocumentSearchRspDto> documents = Lists.newArrayList();
                //查询资源的所属章节
                DocumentChapter documentChapter = new DocumentChapter();
                documentChapter.setDocumentId(id);
                List<DocumentChapter> chapterList = documentChapterDao.select(documentChapter);
                List<Long> chapterIds = chapterList.stream().map(d -> d.getChapterId()).collect(Collectors.toList());
                //按照类型分页查询2个2个查询
                for (Integer d : typeIds) {
                    documents.addAll(dao.selectGuessByIdAndPage(chapterIds, document.getVersionId(), d, p, 2));
                    if (documents.size() >= s) {
                        break;
                    }
                }
                dto.setList(documents);
                dto.setStatus(true);
            } else {
                dto.setMsg("文档不存在");
            }
        }
        return dto;
    }

    public ListDto<DocumentSearchRspDto> findDownloadRecommendDocumentById(List<Long> ids) {
        ListDto<DocumentSearchRspDto> dto = new ListDto<>(false);
        if (Collections3.isEmpty(ids)) {
            dto.setMsg("传输参数为空");
        } else {
            //查询文档中的所有类型-去重
            List<Document> documentList = dao.selectByExample(new Example.Builder(Document.class)
                    .where(WeekendSqls.<Document>custom().andEqualTo(Document::getStatus, EntitySetting.COMMON_SUCCESS)
                            .andIn(Document::getId, ids))
                    .build());
            List<Integer> typeIds = documentList.stream().map(d -> d.getTypeId()).collect(Collectors.toList());
            typeIds = typeIds.stream().distinct().collect(Collectors.toList());//去重
            List<Long> versionIds = documentList.stream().map(d -> d.getVersionId()).collect(Collectors.toList());
            versionIds = versionIds.stream().distinct().collect(Collectors.toList());//去重
            List<DocumentSearchRspDto> documents = Lists.newArrayList();
            //查询资源的所属章节
            List<DocumentChapter> chapterList = documentChapterDao.selectByExample(new Example.Builder(DocumentChapter.class)
                    .where(WeekendSqls.<DocumentChapter>custom().andIn(DocumentChapter::getChapterId, typeIds))
                    .build());
            List<Long> chapterIds = chapterList.stream().map(d -> d.getChapterId()).collect(Collectors.toList());
            int s = 6;
            //按照类型分页查询2个2个查询
            for (Integer d : typeIds) {
                documents.addAll(dao.selectDownloadRecommendByIdAndPage(chapterIds, versionIds, d, 0, 2));
                if (documents.size() >= s) {
                    break;
                }
            }
            dto.setList(documents);
            dto.setStatus(true);

        }
        return dto;
    }

    @Transactional
    public StatusDto saveDocument(DocumentUploadDto entity) {
        StatusDto dto = new StatusDto(false);
        if (entity == null) {
            dto.setMsg("参数为空");
        } else {
            Document document = BeanMapper.map(entity, Document.class);
            document.setCreatedAt(System.currentTimeMillis());
            document.setUpdatedAt(System.currentTimeMillis());
            document.setStatus(EntitySetting.COMMON_WAIT);
            document.setUserId(UsersUtil.id());
            document.setUserName(UsersUtil.name());
            document.setViewFlag(EntitySetting.COMMON_FAIL);
            document.setCode(UuidMaker.getInstance().getUuid(true));
            if(document.getTypeId()==null){
                document.setTypeId(0);
            }
            //根据章节id查询版本信息
            Chapters chapters = chaptersDao.selectByPrimaryKey(entity.getChapterId());
            if (chapters != null) {
                document.setTerm(chapters.getTerm());
                //查询版本
                List<Chapters> versionChapters = chaptersDao.selectByExample(new Example.Builder(Chapters.class)
                        .where(WeekendSqls.<Chapters>custom()
                                .andIn(Chapters::getId, Arrays.asList(chapters.getPath().split(",")))
                                .andEqualTo(Chapters::getType, EntitySetting.CHAPTERS_TYPE_VERSION))
                        .build());
                if (Collections3.isNotEmpty(versionChapters)) {
                    Chapters version = versionChapters.get(0);
                    document.setVersionId(version.getId());
                    document.setVersionName(version.getName());
                }
            }
            super.save(document);
            DocumentChapter documentChapter = new DocumentChapter();
            documentChapter.setDocumentCode(document.getCode());
            documentChapter.setDocumentId(document.getId());
            documentChapter.setChapterId(chapters.getId());
            documentChapter.setChapterName(chapters.getName());
            documentChapter.setChapterPath(chapters.getPath());
            documentChapterDao.insert(documentChapter);
            if (1 == entity.getStage()) {
                DocumentStage1 documentStage = BeanMapper.map(document, DocumentStage1.class);
                documentStage.setDocumentId(document.getId());
                documentStage.setId(null);
                documentStage.setUploadAddress(entity.getUploadAddress());
                documentStage.setDownloadAddress(entity.getUploadAddress());
                documentStage1Dao.insert(documentStage);
            } else if (2 == entity.getStage()) {
                DocumentStage2 documentStage = BeanMapper.map(document, DocumentStage2.class);
                documentStage.setDocumentId(document.getId());
                documentStage.setId(null);
                documentStage.setUploadAddress(entity.getUploadAddress());
                documentStage.setDownloadAddress(entity.getUploadAddress());
                documentStage2Dao.insert(documentStage);
            } else if (3 == entity.getStage()) {
                DocumentStage3 documentStage = BeanMapper.map(document, DocumentStage3.class);
                documentStage.setDocumentId(document.getId());
                documentStage.setId(null);
                documentStage.setUploadAddress(entity.getUploadAddress());
                documentStage.setDownloadAddress(entity.getUploadAddress());
                documentStage3Dao.insert(documentStage);
            }
            dto.setStatus(true);
        }
        return dto;
    }

    @Transactional
    public StatusDto updateAuditStatusByIdsAndStatus(List<Long> ids, Integer status) {
        StatusDto dto = new StatusDto(false);
        if (Collections3.isEmpty(ids) || status == null || status == 0L) {
            dto.setMsg("参数不全");
        } else {
            dto.setStatus(true);
            List<Document> list = dao.selectByExample(new Example.Builder(Document.class)
                    .where(WeekendSqls.<Document>custom()
                            .andIn(Document::getId, ids)).build());
            if (Collections3.isNotEmpty(list)) {
                list.forEach(d -> {
                    DocumentAuditRecord documentAuditRecord = new DocumentAuditRecord();
                    d.setStatus(status);
                    dao.updateByPrimaryKey(d);
                    if (d.getStage() == APIConstant.STAGE_XIAOXUE) {
                        DocumentStage1 documentStage = documentStage1Dao.selectOneByExample(new Example.Builder(DocumentStage1.class)
                                .where(WeekendSqls.<DocumentStage1>custom()
                                        .andEqualTo(DocumentStage1::getDocumentId, d.getId()))
                                .build());
                        documentStage.setStatus(status);
                        documentStage.setAuditTime(System.currentTimeMillis());
                        documentStage1Dao.updateByPrimaryKey(documentStage);
                        documentAuditRecord.setDocumentAddress(documentStage.getDownloadAddress());
                    } else if (d.getStage() == APIConstant.STAGE_CHUZHONG) {
                        DocumentStage2 documentStage = documentStage2Dao.selectOneByExample(new Example.Builder(DocumentStage2.class)
                                .where(WeekendSqls.<DocumentStage2>custom()
                                        .andEqualTo(DocumentStage2::getDocumentId, d.getId()))
                                .build());
                        documentStage.setStatus(status);
                        documentStage.setAuditTime(System.currentTimeMillis());
                        documentStage2Dao.updateByPrimaryKey(documentStage);
                        documentAuditRecord.setDocumentAddress(documentStage.getDownloadAddress());
                    } else if (d.getStage() == APIConstant.STAGE_GAOZHONG) {
                        DocumentStage3 documentStage = documentStage3Dao.selectOneByExample(new Example.Builder(DocumentStage3.class)
                                .where(WeekendSqls.<DocumentStage3>custom()
                                        .andEqualTo(DocumentStage3::getDocumentId, d.getId()))
                                .build());
                        documentStage.setStatus(status);
                        documentStage.setAuditTime(System.currentTimeMillis());
                        documentStage3Dao.updateByPrimaryKey(documentStage);
                        documentAuditRecord.setDocumentAddress(documentStage.getDownloadAddress());
                    }
                    documentAuditRecord.setDocumentId(d.getId());
                    documentAuditRecord.setStatus(status);
                    documentAuditRecord.setAuditId(UsersUtil.id());
                    documentAuditRecord.setAuditName(UsersUtil.name());
                    documentAuditRecord.setAuditUserType(EntitySetting.USER_TYPE_ADMIN);
                    documentAuditRecord.setUserId(d.getUserId());
                    documentAuditRecord.setUserName(d.getUserName());
                    documentAuditRecord.setTime(System.currentTimeMillis());
                    documentAuditRecord.setDocumentTitle(d.getTitle());
                    documentAuditRecordService.save(documentAuditRecord);
                });
            }
        }
        return dto;
    }
}