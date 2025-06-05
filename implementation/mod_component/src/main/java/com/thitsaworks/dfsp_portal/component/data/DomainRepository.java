package com.thitsaworks.dfsp_portal.component.data;

public interface DomainRepository <ID, DATA> {

    void save(DATA domain);

    DATA get(ID id);

    void delete(ID id);
}
