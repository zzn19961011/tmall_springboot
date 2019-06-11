package com.how2java.tmall.service;

import com.how2java.tmall.dao.ManagerDAO;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Manager;
import com.how2java.tmall.util.Page4Navigator;
import com.how2java.tmall.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames="managers")
public class ManagerSerive {
    @Autowired
    ManagerDAO managerDAO;
    @Cacheable(key="'managers-page-'+#p0+ '-' + #p1")
    public Page4Navigator<Manager> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size,sort);
        Page pageFromJPA =managerDAO.findAll(pageable);
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }
    @Cacheable(key="'managers-one-name-'+ #p0")
    public Manager getByName(String name){
        return managerDAO.findByName(name);
    }
    @CacheEvict(allEntries=true)
    public void add(Manager bean) {
        managerDAO.save(bean);
    }
    public boolean isExist(String name) {
       ManagerSerive managerSerive = SpringContextUtil.getBean(ManagerSerive.class);
        Manager manager = managerSerive.getByName(name);
        return null!=manager;
    }
    @Cacheable(key="'managers-one-name-'+ #p0 +'-password-'+ #p1")
    public Manager get(String name, String password){
        return managerDAO.getByNameAndPassword(name,password);
    }
}
