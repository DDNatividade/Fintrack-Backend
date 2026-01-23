# RefreshTokenUseCase - Implementación

## ✅ Implementación Completada

Se ha implementado el caso de uso `RefreshTokenUseCase` en el módulo de autenticación siguiendo los principios de **Arquitectura Hexagonal**.

---

## 📂 Archivos Creados

### 1. Puerto de Entrada (Input Port)

**Archivo**: `application/auth/port/input/RefreshTokenUseCase.java`

**Responsabilidad**: Define el contrato para refrescar tokens de acceso.

**Componentes**:
- `RefreshTokenCommand`: Record inmutable que encapsula el refresh token
- `RefreshTokenResult`: Record inmutable que contiene el nuevo access token
- `refresh(RefreshTokenCommand)`: Método principal del caso de uso

**Validaciones**:
- Refresh token no puede ser null ni vacío
- Access token generado no puede ser null ni vacío
- Token type no puede ser null ni vacío
- Expires in debe ser positivo

---

### 2. Implementación del Caso de Uso

**Archivo**: `application/auth/RefreshTokenUseCaseImpl.java`

**Responsabilidad**: Orquesta el proceso de refresh de tokens.

**Flujo de Ejecución**:
1. Validar el comando
2. Extraer username del refresh token via `TokenPort`
3. Cargar usuario autenticado via `AuthenticationPort`
4. Generar nuevo access token via `TokenPort`
5. Construir y retornar `RefreshTokenResult`

**Dependencias**:
- `TokenPort`: Para validar y generar tokens
- `AuthenticationPort`: Para cargar información del usuario

**Métodos Privados**:
- `validateCommand()`: Valida que el comando no sea null
- `extractUsernameFromToken()`: Extrae username del token y lanza excepción si es inválido
- `loadAuthenticatedUser()`: Carga el usuario autenticado
- `generateNewAccessToken()`: Genera el nuevo JWT
- `buildRefreshTokenResult()`: Construye el resultado final

---

## 🔄 Flujo Completo de Refresh Token

```
┌─────────────────┐
│   Controller    │  (Infrastructure - REST)
│   (Adapter)     │
└────────┬────────┘
         │
         │ RefreshTokenCommand
         ▼
┌──────────────────────────┐
│ RefreshTokenUseCaseImpl  │  (Application Layer)
│ (Use Case Implementation)│
└────────┬─────────────────┘
         │
         ├──► TokenPort.validateAndExtractSubject()  (Output Port)
         │           │
         │           ▼
         │    ┌──────────────────┐
         │    │  JwtTokenAdapter │  (Infrastructure)
         │    └──────────────────┘
         │
         ├──► AuthenticationPort.findByUsername()  (Output Port)
         │           │
         │           ▼
         │    ┌──────────────────────┐
         │    │ SpringUserDetails    │  (Infrastructure)
         │    │     Adapter          │
         │    └──────────────────────┘
         │
         └──► TokenPort.generateToken()  (Output Port)
                     │
                     ▼
              ┌──────────────────┐
              │  JwtTokenAdapter │  (Infrastructure)
              └──────────────────┘
                     │
                     ▼
              RefreshTokenResult
```

---

## 🎯 Características

### ✅ Seguridad
- Valida que el refresh token sea válido antes de generar nuevo access token
- Verifica que el usuario aún exista en el sistema
- Lanza excepciones apropiadas si el token es inválido o expirado

### ✅ Separación de Responsabilidades
- **Puerto de entrada**: Define el contrato (qué hace)
- **Implementación**: Orquesta el flujo (cómo lo hace)
- **Puertos de salida**: Abstraen la infraestructura

### ✅ Inmutabilidad
- Uso de Records para garantizar inmutabilidad
- Validaciones en constructor compacto

---

## 🧪 Testing Recomendado

### Unit Tests para `RefreshTokenUseCaseImpl`

1. ✅ **Happy path**: refresh token válido → retorna nuevo access token
2. ✅ **Token inválido**: refresh token inválido → lanza `AuthenticationFailedException`
3. ✅ **Token expirado**: refresh token expirado → lanza `AuthenticationFailedException`
4. ✅ **Usuario no encontrado**: usuario eliminado → lanza `AuthenticationFailedException`
5. ✅ **Command null**: command null → lanza `IllegalArgumentException`
6. ✅ **Refresh token vacío**: validación del record lanza excepción
7. ✅ **Token generado**: nuevo token con tipo "Bearer" y expiración correcta

