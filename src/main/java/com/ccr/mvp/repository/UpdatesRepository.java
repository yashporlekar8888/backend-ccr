package com.ccr.mvp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ccr.mvp.model.Updates;

public interface UpdatesRepository extends JpaRepository<Updates, Long>{

	@Query("SELECT n FROM Updates n JOIN n.receivers r WHERE r.receiverUserId = :userId")
	List<Updates> findNotificationsByReceiverUserId(@Param("userId") Long userId);


}
