package com.fsocial.postservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Owner {
    @Field("user_id")
    String userId;
    @Field("display_name")
    String displayName;
    @Field("avatar")
    String avatar;
}
