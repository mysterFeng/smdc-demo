package com.myster.demo.repository;

import com.myster.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问层
 * 
 * @author myster
 * @since 2024-01-01
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据OpenID查找用户
     */
    Optional<User> findByOpenid(String openid);

    /**
     * 根据手机号查找用户
     */
    Optional<User> findByPhone(String phone);

    /**
     * 检查OpenID是否存在
     */
    boolean existsByOpenid(String openid);

    /**
     * 检查手机号是否存在
     */
    boolean existsByPhone(String phone);

    /**
     * 根据状态查找用户
     */
    @Query("SELECT u FROM User u WHERE u.status = :status")
    List<User> findByStatus(@Param("status") Integer status);
} 