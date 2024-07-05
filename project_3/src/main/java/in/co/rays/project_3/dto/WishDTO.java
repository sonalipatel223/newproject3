package in.co.rays.project_3.dto;

import java.util.Date;



public class WishDTO extends BaseDTO {

	private String userName;
	private String product;
	private String remark;
	private Date date;
	
	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String getKey() {
		
		return product+"";
	}

	@Override
	public String getValue() {
		
		return product+"";
	}

}
