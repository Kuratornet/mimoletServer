package com.mimolet.server.dao;

import com.mimolet.server.domain.Authority;

public interface AuthorityDAO {
	public Authority findAuthorityByUsername(String username);

	void addAuthority(Authority authority);
}
