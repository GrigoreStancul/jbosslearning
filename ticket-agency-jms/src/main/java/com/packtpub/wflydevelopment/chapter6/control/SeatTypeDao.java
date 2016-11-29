package com.packtpub.wflydevelopment.chapter6.control;

import com.packtpub.wflydevelopment.chapter6.entity.SeatType;

import javax.ejb.Stateless;

@Stateless
public class SeatTypeDao extends AbstractDao<SeatType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SeatTypeDao() {
		super(SeatType.class);
	}
}
