package com.ccr.mvp.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.ccr.mvp.model.ReceiverUpdates;

public interface ReceiverUpdatesRepository extends JpaRepository<ReceiverUpdates, Long> {

//	 @Query("SELECT r.updates FROM ReceiverUpdates r WHERE r.receiverUserId = :userId")
//	    List<Updates> findNotificationsByReceiverUserId(@Param("userId") Long userId);

}
