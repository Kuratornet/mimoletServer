package com.mimolet.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "ordertable")
public class Order {

	@Id
    @Column(name = "ID")
    @GeneratedValue
    private Integer id;

    @Column(name = "LINK")
    private String link;

    @Column(name = "STATUS")
    private String status;
    
    @Column(name = "OWNERID")
    private Integer ownerId;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}
    	
}
