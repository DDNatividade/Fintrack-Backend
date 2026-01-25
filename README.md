
# 📊 FinTrack AI - Backend

> Personal finance management API built with Spring Boot 3, implementing 
> hexagonal architecture and comprehensive testing

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)]()
[![Test Coverage](https://img.shields.io/badge/coverage-87%25-green)]()
[![Java](https://img.shields.io/badge/Java-21-orange)]()
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green)]()

[🌐 Live Demo](#) | [📖 API Docs](#TODO-LINK) | [🎨 Frontend Repo](#TODO-LINK)

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
└── infrastructure/  # Adapters (DB, REST, Security, AI, Spring Configuration)

```

**Design Patterns:** Repository, Strategy, Factory, Builder
**Principles:** SOLID, DDD, Hexagonal Architecture

[Include diagram here - see below]

## 🛠️ Tech Stack

- **Backend:** Java 21, Spring Boot 3.2, Spring Security 6
- **Database:** MySQL 8.0 (JPA/Hibernate)
- **Testing:** JUnit 5, Mockito, TestContainers
- **Security:** JWT (jjwt), BCrypt
- **Build:** Maven, Docker
- **CI/CD:** GitHub Actions

## 🚀 Quick Start
```bash
# Clone repository
git clone https://github.com/DDNatividade/fintrack-backend.git

# Run with Docker Compose
docker-compose up

# Or run locally
./mvnw spring-boot:run

# API will be available at http://localhost:8080
```

[Full API documentation](#TODO-LINK)

## 🧪 Testing
```bash
# Run all tests
./mvnw test

# Run with coverage
./mvnw test jacoco:report

# Current coverage: 37%
- Domain layer: 86%
- Application layer: 23%
- Infrastructure layer: 0%
```

## 📐 Architecture Decisions

**Why Hexagonal Architecture?**
Separation of business logic from infrastructure allows:
- Testing without database/external dependencies
- Easy swapping of implementations (MySQL → MongoDB)
- Clear boundaries between layers

**Why JWT over sessions?**
Stateless authentication for scalability and microservices compatibility.

## 🔐 Environment Variables
```env
DB_URL=jdbc:mysql://localhost:3306/fintrack
DB_USER=root
DB_PASSWORD=password
JWT_SECRET=your-secret-key
JWT_EXPIRATION=86400000
```

## 📝 Future Improvements

- [ ] Add Redis caching for frequent queries
- [ ] Implement recurring transactions
- [ ] Add support for multiple currencies
- [ ] Create scheduled budget notifications
- [ ] Implement event sourcing for audit trail

## 📄 License

MIT License - see [LICENSE](LICENSE) file

## 👤 Author

**Daniel Matías**
- GitHub: [@DDNatividade](https://github.com/DDNatividade)
- LinkedIn: [Daniel Matías](https://linkedin.com/in/danieldnatividade)
- Email: danielfeliciano1597@gmail.com

---

⭐ If you find this project helpful, consider giving it a star!
```

MIT License

Copyright (c) 2025 Daniel Matías

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software...

```
