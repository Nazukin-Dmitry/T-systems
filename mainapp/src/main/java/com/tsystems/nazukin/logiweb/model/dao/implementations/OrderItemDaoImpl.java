package com.tsystems.nazukin.logiweb.model.dao.implementations;

import com.tsystems.nazukin.logiweb.model.dao.interfaces.OrderItemDao;
import com.tsystems.nazukin.logiweb.model.entity.OrderItemEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by 1 on 02.04.2016.
 */
@Repository
public class OrderItemDaoImpl extends GenericDAOImpl<OrderItemEntity, Integer> implements OrderItemDao {
}
