package com.ino.myblog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubscribeDto {
	private int id;
	private String username;
	private Integer subscribeState;
	private Integer equalUserState;
	private String profileimageurl;

	public SubscribeDto(Integer id, String username, BigInteger subscribeState, BigInteger equalUserState, String profileimageurl ) {
		this.id = id;
		this.username = username;
		this.subscribeState = subscribeState.intValue();
		this.equalUserState = equalUserState.intValue();
		this.profileimageurl=profileimageurl;

	}
}
