package com.transcoderG.redis;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class TranscoderGRedisCommand implements Serializable {
    
	private static final long serialVersionUID = 6699363273820335737L;
	
	private int priority;
	
	private String correlation_ID; // 키 값
	
	@NotNull @Size(min=1)
	private String srcfile;
	
	private String destfile1; // base_Adaptive_30fps_320x180_0300K;
	
	private String destfile2; // base_Adaptive_30fps_640x360_0600K;
	
	private String destfile3; // base_Adaptive_30fps_640x360_0900K;
	
	private String destfile4; // base_Adaptive_30fps_960x540_1500K;
	
	private String destfile5; // base_Adaptive_30fps_1280x720_3000K;
	
//	private String destfile6; // base_Adaptive_30fps_1920x1080_5000K;
	
	private double progress = 0;
}
