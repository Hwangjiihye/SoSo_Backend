package com.soso.domain.groupbuy.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soso.domain.groupbuy.dto.GroupBuyDTO;

@Repository
public class GroupBuyDAO {

    @Autowired
    private SqlSession sql;

    public List<GroupBuyDTO> getGroupBuyListByUser(int userSeq, Integer storeSeq) {
        Map<String, Object> params = new HashMap<>();
        params.put("userSeq", userSeq);
        params.put("storeSeq", storeSeq);
        return sql.selectList("GroupBuyMapper.getGroupBuyListByUser", params);
    }
}
