# AuthenticateUserUseCase - Implementación (Refactorizado)

## 📋 Resumen

Se ha implementado el caso de uso de autenticación de usuarios siguiendo los principios de **Arquitectura Hexagonal** y **Clean Architecture**.

**IMPORTANTE**: Auth se encuentra en la **capa de aplicación** (`application.auth`), NO en el dominio, porque:
- No contiene reglas de negocio complejas
- Solo orquesta validación de credenciales y generación de tokens
- Es infraestructura transversal, no una entidad de negocio

## 🏗️ Estructura Creada

### 1. Capa de Aplicación

#### `AuthenticateUserUseCase` (Puerto de Entrada)
**Ubicación**: `com.apis.fintrack.application.auth.port.input`

**Responsabilidad**: Define el contrato para autenticar usuarios.

**Componentes**:
- `AuthenticationCommand`: Record inmutable que encapsula credenciales (username, password)
- `AuthenticationResult`: Record inmutable que contiene el token de acceso y metadatos
- `authenticate(AuthenticationCommand)`: Método principal del caso de uso

**Validaciones en el Command**:
- Username no puede ser null ni vacío
- Password no puede ser null ni vacío

**Validaciones en el Result**:
- Access token no puede ser null ni vacío
- Token type no puede ser null ni vacío
- Expires in debe ser positivo

#### `AuthenticationFailedException` (Excepción de Aplicación)
**Ubicación**: `com.apis.fintrack.application.auth.exception`

**Responsabilidad**: Excepción lanzada cuando falla la autenticación.

**Factory Methods**:
- `invalidCredentials()`: Para credenciales inválidas
- `userNotFound(String username)`: Para usuario no encontrado


#### `AuthenticateUserUseCaseImpl` (Implementación del Caso de Uso)
**Ubicación**: `com.apis.fintrack.application.auth`

**Responsabilidad**: Orquesta el proceso de autenticación coordinando los puertos de infraestructura.

**Dependencias**:
- `AuthenticationPort`: Puerto para autenticar credenciales
- `TokenPort`: Puerto para generar tokens JWT

**Flujo de Ejecución**:
1. Validar el comando de autenticación
2. Autenticar al usuario via `AuthenticationPort`
3. Generar token JWT via `TokenPort`
4. Construir y retornar `AuthenticationResult`

**Constantes**:
- `TOKEN_EXPIRATION_MS`: 3600000L (1 hora)
- `TOKEN_TYPE`: "Bearer"

**Métodos Privados**:
- `validateCommand()`: Valida que el comando no sea null
- `performAuthentication()`: Ejecuta la autenticación y lanza excepción si falla
- `generateAccessToken()`: Genera el JWT
- `buildAuthenticationResult()`: Construye el resultado final

---

## 🔄 Flujo Completo de Autenticación

```
┌─────────────────┐
│   Controller    │  (Infrastructure - REST)
│   (Adapter)     │
└────────┬────────┘
         │
         │ AuthenticationCommand
         ▼
┌─────────────────────────────┐
│ AuthenticateUserUseCaseImpl │  (Application Layer)
│   (Use Case Implementation) │
└────────┬────────────────────┘
         │
         ├──► AuthenticationPort.authenticate()  (Application Port)
         │           │
         │           ▼
         │    ┌──────────────────────┐
         │    │ SpringUserDetails    │  (Infrastructure)
         │    │     Adapter          │
         │    └──────────────────────┘
         │
         └──► TokenPort.generateToken()  (Application Port)
                     │
                     ▼
              ┌──────────────────┐
              │  JwtTokenAdapter │  (Infrastructure)
              └──────────────────┘
                     │
                     ▼
              AuthenticationResult
```

---

## 🎯 Principios Aplicados

### ✅ Arquitectura Hexagonal
- **Puerto de Entrada**: `AuthenticateUserUseCase` define el contrato
- **Implementación**: `AuthenticateUserUseCaseImpl` orquesta sin lógica de infraestructura
- **Puertos de Salida**: `AuthenticationPort` y `TokenPort` abstraen la infraestructura
- **Adaptadores**: Implementaciones en `infrastructure.security.adapter`

