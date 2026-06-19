package com.soso.domain.groupbuy.services;

import com.soso.domain.groupbuy.dao.GroupBuyDAO;
import com.soso.domain.groupbuy.dto.GroupBuyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupBuyService {

    @Autowired
    private GroupBuyDAO groupBuyDAO;

    public List<GroupBuyDTO> getGroupBuyListByUser(int userSeq, Integer storeSeq) {
        return groupBuyDAO.getGroupBuyListByUser(userSeq, storeSeq);
    }
}