**Ejemplo de Test**:
```java
@ExtendWith(MockitoExtension.class)
class RefreshTokenUseCaseImplTest {
    
    @Mock private TokenPort tokenPort;
    @Mock private AuthenticationPort authenticationPort;
    @InjectMocks private RefreshTokenUseCaseImpl useCase;
    
    @Test
    void shouldRefreshTokenSuccessfully() {
        // Given
        var command = new RefreshTokenCommand("valid-refresh-token");
        var user = new AuthenticatedUser(
            UUID.randomUUID(),
            "user@example.com",
            Set.of("ROLE_USER")
        );
        
        when(tokenPort.validateAndExtractSubject("valid-refresh-token"))
            .thenReturn(Optional.of("user@example.com"));
        when(authenticationPort.findByUsername("user@example.com"))
            .thenReturn(Optional.of(user));
        when(tokenPort.generateToken(user))
            .thenReturn("new-access-token");
        
        // When
        var result = useCase.refresh(command);
        
        // Then
        assertThat(result.accessToken()).isEqualTo("new-access-token");
        assertThat(result.tokenType()).isEqualTo("Bearer");
        assertThat(result.expiresIn()).isEqualTo(3600);
    }
    
    @Test
    void shouldThrowExceptionWhenRefreshTokenIsInvalid() {
        // Given
        var command = new RefreshTokenCommand("invalid-token");
        when(tokenPort.validateAndExtractSubject("invalid-token"))
            .thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> useCase.refresh(command))
            .isInstanceOf(AuthenticationFailedException.class)
            .hasMessage("Invalid or expired refresh token");
    }
}
```

---

## 🚀 Siguiente Paso: Controller REST

Para completar el flujo, crear un endpoint en el controller:

**Ubicación**: `infrastructure.adapter.input.rest.auth.AuthController`

**Endpoint Sugerido**:
```java
@PostMapping("/refresh")
public ResponseEntity<RefreshTokenResponse> refresh(
        @Valid @RequestBody RefreshTokenRequest request) {
    
    var command = new RefreshTokenCommand(request.refreshToken());
    var result = refreshTokenUseCase.refresh(command);
    
    return ResponseEntity.ok(new RefreshTokenResponse(
        result.accessToken(),
        result.tokenType(),
        result.expiresIn()
    ));
}
```

**DTOs**:
```java
record RefreshTokenRequest(
    @NotBlank String refreshToken
) {}

record RefreshTokenResponse(
    String accessToken,
    String tokenType,
    long expiresIn
) {}
```

---

## 📊 Estado Actual del Módulo Auth

| Caso de Uso | Estado | Ubicación |
|------------|--------|-----------|
| AuthenticateUserUseCase | ✅ Implementado | `application/auth/AuthenticateUserUseCaseImpl.java` |
| RefreshTokenUseCase | ✅ Implementado | `application/auth/RefreshTokenUseCaseImpl.java` |

---

## ✅ Verificación

**Errores de compilación**: 0 ✓  
**Warnings**: 2 (clase y constructor no usados - esperado hasta crear controller) ✓

---

## 📝 Notas

### ⚠️ Consideración Importante

**En una implementación completa de JWT con refresh tokens**, típicamente:

1. **Access Token**: Corta duración (15 min - 1 hora)
2. **Refresh Token**: Larga duración (7-30 días)
3. **Refresh tokens** se almacenan en base de datos para:
   - Revocar tokens comprometidos
   - Logout en todos los dispositivos
   - Auditoría de sesiones

**En este MVP simplificado**:
- Solo se valida que el token sea válido
- No hay persistencia de refresh tokens
- No hay revocación explícita

**Para producción, considerar**:
- Tabla `refresh_tokens` con: id, user_id, token_hash, expires_at, revoked_at
- Endpoint `/logout` que revoque el refresh token
- Rotación de refresh tokens (generar nuevo refresh token en cada refresh)

---

**Implementado por**: GitHub Copilot  
**Fecha**: 2026-01-20  
**Estado**: ✅ Completado

