package com.ccr.mvp.model;

import javax.management.Notification;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class ReceiverUpdates {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ReceiverUpdatesId;

	private Long receiverUserId;

	@ManyToOne
	@JoinColumn(name = "updateId")
	private Updates updates;
}
