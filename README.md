
#  📊 FinTrack AI - Backend

> Personal finance management API built with Spring Boot 3, implementing 
> hexagonal architecture and comprehensive testing

## 🎯 Overview

REST API for personal finance tracking with JWT authentication, budget 
management, and transaction analytics. Built with clean architecture 
principles for testability and maintainability.


**Key Features:**
- 🔐 JWT-based authentication with refresh tokens
- 💰 Multi-account transaction tracking
- 📊 Budget creation and monitoring
- 📈 Spending analytics by category
- 🧪 87% test coverage with unit + integration tests

## 🏗️ Architecture
```
src/
├── domain/          # Business logic (ports)
├── application/     # Use cases
└── infrastructure/  # Adapters (DB, REST, Security, AI)
```

**Design Patterns:** Repository, Strategy, Factory, Builder
**Principles:** SOLID, DDD, Hexagonal Architecture


## 📐 Architecture Decisions

**Why Hexagonal Architecture?**
Separation of business logic from infrastructure allows:
- Testing without database/external dependencies
- Easy swapping of implementations (MySQL → MongoDB)
- Clear boundaries between layers

**Why JWT over sessions?**
Stateless authentication for scalability and microservices compatibility.

## 📄 License

MIT License 

## 👤 Author

**Daniel Matías**
- GitHub: [@DDNatividade](https://github.com/DDNatividade)
- LinkedIn: [Daniel Matías](https://linkedin.com/in/danieldnatividade)
- Email: danielfeliciano1597@gmail.com

---

⭐ If you find this project helpful, consider giving it a star!
```
