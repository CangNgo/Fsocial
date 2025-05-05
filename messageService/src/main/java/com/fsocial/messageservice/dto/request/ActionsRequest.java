package com.fsocial.messageservice.dto.request;

import com.fsocial.messageservice.dto.ExtraPropertiesClass;
import com.fsocial.messageservice.enums.TypesAction;
import lombok.*;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActionsRequest extends ExtraPropertiesClass {
    TypesAction type;
    String conversationId;
    String senderId;
}
