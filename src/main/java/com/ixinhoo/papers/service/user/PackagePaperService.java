package com.ixinhoo.papers.service.user;

import com.chunecai.crumbs.alipay.dto.AlipayPcOrderPayReqDto;
import com.chunecai.crumbs.alipay.service.AlipayService;
import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.code.util.collection.Collections3;
import com.chunecai.crumbs.code.util.image.QrCodeZxingUtil;
import com.chunecai.crumbs.code.util.key.Encodes;
import com.chunecai.crumbs.code.util.key.UuidMaker;
import com.chunecai.crumbs.code.util.mapper.JsonMapper;
import com.chunecai.crumbs.code.util.number.CodeNumberMaker;
import com.chunecai.crumbs.code.util.number.NumberUtil;
import com.chunecai.crumbs.wechat.service.WechatMerchantService;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.zxing.WriterException;
import com.ixinhoo.papers.dao.papers.QuestionAnswerDao;
import com.ixinhoo.papers.dao.papers.QuestionDao;
import com.ixinhoo.papers.dao.papers.QuestionKnowledgeDao;
import com.ixinhoo.papers.dao.user.PackagePaperDao;
import com.ixinhoo.papers.dao.user.UserDao;
import com.ixinhoo.papers.dao.user.UserExtendDao;
import com.ixinhoo.papers.dao.user.UserIntegralDao;
import com.ixinhoo.papers.dao.website.CoinSettingDao;
import com.ixinhoo.papers.dto.freemarker.AnswerCardDto;
import com.ixinhoo.papers.dto.freemarker.PaperDownloadDto;
import com.ixinhoo.papers.dto.freemarker.PaperDownloadQuestionDto;
import com.ixinhoo.papers.dto.freemarker.PaperDownloadQuestionTypeDto;
import com.ixinhoo.papers.dto.user.paper.*;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.papers.Question;
import com.ixinhoo.papers.entity.papers.QuestionAnswer;
import com.ixinhoo.papers.entity.papers.QuestionKnowledge;
import com.ixinhoo.papers.entity.user.*;
import com.ixinhoo.papers.entity.website.CoinSetting;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.sl.usermodel.Placeholder;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PackagePaperService extends BaseService<PackagePaper> {
    @Autowired
    private PackagePaperDao dao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private QuestionAnswerDao questionAnswerDao;
    @Autowired
    private QuestionKnowledgeDao questionKnowledgeDao;
    @Autowired
    private CoinSettingDao coinSettingDao;
    @Autowired
    private UserExtendDao userExtendDao;
    @Autowired
    private UserIntegralDao userIntegralDao;
    @Autowired
    private UserOrderService userOrderService;
    @Autowired
    private WechatMerchantService wechatMerchantService;
    @Autowired
    private AlipayService alipayService;
    @Value("${local.url}")
    private String localUrl;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    @Transactional
    public DetailDto<Long> savePackagePaper(PackagePaperSaveDto reqDto) {
        DetailDto<Long> dto = new DetailDto<>(false);
        if (reqDto == null) {
            dto.setMsg("参数为空");
        } else if (reqDto.getUserId() == null || reqDto.getUserId() == 0L) {
            dto.setMsg("用户为空");
        } else if (Strings.isNullOrEmpty(reqDto.getPaperTitle())) {
            dto.setMsg("试卷标题为空");
        } else if (Strings.isNullOrEmpty(reqDto.getSubjectName())) {
            dto.setMsg("学科名称为空");
        } else if (reqDto.getPaperFile() == null) {
            dto.setMsg("试卷下载格式为空");
        } else if (reqDto.getPaperSize() == null) {
            dto.setMsg("纸张设置为空");
        } /*else if (Collections3.isEmpty(reqDto.getPaperType())) {
            dto.setMsg("试卷类型为空");
        }*/ else if (Collections3.isEmpty(reqDto.getTemplate())) {
            dto.setMsg("试卷模板为空");
        } else if (reqDto.getSubjectId() == null || reqDto.getSubjectId() == 0L) {
            dto.setMsg("学科为空");
        } else if (reqDto.getStage() == null || reqDto.getStage() == 0L) {
            dto.setMsg("学段为空");
        } else if (reqDto.getVersionId() == null || reqDto.getVersionId() == 0L) {
            dto.setMsg("版本为空");
        } else if (reqDto.getGrade() == null || reqDto.getGrade() == 0L) {
            dto.setMsg("年级为空");
        } else {
            User user = userDao.selectByPrimaryKey(reqDto.getUserId());
            if (user == null) {
                dto.setMsg("用户不存在");
            } else {
                PackagePaper packagePaper = new PackagePaper();
                packagePaper.setUserId(reqDto.getUserId());
                packagePaper.setStage(reqDto.getStage());
                packagePaper.setSubjectId(reqDto.getSubjectId());
                packagePaper.setTerm(reqDto.getTerm());
                packagePaper.setTime(System.currentTimeMillis());
                packagePaper.setPaperFile(reqDto.getPaperFile());
                packagePaper.setPaperSize(reqDto.getPaperSize());
                packagePaper.setPaperTitle(reqDto.getPaperTitle());
                packagePaper.setTypeStyle(reqDto.getTypeStyle());
                packagePaper.setSubjectName(reqDto.getSubjectName());
                packagePaper.setPaperType(JsonMapper.nonEmptyMapper().toJson(reqDto.getPaperType()));
                packagePaper.setQuestion(JsonMapper.nonEmptyMapper().toJson(reqDto.getType()));
                packagePaper.setTemplate(JsonMapper.nonEmptyMapper().toJson(reqDto.getTemplate()));
                packagePaper.setRemark(JsonMapper.nonEmptyMapper().toJson(reqDto.getRemark()));
                if (reqDto.getId() != null && reqDto.getId() != 0L) {
                    packagePaper.setId(reqDto.getId());
                }
                super.save(packagePaper);
                dto.setStatus(true);
                dto.setDetail(packagePaper.getId());
            }
        }
        return dto;
    }

    public DetailDto<AnswerCardDto> findCardByPackagePaper(PackagePaperCardDto reqDto) {
        DetailDto<AnswerCardDto> dto = new DetailDto<>(false);
        if (reqDto == null) {
            dto.setMsg("参数为空");
        } else if (Strings.isNullOrEmpty(reqDto.getPaperTitle())) {
            dto.setMsg("试卷标题为空");
        } else if (Collections3.isEmpty(reqDto.getType())) {
            dto.setMsg("试卷类型为空");
        } else {
//            List<Long> questionIds = Lists.newArrayList();
//            reqDto.getType().forEach(d -> {
//                d.getQuestion().forEach(d1->{
//                    questionIds.add(d1.getId());
//                });
//            });
//            List<Question> questions = questionDao.selectByExample(new Example.Builder(Question.class)
//                    .where(WeekendSqls.<Question>custom().andIn(Question::getId,questionIds)).build());
            AnswerCardDto detail = new AnswerCardDto();
            detail.setTitle(reqDto.getPaperTitle());
            List<Object> options = Lists.newArrayList();
            List<String> answers = Lists.newArrayList();
            List<String> tempOptions = Lists.newArrayList();
            for (int i = 0; i < reqDto.getType().size(); i++) {
                PackagePaperCardTypeDto d = reqDto.getType().get(i);
                if (d.getId() == 1L || d.getId() == 2L) {//1-单选题,2-多选题
                    for (int i1 = 0; i1 < d.getQuestion().size(); i1++) {
                        tempOptions.add((i + 1) + "." + (i1 + 1));
                    }
                } else {//非选择题
                    for (int i1 = 0; i1 < d.getQuestion().size(); i1++) {
                        answers.add((i + 1) + "." + (i1 + 1));
                    }
                }
            }
            if (reqDto.getAnswerCard() == 1 || reqDto.getAnswerCard() == 2) {
                for (int i = 0; i < tempOptions.size(); i += 10) {
                    options.add(tempOptions.stream().skip(i).limit(10).collect(Collectors.toList()));
                }
            } else {
                List<List<String>> temp1 = Lists.newArrayList();
                for (int i = 0; i < tempOptions.size(); i += 5) {
                    temp1.add(tempOptions.stream().skip(i).limit(5).collect(Collectors.toList()));
                }
                for (int i = 0; i < temp1.size(); i += 3) {
                    options.add(temp1.stream().skip(i).limit(3).collect(Collectors.toList()));
                }
            }
            detail.setOptions(options);
            detail.setAnswers(answers);
            dto.setDetail(detail);
            dto.setStatus(true);
        }
        return dto;
    }

    public DetailDto<PackagePaperScanPayDto> checkDownloadPaper(Long userId, Long id) {
        DetailDto<PackagePaperScanPayDto> dto = new DetailDto<>(false);
        if (userId == null || userId == 0L) {
            dto.setMsg("用户为空");
        } else if (id == null || id == 0L) {
            dto.setMsg("组卷id为空");
        } else {
            UserExtend user = userExtendDao.selectByPrimaryKey(userId);
            if (user == null) {
                dto.setMsg("用户不存在");
            } else {
                PackagePaper packagePaper = dao.selectByPrimaryKey(id);
                if (packagePaper == null) {
                    dto.setMsg("组卷不存在,请确认组卷信息");
                } else {
                    CoinSetting coinSetting = new CoinSetting();
                    coinSetting.setStatus(EntitySetting.COMMON_SUCCESS);
                    List<CoinSetting> list = coinSettingDao.select(coinSetting);
                    if (Collections3.isEmpty(list)) {//没有设置,默认可以直接下载
                        dto.setStatus(true);
                    } else {
                        coinSetting = list.get(0);
                        Integer coin = coinSetting.getPaperCoin();
                        if (coin == null || coin == 0L) {
                            dto.setStatus(true);
                        } else if (user.getCoin() != null && user.getCoin() >= coin) {
                            dto.setStatus(true);
                        } else {
                            dto.setStatus(false);
                            PackagePaperScanPayDto packagePaperScanPayDto = new PackagePaperScanPayDto();
                            packagePaperScanPayDto.setUserCoin(user.getCoin());
                            packagePaperScanPayDto.setDownloadIcon(coin);
                            Double price = coinSetting.getPrice() * coin;
                            //用户id_类型_数据id_金额_随机数;
                            String wechatProductId = userId + "_3_" + id + "_" + (int)(price * 100) + "_";
                            int wechatLength = wechatProductId.length();
                            wechatProductId = wechatProductId + CodeNumberMaker.getInstance().orderCodeNum(32 - wechatLength);
                            DetailDto<String> wechatDto = wechatMerchantService.wechatUrlSignByProudctId(wechatProductId);
                            if (wechatDto.getStatus()) {
                                try {
                                    packagePaperScanPayDto.setWechatQrcode(QrCodeZxingUtil.getInstall().readQrCodeBase64(wechatDto.getDetail()));
                                    packagePaperScanPayDto.setWechatNum(wechatProductId);
                                } catch (WriterException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            //支付宝
                            AlipayPcOrderPayReqDto alipayReq = new AlipayPcOrderPayReqDto();
                            alipayReq.setMethod("alipay.trade.page.pay");
                            alipayReq.setNotify_url(localUrl + "/api/anon/alipay/order/callback");
                            alipayReq.setBody("教习网-支付宝PC支付");
                            alipayReq.setSubject("教习网");
                            alipayReq.setOut_trade_no(CodeNumberMaker.getInstance().orderCodeNum(32));
                            alipayReq.setTotal_amount(coinSetting.getPrice());//单位是元
                            //支付宝回传参数
                            alipayReq.setPassback_params("{\"userId\":" + userId + ",\"type\":3,\"dataId\":" + id + "}");
                            alipayReq.setTimeout_express("1d");//1天失效
                            alipayReq.setQr_pay_mode("4");
                            alipayReq.setQrcode_width(125l);
                            DetailDto<String> alipayDto = alipayService.alipayPcPayOrder(alipayReq);
                            packagePaperScanPayDto.setAlipayNum(alipayReq.getOut_trade_no());
                            if (alipayDto.getStatus()) {
                                //TODO cici注意此处返回的是html页面文本，前端需要进行插入处理
                                packagePaperScanPayDto.setAlipayQrcode(alipayDto.getDetail());
                            }
                            dto.setDetail(packagePaperScanPayDto);
                        }
                    }
                }
            }
        }
        return dto;
    }

    @Transactional
    public void downloadPaperByPackageId(Long userId, Long id, HttpServletResponse response) {
        StatusDto dto = new StatusDto(false);
        if (userId == null || userId == 0L) {
            dto.setMsg("用户为空");
        } else if (id == null || id == 0L) {
            dto.setMsg("组卷id为空");
        } else {
            UserExtend user = userExtendDao.selectByPrimaryKey(userId);
            if (user != null) {
                PackagePaper packagePaper = dao.selectByPrimaryKey(id);
                if (packagePaper != null) {
                    CoinSetting coinSetting = new CoinSetting();
                    coinSetting.setStatus(EntitySetting.COMMON_SUCCESS);
                    List<CoinSetting> list = coinSettingDao.select(coinSetting);
                    if (Collections3.isEmpty(list)) {//没有设置,默认可以直接下载
                        dto.setStatus(true);
                    } else {
                        coinSetting = list.get(0);
                        Integer coin = coinSetting.getPaperCoin();
                        if (coin == null || coin == 0L) {
                            dto.setStatus(true);
                        } else if (user.getCoin() != null && user.getCoin() >= coin) {
                            dto.setStatus(true);
                            user.setCoin(user.getCoin() - coin);
                            userExtendDao.updateByPrimaryKey(user);
                            //更新用户备课币使用记录
                            UserIntegral userIntegral = new UserIntegral();
                            userIntegral.setTime(System.currentTimeMillis());
                            userIntegral.setUserId(userId);
                            userIntegral.setNum(coin);
                            userIntegral.setOperatorId(userId);
                            userIntegral.setOperatorType(EntitySetting.USER_INTEGRAL_OPERATOR_USER);
                            userIntegral.setSource(EntitySetting.USER_INTEGRAL_SOURCE_CONSUME);
                            userIntegral.setType(EntitySetting.USER_INTEGRAL_REDUCE);
                            userIntegral.setCreateId(userId);
                            userIntegral.setUpdateId(userId);
                            userIntegral.setCreateTime(System.currentTimeMillis());
                            userIntegral.setUpdateTime(System.currentTimeMillis());
                            userIntegral.setRecordType(EntitySetting.USER_INTEGRAL_RECORD_COIN);
                            userIntegralDao.insert(userIntegral);
                            //更新用户订单记录
                            UserOrder userOrder = new UserOrder();
                            userOrder.setTime(System.currentTimeMillis());
                            userOrder.setStatus(EntitySetting.COMMON_SUCCESS);
                            userOrder.setUserId(userId);
                            User mobileUser = userDao.selectByPrimaryKey(userId);
                            userOrder.setUserName(mobileUser.getName());
                            userOrder.setCoin(userIntegral.getNum());
                            userOrder.setDataId(id);
                            userOrder.setOrderNum(CodeNumberMaker.getInstance().orderCodeNum());
                            userOrder.setSource(EntitySetting.USER_TYPE_PC);
                            userOrder.setPayType(EntitySetting.USER_ORDER_PAY_TYPE_COIN);
                            userOrder.setType(EntitySetting.USER_ORDER_TYPE_PAPERS);
                            userOrderService.save(userOrder);

                        } else {
                            dto.setStatus(false);
                        }
                    }
                    if (dto.getStatus()) {
                        //获取所有的问题查询
                        ObjectMapper mapper = new ObjectMapper();
                        JavaType javaType = mapper.getTypeFactory().constructParametricType(
                                List.class, PackagePaperSaveTypeDto.class);
                        List<PackagePaperSaveTypeDto> types = null;
                        try {
                            types = mapper.readValue(packagePaper.getQuestion(), javaType);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (Collections3.isNotEmpty(types)) {
                            List<Long> questionIds = Lists.newArrayList();
                            Map<Long, Integer> questionScoreMap = Maps.newHashMap();
                            types.forEach(d -> {
                                d.getQuestion().forEach(d1 -> {
                                    questionIds.add(d1.getId());
                                    questionScoreMap.put(d1.getId(), d1.getScore());
                                });
                            });
                            //题目
                            List<Question> questions = questionDao.selectByExample(new Example.Builder(Question.class)
                                    .where(WeekendSqls.<Question>custom().andIn(Question::getId, questionIds)).build());
                            Map<Long, Question> questionMap = Maps.newHashMap();
                            questions.forEach(d -> {
                                questionMap.put(d.getId(), d);
                            });
                            //答案
                            List<QuestionAnswer> questionAnswers = questionAnswerDao.selectByExample(new Example.Builder(QuestionAnswer.class)
                                    .where(WeekendSqls.<QuestionAnswer>custom().andIn(QuestionAnswer::getQuestionId, questionIds)).build());
                            Map<Long, QuestionAnswer> questionAnswerMap = Maps.newHashMap();
                            questionAnswers.forEach(d -> {
                                questionAnswerMap.put(d.getQuestionId(), d);
                            });
                            //知识点
                            List<QuestionKnowledge> questionKnowledges = questionKnowledgeDao.selectByExample(new Example.Builder(QuestionKnowledge.class)
                                    .where(WeekendSqls.<QuestionKnowledge>custom().andIn(QuestionKnowledge::getQuestionId, questionIds)).build());
                            Map<Long, List<String>> questionKnowledgeMap = Maps.newHashMap();
                            questionKnowledges.forEach(d -> {
                                List<String> stringList;
                                if (questionKnowledgeMap.containsKey(d.getQuestionId())) {
                                    stringList = questionKnowledgeMap.get(d.getQuestionId());
                                } else {
                                    stringList = Lists.newArrayList();
                                }
                                stringList.add(d.getKnowledgeName());
                                questionKnowledgeMap.put(d.getQuestionId(), stringList);
                            });
                            //  private Integer paperSize;//1-A4纸张，2-A3双栏纸张,3-B5纸张，4-B4双栏
//                        private String paperType;//试卷类型，1答案，2考点，3解析
//                        private Integer paperFile;//试卷下载格式，1-word，2-ppt
//                          template模板,1密封线，2考试时间，3大题评分区，4分卷注释，5主标题，6考生填写，7注意事项，8分大题，9副标题，10总评分，11分卷，12大题注释
                            PaperDownloadDto paperDownloadDto = new PaperDownloadDto();
                            paperDownloadDto.setTitle(packagePaper.getPaperTitle());
                            List<Integer> template = JsonMapper.nonEmptyMapper().fromJson(packagePaper.getTemplate(), List.class);
                            paperDownloadDto.setTemplate(template);
                            if (1 == packagePaper.getPaperFile()) {//word
                                Configuration cfg = new Configuration();
                                cfg.setDefaultEncoding("UTF-8");
                                cfg.setClassForTemplateLoading(this.getClass(),
                                        "/template/paper");
                                Template t = null;
                                String fileName;
                                if (2 == packagePaper.getPaperSize()) {//A3双栏
                                    if (1 == packagePaper.getTypeStyle()) {
                                        fileName = "A3-teacher.xml";
                                    } else {
                                        fileName = "A3-student.xml";
                                    }
                                } else if (3 == packagePaper.getPaperSize()) {//B5
                                    if (1 == packagePaper.getTypeStyle()) {
                                        fileName = "B5-teacher.xml";
                                    } else {
                                        fileName = "B5-student.xml";
                                    }
                                } else if (4 == packagePaper.getPaperSize()) {//B4
                                    if (1 == packagePaper.getTypeStyle()) {
                                        fileName = "B4-teacher.xml";
                                    } else {
                                        fileName = "B4-student.xml";
                                    }
                                } else {//A4
                                    if (1 == packagePaper.getTypeStyle()) {
                                        fileName = "A4-teacher.xml";
                                    } else {
                                        fileName = "A4-student.xml";
                                    }
                                }
                                try {
                                    t = cfg.getTemplate(fileName, "UTF-8");
                                    t.setEncoding("UTF-8");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                List<Integer> paperTypeList = JsonMapper.nonEmptyMapper().fromJson(packagePaper.getPaperType(), List.class);
                                paperDownloadDto.setPaperType(paperTypeList);
                                List<PaperDownloadQuestionTypeDto> typeDtoList1 = Lists.newArrayList();//客观题-选择题/判断题
                                List<PaperDownloadQuestionTypeDto> typeDtoList2 = Lists.newArrayList();//主观题
                                List<String> bigQuestionSort = Lists.newArrayList();
                                for (int i = 0; i < types.size(); i++) {
                                    PackagePaperSaveTypeDto d = types.get(i);
                                    PaperDownloadQuestionTypeDto temp = new PaperDownloadQuestionTypeDto();
                                    int score = 0;
                                    List<PaperDownloadQuestionDto> questionDtos = Lists.newArrayList();
                                    for (int i1 = 0; i1 < d.getQuestion().size(); i1++) {
                                        PackagePaperSaveQuestionDto d1 = d.getQuestion().get(i1);
                                        if (questionMap.containsKey(d1.getId())) {
                                            PaperDownloadQuestionDto temp1 = new PaperDownloadQuestionDto();
                                            temp1.setSort(i1 + 1);
                                            Question q1 = questionMap.get(d1.getId());
                                            temp1.setTitle(htmlToXml(q1.getContent()));
                                            score += d1.getScore();
                                            if (questionAnswerMap.containsKey(d1.getId())) {
                                                QuestionAnswer qa1 = questionAnswerMap.get(d1.getId());
                                                //判断是否有需要输出答案&解析
                                                if (Collections3.isNotEmpty(paperTypeList)) {
                                                    if (paperTypeList.contains(1)) {
                                                        temp1.setAnswer(qa1.getAnswer());
                                                    } else if (paperTypeList.contains(3)) {
                                                        temp1.setExplanation(qa1.getExplanation());
                                                    }
                                                }
                                            }
                                            if (!Strings.isNullOrEmpty(q1.getOptions())) {
                                                List<Map<String, String>> optionList = JsonMapper.nonEmptyMapper().fromJson(q1.getOptions(), List.class);
                                                List<String> options = Lists.newArrayList();
                                                if (!Collections3.isEmpty(optionList)) {
                                                    optionList.forEach(optionMap -> {
                                                        if (optionMap != null && optionMap.size() != 0) {
                                                            optionMap.forEach((k, v) -> {
                                                                options.add(htmlToXml(k + "." + v));
                                                            });
                                                        }
                                                    });
                                                } else {
                                                    Map<String, String> optionMap = JsonMapper.nonEmptyMapper().fromJson(q1.getOptions(), Map.class);
                                                    if (optionMap != null && optionMap.size() != 0) {
                                                        optionMap.forEach((k, v) -> {
                                                            options.add(htmlToXml(k + "." + v));
                                                        });
                                                    }
                                                }
                                                temp1.setOptions(options);
                                            }
                                            temp1.setScore(d1.getScore());
                                            //判断是否有需要输出考点
                                            if (Collections3.isNotEmpty(paperTypeList)) {
                                                if (paperTypeList.contains(2)) {
                                                    if (questionKnowledgeMap.containsKey(d1.getId())) {
                                                        temp1.setKnowledge(String.join(",", questionKnowledgeMap.get(d1.getId())));
                                                    }
                                                }
                                            }
                                            questionDtos.add(temp1);
                                        }
                                    }
                                    temp.setQuestions(questionDtos);
                                    temp.setTitle(d.getName());
                                    temp.setSort(NumberUtil.formatInteger(i + 1));//转中文
                                    bigQuestionSort.add(temp.getSort());
                                    temp.setScore(score);
                                    temp.setQuestionNum(questionDtos.size());
                                    if (Collections3.isNotEmpty(template) && template.contains(11)) {
                                        if (d.getId() == 1L || d.getId() == 2L || d.getId() == 3L) {
                                            typeDtoList1.add(temp);
                                        } else {
                                            typeDtoList2.add(temp);
                                        }
                                    } else {
                                        typeDtoList1.add(temp);
                                    }
                                }
                                paperDownloadDto.setTypes1(typeDtoList1);
                                paperDownloadDto.setTypes2(typeDtoList2);
                                // 输出文档路径及名称
                                response.setContentType("application/msword");
                                response.setCharacterEncoding("UTF-8");
                                Writer out = null;
                                try {
                                    response.setHeader("Content-Disposition", "attachment;Filename=" + new String((paperDownloadDto.getTitle() + ".doc").getBytes(), "iso-8859-1"));
                                    out = new BufferedWriter(new OutputStreamWriter(
                                            response.getOutputStream(), "UTF-8"));
                                    t.process(paperDownloadDto, out);
                                    out.flush();
                                    out.close();
                                } catch (TemplateException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            } else {//ppt
                                try {
                                    XMLSlideShow ppt = new XMLSlideShow();
                                    for (int i = 0; i < types.size(); i++) {
                                        PackagePaperSaveTypeDto d = types.get(i);
                                        int y = 10;
                                        for (int i1 = 0; i1 < d.getQuestion().size(); i1++) {
                                            //创建第一块ppt
                                            XSLFSlide slide = ppt.createSlide();
                                            //1-标题
                                            XSLFTextShape textShapeOnly = slide.createTextBox();
                                            textShapeOnly.setAnchor(new Rectangle(10, y, 650, 200));
                                            y += 200;
                                            textShapeOnly.setPlaceholder(Placeholder.CONTENT);
                                            textShapeOnly.setText(NumberUtil.formatInteger(i + 1) + "、" + d.getName());
                                            PackagePaperSaveQuestionDto d1 = d.getQuestion().get(i1);
                                            if (questionMap.containsKey(d1.getId())) {
                                                Question q1 = questionMap.get(d1.getId());
                                                //2-小标题-文字
                                                XSLFTextParagraph paragraph = textShapeOnly.addNewTextParagraph();
                                                XSLFTextRun run = paragraph.addNewTextRun();
                                                String html = "<div>" + q1.getContent() + "</div>";
                                                Document doc = Jsoup.parseBodyFragment(html);
                                                Element body = doc.body();
                                                run.setText(i1 + "." + body.text() + (Strings.isNullOrEmpty(q1.getOptions()) ? "" : q1.getOptions()));
                                                run.setFontColor(Color.BLACK); //字体颜色
                                                run.setFontSize(16.0);//字体大小
                                                run.setFontFamily("宋体");
                                                Elements imgElements = body.select("img");
                                                if (Collections3.isNotEmpty(imgElements)) {
                                                    for (int i2 = 0; i2 < imgElements.size(); i2++) {
                                                        String src = imgElements.get(i2).attr("src");
                                                        if (!Strings.isNullOrEmpty(src)) {
                                                            //3-图片
                                                            XSLFGroupShape shape = slide.createGroup();
                                                            shape.setAnchor(new Rectangle(10, y, 650, 200));
                                                            y += 200;
                                                            URL url = new URL(src);
                                                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                                            conn.setRequestMethod("GET");
                                                            conn.setConnectTimeout(5 * 1000);
                                                            byte[] picData = IOUtils.toByteArray(conn.getInputStream());
                                                            shape.createPicture(ppt.addPicture(picData, PictureData.PictureType.PNG));
                                                        }
                                                    }
                                                }
                                                Elements tableElements = body.select("table");
                                                if (Collections3.isNotEmpty(tableElements)) {
                                                    for (int i2 = 0; i2 < tableElements.size(); i2++) {
                                                        Element tableElement = tableElements.get(i2);
                                                        Elements trElements = tableElement.select("tr");
                                                        if (Collections3.isNotEmpty(trElements)) {
                                                            //4-表格
                                                            XSLFTable table = slide.createTable();
                                                            /** 设置表格 x ,y ,width,height **/
                                                            Rectangle2D rectangle2D = new Rectangle2D.Double(10, y, 650, 200);
                                                            y += 200;
                                                            for (int i3 = 0; i3 < trElements.size(); i3++) {
                                                                /** 生成第一行 **/
                                                                XSLFTableRow firstRow = table.addRow();
                                                                Elements tdElements = tableElement.select("td");
                                                                if (Collections3.isEmpty(tdElements)) {
                                                                    tdElements = tableElement.select("th");
                                                                }
                                                                if (Collections3.isNotEmpty(tdElements)) {
                                                                    for (int i4 = 0; i4 < tdElements.size(); i4++) {
                                                                        /** 生成第一个单元格**/
                                                                        XSLFTableCell firstCell = firstRow.addCell();
                                                                        firstCell.setText(tdElements.get(i4).text());
                                                                    }
                                                                }
                                                            }
                                                            table.setAnchor(rectangle2D);
                                                        }
                                                    }
                                                }
                                                if (questionAnswerMap.containsKey(d1.getId())) {
                                                    QuestionAnswer qa1 = questionAnswerMap.get(d1.getId());
                                                    //创建第2块ppt
                                                    XSLFSlide slide2 = ppt.createSlide();
                                                    XSLFTextShape textShapeOnly2 = slide2.createTextBox();
                                                    textShapeOnly2.setAnchor(new Rectangle(10, 10, 650, 450));
                                                    textShapeOnly2.setPlaceholder(Placeholder.CONTENT);
                                                    textShapeOnly2.setText(NumberUtil.formatInteger(i + 1) + "、" + d.getName());
                                                    textShapeOnly2.setLeftInset(10.0);
//                                                    textShapeOnly2.setTA
                                                    //2-小标题-文字
                                                    XSLFTextParagraph paragraph2 = textShapeOnly2.addNewTextParagraph();
                                                    XSLFTextRun run2 = paragraph2.addNewTextRun();
                                                    StringBuffer sbf = new StringBuffer();
                                                    sbf.append("【答案】").append(qa1.getAnswer()).append("\n");
                                                    if (questionKnowledgeMap.containsKey(d1.getId())) {
                                                        sbf.append("【考点】").append(String.join(",", questionKnowledgeMap.get(d1.getId()))).append("\n");
                                                    }
                                                    sbf.append("【解析】").append(qa1.getExplanation());
                                                    run2.setText(sbf.toString());
                                                    run2.setFontColor(Color.BLACK); //字体颜色
                                                    run2.setFontSize(16.0);//字体大小
                                                    run2.setFontFamily("宋体");
                                                }
                                            }
                                        }
                                    }
                                    // 输出文档路径及名称
                                    response.setContentType("application/vnd.ms-powerpoint");
                                    response.setCharacterEncoding("UTF-8");
                                    response.setHeader("Content-Disposition", "attachment;Filename=" + new String((paperDownloadDto.getTitle() + ".pptx").getBytes(), "iso-8859-1"));
                                    ppt.write(response.getOutputStream());
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } finally {
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private String htmlToXml(String content) {
        String html = "<div>" + content + "</div>";
        Document doc = Jsoup.parseBodyFragment(html);
        Element body = doc.body();
        //获取子节点
        Elements elements = body.children();
        return elementsXml(elements);
    }

    private String elementsXml(Elements elements) {//采用word2003的xml格式；此处是内容项
        StringBuffer sbf = new StringBuffer();
        if (elements != null) {
            for (Element element : elements) {
                if ("p".equalsIgnoreCase(element.tagName()) || "div".equalsIgnoreCase(element.tagName()) || "span".equalsIgnoreCase(element.tagName())) {
                    int nodeSize = element.childNodeSize();
                    if (nodeSize != 0 && nodeSize > element.children().size()) {//有文本框,不带标签，直接录入
                        //取得字符串中的文本录入
//                        Node node = element.childNode(0);
                        if (!Strings.isNullOrEmpty(element.text()) && !Strings.isNullOrEmpty(element.text().trim())) {
                            sbf.append("<w:r>\n" +
                                    "                    <w:rPr>\n" +
                                    "                        <w:color w:val=\"000000\"/>\n" +
                                    "                    </w:rPr>\n" +
                                    "                    <w:t>").append(element.text()).append("</w:t>\n" +
                                    "                                    </w:r>\n");
                        }
                    }
                    if (Collections3.isNotEmpty(element.children())) {
                        sbf.append(elementsXml(element.children()));
                    } else if (!Strings.isNullOrEmpty(element.text()) && !Strings.isNullOrEmpty(element.text().trim())) {
                        if(!(nodeSize != 0 && nodeSize > element.children().size())){
                            sbf.append("<w:r>\n" +
                                    "                    <w:rPr>\n" +
                                    "                        <w:color w:val=\"000000\"/>\n" +
                                    "                    </w:rPr>\n" +
                                    "                    <w:t>").append(element.text()).append("</w:t>\n" +
                                    "                                    </w:r>\n");
                        }
                    }
                } else if ("table".equalsIgnoreCase(element.tagName())) {
                    sbf.append(" <w:tbl>");
                    sbf.append("<w:tblPr>\n" +
                            "                            <w:tblW w:w=\"0\" w:type=\"auto\"/>\n" +
                            "                            <w:tblBorders>\n" +
                            "                                <w:top w:val=\"single\" w:sz=\"4\" w:space=\"0\" w:color=\"auto\"/>\n" +
                            "                                <w:left w:val=\"single\" w:sz=\"4\" w:space=\"0\" w:color=\"auto\"/>\n" +
                            "                                <w:bottom w:val=\"single\" w:sz=\"4\" w:space=\"0\" w:color=\"auto\"/>\n" +
                            "                                <w:right w:val=\"single\" w:sz=\"4\" w:space=\"0\" w:color=\"auto\"/>\n" +
                            "                                <w:insideH w:val=\"single\" w:sz=\"4\" w:space=\"0\" w:color=\"auto\"/>\n" +
                            "                                <w:insideV w:val=\"single\" w:sz=\"4\" w:space=\"0\" w:color=\"auto\"/>\n" +
                            "                            </w:tblBorders>\n" +
                            "                            <w:tblLook w:val=\"0000\" w:firstRow=\"0\" w:lastRow=\"0\" w:firstColumn=\"0\" w:lastColumn=\"0\" w:noHBand=\"0\" w:noVBand=\"0\"/>\n" +
                            "                        </w:tblPr>");
                    //判断是否有子元素,获取第一个tr下td中的宽度设置 TODO cici
                    if (Collections3.isNotEmpty(element.children())) {
                        sbf.append(elementsXml(element.children()));
                    } else if (!Strings.isNullOrEmpty(element.text()) && !Strings.isNullOrEmpty(element.text().trim())) {
                        sbf.append(element.text());
                    }
                    sbf.append("</w:tbl>");
                } else if ("tr".equalsIgnoreCase(element.tagName())) {
                    sbf.append(" <w:tr w:rsidR=\"00DC7489\" w14:paraId=\"6AE70758\" w14:textId=\"77777777\" w:rsidTr=\"0021342C\">");
                    sbf.append(" <w:trPr>\n" +
                            "                                <w:trHeight w:val=\"500\"/>\n" +
                            "                            </w:trPr>");
                    if (element.children() != null) {
                        sbf.append(elementsXml(element.children()));
                    } else if (!Strings.isNullOrEmpty(element.text()) && !Strings.isNullOrEmpty(element.text().trim())) {
                        sbf.append(element.text());
                    }
                    sbf.append("</w:tr>");
                } else if ("td".equalsIgnoreCase(element.tagName())) {
                    sbf.append(" <w:tc>");
                    sbf.append("<w:tcPr>\n" +
                            "                                    <w:tcW w:w=\"1000\" w:type=\"dxa\"/>\n" +
                            "                                    <w:tcBorders>\n" +
                            "                                        <w:top w:val=\"single\" w:sz=\"0\" w:space=\"0\" w:color=\"auto\"/>\n" +
                            "                                        <w:left w:val=\"single\" w:sz=\"0\" w:space=\"0\" w:color=\"auto\"/>\n" +
                            "                                        <w:bottom w:val=\"single\" w:sz=\"0\" w:space=\"0\" w:color=\"auto\"/>\n" +
                            "                                        <w:right w:val=\"single\" w:sz=\"0\" w:space=\"0\" w:color=\"auto\"/>\n" +
                            "                                    </w:tcBorders>\n" +
                            "                                    <w:vAlign w:val=\"center\"/>\n" +
                            "                                </w:tcPr>");
                    if (Collections3.isNotEmpty(element.children())) {
                        sbf.append(elementsXml(element.children()));
                    } else if (!Strings.isNullOrEmpty(element.text()) && !Strings.isNullOrEmpty(element.text().trim())) {
                        sbf.append("<w:p w14:paraId=\"72849F23\" w14:textId=\"77777777\" w:rsidR=\"00DC7489\" w:rsidRDefault=\"007D418A\" w:rsidP=\"0021342C\">\n" +
                                "                                    <w:pPr>\n" +
                                "                                        <w:jc w:val=\"center\"/>\n" +
                                "                                    </w:pPr>\n" +
                                "                                    <w:r>\n" +
                                "                                        <w:rPr>\n" +
                                "                                            <w:rFonts w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:cs=\"宋体\"/>\n" +
                                "                                            <w:sz w:val=\"21\"/>\n" +
                                "                                        </w:rPr>\n" +
                                "                                        <w:t>").append(element.text()).append("</w:t>\n" +
                                "                                    </w:r>\n" +
                                "                                </w:p>");
                    }
                    sbf.append("</w:tr>");
                } else if ("img".equalsIgnoreCase(element.tagName())) {
                    String id = UuidMaker.getInstance().getUuid(true);
                    String src = element.attr("src");
                    if (!Strings.isNullOrEmpty(src) && src.startsWith("http")) {//网络请求获取图片数据,转base64存储
                        src = base64FromUrl(src);
                    }
                    if (!Strings.isNullOrEmpty(src)) {
                        //获取img的宽度
                        String width = element.attr("width");
                        String height = element.attr("height");
                        //将px转为pt
                        Double w = Strings.isNullOrEmpty(width)? null : Double.parseDouble(width.split("px")[0].trim()) * 0.75;
                        Double h = Strings.isNullOrEmpty(height)? null : Double.parseDouble(height.split("px")[0].trim()) * 0.75;
                        sbf.append(
                                "               <w:r wsp:rsidR=\"00EC7EA3\" wsp:rsidRPr=\"00D44C2A\">" +
                                        "                <w:rPr>\n" +
                                        "                   <w:noProof/>\n" +
                                        "                        <w:lang w:fareast=\"ZH-CN\"/>" +
                                        "                </w:rPr>\n" +
                                        "                <w:pict>\n" +
                                        "                  <v:shapetype id=\"" + id + "\" coordsize=\"21600,21600\" o:spt=\"75\" o:preferrelative=\"t\" path=\"m@4@5l@4@11@9@11@9@5xe\" filled=\"f\" stroked=\"f\">\n" +
                                        "                    <v:stroke joinstyle=\"miter\"/>\n" +
                                        "                    <v:formulas>\n" +
                                        "                      <v:f eqn=\"if lineDrawn pixelLineWidth 0\"/>\n" +
                                        "                      <v:f eqn=\"sum @0 1 0\"/>\n" +
                                        "                      <v:f eqn=\"sum 0 0 @1\"/>\n" +
                                        "                      <v:f eqn=\"prod @2 1 2\"/>\n" +
                                        "                      <v:f eqn=\"prod @3 21600 pixelWidth\"/>\n" +
                                        "                      <v:f eqn=\"prod @3 21600 pixelHeight\"/>\n" +
                                        "                      <v:f eqn=\"sum @0 0 1\"/>\n" +
                                        "                      <v:f eqn=\"prod @6 1 2\"/>\n" +
                                        "                      <v:f eqn=\"prod @7 21600 pixelWidth\"/>\n" +
                                        "                      <v:f eqn=\"sum @8 21600 0\"/>\n" +
                                        "                      <v:f eqn=\"prod @7 21600 pixelHeight\"/>\n" +
                                        "                      <v:f eqn=\"sum @10 21600 0\"/>\n" +
                                        "                    </v:formulas>\n" +
                                        "                    <v:path o:extrusionok=\"f\" gradientshapeok=\"t\" o:connecttype=\"rect\"/>\n" +
                                        "                    <o:lock v:ext=\"edit\" aspectratio=\"t\"/>\n" +
                                        "                  </v:shapetype>\n" +
                                        "                  <w:binData w:name=\"wordml://" + id + ".jpg\" xml:space=\"preserve\">" + src +
                                        "</w:binData>                 \n" +
                                        " <v:shape id=\"" + id + "1\" o:spid=\"_x0000_i1025\" type=\"#" + id + "\" style=\""+(w!=null?"width:" + w +"pt;":"")+(h!=null?"height:" + h +"pt;":"")+"visibility:visible;mso-wrap-style:square\"> \n" +
                                        " <v:imagedata src=\"wordml://" + id + ".jpg\" o:title=\"image\"/> \n" +
                                        "   </v:shape> \n" +
                                        " </w:pict>   \n" +
                                        " </w:r>  ");
                    }
                } else if (!Strings.isNullOrEmpty(element.text()) && !Strings.isNullOrEmpty(element.text().trim())) {
                    sbf.append(" <w:r>\n" +
                            "                                        <w:rPr>\n" +
                            "                                            <w:rFonts w:ascii=\"宋体\" w:hAnsi=\"宋体\" w:cs=\"宋体\"/>\n" +
                            "                                            <w:sz w:val=\"21\"/>\n" +
                            "                                        </w:rPr>\n" +
                            "                                        <w:t>").append(element.text()).append("</w:t>\n</w:r>");
                }
            }
        }
        return sbf.toString();
    }

    private String base64FromUrl(String imgURL) {
        byte[] data = null;
        try {
            // 创建URL
            URL url = new URL(imgURL);
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            data = outStream.toByteArray();
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        // 返回Base64编码过的字节数组字符串
        return Encodes.encodeBase64(data);
    }

    public ListDto<PackagePaper> findByUserId(Long userId) {
        ListDto<PackagePaper> dto = new ListDto<>(false);
        if (userId == null || userId == 0L) {
            dto.setMsg("用户为空");
        } else {
            dto.setList(dao.selectByExample(new Example.Builder(PackagePaper.class)
                    .where(WeekendSqls.<PackagePaper>custom()
                            .andEqualTo(PackagePaper::getUserId, userId)).build()));
            dto.setStatus(true);
        }
        return dto;
    }
    public static void main(String[] args){
//        PackagePaperSaveDto reqDto = new PackagePaperSaveDto();
//        reqDto.setId(0L);
//        reqDto.setUserId(18L);
//        List<PackagePaperSaveTypeDto> list = Lists.newArrayList();
//        PackagePaperSaveTypeDto a1 = new PackagePaperSaveTypeDto();
//        a1.setId(1L);
//        a1.setName("ddd");
//        List<PackagePaperSaveQuestionDto> list1 = Lists.newArrayList();
//        PackagePaperSaveQuestionDto a2 = new PackagePaperSaveQuestionDto();
//        a2.setId(2L);
//        a2.setScore(0);
//        list1.add(a2);
//        a1.setQuestion(list1);
//        list.add(a1);
//        reqDto.setType(list);
////        System.out.println(JsonMapper.nonEmptyMapper().toJson("{\"id\":0,\"userId\":18,\"stage\":1,\"subjectId\":2,\"subjectName\":\"语文\",\"grade\":1,\"versionId\":141764,\"term\":0,\"paperTitle\":\"2018年08月19日\",\"type\":[{\"id\":4,\"name\":\"填空题\",\"question\":[{\"id\":5791791,\"score\":0}]},{\"id\":5,\"name\":\"未识别\",\"question\":[{\"id\":5791795,\"score\":0}]}],\"template\":[1,5,9],\"paperSize\":1,\"paperType\":[1,2,3],\"paperFile\":1,\"examStructure\":1,\"typeStyle\":1,\"remark\":{\"secondTitle\":\"\",\"firstSection\":\"\",\"secondSection\":\"\",\"attention\":\"\",\"minute\":90,\"score\":100}}"));
//        System.out.println(JsonMapper.nonEmptyMapper().toJson(reqDto.getType()));
//        list = System.out.println(JsonMapper.nonEmptyMapper().fromJson("[{\"id\":1,\"name\":\"ddd\",\"question\":[{\"id\":2}]}]",List.class));
        String s = new PackagePaperService().htmlToXml("<p>照样子，写一写。<br></p><p>ā &nbsp; á &nbsp; ǎ &nbsp; à&nbsp;&nbsp;&nbsp; ō&nbsp;&nbsp; ó&nbsp;&nbsp; ǒ&nbsp;&nbsp;&nbsp; ò&nbsp;&nbsp; ē&nbsp;&nbsp; é&nbsp;&nbsp; ě&nbsp;&nbsp; è&nbsp;&nbsp; <br></p><div><img data-cke-saved-src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAARkAAABGCAIAAAB33XfWAAAA8ElEQVR4nO3aUQkDUAwEwSetziutNbFwEGYUBML+3fsBhbc+AI7QEjS0BA0tQUNL0NASNLQEDS1BQ0vQ0BI0tAQNLUFDS9B4QGMdMxyhJWhoCRpagoaWoKElaGgJGlqChpagoSVoaAka70tt/VM23ofaemLJyDpmOEJL0NASNLQEDS1BQ0vQ0BI0tAQNLUFDS9DQEjS0BA0tQWM9rYUz1jHDEVqChpagoSVoaAkaWoKGlqChJWhoCRpagoaWoKElaGgJGlqCxnqmDmesY4YjtAQNLUFDS9DQEjS0BA0tQUNL0NASNLQEDS1BQ0vQ0BI0/ogeiVNdCsZIAAAAAElFTkSuQmCC\" src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAARkAAABGCAIAAAB33XfWAAAA8ElEQVR4nO3aUQkDUAwEwSetziutNbFwEGYUBML+3fsBhbc+AI7QEjS0BA0tQUNL0NASNLQEDS1BQ0vQ0BI0tAQNLUFDS9B4QGMdMxyhJWhoCRpagoaWoKElaGgJGlqChpagoSVoaAka70tt/VM23ofaemLJyDpmOEJL0NASNLQEDS1BQ0vQ0BI0tAQNLUFDS9DQEjS0BA0tQWM9rYUz1jHDEVqChpagoSVoaAkaWoKGlqChJWhoCRpagoaWoKElaGgJGlqCxnqmDmesY4YjtAQNLUFDS9DQEjS0BA0tQUNL0NASNLQEDS1BQ0vQ0BI0/ogeiVNdCsZIAAAAAElFTkSuQmCC\" alt=\"\" width=\"229\" height=\"57\"><br></div>");
        System.out.println(s);
    }
}