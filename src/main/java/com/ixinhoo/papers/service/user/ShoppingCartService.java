package com.ixinhoo.papers.service.user;

import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.PageDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.code.util.collection.Collections3;
import com.chunecai.crumbs.code.util.mapper.BeanMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ixinhoo.papers.dao.resources.DocumentDao;
import com.ixinhoo.papers.dao.user.ShoppingCartDao;
import com.ixinhoo.papers.dao.user.UserDao;
import com.ixinhoo.papers.dto.user.ShoppingCartDocumentDto;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.resources.Document;
import com.ixinhoo.papers.entity.user.ShoppingCart;
import com.ixinhoo.papers.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShoppingCartService extends BaseService<ShoppingCart> {
    @Autowired
    private ShoppingCartDao dao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private DocumentDao documentDao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    @Transactional
    public DetailDto<Integer> createByUserAndDocument(Long userId, Long documentId) {
        DetailDto<Integer> dto = new DetailDto<>(false);
        if (userId == null || userId == 0L) {
            dto.setMsg("用户未传输");
        } else if (documentId == null || documentId == 0L) {
            dto.setMsg("文档未传输");
        } else {
            User user = userDao.selectByPrimaryKey(userId);
            if (user == null) {
                dto.setMsg("用户不存在");
            } else {
                ShoppingCart shoppingCart = new ShoppingCart();
                shoppingCart.setDataId(documentId);
                shoppingCart.setDataType(EntitySetting.DATA_TYPE_DOCUMENT);
                shoppingCart.setUserId(userId);
                shoppingCart = dao.selectOne(shoppingCart);
                dto.setStatus(true);
                if (shoppingCart != null) {
                    dto.setMsg("数据已存在,跳过");
                } else {
                    shoppingCart = new ShoppingCart();
                    shoppingCart.setDataId(documentId);
                    shoppingCart.setDataType(EntitySetting.DATA_TYPE_DOCUMENT);
                    shoppingCart.setUserId(userId);
                    shoppingCart.setTime(System.currentTimeMillis());
                    super.create(shoppingCart);
                    dto.setDetail(listDocumentByShoppingCart(userId).getList().size());
                }
            }
        }
        return dto;
    }

    public ListDto<ShoppingCartDocumentDto> listDocumentByShoppingCart(Long userId) {
        ListDto<ShoppingCartDocumentDto> dto = new ListDto<>(true);
        if (userId == null || userId == 0L) {
            dto.setMsg("用户为空");
        } else {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUserId(userId);
            List<ShoppingCart> shoppingCarts = dao.select(shoppingCart);
            List<Long> documentIds = shoppingCarts.stream().map(d -> d.getDataId()).collect(Collectors.toList());
            dto.setStatus(true);
            if (Collections3.isNotEmpty(documentIds)) {
                Map<Long,Long> map = Maps.newHashMap();
                shoppingCarts.forEach(d->{
                    map.put(d.getDataId(),d.getId());
                });
                List<Document> documents = documentDao.selectByExample(new Example.Builder(Document.class)
                        .where(WeekendSqls.<Document>custom().andEqualTo(Document::getStatus, EntitySetting.COMMON_SUCCESS)
                                .andIn(Document::getId, documentIds))
                        .build());
                List<ShoppingCartDocumentDto> list = Lists.newArrayList();
                documents.forEach(d->{
                    ShoppingCartDocumentDto temp = BeanMapper.map(d,ShoppingCartDocumentDto.class);
                    if(map.containsKey(d.getId())){
                        temp.setCartId(map.get(d.getId()));
                    }
                    list.add(temp);
                });
                dto.setList(list);
            }
        }
        return dto;
    }
}