package com.packtpub.wflydevelopment.chapter6.control;

import com.packtpub.wflydevelopment.chapter6.entity.Seat;

import javax.ejb.Stateless;


@Stateless
public class SeatDao extends AbstractDao<Seat> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SeatDao() {
        super(Seat.class);
    }
}
