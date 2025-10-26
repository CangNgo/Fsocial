package com.fsocial.postservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "attachments")
@Builder
public class Attachments extends AbstractEntity<String> {
    @Field("public_id")
    String publicId;
    @Field("resource_type")
    String resourceType;
    @Field("file_type")
    String fileType;
    @Field("size")
    String size;
    @Field("url")
    String url;
    @Field("owner_id")
    String ownerId;
}
