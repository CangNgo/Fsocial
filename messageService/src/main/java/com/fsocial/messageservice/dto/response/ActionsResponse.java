package com.fsocial.messageservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fsocial.messageservice.dto.ExtraPropertiesClass;
import com.fsocial.messageservice.enums.TypesAction;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActionsResponse extends ExtraPropertiesClass {
    TypesAction type;
    String conversationId;
    String senderId;
}