### ✅ Dependency Inversion Principle (DIP)
- La aplicación depende de abstracciones (ports), no de implementaciones concretas
- La infraestructura implementa los puertos definidos en la aplicación

### ✅ Single Responsibility Principle (SRP)
- Cada clase tiene una única responsabilidad bien definida
- Métodos privados pequeños y enfocados

### ✅ Open/Closed Principle (OCP)
- Nuevos proveedores de autenticación pueden agregarse sin modificar el caso de uso
- Solo se implementa un nuevo adapter

---

## 🧪 Testing

### Unit Tests Recomendados

**Para `AuthenticateUserUseCaseImpl`**:
1. ✅ Happy path: credenciales válidas → retorna token
2. ✅ Credenciales inválidas → lanza `AuthenticationFailedException`
3. ✅ Usuario no encontrado → lanza `AuthenticationFailedException`
4. ✅ Command null → lanza `IllegalArgumentException`
5. ✅ Username vacío → validación del record lanza excepción
6. ✅ Password vacío → validación del record lanza excepción
7. ✅ Token generado correctamente con tipo "Bearer"
8. ✅ Expiration time es 3600 segundos

**Mocking**:
```java
@Mock
private AuthenticationPort authenticationPort;

@Mock
private TokenPort tokenPort;

@InjectMocks
private AuthenticateUserUseCaseImpl useCase;
```

---

## 📦 Archivos Creados

1. ✅ `application/auth/port/input/AuthenticateUserUseCase.java`
2. ✅ `application/auth/port/input/package-info.java`
3. ✅ `application/auth/exception/AuthenticationFailedException.java`
4. ✅ `application/auth/AuthenticateUserUseCaseImpl.java`
5. ✅ Actualizado: `application/auth/package-info.java`

**Nota**: Originalmente se creó en `domain/auth`, pero se refactorizó a `application/auth` 
porque la autenticación es orquestación de infraestructura, no lógica de negocio del dominio.

---

## 🚀 Siguiente Paso

### Crear el Controller REST (Infrastructure)

Para completar el flujo, se necesita crear un controller en la capa de infraestructura:

**Ubicación**: `infrastructure.adapter.input.rest.auth.AuthController`

**Responsabilidades**:
- Recibir peticiones HTTP POST `/api/auth/login`
- Mapear DTO de request → `AuthenticationCommand`
- Invocar `AuthenticateUserUseCase`
- Mapear `AuthenticationResult` → DTO de response
- Retornar HTTP 200 con token o HTTP 401 si falla

**Ejemplo de DTO**:
```java
record LoginRequest(String username, String password) {}
record LoginResponse(String accessToken, String tokenType, long expiresIn) {}
```

---

## ✅ Verificación

**No hay errores de compilación** ✓

Solo warnings menores que no afectan la funcionalidad:
- Métodos no usados aún (se usarán cuando se cree el controller)
- Líneas en blanco en JavaDoc (estilo, no afecta)

---

## 📝 Notas Importantes

1. **Separación de Responsabilidades**:
   - Aplicación: Define contratos de orquestación, excepciones de aplicación y casos de uso
   - Infraestructura: Implementa puertos y expone APIs

2. **¿Por qué Auth NO está en Dominio?**:
   - No tiene reglas de negocio complejas (ej: políticas de bloqueo, auditoría, roles complejos)
   - Solo valida credenciales y genera tokens (orquestación)
   - Es infraestructura transversal, no una entidad de negocio como User, Transaction, Budget
   - **Regla**: Si solo orquesta sin reglas de negocio → Application. Si tiene invariantes y reglas → Domain

3. **No hay Spring en Contratos**:
   - Los puertos (interfaces) son framework-agnostic
   - Solo las implementaciones usan `@Service` para registro en Spring

4. **Validaciones en Múltiples Capas**:
   - Records de aplicación: validaciones básicas (null/empty)
   - Aplicación: validaciones de orquestación
   - Infraestructura: validaciones de transporte (si aplica)

5. **Inmutabilidad**:
   - Se usan Java Records para garantizar inmutabilidad
   - Todas las validaciones en constructor compacto

---

**Implementado por**: GitHub Copilot  
**Fecha**: 2026-01-20  
**Arquitectura**: Hexagonal (Ports & Adapters) + Clean Architecture

