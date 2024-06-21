package com.dhflour.dhflourdemo1.jpa.domain.push;

import com.dhflour.dhflourdemo1.jpa.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PushNotification")
public class PushNotificationEntity extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private String recipient;

    @Override
    public void delete() {

    }

    @Override
    public void lazy() {

    }
}