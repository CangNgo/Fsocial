# ⚡ Fsocial

<div align="center">

[![GitHub stars](https://img.shields.io/github/stars/CangNgo/Fsocial?style=for-the-badge)](https://github.com/CangNgo/Fsocial/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/CangNgo/Fsocial?style=for-the-badge)](https://github.com/CangNgo/Fsocial/network)
[![GitHub issues](https://img.shields.io/github/issues/CangNgo/Fsocial?style=for-the-badge)](https://github.com/CangNgo/Fsocial/issues)
[![GitHub license](https://img.shields.io/github/license/CangNgo/Fsocial?style=for-the-badge)](LICENSE)

**A microservice-based social media platform.**

</div>

## 📖 Overview

Fsocial is a social media platform built using a microservices architecture.  It's designed for scalability and maintainability, with individual services handling specific functionalities like user accounts, messaging, and posts.  The project uses Docker for containerization and Docker Compose for orchestrating the services.  The technology stack primarily leverages Java.

## ✨ Features

- User Account Management (registration, login, profile management)
- Post Creation and Sharing
- Messaging System (direct messaging between users)
- Notifications (alerts for new messages, follows, etc.)
- Relationship Management (following/unfollowing users)
- Timeline (displaying posts from followed users)


## 🛠️ Tech Stack

**Backend:**

- Java
- Microservices Architecture

**Databases:**  (To be determined based on further code inspection of individual services)

**DevOps:**

- Docker
- Docker Compose


## 🚀 Quick Start

This setup requires Docker and Docker Compose to be installed on your system.

### Prerequisites

- Docker
- Docker Compose

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/CangNgo/Fsocial.git
   cd Fsocial
   ```

2. **Build and run the services:**
   ```bash
   docker-compose up -d --build
   ```
   This will build and start all services defined in `docker-compose.yml`.  You may need to adjust the `docker-config` file to specify appropriate database configurations, Kafka broker connection information, and other environment variables according to your setup.

3. **Access the services:** (Addresses to be determined upon service identification and port mapping definition)  Each microservice will be accessible via its respective container address and port (defined within `docker-compose.yml`).

### Shutting down the services:

```bash
docker-compose down
```


## 📁 Project Structure

```
Fsocial/
├── accountService/    
├── apigateway/       
├── docker-compose.yml
├── docker-config     
├── kafka.yml         
├── logs/             
├── messageService/   
├── notificationService/
├── postService/      
├── processorService/ 
├── profileService/   
├── relationshipService/
└── timelineService/
```

## ⚙️ Configuration

Configuration is managed primarily through environment variables within each service's Docker container and the `docker-config` file. The specific configuration options for each service needs further investigation of their respective source code.

## 🤝 Contributing

Contributions are welcome! Please open an issue or submit a pull request.  A more detailed contribution guideline would need to be created.


## 📄 License

TODO: Add License information


## 🙏 Acknowledgments

TODO: Add acknowledgments if any


## 📞 Support & Contact

TODO: Add contact information

---

<div align="center">

**⭐ Star this repo if you find it helpful!**

</div>
