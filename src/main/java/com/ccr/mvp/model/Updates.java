package com.ccr.mvp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Updates {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long updateId;
	private Long senderUserId;
	@OneToMany(mappedBy = "updates")
	@JsonIgnore
    private List<ReceiverUpdates> receivers;

	private String notification;
}
