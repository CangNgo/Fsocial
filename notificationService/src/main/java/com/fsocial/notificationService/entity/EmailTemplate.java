package com.fsocial.notificationService.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "email_template")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailTemplate extends AbstractEntity<String> {
    // id đã được kế thừa từ AbstractEntity
    String name;
    String content;
    boolean isActive;
    boolean isDefault;
}
