package com.project.model.vo;

import lombok.Data;

@Data
public class ClassVO {
	private int class_no;
	private String class_title;
	private String class_desc;
	private String class_loc;
	private String class_category;
	private int class_price;
	private String class_img1;
	private String class_img2;
	private String class_img3;
	private String class_img4;
	private String class_img5;	
	
	
	public ClassVO() {
	}


	public ClassVO(int class_no, String class_title, String class_desc, String class_loc, String class_category,
			int class_price, String class_img1, String class_img2, String class_img3, String class_img4,
			String class_img5) {
		super();
		this.class_no = class_no;
		this.class_title = class_title;
		this.class_desc = class_desc;
		this.class_loc = class_loc;
		this.class_category = class_category;
		this.class_price = class_price;
		this.class_img1 = class_img1;
		this.class_img2 = class_img2;
		this.class_img3 = class_img3;
		this.class_img4 = class_img4;
		this.class_img5 = class_img5;
	}

	
}
