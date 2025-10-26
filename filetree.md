# File Tree: Fsocial_be

Generated on: 10/16/2025, 9:32:15 AM
Root path: `c:\Users\acer\OneDrive\Workspace\Fsocial\Fsocial_be`

```
├── .cursor/ 🚫 (auto-hidden)
├── .git/ 🚫 (auto-hidden)
├── .idea/ 🚫 (auto-hidden)
├── .vscode/ 🚫 (auto-hidden)
├── accountService/
│   ├── .mvn/
│   │   └── wrapper/
│   │       └── maven-wrapper.properties
│   ├── logs/
│   │   ├── accountservice-2025-07-20.0.log.gz
│   │   ├── accountservice-2025-07-23.0.log.gz
│   │   ├── accountservice-2025-08-09.0.log.gz
│   │   ├── accountservice-2025-08-14.0.log.gz
│   │   ├── accountservice-2025-08-15.0.log.gz
│   │   ├── accountservice-2025-08-16.0.log.gz
│   │   ├── accountservice-2025-08-16.0.log13384063304700.tmp 🚫 (auto-hidden)
│   │   ├── accountservice-error.log 🚫 (auto-hidden)
│   │   └── accountservice.log 🚫 (auto-hidden)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── fsocial/
│   │   │   │           └── accountservice/
│   │   │   │               ├── config/
│   │   │   │               │   ├── AppConfig.java
│   │   │   │               │   ├── ApplicationAuditAware.java
│   │   │   │               │   ├── AuditingConfig.java
│   │   │   │               │   ├── AuthenticationConfig.java
│   │   │   │               │   ├── CorsConfig.java
│   │   │   │               │   ├── CustomJwtDecode.java
│   │   │   │               │   ├── JacksonConfig.java
│   │   │   │               │   ├── JwtAuthenticationEntryPoint.java
│   │   │   │               │   ├── JwtConfig.java
│   │   │   │               │   ├── OpenAPIConfig.java
│   │   │   │               │   └── RedisConfig.java
│   │   │   │               ├── controller/
│   │   │   │               │   ├── AccountController.java
│   │   │   │               │   ├── AuthenticateController.java
│   │   │   │               │   ├── CheckConnection.java
│   │   │   │               │   ├── InternalController.java
│   │   │   │               │   ├── PermissionController.java
│   │   │   │               │   └── RoleController.java
│   │   │   │               ├── dto/
│   │   │   │               │   ├── request/
│   │   │   │               │   │   ├── account/
│   │   │   │               │   │   │   ├── AccountLoginRequest.java
│   │   │   │               │   │   │   ├── AccountRegisterRequest.java
│   │   │   │               │   │   │   ├── ChangePasswordRequest.java
│   │   │   │               │   │   │   ├── DuplicationRequest.java
│   │   │   │               │   │   │   ├── EmailRequest.java
│   │   │   │               │   │   │   ├── OtpRequest.java
│   │   │   │               │   │   │   └── ResetPasswordRequest.java
│   │   │   │               │   │   ├── auth/
│   │   │   │               │   │   │   ├── RefreshTokenRequest.java
│   │   │   │               │   │   │   └── TokenRequest.java
│   │   │   │               │   │   ├── role/
│   │   │   │               │   │   │   ├── PermissionRequest.java
│   │   │   │               │   │   │   └── RoleCreationRequest.java
│   │   │   │               │   │   └── ProfileRegisterRequest.java
│   │   │   │               │   ├── response/
│   │   │   │               │   │   ├── auth/
│   │   │   │               │   │   │   ├── AuthenticationResponse.java
│   │   │   │               │   │   │   ├── DuplicationResponse.java
│   │   │   │               │   │   │   └── IntrospectResponse.java
│   │   │   │               │   │   ├── role/
│   │   │   │               │   │   │   ├── PermissionResponse.java
│   │   │   │               │   │   │   └── RoleResponse.java
│   │   │   │               │   │   ├── AccountResponse.java
│   │   │   │               │   │   ├── AccountStatisticRegiserDTO.java
│   │   │   │               │   │   ├── AccountStatisticRegiserLongDateDTO.java
│   │   │   │               │   │   └── DuplicationCheckResult.java
│   │   │   │               │   └── ApiResponse.java
│   │   │   │               ├── entity/
│   │   │   │               │   ├── AbstractEntity.java
│   │   │   │               │   ├── Account.java
│   │   │   │               │   ├── Permission.java
│   │   │   │               │   ├── RefreshToken.java
│   │   │   │               │   ├── Role.java
│   │   │   │               │   └── Token.java
│   │   │   │               ├── enums/
│   │   │   │               │   ├── CodeEnum.java
│   │   │   │               │   ├── ErrorCode.java
│   │   │   │               │   ├── RedisKeyType.java
│   │   │   │               │   ├── ResponseStatus.java
│   │   │   │               │   └── ValidErrorCode.java
│   │   │   │               ├── exception/
│   │   │   │               │   ├── AppCheckedException.java
│   │   │   │               │   ├── AppException.java
│   │   │   │               │   └── GlobalExceptionHandler.java
│   │   │   │               ├── mapper/
│   │   │   │               │   ├── AccountMapper.java
│   │   │   │               │   ├── PermissionMapper.java
│   │   │   │               │   ├── ProfileMapper.java
│   │   │   │               │   └── RoleMapper.java
│   │   │   │               ├── repository/
│   │   │   │               │   ├── httpclient/
│   │   │   │               │   │   └── ProfileClient.java
│   │   │   │               │   ├── AccountRepository.java
│   │   │   │               │   ├── PermissionRepository.java
│   │   │   │               │   ├── RefreshTokenRepository.java
│   │   │   │               │   ├── RoleRepository.java
│   │   │   │               │   └── TokenRepository.java
│   │   │   │               ├── services/
│   │   │   │               │   ├── impl/
│   │   │   │               │   │   ├── AccountServiceImpl.java
│   │   │   │               │   │   ├── AuthenticationServiceImpl.java
│   │   │   │               │   │   ├── BanServiceImpl.java
│   │   │   │               │   │   ├── JwtServiceImpl.java
│   │   │   │               │   │   ├── OtpServiceImpl.java
│   │   │   │               │   │   ├── PermissionServiceImpl.java
│   │   │   │               │   │   ├── RefreshTokenServiceImpl.java
│   │   │   │               │   │   └── RoleServiceImpl.java
│   │   │   │               │   ├── AccountService.java
│   │   │   │               │   ├── AuthenticationService.java
│   │   │   │               │   ├── BanService.java
│   │   │   │               │   ├── JwtService.java
│   │   │   │               │   ├── OtpService.java
│   │   │   │               │   ├── PermissionService.java
│   │   │   │               │   ├── RefreshTokenService.java
│   │   │   │               │   └── RoleService.java
│   │   │   │               ├── util/
│   │   │   │               │   └── MailUtils.java
│   │   │   │               ├── validation/
│   │   │   │               │   ├── constrain/
│   │   │   │               │   │   ├── DobValid.java
│   │   │   │               │   │   ├── NameValid.java
│   │   │   │               │   │   ├── NotNullOrBlank.java
│   │   │   │               │   │   └── PasswordValid.java
│   │   │   │               │   ├── DobValidator.java
│   │   │   │               │   ├── NameValidator.java
│   │   │   │               │   ├── NotNullOrBlankValidator.java
│   │   │   │               │   └── PasswordValidator.java
│   │   │   │               └── AccountServiceApplication.java
│   │   │   └── resources/
│   │   │       ├── static/
│   │   │       ├── templates/
│   │   │       ├── application-dev.yml 🚫 (auto-hidden)
│   │   │       ├── application.properties
│   │   │       ├── application.yml
│   │   │       └── logback-spring.xml
│   │   └── test/
│   │       └── java/
│   │           └── com/
│   │               ├── cangngo/
│   │               └── fsocial/
│   │                   └── accountservice/
│   │                       └── AccountServiceApplicationTests.java
│   ├── target/ 🚫 (auto-hidden)
│   ├── .gitattributes
│   ├── .gitignore
│   ├── Dockerfile
│   ├── HELP.md 🚫 (auto-hidden)
│   ├── mvnw
│   ├── mvnw.cmd
│   └── pom.xml
├── apigateway/
│   ├── .mvn/
│   │   └── wrapper/
│   │       └── maven-wrapper.properties
│   ├── logs/
│   │   ├── apigateway-2025-07-20.0.log.gz
│   │   ├── apigateway-2025-08-07.0.log.gz
│   │   ├── apigateway-2025-08-08.0.log.gz
│   │   ├── apigateway-2025-08-09.0.log.gz
│   │   ├── apigateway-2025-08-14.0.log.gz
│   │   ├── apigateway-2025-08-15.0.log.gz
│   │   ├── apigateway-2025-08-16.0.log13384290291800.tmp 🚫 (auto-hidden)
│   │   └── apigateway-error.log 🚫 (auto-hidden)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── cangngo/
│   │   │   │           └── apigateway/
│   │   │   │               ├── config/
│   │   │   │               │   ├── CorsConfig.java
│   │   │   │               │   ├── GlobalConfig.java
│   │   │   │               │   ├── OpenAPIConfig.java
│   │   │   │               │   └── WebClientConfig.java
│   │   │   │               ├── dto/
│   │   │   │               │   ├── request/
│   │   │   │               │   │   └── TokenRequest.java
│   │   │   │               │   ├── response/
│   │   │   │               │   │   └── IntrospectResponse.java
│   │   │   │               │   ├── ApiResponse.java
│   │   │   │               │   └── Response.java
│   │   │   │               ├── enums/
│   │   │   │               │   ├── ErrorCode.java
│   │   │   │               │   └── StatusCode.java
│   │   │   │               ├── exception/
│   │   │   │               │   ├── AppCheckedException.java
│   │   │   │               │   ├── AppUnCheckedException.java
│   │   │   │               │   └── GlobalExceptionHandler.java
│   │   │   │               ├── repository/
│   │   │   │               │   └── AccountClient.java
│   │   │   │               ├── service/
│   │   │   │               │   ├── impl/
│   │   │   │               │   │   ├── AccountServiceImpl.java
│   │   │   │               │   │   └── BanServiceImpl.java
│   │   │   │               │   ├── AccountService.java
│   │   │   │               │   └── BanService.java
│   │   │   │               └── ApigatewayApplication.java
│   │   │   └── resources/
│   │   │       ├── static/
│   │   │       ├── templates/
│   │   │       ├── application-dev.yml
│   │   │       ├── application-pro.yml
│   │   │       ├── application.properties
│   │   │       ├── application.yml
│   │   │       └── logback-spring.xml
│   │   └── test/
│   │       └── java/
│   │           └── com/
│   │               └── cangngo/
│   │                   └── apigateway/
│   │                       └── ApigatewayApplicationTests.java
│   ├── target/ 🚫 (auto-hidden)
│   ├── .gitattributes
│   ├── .gitignore
│   ├── Dockerfile
│   ├── HELP.md 🚫 (auto-hidden)
│   ├── mvnw
│   ├── mvnw.cmd
│   └── pom.xml
├── logs/
│   ├── accountservice-2025-10-15.0.log.gz
│   ├── accountservice-error-2025-10-15.0.log.gz
│   ├── accountservice-error.log 🚫 (auto-hidden)
│   ├── accountservice.log 🚫 (auto-hidden)
│   ├── apigateway-2025-10-15.0.log.gz
│   ├── apigateway-error.log 🚫 (auto-hidden)
│   ├── apigateway.log 🚫 (auto-hidden)
│   ├── messageservice-2025-10-15.0.log.gz
│   ├── messageservice-error.log 🚫 (auto-hidden)
│   ├── messageservice.log 🚫 (auto-hidden)
│   ├── notificationservice-2025-10-15.0.log.gz
│   ├── notificationservice-2025-10-15.1.log.gz
│   ├── notificationservice-2025-10-16.0.log.gz
│   ├── notificationservice-2025-10-16.1.log.gz
│   ├── notificationservice-2025-10-16.2.log.gz
│   ├── notificationservice-2025-10-16.3.log.gz
│   ├── notificationservice-error.log 🚫 (auto-hidden)
│   ├── notificationservice.log 🚫 (auto-hidden)
│   ├── postservice-2025-10-15.0.log.gz
│   ├── postservice-error.log 🚫 (auto-hidden)
│   ├── postservice.log 🚫 (auto-hidden)
│   ├── processorservice-2025-10-15.0.log.gz
│   ├── processorservice-error.log 🚫 (auto-hidden)
│   ├── processorservice.log 🚫 (auto-hidden)
│   ├── profileservice-2025-10-15.0.log.gz
│   ├── profileservice-error.log 🚫 (auto-hidden)
│   ├── profileservice.log 🚫 (auto-hidden)
│   ├── relationship-2025-10-15.0.log.gz
│   ├── relationship-error.log 🚫 (auto-hidden)
│   └── relationship.log 🚫 (auto-hidden)
├── messageService/
│   ├── .mvn/
│   │   └── wrapper/
│   │       └── maven-wrapper.properties
│   ├── logs/
│   │   ├── mesageservice-2025-08-14.0.log.gz
│   │   ├── mesageservice.log 🚫 (auto-hidden)
│   │   ├── messageservice-error.log 🚫 (auto-hidden)
│   │   └── messageservice.log 🚫 (auto-hidden)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── fsocial/
│   │   │   │           └── messageservice/
│   │   │   │               ├── Entity/
│   │   │   │               │   ├── Conversation.java
│   │   │   │               │   └── Message.java
│   │   │   │               ├── config/
│   │   │   │               │   ├── AppConfig.java
│   │   │   │               │   ├── ApplicationAuditAware.java
│   │   │   │               │   ├── AuditingConfig.java
│   │   │   │               │   ├── CorsConfig.java
│   │   │   │               │   ├── CustomJwtDecode.java
│   │   │   │               │   ├── JwtAuthenticationEntryPoint.java
│   │   │   │               │   ├── OpenAPIConfig.java
│   │   │   │               │   ├── RedisConfig.java
│   │   │   │               │   ├── RedissonConfig.java
│   │   │   │               │   └── WebSocketConfig.java
│   │   │   │               ├── controller/
│   │   │   │               │   ├── ChatController.java
│   │   │   │               │   ├── CheckConnection.java
│   │   │   │               │   ├── ConversationController.java
│   │   │   │               │   └── MessageController.java
│   │   │   │               ├── dto/
│   │   │   │               │   ├── request/
│   │   │   │               │   │   ├── ActionsRequest.java
│   │   │   │               │   │   ├── ConversationRequest.java
│   │   │   │               │   │   ├── MarkReadRequest.java
│   │   │   │               │   │   ├── MarkReadResponse.java
│   │   │   │               │   │   └── MessageRequest.java
│   │   │   │               │   ├── response/
│   │   │   │               │   │   ├── ActionsResponse.java
│   │   │   │               │   │   ├── ConversationCreateResponse.java
│   │   │   │               │   │   ├── ConversationResponse.java
│   │   │   │               │   │   ├── LastMessage.java
│   │   │   │               │   │   ├── MessageListResponse.java
│   │   │   │               │   │   ├── MessageRecallResponse.java
│   │   │   │               │   │   ├── MessageResponse.java
│   │   │   │               │   │   └── ProfileResponse.java
│   │   │   │               │   ├── ApiResponse.java
│   │   │   │               │   └── ExtraPropertiesClass.java
│   │   │   │               ├── enums/
│   │   │   │               │   ├── ErrorCode.java
│   │   │   │               │   ├── MessageType.java
│   │   │   │               │   ├── ResponseStatus.java
│   │   │   │               │   └── TypesAction.java
│   │   │   │               ├── exception/
│   │   │   │               │   ├── AppCheckedException.java
│   │   │   │               │   ├── AppException.java
│   │   │   │               │   └── GlobalExceptionHandler.java
│   │   │   │               ├── mapper/
│   │   │   │               │   ├── ConversationMapper.java
│   │   │   │               │   └── MessageMapper.java
│   │   │   │               ├── repository/
│   │   │   │               │   ├── httpClient/
│   │   │   │               │   │   ├── AccountClient.java
│   │   │   │               │   │   └── ProfileClient.java
│   │   │   │               │   ├── impl/
│   │   │   │               │   │   └── MessageRepositoryCustomImpl.java
│   │   │   │               │   ├── ConversationRepository.java
│   │   │   │               │   ├── MessageRepository.java
│   │   │   │               │   └── MessageRepositoryCustom.java
│   │   │   │               ├── services/
│   │   │   │               │   ├── impl/
│   │   │   │               │   │   ├── CacheServiceImpl.java
│   │   │   │               │   │   ├── ChatServiceImpl.java
│   │   │   │               │   │   ├── ConversationServiceImpl.java
│   │   │   │               │   │   ├── DelayedMessage.java
│   │   │   │               │   │   └── MessageServiceImpl.java
│   │   │   │               │   ├── CacheService.java
│   │   │   │               │   ├── ChatService.java
│   │   │   │               │   ├── ConversationService.java
│   │   │   │               │   └── MessageService.java
│   │   │   │               ├── util/
│   │   │   │               │   └── RedissonLock.java
│   │   │   │               └── MessageServiceApplication.java
│   │   │   └── resources/
│   │   │       ├── static/
│   │   │       ├── templates/
│   │   │       ├── application-dev.yml
│   │   │       ├── application.properties
│   │   │       ├── application.yml
│   │   │       └── logback-spring.xml
│   │   └── test/
│   │       └── java/
│   │           └── com/
│   │               ├── cangngo/
│   │               └── fsocial/
│   │                   └── messageservice/
│   │                       └── MessageServiceApplicationTests.java
│   ├── target/ 🚫 (auto-hidden)
│   ├── .gitattributes
│   ├── .gitignore
│   ├── Dockerfile
│   ├── HELP.md 🚫 (auto-hidden)
│   ├── mvnw
│   ├── mvnw.cmd
│   └── pom.xml
├── notificationService/
│   ├── .mvn/
│   │   └── wrapper/
│   │       └── maven-wrapper.properties
│   ├── logs/
│   │   ├── notificationservice-2025-08-14.0.log.gz
│   │   ├── notificationservice-2025-08-15.0.log.gz
│   │   ├── notificationservice-2025-08-15.0.log13388024049700.tmp 🚫 (auto-hidden)
│   │   ├── notificationservice-error.log 🚫 (auto-hidden)
│   │   └── notificationservice.log 🚫 (auto-hidden)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── fsocial/
│   │   │   │           ├── event/
│   │   │   │           │   └── NotificationRequest.java
│   │   │   │           └── notificationService/
│   │   │   │               ├── config/
│   │   │   │               │   ├── AppConfig.java
│   │   │   │               │   ├── ApplicationAuditAware.java
│   │   │   │               │   ├── AuditingConfig.java
│   │   │   │               │   ├── AuthenticationConfig.java
│   │   │   │               │   ├── CustomJwtDecode.java
│   │   │   │               │   ├── JwtAuthenticationEntryPoint.java
│   │   │   │               │   ├── JwtConfig.java
│   │   │   │               │   ├── MongoStartupCheck.java
│   │   │   │               │   ├── OpenAPIConfig.java
│   │   │   │               │   ├── RedisConfig.java
│   │   │   │               │   └── WebSocketConfig.java
│   │   │   │               ├── controller/
│   │   │   │               │   ├── CheckConnection.java
│   │   │   │               │   └── NotificationController.java
│   │   │   │               ├── dto/
│   │   │   │               │   ├── request/
│   │   │   │               │   │   └── NoticeRequest.java
│   │   │   │               │   ├── response/
│   │   │   │               │   │   ├── AllNotificationResponse.java
│   │   │   │               │   │   ├── NotificationResponse.java
│   │   │   │               │   │   └── ProfileNameResponse.java
│   │   │   │               │   └── ApiResponse.java
│   │   │   │               ├── entity/
│   │   │   │               │   └── Notification.java
│   │   │   │               ├── enums/
│   │   │   │               │   ├── ErrorCode.java
│   │   │   │               │   ├── ResponseStatus.java
│   │   │   │               │   └── TopicKafka.java
│   │   │   │               ├── exception/
│   │   │   │               │   ├── AppCheckedException.java
│   │   │   │               │   ├── AppException.java
│   │   │   │               │   └── GlobalExceptionHandler.java
│   │   │   │               ├── mapper/
│   │   │   │               │   └── NotificationMapper.java
│   │   │   │               ├── repository/
│   │   │   │               │   ├── httpClient/
│   │   │   │               │   │   └── ProfileClient.java
│   │   │   │               │   ├── impl/
│   │   │   │               │   │   └── CustomNotificationRepositoryImpl.java
│   │   │   │               │   ├── CustomNotificationRepository.java
│   │   │   │               │   └── NotificationRepository.java
│   │   │   │               ├── service/
│   │   │   │               │   ├── impl/
│   │   │   │               │   │   └── NotificationServiceImpl.java
│   │   │   │               │   └── NotificationService.java
│   │   │   │               └── NotificationServiceApplication.java
│   │   │   └── resources/
│   │   │       ├── application-dev.yml
│   │   │       ├── application.properties
│   │   │       ├── application.yml
│   │   │       └── logback-spring.xml
│   │   └── test/
│   │       └── java/
│   │           └── com/
│   │               └── fsocial/
│   │                   └── notificationService/
│   │                       └── NotificationServiceApplicationTests.java
│   ├── target/ 🚫 (auto-hidden)
│   ├── .gitattributes
│   ├── .gitignore
│   ├── Dockerfile
│   ├── mvnw
│   ├── mvnw.cmd
│   └── pom.xml
├── postService/
│   ├── .mvn/
│   │   └── wrapper/
│   │       └── maven-wrapper.properties
│   ├── logs/
│   │   ├── postservice-2025-07-13.0.log.gz
│   │   ├── postservice-2025-08-14.0.log.gz
│   │   ├── postservice-2025-08-14.0.log13389548687700.tmp 🚫 (auto-hidden)
│   │   ├── postservice-error.log 🚫 (auto-hidden)
│   │   └── postservice.log 🚫 (auto-hidden)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── fsocial/
│   │   │   │           ├── event/
│   │   │   │           │   └── NotificationRequest.java
│   │   │   │           └── postservice/
│   │   │   │               ├── config/
│   │   │   │               │   ├── AppConfig.java
│   │   │   │               │   ├── ApplicationAuditAware.java
│   │   │   │               │   ├── AuditingConfig.java
│   │   │   │               │   ├── ConfigCloudinary.java
│   │   │   │               │   ├── CorsConfig.java
│   │   │   │               │   ├── CustomJwtDecode.java
│   │   │   │               │   ├── JwtAuthenticationEntryPoint.java
│   │   │   │               │   ├── OpenAPIConfig.java
│   │   │   │               │   └── RestTemplateConfig.java
│   │   │   │               ├── controller/
│   │   │   │               │   ├── CheckConnection.java
│   │   │   │               │   ├── CommentController.java
│   │   │   │               │   ├── ComplainController.java
│   │   │   │               │   ├── FileUploadController.java
│   │   │   │               │   ├── InternalApi.java
│   │   │   │               │   ├── PostController.java
│   │   │   │               │   ├── ReplyCommentController.java
│   │   │   │               │   └── TermOfServiceController.java
│   │   │   │               ├── dto/
│   │   │   │               │   ├── comment/
│   │   │   │               │   │   ├── CommentDTO.java
│   │   │   │               │   │   ├── CommentDTORequest.java
│   │   │   │               │   │   ├── CommentUpdateDTORequest.java
│   │   │   │               │   │   └── LikeCommentDTO.java
│   │   │   │               │   ├── complaint/
│   │   │   │               │   │   └── ComplaintDTO.java
│   │   │   │               │   ├── post/
│   │   │   │               │   │   ├── LikePostDTO.java
│   │   │   │               │   │   ├── PostDTO.java
│   │   │   │               │   │   ├── PostDTORequest.java
│   │   │   │               │   │   └── PostShareDTORequest.java
│   │   │   │               │   ├── profile/
│   │   │   │               │   │   └── ProfileDTO.java
│   │   │   │               │   ├── replyComment/
│   │   │   │               │   │   ├── LikeReplyCommentDTO.java
│   │   │   │               │   │   ├── ReplyCommentDTO.java
│   │   │   │               │   │   ├── ReplyCommentRequest.java
│   │   │   │               │   │   └── ReplyCommentUpdateDTORequest.java
│   │   │   │               │   ├── termOfService/
│   │   │   │               │   │   └── TermOfServiceDTO.java
│   │   │   │               │   ├── ApiResponse.java
│   │   │   │               │   ├── ContentDTO.java
│   │   │   │               │   ├── ContentDTORequest.java
│   │   │   │               │   ├── Response.java
│   │   │   │               │   └── TypeNotification.java
│   │   │   │               ├── entity/
│   │   │   │               │   ├── AbstractEntity.java
│   │   │   │               │   ├── Comment.java
│   │   │   │               │   ├── Complaint.java
│   │   │   │               │   ├── Content.java
│   │   │   │               │   ├── Post.java
│   │   │   │               │   ├── ReplyComment.java
│   │   │   │               │   └── TermOfServices.java
│   │   │   │               ├── enums/
│   │   │   │               │   ├── ResponseStatus.java
│   │   │   │               │   └── TopicKafka.java
│   │   │   │               ├── exception/
│   │   │   │               │   ├── AppCheckedException.java
│   │   │   │               │   ├── AppUnCheckedException.java
│   │   │   │               │   ├── GlobalExceptionHandler.java
│   │   │   │               │   └── StatusCode.java
│   │   │   │               ├── mapper/
│   │   │   │               │   ├── ComplantMapper.java
│   │   │   │               │   ├── ContentMapper.java
│   │   │   │               │   ├── PostMapper.java
│   │   │   │               │   ├── ReplyCommentMapper.java
│   │   │   │               │   └── TermOfServiceMapper.java
│   │   │   │               ├── repository/
│   │   │   │               │   ├── httpClient/
│   │   │   │               │   │   ├── Accountclient.java
│   │   │   │               │   │   └── ProfileClient.java
│   │   │   │               │   ├── CommentRepository.java
│   │   │   │               │   ├── ComplaintRepository.java
│   │   │   │               │   ├── PostRepository.java
│   │   │   │               │   ├── ReplyCommentRepository.java
│   │   │   │               │   └── TermRepository.java
│   │   │   │               ├── services/
│   │   │   │               │   ├── impl/
│   │   │   │               │   │   ├── CommentServiceImpl.java
│   │   │   │               │   │   ├── ComplainServiceImpl.java
│   │   │   │               │   │   ├── KafkaServiceImpl.java
│   │   │   │               │   │   ├── PostServiceImpl.java
│   │   │   │               │   │   ├── RedisServiceImpl.java
│   │   │   │               │   │   ├── ReplyCommentServiceImpl.java
│   │   │   │               │   │   ├── TermOfServicesServiceImpl.java
│   │   │   │               │   │   └── UploadMediaImpl.java
│   │   │   │               │   ├── CommentService.java
│   │   │   │               │   ├── ComplaintService.java
│   │   │   │               │   ├── KafkaService.java
│   │   │   │               │   ├── PostService.java
│   │   │   │               │   ├── RedisService.java
│   │   │   │               │   ├── ReplyCommentService.java
│   │   │   │               │   ├── TermOfServicesService.java
│   │   │   │               │   └── UploadMedia.java
│   │   │   │               └── PostServiceApplication.java
│   │   │   └── resources/
│   │   │       ├── static/
│   │   │       ├── templates/
│   │   │       ├── application-dev.yml
│   │   │       ├── application.properties
│   │   │       ├── application.yml
│   │   │       └── logback-spring.xml
│   │   └── test/
│   │       └── java/
│   │           └── com/
│   │               ├── cangngo/
│   │               └── fsocial/
│   │                   └── postservice/
│   │                       └── PostServiceApplicationTests.java
│   ├── target/ 🚫 (auto-hidden)
│   ├── .gitattributes
│   ├── .gitignore
│   ├── Dockerfile
│   ├── HELP.md 🚫 (auto-hidden)
│   ├── mvnw
│   ├── mvnw.cmd
│   └── pom.xml
├── profileService/
│   ├── .mvn/
│   │   └── wrapper/
│   │       └── maven-wrapper.properties
│   ├── logs/
│   │   ├── profileservice-2025-08-10.0.log.gz
│   │   ├── profileservice-2025-08-14.0.log.gz
│   │   ├── profileservice-2025-08-15.0.log.gz
│   │   ├── profileservice-2025-08-15.0.log13391503997300.tmp 🚫 (auto-hidden)
│   │   ├── profileservice-error.log 🚫 (auto-hidden)
│   │   └── profileservice.log 🚫 (auto-hidden)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── fsocial/
│   │   │   │           ├── event/
│   │   │   │           │   └── NotificationRequest.java
│   │   │   │           └── profileservice/
│   │   │   │               ├── config/
│   │   │   │               │   ├── AppConfig.java
│   │   │   │               │   ├── ApplicationAuditAware.java
│   │   │   │               │   ├── AuditingConfig.java
│   │   │   │               │   ├── CorsConfig.java
│   │   │   │               │   ├── CustomJwtDecode.java
│   │   │   │               │   ├── JwtAuthenticationEntryPoint.java
│   │   │   │               │   ├── OpenAPIConfig.java
│   │   │   │               │   ├── RedisConfig.java
│   │   │   │               │   └── RestTemplateConfig.java
│   │   │   │               ├── controller/
│   │   │   │               │   ├── APIProfileInternal.java
│   │   │   │               │   ├── AccountProfileController.java
│   │   │   │               │   ├── CheckConnection.java
│   │   │   │               │   ├── FollowController.java
│   │   │   │               │   └── ProfileController.java
│   │   │   │               ├── dto/
│   │   │   │               │   ├── request/
│   │   │   │               │   │   ├── FollowRequest.java
│   │   │   │               │   │   ├── ProfileRegisterRequest.java
│   │   │   │               │   │   └── ProfileUpdateRequest.java
│   │   │   │               │   ├── response/
│   │   │   │               │   │   ├── FindProfileDTO.java
│   │   │   │               │   │   ├── FindProfileResponse.java
│   │   │   │               │   │   ├── ProfileAdminResponse.java
│   │   │   │               │   │   ├── ProfileNameResponse.java
│   │   │   │               │   │   ├── ProfilePageOtherResponse.java
│   │   │   │               │   │   ├── ProfilePageResponse.java
│   │   │   │               │   │   ├── ProfileResponse.java
│   │   │   │               │   │   ├── ProfileUpdateResponse.java
│   │   │   │               │   │   ├── UpdatePrivacyResponse.java
│   │   │   │               │   │   └── UserResponse.java
│   │   │   │               │   └── ApiResponse.java
│   │   │   │               ├── entity/
│   │   │   │               │   └── AccountProfile.java
│   │   │   │               ├── enums/
│   │   │   │               │   ├── ErrorCode.java
│   │   │   │               │   ├── ProfileVisibility.kt
│   │   │   │               │   ├── ResponseStatus.java
│   │   │   │               │   ├── TopicKafka.java
│   │   │   │               │   └── ValidErrorCode.java
│   │   │   │               ├── exception/
│   │   │   │               │   ├── AppCheckedException.java
│   │   │   │               │   ├── AppException.java
│   │   │   │               │   ├── GlobalExceptionHandler.java
│   │   │   │               │   └── StatusCode.java
│   │   │   │               ├── mapper/
│   │   │   │               │   └── AccountProfileMapper.java
│   │   │   │               ├── repository/
│   │   │   │               │   ├── httpClient/
│   │   │   │               │   │   ├── AccountClient.java
│   │   │   │               │   │   └── PostClient.java
│   │   │   │               │   ├── AccountProfileRepository.java
│   │   │   │               │   └── ProfileRepository.java
│   │   │   │               ├── services/
│   │   │   │               │   ├── impl/
│   │   │   │               │   │   ├── AccountProfileServiceImpl.java
│   │   │   │               │   │   ├── FollowServiceImpl.java
│   │   │   │               │   │   └── ProfileServiceImpl.java
│   │   │   │               │   ├── AccountProfileService.java
│   │   │   │               │   ├── FollowService.java
│   │   │   │               │   └── ProfileService.java
│   │   │   │               ├── validation/
│   │   │   │               │   ├── constrain/
│   │   │   │               │   │   └── NotNullOrBlank.java
│   │   │   │               │   └── NotNullOrBlankValidator.java
│   │   │   │               └── ProfileServiceApplication.java
│   │   │   └── resources/
│   │   │       ├── static/
│   │   │       ├── templates/
│   │   │       ├── application-dev.yml
│   │   │       ├── application.properties
│   │   │       ├── application.yml
│   │   │       └── logback-spring.xml
│   │   └── test/
│   │       └── java/
│   │           └── com/
│   │               └── cangngo/
│   │                   └── profileservice/
│   │                       └── ProfileServiceApplicationTests.java
│   ├── target/ 🚫 (auto-hidden)
│   ├── .gitattributes
│   ├── .gitignore
│   ├── Dockerfile
│   ├── HELP.md 🚫 (auto-hidden)
│   ├── mvnw
│   ├── mvnw.cmd
│   └── pom.xml
├── relationshipService/
│   ├── .mvn/
│   │   └── wrapper/
│   │       └── maven-wrapper.properties
│   ├── logs/
│   │   ├── relationship-error.log 🚫 (auto-hidden)
│   │   └── relationship.log 🚫 (auto-hidden)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── fsocial/
│   │   │   │           └── relationshipService/
│   │   │   │               ├── config/
│   │   │   │               │   ├── AppConfig.java
│   │   │   │               │   ├── CorsConfig.java
│   │   │   │               │   ├── CustomJwtDecode.java
│   │   │   │               │   ├── JwtAuthenticationEntryPoint.java
│   │   │   │               │   ├── OpenAPIConfig.java
│   │   │   │               │   └── RedisConfig.java
│   │   │   │               ├── controller/
│   │   │   │               │   ├── CheckConnection.java
│   │   │   │               │   └── RelationshipController.java
│   │   │   │               ├── dto/
│   │   │   │               │   ├── request/
│   │   │   │               │   │   └── FollowRequest.java
│   │   │   │               │   └── ApiResponse.java
│   │   │   │               ├── entity/
│   │   │   │               │   └── Relationship.java
│   │   │   │               ├── enums/
│   │   │   │               │   ├── ErrorCode.java
│   │   │   │               │   ├── ResponseStatus.java
│   │   │   │               │   └── ValidErrorCode.java
│   │   │   │               ├── exception/
│   │   │   │               │   ├── AppCheckedException.java
│   │   │   │               │   ├── AppException.java
│   │   │   │               │   └── GlobalExceptionHandler.java
│   │   │   │               ├── repository/
│   │   │   │               │   └── RelationshipRepository.java
│   │   │   │               ├── service/
│   │   │   │               │   ├── impl/
│   │   │   │               │   │   └── RelationshipServiceImpl.java
│   │   │   │               │   └── RelationshipService.java
│   │   │   │               ├── validation/
│   │   │   │               │   ├── constrain/
│   │   │   │               │   │   └── NotNullOrBlank.java
│   │   │   │               │   └── NotNullOrBlankValidator.java
│   │   │   │               └── RelationshipServiceApplication.java
│   │   │   └── resources/
│   │   │       ├── static/
│   │   │       ├── templates/
│   │   │       ├── application-dev.yml
│   │   │       ├── application.properties
│   │   │       ├── application.yml
│   │   │       └── logback-spring.xml
│   │   └── test/
│   │       └── java/
│   │           └── com/
│   │               └── fsocial/
│   │                   └── relationshipService/
│   │                       └── RelationshipServiceApplicationTests.java
│   ├── target/ 🚫 (auto-hidden)
│   ├── .gitattributes
│   ├── .gitignore
│   ├── Dockerfile
│   ├── mvnw
│   ├── mvnw.cmd
│   └── pom.xml
├── timelineService/
│   ├── .mvn/
│   │   └── wrapper/
│   │       └── maven-wrapper.properties
│   ├── logs/
│   │   ├── postservice-2025-08-14.0.log.gz
│   │   └── postservice.log 🚫 (auto-hidden)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── fsocial/
│   │   │   │           └── timelineservice/
│   │   │   │               ├── config/
│   │   │   │               │   ├── AppConfig.java
│   │   │   │               │   ├── CorsConfig.java
│   │   │   │               │   ├── CustomJwtDecode.java
│   │   │   │               │   ├── JwtAuthenticationEntryPoint.java
│   │   │   │               │   └── OpenAPIConfig.java
│   │   │   │               ├── controller/
│   │   │   │               │   ├── CheckConnection.java
│   │   │   │               │   ├── CommentController.java
│   │   │   │               │   ├── ComplainController.java
│   │   │   │               │   ├── PostController.java
│   │   │   │               │   ├── ReplyCommentController.java
│   │   │   │               │   └── TermOfServiceController.java
│   │   │   │               ├── dto/
│   │   │   │               │   ├── comment/
│   │   │   │               │   │   └── CommentResponse.java
│   │   │   │               │   ├── complaint/
│   │   │   │               │   │   ├── ComplaintDTO.java
│   │   │   │               │   │   ├── ComplaintDTOResponse.java
│   │   │   │               │   │   ├── ComplaintStatisticsDTO.java
│   │   │   │               │   │   └── ComplaintStatisticsLongDayDTO.java
│   │   │   │               │   ├── post/
│   │   │   │               │   │   ├── CommentDTO.java
│   │   │   │               │   │   ├── ContentDTO.java
│   │   │   │               │   │   ├── ContentDTORequest.java
│   │   │   │               │   │   ├── PostByUserIdResponse.java
│   │   │   │               │   │   ├── PostDTO.java
│   │   │   │               │   │   ├── PostDTORequest.java
│   │   │   │               │   │   ├── PostResponse.java
│   │   │   │               │   │   ├── PostStatisticsDTO.java
│   │   │   │               │   │   └── PostStatisticsLongDateDTO.java
│   │   │   │               │   ├── profile/
│   │   │   │               │   │   ├── ProfileResponse.java
│   │   │   │               │   │   └── ProfileServiceResponse.java
│   │   │   │               │   ├── replyComment/
│   │   │   │               │   │   ├── ReplyCommentDTO.java
│   │   │   │               │   │   └── ReplyCommentResponse.java
│   │   │   │               │   ├── termOfService/
│   │   │   │               │   │   └── TermOfServiceDTO.java
│   │   │   │               │   ├── ApiResponse.java
│   │   │   │               │   └── Response.java
│   │   │   │               ├── entity/
│   │   │   │               │   ├── AbstractEntity.java
│   │   │   │               │   ├── Comment.java
│   │   │   │               │   ├── Complaint.java
│   │   │   │               │   ├── Content.java
│   │   │   │               │   ├── LikeComment.java
│   │   │   │               │   ├── Post.java
│   │   │   │               │   ├── ReplyComment.java
│   │   │   │               │   └── TermOfServices.java
│   │   │   │               ├── enums/
│   │   │   │               │   ├── ResponseStatus.java
│   │   │   │               │   └── StatusCode.java
│   │   │   │               ├── exception/
│   │   │   │               │   ├── AppCheckedException.java
│   │   │   │               │   ├── AppUnCheckedException.java
│   │   │   │               │   └── GlobalExceptionHandler.java
│   │   │   │               ├── mapper/
│   │   │   │               │   ├── ComplantMapper.java
│   │   │   │               │   ├── ContentMapper.java
│   │   │   │               │   ├── PostMapper.java
│   │   │   │               │   └── TermOfSerivceMapper.java
│   │   │   │               ├── repository/
│   │   │   │               │   ├── httpClient/
│   │   │   │               │   │   ├── AccountClient.java
│   │   │   │               │   │   └── ProfileClient.java
│   │   │   │               │   ├── CommentRepository.java
│   │   │   │               │   ├── ComplaintRepository.java
│   │   │   │               │   ├── PostRepository.java
│   │   │   │               │   ├── ReplyCommentRepository.java
│   │   │   │               │   └── TermOfServicesRepository.java
│   │   │   │               ├── services/
│   │   │   │               │   ├── impl/
│   │   │   │               │   │   ├── ChatService.java
│   │   │   │               │   │   ├── CommentServiceImpl.java
│   │   │   │               │   │   ├── ComplainServiceImpl.java
│   │   │   │               │   │   ├── PostServiceImpl.java
│   │   │   │               │   │   ├── RedisServiceImpl.java
│   │   │   │               │   │   ├── ReplyCommentImpl.java
│   │   │   │               │   │   └── TermOfServiceImpl.java
│   │   │   │               │   ├── CommentService.java
│   │   │   │               │   ├── ComplaintService.java
│   │   │   │               │   ├── PostService.java
│   │   │   │               │   ├── RedisService.java
│   │   │   │               │   ├── ReplyCommentService.java
│   │   │   │               │   └── TermOfServiceService.java
│   │   │   │               └── TimeLineServiceApplication.java
│   │   │   └── resources/
│   │   │       ├── static/
│   │   │       ├── templates/
│   │   │       ├── application-dev.yml
│   │   │       ├── application.properties
│   │   │       ├── application.yml
│   │   │       └── logback-spring.xml
│   │   └── test/
│   │       └── java/
│   │           └── com/
│   │               └── cangngo/
│   │                   └── timelineservice/
│   │                       └── TimelineServiceApplicationTests.java
│   ├── target/ 🚫 (auto-hidden)
│   ├── .gitattributes
│   ├── .gitignore
│   ├── Dockerfile
│   ├── HELP.md 🚫 (auto-hidden)
│   ├── mvnw
│   ├── mvnw.cmd
│   └── pom.xml
├── .env 🚫 (auto-hidden)
├── .gitignore
├── AUTHENTICATION_FIX_README.md
├── docker-compose-dev.yml
├── docker-compose.yml
├── dockerTag.sh
├── pushimage.sh
├── removeContainer.sh
├── removeImage.sh
└── stopContainer.sh
```

---
*Generated by FileTree Pro Extension*