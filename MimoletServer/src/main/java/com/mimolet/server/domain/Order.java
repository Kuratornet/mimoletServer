package com.mimolet.server.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ordertable")
public class Order {

	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Integer id;

	@Column(name = "LINK")
	private String link;
	
	@Column(name = "IMAGELINK")
	private String imagelink;	

	@Column(name = "STATUS")
	private Integer status;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "OWNERID")
	private Integer ownerId;

	@Column(name = "BINDING")
	private Integer binding;

	@Column(name = "PAPER")
	private Integer paper;

	@Column(name = "PRINT")
	private Integer print;

	@Column(name = "BLOCKSIZE")
	private Integer blocksize;

	@Column(name = "PAGES")
	private Integer pages;

	@Column(name = "CREATED")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createData;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImagelink() {
		return imagelink;
	}

	public void setImagelink(String imagelink) {
		this.imagelink = imagelink;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getBinding() {
		return binding;
	}

	public void setBinding(Integer binding) {
		this.binding = binding;
	}

	public Integer getPaper() {
		return paper;
	}

	public void setPaper(Integer paper) {
		this.paper = paper;
	}

	public Integer getPrint() {
		return print;
	}

	public void setPrint(Integer print) {
		this.print = print;
	}

	public Integer getBlocksize() {
		return blocksize;
	}

	public void setBlocksize(Integer blocksize) {
		this.blocksize = blocksize;
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	public void setCreateData(Date createData) {
		this.createData = createData;
	}

	public Date getCreateData() {
		return createData;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", link=" + link + ", status=" + status
				+ ", description=" + description + ", ownerId=" + ownerId
				+ ", binding=" + binding + ", paper=" + paper + ", print="
				+ print + ", blocksize=" + blocksize + ", pages=" + pages
				+ ", createData=" + createData + "]";
	}
}
