# Refactorización: domain.auth → application.auth

## ✅ Refactorización Completada

Se ha movido todo el paquete de autenticación de `domain.auth` a `application.auth`.

---

## 🎯 Razón del Cambio

### ¿Por qué Auth NO pertenece al Dominio?

**Autenticación en este proyecto**:
- ❌ No tiene reglas de negocio complejas
- ❌ No tiene invariantes de dominio
- ❌ No es una entidad de negocio (como User, Transaction, Budget)
- ✅ Solo orquesta: validar credenciales + generar token
- ✅ Es infraestructura transversal

### Regla de Oro
```
┌─────────────────────────────────────────────┐
│ ¿Tiene reglas de negocio o invariantes?     │
│                                             │
│ SÍ → Domain                                 │
│ NO → Application (si solo orquesta)         │
└─────────────────────────────────────────────┘
```

**Ejemplos**:
- ✅ **Domain**: `User`, `Transaction`, `Budget` → tienen invariantes y reglas
- ✅ **Application**: `Auth` → solo valida credenciales y genera tokens
- ✅ **Application**: Casos de uso que orquestan dominio

---

## 📂 Estructura ANTES

```
domain/
└── auth/
    ├── port/
    │   └── input/
    │       ├── AuthenticateUserUseCase.java
    │       └── package-info.java
    └── exception/
        └── AuthenticationFailedException.java

application/
└── auth/
    ├── AuthenticateUserUseCaseImpl.java
    └── package-info.java
```

---

## 📂 Estructura DESPUÉS (Refactorizada)

```
application/
└── auth/
    ├── port/
    │   └── input/
    │       ├── AuthenticateUserUseCase.java
    │       └── package-info.java
    ├── exception/
    │   └── AuthenticationFailedException.java
    ├── AuthenticateUserUseCaseImpl.java
    └── package-info.java
```

**Eliminado**: `domain/auth/` (completo)

---

## 🔄 Cambios Realizados

### 1. Archivos Movidos

| Origen | Destino |
|--------|---------|
| `domain.auth.port.input.AuthenticateUserUseCase` | `application.auth.port.input.AuthenticateUserUseCase` |
| `domain.auth.exception.AuthenticationFailedException` | `application.auth.exception.AuthenticationFailedException` |

### 2. Imports Actualizados

**AuthenticateUserUseCaseImpl.java**:
```java
// ANTES
import com.apis.fintrack.domain.auth.port.input.AuthenticateUserUseCase;
import com.apis.fintrack.domain.auth.exception.AuthenticationFailedException;

// DESPUÉS
import com.apis.fintrack.application.auth.port.input.AuthenticateUserUseCase;
import com.apis.fintrack.application.auth.exception.AuthenticationFailedException;
```

### 3. Documentación Actualizada

- ✅ `AuthenticateUserUseCase-Implementation.md`: Actualizado con explicación del cambio
- ✅ JavaDocs: Actualizados en las clases afectadas

---

## ✅ Verificación

### Compilación
```bash
# Sin errores de compilación ✓
# Solo warnings de métodos no usados (se usarán con el controller)
```

### Búsqueda de Referencias
```bash
grep -r "domain.auth" src/
# Resultado: 0 referencias en código fuente ✓
# Solo 2 referencias en documentación (ya actualizadas) ✓
```

### Estructura de Directorios
```bash
ls domain/
# Resultado: analysis, budget, payment, property, role, shared, subscription, transaction, user
# NO hay "auth" ✓
```

---

## 🎓 Lección Aprendida

### ¿Cuándo usar Domain vs Application?

#### ✅ Domain (Capa de Dominio)
- Entidades con identidad y ciclo de vida
- Agregados con invariantes
- Reglas de negocio complejas
- Lógica que NO cambia con la tecnología
- **Ejemplo**: `User`, `Transaction`, `Budget`, `Subscription`

#### ✅ Application (Capa de Aplicación)
- Orquestación de casos de uso
- Coordinación de servicios
- Transacciones
- Lógica que puede cambiar con requisitos de la app
- **Ejemplo**: `AuthenticateUserUseCase`, `RegisterUserUseCaseImpl`

#### ❌ Confusion Común
**"Auth es importante para el negocio, debe estar en Domain"**
- ❌ Incorrecto: Importancia ≠ Dominio
- ✅ Correcto: Auth es **infraestructura transversal**, no negocio core

---

## 📋 Checklist de Refactorización

- [x] Crear nuevos archivos en `application.auth`
- [x] Actualizar imports en `AuthenticateUserUseCaseImpl`
- [x] Eliminar paquete `domain.auth`
- [x] Verificar que no haya referencias antiguas
- [x] Actualizar documentación
- [x] Verificar compilación sin errores
- [x] Actualizar JavaDocs

---

## 🚀 Próximos Pasos

1. **Crear AuthController** en `infrastructure.adapter.input.rest.auth`
   - Endpoint: `POST /api/auth/login`
   - Mapear DTOs → Commands
   - Invocar `AuthenticateUserUseCase`

2. **Testing**
   - Unit tests para `AuthenticateUserUseCaseImpl`
   - Integration tests para flujo completo de autenticación

3. **Seguridad**
   - Rate limiting en endpoint de login
   - Logging de intentos fallidos
   - Auditoría de accesos

---

**Refactorizado por**: GitHub Copilot  
**Fecha**: 2026-01-20  
**Motivo**: Separación correcta de responsabilidades en Arquitectura Hexagonal

