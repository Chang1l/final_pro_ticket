package com.ticket.biz.pay.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ticket.biz.pay.PayVO;


@Repository
public class PayDAO {

	@Autowired
    private	SqlSessionTemplate mybatis;

	//CRUD 메소드 구현
			public void insertPay(PayVO vo) {
				mybatis.insert("PayDAO.insertPay",vo);
			}
			public void updatePay(PayVO vo) {
				mybatis.update("PayDAO.updatePay",vo);
			}

			public void deletePay(PayVO vo) {
				mybatis.delete("PayDAO.deletePay",vo);
			}

			public List<PayVO> getPayList(PayVO vo) {
				return mybatis.selectList("PayDAO.getPayList",vo);
			}
			public int totalPayListCnt(PayVO vo) {
					return mybatis.selectOne("PayDAO.totalPayListCnt",vo);
			}
			public PayVO getPay(PayVO vo) {
				return (PayVO) mybatis.selectOne("PayDAO.getPay",vo);
			}
}
