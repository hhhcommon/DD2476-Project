34
https://raw.githubusercontent.com/1127140426/tensquare/master/tensquare_recruiter/src/main/java/com/tensquare/recruiter/dao/EnterpriseDao.java
package com.tensquare.recruiter.dao;

import com.tensquare.recruiter.pojo.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author 李聪
 * @date 2020/2/16 21:19
 */
public interface EnterpriseDao extends JpaRepository<Enterprise,String>, JpaSpecificationExecutor<Enterprise> {
    public List<Enterprise> findByIshot(String ishot);  //where ishot = ?
}
